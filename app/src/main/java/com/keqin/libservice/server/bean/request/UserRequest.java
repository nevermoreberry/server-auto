package com.keqin.libservice.server.bean.request;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * 获取请求
 *
 * @author Created by gold on 2018/3/13 10:20
 */
public class UserRequest {
    public static final int GIFT_PRIVATE = 0, GIFT_ROOM = 1, GIFT_GROUP = 2, GIFT_DYNAMIC = 3, GIFT_CALL = 4, GIFT_HOME = 5, GIFT_VIDEO = 6, GIFT_AUDIO = 7;

    @IntDef({GIFT_PRIVATE, GIFT_ROOM, GIFT_GROUP, GIFT_CALL, GIFT_HOME})
    @Retention(RetentionPolicy.SOURCE)
    public @interface NormalSource {
    }

    @IntDef({GIFT_DYNAMIC, GIFT_VIDEO, GIFT_AUDIO})
    @Retention(RetentionPolicy.SOURCE)
    public @interface SpecialSource {
    }

    private transient String phone;
    private String password;
    public String test1;
    public int test3;
    public Integer test4;
    private String teSt2;
    private Long teSt5;
    private long test6;

    public UserRequest() {
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getTeSt2() {
        return null;
    }

    public void setTest2(String test2) {
        this.teSt2 = test2;
    }

    public Long getTeSt5() {
        return teSt5;
    }

    public void setTeSt5(Long teSt5) {
        this.teSt5 = teSt5;
    }

    public long getTest6() {
        return test6;
    }

    public void setTest6(long test6) {
        this.test6 = test6;
    }
}
