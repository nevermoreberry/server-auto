package com.keqin.libservice.server.bean.request;

public class PhoneRequest extends ConfigRequest {

    private String phone;
    private Boolean isStr;
    private boolean isStrBoolean;
    private Boolean bl1;
    private boolean bl2;

    public Boolean isStrPublic;
    public boolean isStrBooleanPublic;
    public Boolean bl1Public;
    public boolean bl2Public;

    public PhoneRequest(String platformType) {
        super(platformType);
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Boolean getStr() {
        return isStr;
    }

    public void setStr(Boolean str) {
        isStr = str;
    }

    public boolean isStrBoolean() {
        return isStrBoolean;
    }

    public void setStrBoolean(boolean strBoolean) {
        isStrBoolean = strBoolean;
    }

    public Boolean getBl1() {
        return bl1;
    }

    public void setBl1(Boolean bl1) {
        this.bl1 = bl1;
    }

    public boolean isBl2() {
        return bl2;
    }

    public void setBl2(boolean bl2) {
        this.bl2 = bl2;
    }
}
