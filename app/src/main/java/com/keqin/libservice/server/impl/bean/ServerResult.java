package com.keqin.libservice.server.impl.bean;

/**
 * 服务返回
 *
 * @author Created by gold on 2016/12/28 10:54
 */
public class ServerResult<T> {
    private int code;
    private T result;
    private String ext;
    private String errorMsg;
    private int errorCode;
    private long timestamp;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public T getResult() {
        return result;
    }

    public void setResult(T result) {
        this.result = result;
    }

    public String getExt() {
        return ext;
    }

    public void setExt(String ext) {
        this.ext = ext;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }

    public int getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public String toString() {
        return "ServerResult{" +
                "code=" + code +
                ", result=" + result +
                ", ext='" + ext + '\'' +
                ", errorMsg='" + errorMsg + '\'' +
                ", errorCode=" + errorCode +
                ", timestamp=" + timestamp +
                '}';
    }
}
