package com.keqin.libbase.utils.encrypt;

import android.text.TextUtils;

import com.blankj.utilcode.util.EncodeUtils;
import com.blankj.utilcode.util.EncryptUtils;
import com.keqin.libbase.utils.logger.L;

import java.io.UnsupportedEncodingException;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 加解密数据
 *
 * @author Created by jz on 2017/7/3 11:23
 */
public class EncryptUtil {

    private static final String AES_Transformation = "AES/ECB/PKCS5Padding";

    private static byte[] AES_KEY;
    private static String KEY;

    private static final byte[] INITIAL_AES_KEY = "OUUNHyE9ASUrVSsnFxEiLkUXHks2GFRZ".getBytes();
    private static final String INITIAL_KEY = "I6iVOTuLJ9jBdZGWtVp/Dw==";

    public static void log() throws UnsupportedEncodingException {
        Map<String, String> map = new LinkedHashMap<>();

        String encryptInitialAesKey = new String(EncodeUtils.base64Encode(encryption("psdInitialAesKey1Android", INITIAL_KEY)));

        byte[] byteInitialAesKey = EncodeUtils.base64Decode(encryptInitialAesKey.getBytes());
        String initialAesKey = new String(decryption(byteInitialAesKey, INITIAL_KEY, byteInitialAesKey.length));

        map.put("encryptInitialAesKey", encryptInitialAesKey);
        map.put("initialAesKey", initialAesKey);

        String encryptDecodeKey = new String(EncodeUtils.base64Encode(encryptAES("Psd45678AndroidDecodeKey".getBytes(), "psdInitialAesKey1Android".getBytes())));
        String decodeKey = new String(getDecodeKey());

        map.put("encryptDecodeKey", encryptDecodeKey);
        map.put("decodeKey", decodeKey);

        String encryptKey = new String(EncodeUtils.base64Encode(encryptAES("psd7744jj22klshttock.123".getBytes(), getDecodeKey())));
        String key = getKey();

        map.put("encryptKey", encryptKey);
        map.put("key", key);

        String encryptDebugServerKey = encrypt("psd4c45gw8er7a5s");
        String debugServerKey = decrypt(encryptDebugServerKey);

        map.put("encryptDebugServerKey", encryptDebugServerKey);
        map.put("debugServerKey", debugServerKey);

        String encryptReleaseServerKey = encrypt("psd4c98gw8Kr1a6s");
        String releaseServerKey = decrypt(encryptReleaseServerKey);

        map.put("encryptReleaseServerKey", encryptReleaseServerKey);
        map.put("releaseServerKey", releaseServerKey);

        String encryptUpyunOperator = encrypt("psdandroid");
        String upyunOperator = decrypt(encryptUpyunOperator);

        map.put("encryptUpyunOperator", encryptUpyunOperator);
        map.put("upyunOperator", upyunOperator);

        String encryptUpyunPassword = encrypt("psd77314android");
        String upyunPassword = decrypt(encryptUpyunPassword);

        map.put("encryptUpyunPassword", encryptUpyunPassword);
        map.put("upyunPassword", upyunPassword);

        String encryptBackupPrefixPassword = encrypt("PsdZip");
        String backupPrefixPassword = decrypt(encryptBackupPrefixPassword);

        map.put("encryptBackupPrefixPassword", encryptBackupPrefixPassword);
        map.put("backupPrefixPassword", backupPrefixPassword);

        String encryptBackupSuffixPassword = encrypt("PasswordDb");
        String backupSuffixPassword = decrypt(encryptBackupSuffixPassword);

        map.put("encryptBackupSuffixPassword", encryptBackupSuffixPassword);
        map.put("backupSuffixPassword", backupSuffixPassword);

        for (Map.Entry<String, String> entry : map.entrySet()) {
            L.is("Tool", "%s = %s", entry.getKey(), entry.getValue());
        }
    }

    /**
     * 加密
     *
     * @param msg 需要加密的字符串
     */
    public static String encrypt(String msg) throws UnsupportedEncodingException {
        return encrypt(msg, getKey());
    }

    /**
     * 加密
     *
     * @param msg 需要加密的字符串
     * @param key 加密key
     */
    public static String encrypt(String msg, String key) throws UnsupportedEncodingException {
        if (TextUtils.isEmpty(msg)) {
            throw new NullPointerException("加密的字符串为空！");
        }

        byte[] bytes = EncodeUtils.base64Encode(encryptAES(EncodeUtils.base64Encode(msg), key.getBytes()));
        return new String(bytes);
    }

    /**
     * 解密
     *
     * @param msg 需要解密的字符串
     */
    public static String decrypt(String msg) throws UnsupportedEncodingException {
        return decrypt(msg, getKey());
    }

    /**
     * 解密
     *
     * @param msg 需要解密的字符串
     * @param key 解密key
     */
    public static String decrypt(String msg, String key) throws UnsupportedEncodingException {
        if (TextUtils.isEmpty(msg)) {
            throw new NullPointerException("加密的字符串为空！");
        }

        byte[] bytes = EncodeUtils.base64Decode(decryptAES(EncodeUtils.base64Decode(msg), key.getBytes()));
        return new String(bytes);
    }

    private static byte[] getDecodeKey() throws UnsupportedEncodingException {
        if (AES_KEY == null) {
            byte[] initialAesKey = INITIAL_AES_KEY;
            initialAesKey = EncodeUtils.base64Decode(initialAesKey);
            AES_KEY = decryptAES(
                    EncodeUtils.base64Decode(readDecodeKey()),
                    decryption(initialAesKey, INITIAL_KEY, initialAesKey.length));
        }
        return AES_KEY;
    }

    private static String getKey() throws UnsupportedEncodingException {
        if (KEY == null) {
            KEY = new String(decryptAES(EncodeUtils.base64Decode(readKey()), getDecodeKey()));
        }
        return KEY;
    }

    public static byte[] encryptAES(byte[] data, byte[] key) {
        return EncryptUtils.encryptAES(data, key, AES_Transformation, null);
    }

    public static byte[] decryptAES(byte[] data, byte[] key) {
        return EncryptUtils.decryptAES(data, key, AES_Transformation, null);
    }

    private static native String readDecodeKey();

    private static native String readKey();

    private static native byte[] encryption(String src, String key);

    private static native byte[] decryption(byte[] src, String key, int len);

    static {
        System.loadLibrary("EncryptUtil");
    }
}
