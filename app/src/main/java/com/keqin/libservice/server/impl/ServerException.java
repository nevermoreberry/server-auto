package com.keqin.libservice.server.impl;

/**
 * 服务器返回错误
 *
 * @author Created by gold on 2016/12/28 11:41
 */
public class ServerException extends RuntimeException {

    private final int responseCode;

    public ServerException(int responseCode, String detailMessage) {
        super(detailMessage);
        this.responseCode = responseCode;
    }

    public int getResponseCode() {
        return responseCode;
    }
}
