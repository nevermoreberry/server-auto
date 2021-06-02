package com.keqin.libservice.server.bean.request;

/**
 * 配置信息请求
 *
 * @author Created by gold on 2018/5/10 10:33
 */
public class ConfigRequest {
    private String platformType;

    public ConfigRequest(String platformType) {
        this.platformType = platformType;
    }

    public String getPlatformType() {
        return platformType;
    }

    public void setPlatformType(String platformType) {
        this.platformType = platformType;
    }
}