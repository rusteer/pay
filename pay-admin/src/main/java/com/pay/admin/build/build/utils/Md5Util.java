package com.pay.admin.build.build.utils;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import com.pay.admin.build.build.LogUtil;

public class Md5Util {
    public final static String MD5(String s) throws NoSuchAlgorithmException {
        char hexDigits[] = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };
        byte[] btInput = s.getBytes();
        // 获得MD5摘要算法的 MessageDigest 对象
        MessageDigest mdInst = MessageDigest.getInstance("MD5");
        // 使用指定的字节更新摘要
        mdInst.update(btInput);
        // 获得密文
        byte[] md = mdInst.digest();
        // 把密文转换成十六进制的字符串形式
        int j = md.length;
        char str[] = new char[j * 2];
        int k = 0;
        for (int i = 0; i < j; i++) {
            byte byte0 = md[i];
            str[k++] = hexDigits[byte0 >>> 4 & 0xf];
            str[k++] = hexDigits[byte0 & 0xf];
        }
        return new String(str);
    }
    public static void main(String[] args) {
        //md5(app_secret +”#”+ app_key)
        String appSecret = "6d462c45daa8c625f6e6f0acfa1a3ba0";
        String appKey = "74d928449a9ced702e132f602605aec2";
        try {
            LogUtil.info(MD5(appSecret + "#" + appKey));
        } catch (NoSuchAlgorithmException e) {
            // TODO Auto-generated catch block
            LogUtil.error(e);
        }
    }
}
