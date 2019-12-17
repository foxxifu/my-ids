package com.interest.ids.dev.api.utils;

import java.io.UnsupportedEncodingException;

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
   
    public static short decodeUnsigned(byte signed) {
    	
    	return (short) (signed & 0xFF);
    }
    
    public static String decodeParameteLittleEndian(UnSafeHeapBuffer buffer,int paraType, int paraLen) {
        String tmp = "";
        if(paraType == 0){
            if(paraLen == 1){
            	tmp = String.valueOf(buffer.readUnsignedByte());
            }
            else if(paraLen == 2){
                tmp = String.valueOf(buffer.readUnsignedShortLittleEndian());
            }
            else if(paraLen == 4){
                tmp = String.valueOf(buffer.readUnsignedIntLittleEndian());
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
                tmp = String.valueOf(buffer.readShortLittleEndian());
            }
            else if(paraLen == 4){
                tmp = String.valueOf(buffer.readIntLittleEndian());
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
            	tmp = getStr(data);
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
    /**
     * 去掉byte数组中的00补位
     * @param data
     * @return
     * @throws UnsupportedEncodingException
     */
    private static String getStr(byte[] data) throws UnsupportedEncodingException{
    	int len = 0;
    	for(int i = 0; i < data.length; i++) {
    		if (data[i] != 0x00 && data[i] != (byte)0xff) {
    			len ++;
    		}
    	}
    	String str = new String(data, 0, len,"ASCII");
    	return StringUtils.trim(str);
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
    
    
    	 
    public static String conventByteToString(Integer obj) {
        StringBuffer hexString = new StringBuffer("0x");
        String hexStr = String.format("%X", obj);
        if(hexStr.length() == 1){
            hexStr = "0" + hexStr;
        }
        hexString.append(hexStr);
        return hexString.toString();
    }
	
}
