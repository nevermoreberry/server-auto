package com.keqin.libbase.server.intefaces;

import com.keqin.libbase.server.ServerManager;

/**
 * 服务请求
 *
 * @author Created by gold on 2018/5/28 17:05
 */
public abstract class IServeApi<API> {
    protected final String TAG = getClass().getSimpleName();

    protected final API mApi;

    public IServeApi() {
        this.mApi = create();
    }

    protected API create() {
        return ServerManager.get()
                .getRetrofit()
                .create(createApi());
    }

    /**
     * 返回API
     */
    protected abstract Class<API> createApi();
}
