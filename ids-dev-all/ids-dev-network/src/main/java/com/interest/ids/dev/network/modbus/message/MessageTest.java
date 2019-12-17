package com.interest.ids.dev.network.modbus.message;

import com.interest.ids.common.project.buffer.UnSafeHeapBuffer;
import com.interest.ids.dev.network.modbus.utils.ByteUtils;
import com.interest.ids.dev.network.modbus.utils.RC4Utils;

import io.netty.channel.Channel;

import java.sql.SQLOutput;

public class MessageTest {
    public static void main(String[] args) {
        String messageStr = "00 00 00 01 00 eb 00 64 04 03 01 02 5d 1f 85 52 03 0b b8 00 21 03 9f 03 9f 03 9f 03 9f 03 9f 03 9f 03 9f 03 9f 03 9f 03 9f 03 9f 03 9f 03 9f 03 9f 03 9f 03 9f 03 9f 03 9f 03 9f 03 9f 03 9f 03 9f 03 9f 03 9f 03 9f 03 9f 03 9f 03 9f 03 9f 03 9f 03 9f 03 9f 03 9f 02 02 5d 1f 85 52 03 0b b8 00 21 03 9f 03 9f 03 9f 03 9f 03 9f 03 9f 03 9f 03 9f 03 9f 03 9f 03 9f 03 9f 03 9f 03 9f 03 9f 03 9f 03 9f 03 9f 03 9f 03 9f 03 9f 03 9f 03 9f 03 9f 03 9f 03 9f 03 9f 03 9f 03 9f 03 9f 03 9f 03 9f 03 9f 03 02 5d 1f 85 53 03 0b b8 00 21 03 9f 03 9f 03 9f 03 9f 03 9f 03 9f 03 9f 03 9f 03 9f 03 9f 03 9f 03 9f 03 9f 03 9f 03 9f 03 9f 03 9f 03 9f 03 9f 03 9f 03 9f 03 9f 03 9f 03 9f 03 9f 03 9f 03 9f 03 9f 03 9f 03 9f 03 9f 03 9f 03 9f";
//        String messageStr1 = "06 d0 00 01 00 07 00 43 01 5c 89 e3 7d";
//        String messageStr2 = "00 04 40 00 00 20 00 64 01 01 00 02 01 00 00 00 00 1e 61 62 63 64 65 66 67 68 69 6a 6b 6c 6d 6e 6f 70 71 72 73 74";
//        String messageStr3 = "00 31 40 00 00 08 00 64 03 01 01 01 00 1e";
//        String messageStr4 = "01 44 01 00 01 03 0a 44 43 53 34 47 55 31 31 32 00 0b 30 30 30 30 30 30 30 30 31 30 00";
//        String messageStr5 = "00 0b 40 00 00 08 00 44 04 00 00 03 20 03";
        String messageDevUnder="00 00 40 01 01 3c 01 42 05 03 00 01 01 34 7b 22 64 65 76 49 6e 66 6f 22 3a 7b 22 66 61 63 74 6f 72 79 22 3a 22 53 69 4e 65 6e 67 22 2c 22 6d 6f 64 65 6c 22 3a 22 45 50 32 35 48 41 5a 22 2c 22 73 6e 22 3a 22 35 38 39 35 34 37 35 32 31 35 36 34 35 32 32 35 22 2c 22 70 72 6f 74 6f 63 6f 6c 49 64 65 6e 74 69 66 69 65 72 22 3a 22 31 30 30 22 7d 2c 22 70 6c 61 6e 74 49 6e 66 6f 22 3a 7b 22 6e 61 6d 65 49 6e 50 6c 61 6e 74 22 3a 22 31 23 e5 ad 90 e9 98 b5 33 e5 8f b7 e9 80 86 e5 8f 98 e5 99 a8 22 2c 22 6c 6f 63 61 74 69 6f 6e 49 6e 50 6c 61 6e 74 22 3a 22 33 e6 a0 8b e8 a5 bf e4 be a7 22 7d 2c 22 73 6f 66 74 77 61 72 65 49 6e 66 6f 73 22 3a 5b 7b 22 73 6e 22 3a 31 2c 22 6e 61 6d 65 22 3a 22 45 50 32 35 48 41 5a 55 31 31 31 22 2c 22 76 65 72 73 69 6f 6e 22 3a 22 31 30 30 30 30 30 30 30 30 30 22 7d 2c 7b 22 73 6e 22 3a 32 2c 22 6e 61 6d 65 22 3a 22 45 50 32 35 48 41 5a 55 32 31 31 22 2c 22 76 65 72 73 69 6f 6e 22 3a 22 31 30 30 30 30 30 30 30 30 30 22 7d 5d 7d";
        byte[] testArray = stringToByteArray(messageDevUnder);
        UnSafeHeapBuffer buffer = new UnSafeHeapBuffer(testArray);
//        buffer=RC4Utils.rc4Deal(buffer);
//        System.out.println(ByteUtils.formatHexString(buffer.array()));
        ModbusMessageRunnable runnable = new ModbusMessageRunnable(null, buffer);
        new Thread(runnable).start();

//        System.out.println("softInfo responce is .." + ByteUtils.formatHexString(buffer.array()));
//        //去掉功能码和子功能码以及设备的二级地址。
//        buffer.skipBytes(6);
//        buffer.skipBytes(3);
//        int status = buffer.readByte();
//        int process=buffer.readInt();
//        process = process >> 11;
//        process = process << 13;
//        process = process >> 13;
//        System.out.println(" process is " + process);

    }

    public static byte[] stringToByteArray(String messageStr) {
        messageStr = messageStr.trim();
        String[] strArray = messageStr.split(" ");
        byte[] result = new byte[strArray.length];
        for (int i = 0; i < strArray.length; i++) {
            String a = strArray[i];
            result[i] = (byte) Integer.parseInt(a, 16);
        }
        return result;
    }
}
