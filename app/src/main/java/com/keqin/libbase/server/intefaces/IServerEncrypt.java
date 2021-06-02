package com.keqin.libbase.server.intefaces;

import java.io.UnsupportedEncodingException;

/**
 * 请求加密
 *
 * @author Created by gold on 2018/5/28 17:43
 */
public interface IServerEncrypt {
    /**
     * 加密
     *
     * @param msg 需要加密的字符串
     */
    String encrypt(String msg) throws UnsupportedEncodingException;

    /**
     * 解密
     *
     * @param msg 需要解密的字符串
     */
    String decrypt(String msg) throws UnsupportedEncodingException;
}
