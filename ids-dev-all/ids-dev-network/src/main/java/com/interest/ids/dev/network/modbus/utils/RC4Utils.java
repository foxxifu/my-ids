package com.interest.ids.dev.network.modbus.utils;

import com.interest.ids.common.project.buffer.UnSafeHeapBuffer;
import com.interest.ids.dev.network.modbus.message.MessageTest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * yaokun
 */
public class RC4Utils {

    private static final Logger log = LoggerFactory.getLogger(RC4Utils.class);
    private static int[] mS = new int[256];
    private static byte[] key = {1,2,3,4,5,6,7,8};

    public static byte[] code(byte[] sourceBytes) {
        try {
            return Rc4Transition(sourceBytes);
        } catch (Exception e) {
            log.error("encryptWithRC4 error.." + ByteUtils.formatHexString(sourceBytes));
        }
        return null;
    }

    /**
     * 报文的加密和解密操作
     *
     * @param buffer
     * @return
     */
    public static UnSafeHeapBuffer rc4Deal(UnSafeHeapBuffer buffer) {
        byte[] data = buffer.array().clone();
        buffer.readBytes(2);
        int mark = buffer.readShort();
        mark=mark>>14;
        mark = mark & 3;
        if (mark == 1) {
            buffer.skipBytes(3);
            byte[] realDate = buffer.readBytes(data.length - 7);
            realDate = code(realDate);
            System.arraycopy(realDate, 0, data, 7, realDate.length);
        }
        return new UnSafeHeapBuffer(data);
    }

    /**
     * 自己写的加密解密算法
     */
    private static byte[] Rc4Transition(byte[] data) {
        for (int i = 0; i < mS.length; i++) {
            mS[i] = i;
        }
        int j = 0;
        int temp;
        for (int i = 0; i < mS.length; i++) {
            temp = mS[i];
            j = (j + temp + key[i % key.length]) % mS.length;
            mS[i] = mS[j];
            mS[j] = temp;
        }
        int x = 0;
        int y = 0;
        for (int i = 0; i < data.length; i++) {
            x = (x + 1) % mS.length;
            y = (y + mS[x]) % mS.length;
            data[i] ^= (byte) (mS[(mS[x] + mS[y]) % mS.length]);
        }
        return data;
    }

    public static void main(String[] args) {
//        String sourceStr = "54 68 69 73 49 73 53 65 72 69 61 6c 4e 75 6d 62 65 72 00 87 65 44 1b";
        String messageStr = "00 00 40 01 01 69 00 d5 af 8a 1b f0 ab a5 f4 14 fa 1f ea 84 85 65 b1 b9 cc 1c 45 2e c0 4a 83 8c d9 01 9c ce 00 0c e3 41 50 87 9e 69 d4 c5 9a 71 8b ff ee 4b 56 49 03 7d ec 9a d7 50 58 68 80 67 1e 20 b1 d2 53 8e dd 3e 06 68 97 e7 6e db f1 1e 7f 60 18 6a 9a 6c b6 fe 69 8b 13 f8 86 4e 2f c4 17 4d 0f ad 06 a8 f9 0a b1 55 6c 10 4f 7d 49 dc 58 b7 0d a3 1e 17 c6 10 67 cc 3c 65 f6 ce 52 8c ae 17 49 63 34 2e b1 c1 e3 24 3e 60 16 76 46 ae a6 9f 81 4a e4 9a 2d 08 5a e5 32 fe 3e 95 f9 e8 de 7f cc b8 78 7a e6 c7 87 fe 0f a4 57 1b af b2 5f 7b ac d3 fb 3b 65 fd 1f 92 ea bd 14 71 d5 c7 8b 0e ca ed 91 7f 73 20 8e 52 b1 4f e5 68 9c 42 65 48 13 94 43 3d 2b 38 b6 e1 16 1a f0 c3 35 28 54 7e 01 c4 46 65 5f 6f 90 98 41 41 89 36 82 d2 6e 92 8d 4a d6 44 54 91 63 27 94 e7 9c 57 b6 3e d2 d5 90 3f 99 5a 00 e0 86 f3 d1 1c 7f f1 17 ff a0 aa 4b 02 ac a4 d2 48 57 c0 a0 0c fd 4c 9a 36 3c 47 72 b8 ed 53 1b 55 31 1c 54 9e 3b 55 2f 1a f3 e1 41 d8 74 cf b1 70 fd c9 c5 07 bf ab 24 16 63 a3 77 df 8c 18 66 ac c0 74 a2 12 9d 91 d5 9f 78 94 e5 77 22 9e a2 69 67 d6 49 d9 a8 f9 d6 93 44 78 18 f0 09 b1 2f d3 68 40 7b fa 28 44 19";
        byte[] testArray = MessageTest.stringToByteArray(messageStr);
        UnSafeHeapBuffer buffer=new UnSafeHeapBuffer(testArray);
        buffer=rc4Deal(buffer);
//        byte[] result = Rc4Transition(testArray);
        System.out.println(ByteUtils.formatHexString(buffer.array()));
//        System.out.println("hex string = " + sourceStr);
//    	for (int i = 0; i < mS.length; i++) {
//            mS[i] = i;
//        }
//        int j = 0;
//        int temp;
//        for (int i = 0; i < mS.length; i++) {
//            temp = mS[i];
//            j = (j + temp + key[i % key.length]) % mS.length;
//            mS[i] = mS[j];
//            mS[j] = temp;
//        }
//        for(int i=0;i<256;i++) {
//        	System.out.print(mS[i]+" ");
//        }
    }
}