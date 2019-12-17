package com.interest.ids.common.project.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * @Author: sunbjx
 * @Description:
 * @Date: Created in 上午10:13 18-3-5
 * @Modified By:
 */
public class HashUtil {

    public static String sha1(String data) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("SHA1");
        md.update(data.getBytes());
        StringBuffer buf = new StringBuffer();
        byte[] bits = md.digest();
        for (int i = 0; i < bits.length; i++) {
            int a = bits[i];
            if (a < 0) {
                a += 256;
            }
            if (a < 16) {
                buf.append("0").append(Integer.toHexString(a));
            }
        }
        return buf.toString();
    }
}
