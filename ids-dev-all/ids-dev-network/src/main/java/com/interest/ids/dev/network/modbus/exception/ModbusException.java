package com.interest.ids.dev.network.modbus.exception;

import java.util.HashMap;
import java.util.Map;




public class ModbusException extends Exception{
    
    private static final long serialVersionUID = 1L;
    private static Map<Integer,String> errorMsg = new HashMap<Integer,String>();
    private String responseErrorCode;
    private String responseExceptionCode;
    
    private int type = 0;
    //异常信息
    private String errorMessage = "";
    
    public final static int COMMON_EXCEPTION = 0;
    //非法功能
    public final static int ILLEGAL_EXCEPTION = 1;
    //非法地址
    public final static int ILLEGAL_ADDRESS_EXCEPTION = 2;
    //非法数据值
    public final static int PROTOCOL_EXCEPTION = 3;

    public final static int PROTOCOL_TIMEOUT_EXCEPTION = 5;
    
    public final static int SESSION_EXCEPTION = 6;
   
    
   
    public ModbusException(int type,String msg){
        super(msg);
        this.type = type;
    }
    
    public ModbusException(String msg){
        super(msg);
    }
   /* public void parse(SimpleByteBuffer buffer)
    {
        
        responseErrorCode = ByteUtils.conventByteToString(Integer.valueOf(ByteUtils.decodeUnsigned(buffer.removeByte())));
        responseExceptionCode = ByteUtils.conventByteToString(Integer.valueOf(ByteUtils.decodeUnsigned(buffer.removeByte())));
    }*/
    
    static{
        errorMsg.put(ILLEGAL_EXCEPTION, "illegal exception");
        errorMsg.put(ILLEGAL_ADDRESS_EXCEPTION, "illegal address exception");
        errorMsg.put(PROTOCOL_EXCEPTION, "illegal data exception");
        errorMsg.put(COMMON_EXCEPTION, "common exception");
        errorMsg.put(PROTOCOL_TIMEOUT_EXCEPTION, "protocol timeout exception");
        errorMsg.put(SESSION_EXCEPTION, "session exception");
        errorMsg.put(ILLEGAL_ADDRESS_EXCEPTION, "illegal address exception");
    }
    public String getErrorMessage(Integer type){
        
        if(errorMsg.containsKey(type)){
            return errorMsg.get(type);
        }
        return errorMessage;
    }

    public String getResponseErrorCode()
    {
        return responseErrorCode;
    }
    
    public String getResponseExceptionCode()
    {
        return responseExceptionCode;
    }

	public int getType() {
		return type;
	}
    
    
    
}
