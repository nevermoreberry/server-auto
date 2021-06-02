package com.keqin.libservice.server.bean.request;

/**
 * 获取请求
 *
 * @author Created by gold on 2018/3/13 10:20
 */
public class VerifyCodeRequest {
    private String phoneNum;
    private Integer pint;
    private Long plong;
    private Float pfloat;
    private Double pdouble;
    private Boolean pboolean;
    public char pchar1;
    public int pint1;
    public long plong1;
    public float pfloat1;
    public double pdouble1;
    public boolean pboolean1;
    boolean pboolean2;
    protected boolean pboolean3;

    public VerifyCodeRequest() {
    }

    public VerifyCodeRequest(String phoneNum) {
        this.phoneNum = phoneNum;
    }

    public String getPhoneNum() {
        return phoneNum;
    }

    public void setPhoneNum(String phoneNum) {
        this.phoneNum = phoneNum;
    }

    public Integer getPint() {
        return pint;
    }

    public void setPint(Integer pint) {
        this.pint = pint;
    }

    public Long getPlong() {
        return plong;
    }

    public void setPlong(Long plong) {
        this.plong = plong;
    }

    public Float getPfloat() {
        return pfloat;
    }

    public void setPfloat(Float pfloat) {
        this.pfloat = pfloat;
    }

    public Double getPdouble() {
        return pdouble;
    }

    public void setPdouble(Double pdouble) {
        this.pdouble = pdouble;
    }

    public Boolean getPboolean() {
        return pboolean;
    }

    public void setPboolean(Boolean pboolean) {
        this.pboolean = pboolean;
    }
}
