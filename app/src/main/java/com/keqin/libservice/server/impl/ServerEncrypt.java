package com.keqin.libservice.server.impl;

import com.keqin.libbase.server.intefaces.IServerEncrypt;
import com.keqin.libbase.utils.encrypt.EncryptUtil;

import java.io.UnsupportedEncodingException;

/**
 * 请求加解密
 *
 * @author Created by jz on 2017/7/3 11:23
 */
public class ServerEncrypt implements IServerEncrypt {
    private String mKey;

    /**
     * 加密
     *
     * @param msg 需要加密的字符串
     */
    public String encrypt(String msg) throws UnsupportedEncodingException {
        return EncryptUtil.encrypt(msg, getKey());
    }

    /**
     * 解密
     *
     * @param msg 需要解密的字符串
     */
    public String decrypt(String msg) throws UnsupportedEncodingException {
        return EncryptUtil.decrypt(msg, getKey());
    }

    public String getKey() throws UnsupportedEncodingException {
        if (mKey == null) {
            mKey = EncryptUtil.decrypt("bPzvxfj9eI4Feo+XVN0aDG+aMGBHtd07XjU27xn1foQ=");
        }
        return mKey;
    }

}
