package com.keqin.server;

import com.google.auto.service.AutoService;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeSpec;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.MirroredTypeException;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.Types;
import javax.tools.Diagnostic;

import io.reactivex.Observable;
import io.reactivex.functions.Function;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Query;

import static com.keqin.server.ProcessorUtil.getGenericType;
import static com.keqin.server.ProcessorUtil.isSystemClass;

/**
 * 扫描工程，生成ServerApi序列
 *
 * @author Created by gold on 2018/3/13 11:20
 */
@AutoService(Processor.class)
public class ServerAnnotationProcessor extends AbstractProcessor {

    private Types mTypes;
    private Filer mFiler;

    private List<String> mFields = new ArrayList<>();

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        Set<? extends Element> elements = roundEnv.getElementsAnnotatedWith(ServerApi.class);
        if (elements == null || elements.isEmpty()) {
            return false;
        }

        String serverPrefix = processingEnv.getOptions().get("serverPrefix");
        if (serverPrefix == null) {
            serverPrefix = "com.keqin";
        }

        mTypes = processingEnv.getTypeUtils();
        mFiler = processingEnv.getFiler();

        for (Element element : elements) {
            if (element.getKind() != ElementKind.INTERFACE) {
                logError(element, "被ServerApi注解的必须是接口。");
                return false;
            }
            if (!element.getModifiers().contains(Modifier.PUBLIC)) {
                logError(element, "被ServerApi注解的接口必须是公开的。");
                return false;
            }

            String className = element.getSimpleName().toString();
            String path = element.getEnclosingElement().toString();

            ArrayList<com.keqin.server.MethodInfo> list = new ArrayList<>();

            for (Element childElement : element.getEnclosedElements()) {
                if (!(childElement instanceof ExecutableElement)) {
                    continue;
                }

                ExecutableElement executableElement = (ExecutableElement) childElement;

                String methodName = String.valueOf(childElement.getSimpleName());

                //地址检查
                POST post = executableElement.getAnnotation(POST.class);
                GET get = executableElement.getAnnotation(GET.class);
                PUT put = executableElement.getAnnotation(PUT.class);
                DELETE delete = executableElement.getAnnotation(DELETE.class);
                if (post == null && get == null && put == null && delete == null) {
                    logError(executableElement, "方法必须有Post、Get、Put、Delete注解。");
                    return false;
                }
                String urlValue;
                if (post != null) {
                    urlValue = post.value();
                } else if (get != null) {
                    urlValue = get.value();
                } else if (put != null) {
                    urlValue = put.value();
                } else {
                    urlValue = delete.value();
                }
                if (urlValue.equals("")) {
                    logError(executableElement, "Url地址不允许为空。");
                    return false;
                }

                TypeMirror requestTypeMirror = null;
                List<ParamInfo> params = null;

                //参数检测
                List<? extends VariableElement> variableElements = executableElement.getParameters();

                if (variableElements == null || variableElements.size() != 1) {
                    logError(executableElement, "方法必须有参数并且只能有一个参数。");
                    return false;
                }

                VariableElement variableElement = variableElements.get(0);

                if (!variableElement.asType().toString().equals(String.class.getName())) {
                    logError(executableElement, "方法参数的类型只能是String。");
                    return false;
                }

                //参数注解检查
                Query fieldMap = variableElement.getAnnotation(Query.class);
                if (fieldMap == null) {
                    logError(executableElement, "方法参数的注解类型必须是Query。");
                    return false;
                }

                //请求类检查
                ServerRequest serverRequest = executableElement.getAnnotation(ServerRequest.class);
                if (serverRequest != null) {
                    try {
                        serverRequest.value();
                    } catch (MirroredTypeException mte) {
                        requestTypeMirror = mte.getTypeMirror();
                    }

                    if (requestTypeMirror == null) {
                        logError(executableElement, "注解ServerRequest的参数为空。");
                        return false;
                    }

                    //请求类参数检查
                    Element requestElement = mTypes.asElement(requestTypeMirror);
                    if (requestElement.getKind() != ElementKind.CLASS) {
                        logError(requestElement, "请求参数类必须是Class。");
                        return false;
                    }
                    if (!requestElement.getModifiers().contains(Modifier.PUBLIC)) {
                        logError(requestElement, "请求参数类必须是必须是公开的。");
                        return false;
                    }

                    params = new ArrayList<>();

                    readParams(params, requestElement);
                }

                //返回检查
                TypeMirror typeMirror = executableElement.getReturnType();

                if (!mTypes.asElement(typeMirror).asType().toString().equals(Observable.class.getName() + "<T>")) {
                    logError(executableElement, "返回参数的类型只能是Observable。");
                    return false;
                }

                TypeMirror serverResultTypeMirror = getGenericType(typeMirror);
                Element serverResultElement = mTypes.asElement(serverResultTypeMirror);

                if (!serverResultElement.asType().toString()
                        .equals(serverPrefix + ".libservice.server.impl.bean.ServerResult" + "<T>")
                ) {
                    logError(executableElement, "Observable的泛型必须是ServerResult。");
                }

                com.keqin.server.MethodInfo info = new com.keqin.server.MethodInfo(methodName, requestTypeMirror, params, getGenericType(serverResultTypeMirror));

                list.add(info);
            }

            ClassName apiClassName = ClassName.get(path, className);
            ClassName serverClassName = ClassName.get(path, className + "Server");
            ClassName jsonObjectClassName = ClassName.get("org.json", "JSONObject");

            ArrayList<MethodSpec> methodSpecs = new ArrayList<>();

            for (com.keqin.server.MethodInfo info : list) {
                MethodSpec.Builder methodBuilder = MethodSpec
                        .methodBuilder(info.name)
                        .addModifiers(Modifier.PUBLIC)
                        .returns(ParameterizedTypeName.get(
                                ClassName.get(Observable.class),
                                ClassName.get(info.result)));

                if (info.request != null) {
                    methodBuilder.addParameter(ClassName.get(info.request), "request")
                            .addCode("return Observable.just(request)")
                            .addCode(".map($NAdapter())", info.name);
                } else {
                    methodBuilder.addCode("return Observable.just(new $T())", jsonObjectClassName);
                }
                methodBuilder.addCode(".map(formatParams())")
                        .addCode(".flatMap(mApi::$N)", info.name)
                        .addStatement(".compose(applyScheduler())");

                methodSpecs.add(methodBuilder.build());

                if (info.request != null) {
                    ParameterizedTypeName typeName = ParameterizedTypeName.get(
                            ClassName.get(Function.class),
                            ClassName.get(info.request),
                            jsonObjectClassName);

                    MethodSpec.Builder adapterMethodBuilder = MethodSpec
                            .methodBuilder(String.format("%sAdapter", info.name))
                            .addModifiers(Modifier.PRIVATE)
                            .returns(typeName)
                            .addCode(" return request -> {")
                            .addStatement("$T jsonObject = new $T()", jsonObjectClassName, jsonObjectClassName);

                    for (ParamInfo paramInfo : info.params) {
                        adapterMethodBuilder.addStatement(
                                "jsonObject.put($S, request.$N)", paramInfo.name, paramInfo.paramName);
                    }
                    adapterMethodBuilder.addStatement("return jsonObject")
                            .addStatement("}");

                    methodSpecs.add(adapterMethodBuilder.build());
                }
            }

            FieldSpec instanceField = FieldSpec.builder(serverClassName, "instance")
                    .addModifiers(Modifier.PRIVATE, Modifier.STATIC, Modifier.VOLATILE)
                    .build();

            String instanceName = "instance";

            MethodSpec instanceMethod = MethodSpec.methodBuilder("get")
                    .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
                    .returns(serverClassName)
                    .beginControlFlow("if($N == null)", instanceName)
                    .beginControlFlow("synchronized ($T.class)", serverClassName)
                    .beginControlFlow("if($N == null)", instanceName)
                    .addStatement("instance = new $T()", serverClassName)
                    .endControlFlow()
                    .endControlFlow()
                    .endControlFlow()
                    .addStatement("return $N", instanceName)
                    .build();

            MethodSpec createMethod = MethodSpec.methodBuilder("createApi")
                    .addAnnotation(Override.class)
                    .addModifiers(Modifier.PROTECTED)
                    .returns(ParameterizedTypeName.get(ClassName.get(Class.class), apiClassName))
                    .addStatement("return $T.class", apiClassName)
                    .build();

            TypeSpec typeSpec = TypeSpec.classBuilder(serverClassName)
                    .addJavadoc("server 生成代码 请不要修改！")
                    .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
                    .superclass(ParameterizedTypeName.get(
                            ClassName.get(serverPrefix + ".libservice.server.impl", "ServerApi"),
                            apiClassName))
                    .addField(instanceField)
                    .addMethod(instanceMethod)
                    .addMethod(createMethod)
                    .addMethods(methodSpecs)
                    .build();

            JavaFile javaFile = JavaFile.builder(path, typeSpec).build();
            try {
                javaFile.writeTo(mFiler);
            } catch (IOException e) {
                // ignored
            }
        }
        return true;
    }

    private void readParams(List<ParamInfo> params, Element element) {
        if (element == null) {
            return;
        }
        if (isSystemClass(element.toString())) {
            return;
        }

        mFields.clear();
        for (Element paramElement : element.getEnclosedElements()) {
            if (paramElement.getModifiers().contains(Modifier.STATIC)) {
                continue;
            }
            String paramName = paramElement.toString();
            if (paramElement.getKind() == ElementKind.FIELD) {
                if (paramElement.getModifiers().contains(Modifier.TRANSIENT)) {
                    continue;
                }
                if (paramElement.getModifiers().contains(Modifier.PUBLIC)) {
                    params.add(new ParamInfo(paramName, paramName));
                } else {
                    mFields.add(paramName);
                }
            } else if (paramElement.getKind() == ElementKind.METHOD) {
                if (!paramName.startsWith("get")
                        && !paramName.startsWith("is")) {
                    continue;
                }
                if (!(paramElement instanceof ExecutableElement)) {
                    continue;
                }

                String name = paramElement.getSimpleName().toString();
                String simpleName;

                String paramReturnTypeMirror = ((ExecutableElement) paramElement).getReturnType().toString();
                boolean isBoolean = paramReturnTypeMirror.equals(boolean.class.getName())
                        || paramReturnTypeMirror.equals(Boolean.class.getName());

                if (paramReturnTypeMirror.equals(boolean.class.getName())) {
                    simpleName = name.substring(2, name.length()).toLowerCase();
                } else {
                    simpleName = name.substring(3, name.length()).toLowerCase();
                }

                Iterator<String> iterable = mFields.iterator();
                while (iterable.hasNext()) {
                    name = iterable.next();
                    String lowerName = name.toLowerCase();

                    boolean isNameBoolean = isBoolean && lowerName.startsWith("is");
                    if (isNameBoolean) {
                        lowerName = lowerName.substring(2, lowerName.length());
                    }

                    if (simpleName.equals(lowerName.toLowerCase())) {
                        params.add(new ParamInfo(name, paramName));

                        iterable.remove();
                    }
                }
            }
        }

        if (!(element instanceof TypeElement)) {
            return;
        }
        TypeElement typeElement = (TypeElement) element;
        TypeMirror typeMirror = typeElement.getSuperclass();

        if (typeMirror != null) {
            readParams(params, mTypes.asElement(typeMirror));
        }
    }

    private void logError(Element element, String str) {
        log(Diagnostic.Kind.ERROR, element, str);
    }

    private void logNote(String str) {
        processingEnv.getMessager().printMessage(Diagnostic.Kind.NOTE, str);
    }

    private void log(Diagnostic.Kind kind, Element element, String str) {
        processingEnv.getMessager().printMessage(kind, str, element);
    }

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        Set<String> supportedTypes = new LinkedHashSet<>();
        supportedTypes.add(ServerApi.class.getCanonicalName());
        return supportedTypes;
    }

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latestSupported();
    }
}