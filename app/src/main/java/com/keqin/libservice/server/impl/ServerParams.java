package com.keqin.libservice.server.impl;

import java.util.HashMap;
import java.util.Map;

/**
 * 额外的参数
 *
 * @author Created by gold on 2018/3/19 16:58
 */
public class ServerParams {

    private static volatile ServerParams instance;

    public static ServerParams get() {
        if (instance == null) {
            synchronized (ServerParams.class) {
                if (instance == null) {
                    instance = new ServerParams();
                }
            }
        }
        return instance;
    }

    private Map<String, String> mParams;
    private long mTimestamp;
    private long mCurrentTime;

    public ServerParams() {
        this.mParams = new HashMap<>();
    }

    //获取所有参数
    Map<String, String> getParams() {
        return mParams;
    }

    /**
     * 添加公共参数
     *
     * @param key   键
     * @param value 值
     */
    public void put(String key, String value) {
        mParams.put(key, value);
    }

    /**
     * 清空公共参数
     */
    public void clearParams() {
        mParams.clear();
    }

    //保存时间戳
    void putTimestamp(long timestamp) {
        this.mTimestamp = timestamp;
        this.mCurrentTime = System.currentTimeMillis();
    }

    //获取时间戳
    long getTimestamp() {
        long currentTime = System.currentTimeMillis();
        return mTimestamp + currentTime - mCurrentTime;
    }
}
