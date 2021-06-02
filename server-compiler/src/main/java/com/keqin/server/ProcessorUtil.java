package com.keqin.server;

import java.util.List;

import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.SimpleTypeVisitor7;

/**
 * 工具类
 *
 * @author Created by gold on 2017/8/24 17:32
 */
class ProcessorUtil {
    private ProcessorUtil() {
    }

    static TypeMirror getGenericType(final TypeMirror type) {
        final TypeMirror[] result = {null};

        type.accept(new SimpleTypeVisitor7<Void, Void>() {
            @Override
            public Void visitDeclared(DeclaredType declaredType, Void v) {
                List<? extends TypeMirror> typeArguments = declaredType.getTypeArguments();
                if (!typeArguments.isEmpty()) {
                    result[0] = typeArguments.get(0);
                }
                return null;
            }

            @Override
            protected Void defaultAction(TypeMirror typeMirror, Void v) {
                throw new UnsupportedOperationException();
            }

        }, null);

        return result[0];
    }

    static boolean isSystemClass(String name) {
        return name.startsWith("java.") || name.startsWith("javax.") || name.startsWith("android.");
    }
}
