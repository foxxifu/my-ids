/*
 * Copyright (C) TD Tech<br>
 * All Rights Reserved.<br>
 * 
 */
package com.interest.ids.dev.network.modbus.exception;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.interest.ids.common.project.buffer.UnSafeHeapBuffer;
import com.interest.ids.dev.api.utils.ByteUtils;
import com.interest.ids.dev.network.modbus.utils.ModbusConstant;


public class NeResponseException extends Exception{
    
    private static final long serialVersionUID = 1L;
	private static final Logger log = LoggerFactory.getLogger(NeResponseException.class);

    private static Map<Integer,String> map = new HashMap<Integer,String>();
    /**
     * @param args
     */
    private String responseErrorCode;
    private int responseExceptionCode;
    private static String exceptionMsg = "No enough data in buffer.";
    
    public NeResponseException(int responseExceptionCode){
        this.responseExceptionCode = responseExceptionCode;
    }
    
    public NeResponseException ()
    {
        super(exceptionMsg);
        responseErrorCode = "";
        responseExceptionCode = -1;
    }
    //解析异常码
    public void parse(UnSafeHeapBuffer buffer)
    {
        //首先移除设备号
        buffer.readByte();
        int errorcode = buffer.readUnsignedByte();
        //responseErrorCode = ByteUtils.conventByteToString(errorcode);
        responseExceptionCode = buffer.readUnsignedByte();
        log.info("responseErrorCode = " + responseErrorCode);
        log.info("responseExceptionCode = " + responseExceptionCode);
        log.error("response exception : "+getException(responseExceptionCode));
        exceptionMsg = getException(responseExceptionCode);
    }

    public String getResponseErrorCode()
    {
        return responseErrorCode;
    }
    
    public int getResponseExceptionCode()
    {
        return responseExceptionCode;
    }
    static{
        map.put(0x01, ModbusConstant.ILLEGAL_FUNCTION_01);
        map.put(0x02, ModbusConstant.ILLEGAL_DATA_ADDRESS_02);
        map.put(0x03, ModbusConstant.ILLEGAL_DATA_VALUE_03);
        map.put(0x04, ModbusConstant.ILLEGAL_SERVER_BREAKDOWN_04);
        map.put(0x05, ModbusConstant.SERVER_CONFIRM_05);
        map.put(0x06, ModbusConstant.SLAVE_DEVICE_BUSY_06);
        map.put(0x08, ModbusConstant.MEMORY_ODD_EVEN_08);
        map.put(0x0A, ModbusConstant.FORBID_GATEWAY_0A);
        map.put(0x0B, ModbusConstant.DEVICE_RESPONSE_FALIED_0B);
        map.put(0x80, ModbusConstant.HAVE_NO_AUTHORITY_80);
    }
    private  String getException(int exceptionCode){
        
        if(map.containsKey(exceptionCode)){
            return map.get(exceptionCode);
        }
        return exceptionMsg;
    }

}
