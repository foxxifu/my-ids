package com.interest.ids.dev.network.modbus.utils;

import io.netty.buffer.ByteBuf;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.interest.ids.common.project.buffer.UnSafeHeapBuffer;


/**
 * 
 * @author lhq
 *
 */
public class ByteUtils {

	private static final Logger log = LoggerFactory.getLogger(ByteUtils.class);

    public static byte encodeUnsigned(short positive) {
        if(positive < 128){
            return (byte) positive;
        }
        else{
            return (byte) (-(256 - positive));
        }
    }
    
    //针对103的可变结构限定词
    public static byte decodeUnsignedByte(byte signed){
        short sq = decodeUnsigned(signed);
        if(sq > 128){
            sq -= 128;
        }
        return (byte) sq;
    }
    
    public static short decodeUnsigned(byte signed) {
    	
    	return (short) (signed & 0xFF);
    }

    public static short encodeUnsigned(int positive) {
        if(positive < 32768)
            return (short) positive;
        else return (short) (-(0x10000 - positive));
    }

    public static int decodeUnsigned(short signed) {
    	return signed & 0xFFFF;
    }
    
    public static long getInt(int signed) {
    	
    	return signed & 0xFFFFFFFFL;
    }
    

    public static int encodeUnsigned(long positive) {
        if(positive < (long) 0x7FFFFFFF + 1)
            return (int) positive;
        else return (int) (-((1L << 32) - positive));
    }

    public static long decodeUnsigned(int signed) {
    	return signed & 0xFFFFFFFFL;
    }

    public static String formatHexString(byte[] bytearray) {
        StringBuffer hexString = new StringBuffer("hex string = ");
        for (int i = 0; i < bytearray.length; ++i){
            String hexStr = Integer.toHexString(ByteUtils
                .decodeUnsigned(bytearray[i]));
            if(hexStr.length() == 1){
                hexStr = "0" + hexStr;
            }
            hexString.append(hexStr);
            hexString.append(" ");
        }
        return hexString.toString();
    }
    
    public static Object decodeBigEndianObject(UnSafeHeapBuffer buffer,int paraType, int paraLen){
        Object tmp = null;
        switch (paraType) {
        //无符号数
		case 0:
			if(paraLen == 1){
                tmp = buffer.readUnsignedByte();
            }
            else if(paraLen == 2){
            	tmp = buffer.readUnsignedShort();
            }
            else if(paraLen == 4){
            	tmp = buffer.readUnsignedInt();
            }
            else{
                log.error("Error data lenth!");
            }
			break;
		case 1:
			 if(paraLen == 1){
	            	tmp = buffer.readByte();
	            }
	            else if(paraLen == 2){
	            	tmp = buffer.readShort();
	            }
	            else if(paraLen == 4){
	            	tmp = buffer.readInt();
	            }
	            else{
	                log.error("Error data lenth!");
	            }
			break;
		case 2:
			tmp = buffer.readFloat();
		case 3:
			try{
            	byte[] data = new byte[paraLen];
            	buffer.readBytes(data);
            	tmp = new String(data, 0, paraLen,"ASCII");
            }
            catch(Exception e){
                log.error("decodeBigEndianObject UnsupportedEncodingException:",e);
            }
		default:
			log.error("unknow data type!");
			break;
		}
        return tmp;
    }
    
    
    

    //处理ACSII不可见字符
    public static String filterStr(String str){
        try{
            if(StringUtils.isBlank(str)){
                return str;
            }
            char[] charArr = str.toCharArray();
            for(int i=0;i<charArr.length;i++){
                if(charArr[i] < 0x20 || charArr[i] == 0x7f){
                    charArr[i] = 0x20;
                }
            }
            return new String(charArr).trim();           
        }catch(Exception e){
            log.error("filterStr error "+str,e);
        }
        return str;
    }
    
    public static Object getLocationBit(String hexString,int len)	{
		if (hexString == null || hexString.length() % 2 != 0)	
			return null;		
		String bString = "", tmp;		
		for (int i = 0; i < hexString.length(); i++){
			tmp = "0000"+ Integer.toBinaryString(Integer.parseInt(hexString.substring(i, i + 1), 16));
			bString += tmp.substring(tmp.length() - 4);		
		}		
		
		char tem[] = bString.toCharArray();
		
	    return tem[len];	
	 }
    
  
    
    public static String decodeParameteBigEndian(ByteBuf buffer,int paraType, int paraLen) {
            String tmp = "";
            if(paraType == 0){
                if(paraLen == 1){
                	tmp = String.valueOf(buffer.readUnsignedByte());
                }
                else if(paraLen == 2){
                    tmp = String.valueOf(buffer.readUnsignedShort());
                }
                else if(paraLen == 4){
                    tmp = String.valueOf(buffer.readUnsignedInt());
                }
                else{
                    log.error("Error data lenth!");
                }
            }
            else if(paraType == 1){
                if(paraLen == 1){
                    tmp = String.valueOf(buffer.readByte());
                }
                else if(paraLen == 2){
                    tmp = String.valueOf(buffer.readShort());
                }
                else if(paraLen == 4){
                    tmp = String.valueOf(buffer.readInt());
                }
                else{
                    log.error("Error data lenth!");
                }
            }
            // 浮点数
            else if(paraType == 2){
                tmp = String.valueOf(buffer.readFloat());
            }
            // 字符串
            else if(paraType == 3){
                try{
                	byte[] data = new byte[paraLen];
                	buffer.readBytes(data);
                    tmp = new String(data, 0, paraLen,"ASCII");
                }
                catch(Exception e){
                    log.error("decodeParameteBigEndian UnsupportedEncodingException:",e);
                }
            }
            else{
                log.error("Unknow data type!");
            }
            return tmp.trim();
        }
    
    public static String decodeParameteBigEndian(UnSafeHeapBuffer buffer,int paraType, int paraLen) {
        String tmp = "";
        if(paraType == 0){
            if(paraLen == 1){
            	tmp = String.valueOf(buffer.readUnsignedByte());
            }
            else if(paraLen == 2){
                tmp = String.valueOf(buffer.readUnsignedShort());
            }
            else if(paraLen == 4){
                tmp = String.valueOf(buffer.readUnsignedInt());
            }
            else{
                log.error("Error data lenth!");
            }
        }
        else if(paraType == 1){
            if(paraLen == 1){
                tmp = String.valueOf(buffer.readByte());
            }
            else if(paraLen == 2){
                tmp = String.valueOf(buffer.readShort());
            }
            else if(paraLen == 4){
                tmp = String.valueOf(buffer.readInt());
            }
            else{
                log.error("Error data lenth!");
            }
        }
        // 浮点数
        else if(paraType == 2){
            tmp = String.valueOf(buffer.readFloat());
        }
        // 字符串
        else if(paraType == 3){
            try{
            	byte[] data = new byte[paraLen];
            	buffer.readBytes(data);
                tmp = new String(data, 0, paraLen,"ASCII");
            }
            catch(Exception e){
                log.error("decodeParameteBigEndian UnsupportedEncodingException:",e);
            }
        }
        else{
            log.error("Unknow data type!");
        }
        return tmp.trim();
    }

   
    
    public static String decodeParameteLittleEndian(ByteBuf buffer,int paraType, int paraLen) {
    	
    		byte[] data = new byte[paraLen];
    		buffer.readBytes(data);
    		ByteBuffer buf = ByteBuffer.wrap(data).order(ByteOrder.LITTLE_ENDIAN);
            String tmp = "";
            if(paraType == 0){
                if(paraLen == 1){
                	tmp = String.valueOf(decodeUnsigned(buf.get()));
                }
                else if(paraLen == 2){
                    tmp = String.valueOf(decodeUnsigned(buf.getShort()));
                }
                else if(paraLen == 4){
                    tmp = String.valueOf(decodeUnsigned(buf.getInt()));
                }
                else{
                    log.error("Error data lenth!");
                }
            }
            else if(paraType == 1){
                if(paraLen == 1){
                    tmp = String.valueOf(buf.get());
                }
                else if(paraLen == 2){
                    tmp = String.valueOf(buf.getShort());
                }
                else if(paraLen == 4){
                    tmp = String.valueOf(buf.getInt());
                }
                else{
                    log.error("Error data lenth!");
                }
            }
            // 浮点数
            else if(paraType == 2){
                tmp = String.valueOf(buf.getFloat());
            }
            // 字符串
            else if(paraType == 3){
                try{
                    tmp = new String(data, 0, paraLen,"ASCII");
                }
                catch(Exception e){
                    log.error("decodeParameteLittleEndian UnsupportedEncodingException:",e);
                }
            }
            else{
                log.error("Unknow data type!");
            }
            return tmp.trim();
        }

    
    
    public static void encodeParameteBigEndian(UnSafeHeapBuffer bytebuffer,
            Object paraValue, int paraType, int paraLen) {
    	
	    	switch (paraType) {
				case 0:
				case 1:
					long tmp = Long.decode(String.valueOf(paraValue)).intValue();
		            if(paraLen == 1){
		                bytebuffer.writeByte((byte)tmp);
		            }
		            else if(paraLen == 2){
		            	bytebuffer.writeShort((short) tmp);
		            }
		            else if(paraLen == 4){
		                //bytebuffer.writeInt(encodeUnsigned(tmp));
		            	bytebuffer.writeInt((int) tmp);
		            }
		            else if(paraLen == 8){
		            	bytebuffer.writeLong(tmp);
		            }
		            else{
		                log.error("Error data lenth!");
		            }
					break;
					
				case 2:
					 float v = Float.parseFloat((String) paraValue);
		             bytebuffer.writeFloat(v);
		             break;
				case 3:
					 byte[] data = new byte[paraLen];
		             int valLen = ((String) paraValue).length();
		             System.arraycopy(((String) paraValue).getBytes(), 0, data, 0, valLen);
		             bytebuffer.writeBytes(data);
		             break;
		             
				default:
					log.error("Unknow data type!");
			}
        }
    
    
	public static void encodeParameteBigEndian(ByteBuf buffer,
    		Object paraValue, int paraType, int paraLen) {
    		buffer.order(ByteOrder.BIG_ENDIAN);
            // 整型
            if(paraType == 0 || paraType == 1){
                long tmp = Long.decode(String.valueOf(paraValue)).intValue();
                if(paraLen == 1){
                	buffer.writeByte((byte) tmp);
                }
                else if(paraLen == 2){
                	buffer.writeShort((short) tmp);
                }
                else if(paraLen == 4){
                	buffer.writeInt((int)tmp);
                }
                else{
                    log.error("Error data lenth!");
                }
            }
            // 浮点数
            else if(paraType == 2){
                float tmp = Float.parseFloat((String) paraValue);
                buffer.writeFloat(tmp);
            }
            // 字符串
            else if(paraType == 3){
                byte[] tmp = new byte[paraLen];
                int valLen = ((String) paraValue).length();
                System.arraycopy(((String) paraValue).getBytes(), 0, tmp, 0, valLen);
                buffer.writeBytes(tmp);
            }
            //
//            else if(paraType == 4){
            	//buffer.writeBytes(abyte0);
                //bytebuffer.appendBytes(((SimpleByteBuffer) paraValue).getBuffer(), paraLen);
//            }
            // 其它
            else{
                log.error("Unknow data type!");
            }
        }


   
    
	public static void encodeParameteLittleEndian(ByteBuf buffer,
            Object paraValue, int paraType, int paraLen) {
    		buffer.order(ByteOrder.LITTLE_ENDIAN);
            if(paraType == 0 || paraType == 1){
                int tmp = Integer.decode(String.valueOf(paraValue)).intValue();
                if(paraLen == 1){
                	buffer.writeByte((byte) tmp);
                }
                else if(paraLen == 2){
                	buffer.writeShort((short) tmp);
                }
                else if(paraLen == 4){
                	buffer.writeInt(tmp);
                }
                else{
                    log.error("Error data lenth!");
                }
            }
            // 浮点数
            else if(paraType == 2){
                float tmp = Float.parseFloat((String) paraValue);
                buffer.writeFloat(tmp);
            }
            // 字符串
            else if(paraType == 3){
                byte[] tmp = new byte[paraLen];
                int valLen = ((String) paraValue).length();
                System.arraycopy(((String) paraValue).getBytes(), 0, tmp, 0, valLen);
                buffer.writeBytes(tmp);
            }
            // 其它
//            else if(paraType == 4){
                //bytebuffer.appendBytes(((SimpleByteBuffer) paraValue).getBuffer(), paraLen);
//            }
            else{
                log.error("Unknow data type!");
            }
        }
    

    /**
     * 将IP地址转化为long型整数
     * 
     * @param ipAdress
     * @return long
     */

    public static long convertIP2Int(java.net.InetAddress ipAdress) {
        byte[] tmp = ipAdress.getAddress();
        int result = 0;
        result |= decodeUnsigned(tmp[0]);
        result <<= 8;
        result |= decodeUnsigned(tmp[1]);
        result <<= 8;
        result |= decodeUnsigned(tmp[2]);
        result <<= 8;
        result |= decodeUnsigned(tmp[3]);
        return result;
    }

    public static String conventByteToString(Integer obj) {
        StringBuffer hexString = new StringBuffer("0x");
        String hexStr = String.format("%X", obj);
        if(hexStr.length() == 1){
            hexStr = "0" + hexStr;
        }
        hexString.append(hexStr);
        return hexString.toString();
    }

    public static void deleteSubFile(File file) {
        if(file.exists()){ // 判断文件是否存在
            if(file.isFile()){ // 判断是否是文件
                if(!file.delete()) // delete()方法;
                {
                    log.info("delete file failed,file is :"
                        + file.getAbsolutePath());
                }
            }
            else if(file.isDirectory()){ // 否则如果它是一个目录
                File files[] = file.listFiles(); // 声明目录下所有的文件
                // files[];
                for (int i = 0; i < files.length; i++){ // 遍历目录下所有的文件
                    deleteFile(files[i]); // 把每个文件 用这个方法进行迭代
                }

            }
        }
    }

    public static void deleteFile(File file) {
        if(file.exists()){ // 判断文件是否存在
            if(file.isFile()){ // 判断是否是文件
                if(!file.delete()) // delete()方法;
                {
                    log.info("delete file failed,file is :"
                        + file.getAbsolutePath());
                }
            }
            else if(file.isDirectory()){ // 否则如果它是一个目录
                File files[] = file.listFiles(); // 声明目录下所有的文件
                // files[];
                for (int i = 0; i < files.length; i++){ // 遍历目录下所有的文件
                    deleteFile(files[i]); // 把每个文件 用这个方法进行迭代
                }

                // 删除父目录
                if(!file.delete()){
                    log.info("delete directory failed,directory is :"
                        + file.getAbsolutePath());
                }
            }
        }
    }
    
    public static int[] decodeHighLow(String num){
        try{
            if(num == null){
                return null;
            }
            int totalNum = Integer.parseInt(num);
            int[] retArr = new int[2];
            retArr[1] = (int) (totalNum & 0x0000FFFFL);
            totalNum >>= 16;
            retArr[0] = (int) (totalNum & 0x0000FFFFL);
            return retArr;            
        }catch(Exception e){
            log.error("decodeHighLow error"+num,e);
        }
        return null;
    }

    /**
     * 处理户用逆变器订阅回复的数据单元个数
     * @param buffer
     * @return
     */
    public static int[] decodeHouseSubRegistNum(ByteBuf buffer){
        int[] retArr = new int[2];
        try{
            int regVal = Integer.valueOf(decodeParameteBigEndian(buffer, 0, 1));
            int tmp = regVal;
            retArr[0] = tmp & 0xFF3F;
            int tmp2 = regVal;
            tmp2 = (tmp2 & 0x80) >> 7;
            retArr[1] = tmp2;            
            return retArr;
        }catch(Exception e){
            log.error("decodeHouseSubRegistNum error",e);
        }
        return null;
    }
    
    /**
     * 获取指定bit位的值
     * @param value 原始值
     * @param location bit位
     * @return
     */
    public static int getBitValue(long value,int location){
        long bitValue = 0;
        bitValue = (1<<location) & value;
        if(bitValue == 0){
            return 0;
        }else{
            return 1;
        }
    }
}
