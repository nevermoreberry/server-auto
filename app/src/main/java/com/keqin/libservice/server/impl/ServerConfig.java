package com.keqin.libservice.server.impl;

import com.keqin.libbase.server.intefaces.IServerConfig;

/**
 * 服务器配置
 *
 * @author Created by gold on 2016/12/6 17:21
 */
public class ServerConfig implements IServerConfig {

    public static ConfigInfo getConfigInfo() {
        return ConfigInfo.DEBUG;
    }

    private final String mUrl;//服务器请求地址
    private final String mUploadUrl;//上传地址

    public static ServerConfig get() {
//        return new Release();
//        return new OnlineDebug();
        return new Debug();
    }

    public ServerConfig(String url, String uploadUrl) {
        this.mUrl = url;
        this.mUploadUrl = uploadUrl;
    }

    /**
     * 获取请求地址
     */
    @Override
    public String getUrl() {
        return mUrl;
    }

    /**
     * 获取上传地址
     */
    public String getUploadUrl() {
        return mUploadUrl;
    }

    /**
     * 测试模式
     */
    public static class Debug extends ServerConfig {
        private static final String url = "http://118.31.3.144:8088/api/";
        //        private static final String url = "http://192.168.4.74:8088/api/";
        private static final String uploadUrl = "http://psdfile-dev.b0.upaiyun.com";

        private Debug() {
            super(url, uploadUrl);
        }
    }

    /**
     * 线上测试模式
     */
    public static class OnlineDebug extends ServerConfig {
        private static final String url = "http://apitst.nidong.com/";
        private static final String uploadUrl = "http://file.nidong.com";

        private OnlineDebug() {
            super(url, uploadUrl);
        }
    }

    /**
     * 发布模式
     */
    public static class Release extends ServerConfig {
        private static final String url = "https://api.tantanqing.cn/";
        private static final String uploadUrl = "http://file.nidong.com";

        private Release() {
            super(url, uploadUrl);
        }
    }

    public enum ConfigInfo {
        DEBUG, DEBUG_ONLINE, RELEASE;
    }

    public static boolean isDebug() {
        return getConfigInfo() == ConfigInfo.DEBUG;
    }

    public static boolean isDebugOnline() {
        return getConfigInfo() == ConfigInfo.DEBUG_ONLINE;
    }

    public static boolean isRelease() {
        return getConfigInfo() == ConfigInfo.RELEASE;
    }
}
