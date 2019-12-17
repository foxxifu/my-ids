package com.interest.ids.common.project.utils;

import java.security.SecureRandom;
import java.util.Random;
import java.util.UUID;

public class RandomUtil {
    private static Random randow = new SecureRandom();

    public static String getRandomString() {
        return UUID.randomUUID().toString();
    }

    public static long getRandomLong() {
        return randow.nextLong();
    }

    /**
     * long 19不被js识别 js用15位
     * @return
     */
    public static long getRandomLong15() {
        String k1 = ""+randow.nextInt();
        String k2 =getRandom5()+"";
        return Long.valueOf(k1+k2.trim());
    }

    public static int getRandomInt10(){
        return randow.nextInt();
    }

    /**
     * 最多5位数的随机数
     *
     * @return
     */
    public static int getRandom5() {
        long v = Math.round(Math.random() * 100000);
        return (int) v;
    }

    /**
     * 获取指定位数的的string随机数，随机范围为a-z A-Z 0-9
     * @param length string的长度
     * @return 指定lenght的随机字符串
     */
    public  static  String randomString(int length){
        String str = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        Random random = new Random();
        StringBuffer buf = new StringBuffer();
        for (int i = 0; i < length; i++) {
            int num = random.nextInt(62);
            buf.append(str.charAt(num));
        }
        return buf.toString();
    }

}
