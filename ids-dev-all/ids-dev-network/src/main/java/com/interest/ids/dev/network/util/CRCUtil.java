package com.interest.ids.dev.network.util;

/**
 * @author yaokun
 * @title: CRCUtil
 * @projectName removesame
 * @description: 计算CRC的方法
 * @date 2019-07-2001:09
 */
public class CRCUtil {
    public static byte[] getCRC(byte[] bytes) {
        byte[] crcByte=new byte[2];
        int CRC = 0x0000ffff;
        int POLYNOMIAL = 0x0000a001;
        int i, j;
        for (i = 0; i < bytes.length-2; i++) {
            CRC ^= ((int) bytes[i] & 0x000000ff);
            for (j = 0; j < 8; j++) {
                if ((CRC & 0x00000001) != 0) {
                    CRC >>= 1;
                    CRC ^= POLYNOMIAL;
                } else {
                    CRC >>= 1;
                }
            }
        }
        crcByte[1]=(byte) (CRC>>8);
        crcByte[0]=(byte)(CRC&0x00FF);
        return crcByte;

    }
}
