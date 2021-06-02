package com.keqin.libbase.server;

import com.keqin.libbase.server.intefaces.IServerConfig;
import com.keqin.libbase.server.intefaces.IServerEncrypt;
import com.keqin.libbase.server.interceptor.HttpLoggingInterceptor;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * 服务请求管理器
 *
 * @author Created by gold on 2016/12/27 17:25
 */
public final class ServerManager {

    private static volatile ServerManager instance;

    public static ServerManager get() {
        if (instance == null) {
            synchronized (ServerManager.class) {
                if (instance == null) {
                    instance = new ServerManager();
                }
            }
        }
        return instance;
    }

    public static final long DEFAULT_TIMEOUT = 15 * 1000;

    private OkHttpClient mClient;
    private Retrofit.Builder mRetrofitBuilder;
    private Retrofit mRetrofit;

    private IServerConfig mIServerConfig;
    private IServerEncrypt mIServerEncrypt;

    private ServerManager() {
    }

    public void init(IServerConfig iServerConfig, IServerEncrypt iServerEncrypt) {
        if (iServerConfig == null) {
            return;
        }
        if (iServerEncrypt == null) {
            return;
        }
        if (mClient != null) {
            return;
        }

        mIServerConfig = iServerConfig;
        mIServerEncrypt = iServerEncrypt;

        mClient = new OkHttpClient.Builder()
                .connectTimeout(DEFAULT_TIMEOUT, TimeUnit.MILLISECONDS)
                .readTimeout(DEFAULT_TIMEOUT, TimeUnit.MILLISECONDS)
                .writeTimeout(DEFAULT_TIMEOUT, TimeUnit.MILLISECONDS)
                .addInterceptor(new HttpLoggingInterceptor())
                .build();

        mRetrofitBuilder = new Retrofit.Builder()
                .client(mClient)
                .baseUrl(mIServerConfig.getUrl())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create());

        mRetrofit = mRetrofitBuilder.build();
    }

    /**
     * 获得client
     */
    public OkHttpClient getClient() {
        return mClient;
    }

    /**
     * 获得retrofitBuilder
     */
    public Retrofit.Builder getRetrofitBuilder() {
        return mRetrofitBuilder;
    }

    /**
     * 获得retrofit
     */
    public Retrofit getRetrofit() {
        return mRetrofit;
    }

    /**
     * 获得服务器配置
     */
    public IServerConfig getServerConfig() {
        return mIServerConfig;
    }

    /**
     * 获得请求加密工具
     */
    public IServerEncrypt getServerEncrypt() {
        return mIServerEncrypt;
    }
}
