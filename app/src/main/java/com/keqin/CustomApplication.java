package com.keqin;

import android.app.Application;
import android.content.Context;

import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.Logger;
import com.keqin.libbase.server.ServerManager;
import com.keqin.libservice.server.impl.ServerConfig;
import com.keqin.libservice.server.impl.ServerEncrypt;

/**
 * 自定义Application
 *
 * @author Created by jz on 2017/7/6 19:32
 */
public class CustomApplication extends Application {

    private static CustomApplication mApplication;

    public static CustomApplication get() {
        return mApplication;
    }

    public static Context getContext() {
        return mApplication.getApplicationContext();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mApplication = this;

        Logger.addLogAdapter(new AndroidLogAdapter() {
            @Override
            public boolean isLoggable(int priority, String tag) {
                return !ServerConfig.isRelease();
            }
        });

        ServerManager.get().init(ServerConfig.get(), new ServerEncrypt());
    }
}
