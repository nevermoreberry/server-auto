package com.keqin.server;

import java.util.List;

import javax.lang.model.type.TypeMirror;

/**
 * 方法信息
 *
 * @author Created by gold on 2017/8/24 19:33
 */
class MethodInfo {
    final String name;
    final TypeMirror request;
    final List<ParamInfo> params;
    final TypeMirror result;

    MethodInfo(String name, TypeMirror request, List<ParamInfo> params, TypeMirror result) {
        this.name = name;
        this.request = request;
        this.params = params;
        this.result = result;
    }
}
