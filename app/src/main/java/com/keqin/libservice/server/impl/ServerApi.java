package com.keqin.libservice.server.impl;

import android.text.TextUtils;

import com.keqin.libbase.server.ServerManager;
import com.keqin.libservice.server.impl.bean.ServerResult;
import com.keqin.libbase.server.intefaces.IServeApi;
import com.keqin.libbase.utils.rx.RxUtil;

import org.json.JSONObject;

import java.util.Map;

import io.reactivex.ObservableTransformer;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;

/**
 * 请求基类
 *
 * @author Created by gold on 2016/12/30 13:16
 */
public abstract class ServerApi<API> extends IServeApi<API> {

    /**
     * 应用调度器同时转换
     *
     * @param <T> 类型
     */
    protected <T> ObservableTransformer<ServerResult<T>, T> applyScheduler() {
        return observable -> observable
                .compose(RxUtil.applyScheduler())
                .map(serverResult());
    }

    //将ServerResult转换为T
    private <T> Function<ServerResult<T>, T> serverResult() {
        return result -> {
            int code = result.getCode();
            if (code == 200) {//请求成功
                T t = result.getResult();
                if (t == null) {
                    throw new ServerException(-10001, "服务器返回数据错误！");
                } else {
                    return result.getResult();
                }
            } else if (code == 400) {//业务错误
                throw new ServerException(result.getErrorCode(), result.getErrorMsg());
            } else if (code == 401) {//token失效
                //todo 强制退出
                throw new ServerException(-10000, "Token失效！");
            } else {//服务器错误
                throw new ServerException(code, "服务器发生错误！");
            }
        };
    }

    //添加公共参数
    protected Function<JSONObject, String> formatParams() {
        return jsonObject -> {
            Map<String, String> params = ServerParams.get().getParams();
            if (!params.isEmpty()) {
                for (Map.Entry<String, String> entry : params.entrySet()) {
                    jsonObject.put(entry.getKey(), entry.getValue());
                }
            }
            return jsonObject.toString();
        };
    }
}