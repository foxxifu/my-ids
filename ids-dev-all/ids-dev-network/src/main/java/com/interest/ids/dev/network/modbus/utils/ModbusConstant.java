package com.interest.ids.dev.network.modbus.utils;


/**
 * 
 * @author lhq
 *
 *
 */
public class ModbusConstant {

    public static final byte HEART_BEAT_CMD = 0x65;
    
    public static final byte HEART_BEAT_SUBCMD = 0x07;
    
    public static final String IP = "ip";
    
    public static final String PORT = "port";
    
    public static final String DEVICE_ADDRESS = "deviceAddress";
  
    public static final String ERROR_0X7FFF = "32767";
    
    public static final String ERROR_0XFFFF = "65535";
    
    public static final String ERROR_0X7FFFFFFF = "2147483647";
    
    public static final String ERROR_0XFFFFFFFF = "4294967295";
    
    public static final String ERROR_VALUE = "N/A";
    
    public static final String ERROR_LING = "999";
    
    public static final String STARTADDR = "startAddr";
    public static final String REGIST_VALUE = "registValue";
    // 数据订阅业务
    public static final String SUBSCRIBE = "subscribe";
    
    public static final String ALL_SUBSCRIBE = "allSubscribe";
    
    //功能码
    public static final String MODBUS_CMDCODE = "cmdCode";
    
    public static final String REGISTER_NUM = "registerNum";
    //写单个寄存器
    public static final String WRITE_ONE_REGISTER = "WRITE_ONE_REGISTER";
    //写多个寄存器
    public static final String WRITE_MANY_REGISTER = "WRITE_MANY_REGISTER";

    public static final String SUBTYPE = "subType";
    
    public static final String DEVICE_CHANNEL = "deviceChannel";

    // 查询命0x03,读寄存器值
    public static final String READ_REGISTER = "READ_REGISTER";
    
    public static final String SUBSCRIBERESULT = "subscribeResult";
    
    public static final String REGISTPERIOD = "registPeriod";
    // 读多个寄存器命令，寄存器上限
    public static final int REQUEST_LIMIT_UPS = 10;
    
    public static final int REQUEST_LIMIT_SUN = 120;
    
    //错误码
    public static final String ERROR_CODE = "0xC1";

    //异常码
    public static final int ERRRO_0x06 = 0x06;
   
    public static final String SMARTLOG_UTC = "UTCTime";
    
    public static final String OPEN_ZONE = "0xffffffff";

    public static final String FTP_PM_DATA_TIME = "createTime";

    public static final String CMD_CODE = "cmdCode";
    
    public static final String SUBCOMDCODE = "subCmdCode";
    
    public static final String DATALEN = "dataLen";
    
    public static final String CHILDDATA = "childDataLen";
   
    
   
  //异常信息
    public static final String ILLEGAL_FUNCTION_01 = "ILLEGAL FUNCTION CODE";
    public static final String ILLEGAL_DATA_ADDRESS_02 = "ILLEGAL DATA ADRESS";
    public static final String ILLEGAL_DATA_VALUE_03 = "ILLEGAL DATA　VALUE";
    public static final String ILLEGAL_SERVER_BREAKDOWN_04 = "ILLEGAL SERVER BREAKDOWN";
    public static final String SERVER_CONFIRM_05 = "SERVER CONFIRM";
    public static final String SLAVE_DEVICE_BUSY_06 = "SLAVE DEVICE BUSY";
    public static final String MEMORY_ODD_EVEN_08 = "MEMORY ODD EVEN WRONG";
    public static final String FORBID_GATEWAY_0A = "FORBID GATEWAY";
    public static final String DEVICE_RESPONSE_FALIED_0B = "DEVICE RESPONSE FALIED";
    public static final String HAVE_NO_AUTHORITY_80 = "HAVE　NO AUTHORITY";
    public static final String SSL_DHE_RSA_WITH_DES_CBC_SHA = "SSL_DHE_RSA_WITH_DES_CBC_SHA";
    public static final String SSL_DHE_RSA_EXPORT_WITH_DES40_CBC_SHA = "SSL_DHE_RSA_EXPORT_WITH_DES40_CBC_SHA";
    public static final String SSL_RSA_WITH_DES_CBC_SHA = "SSL_RSA_WITH_DES_CBC_SHA";
    public static final String SSL_RSA_EXPORT_WITH_RC4_40_MD5 = "SSL_RSA_EXPORT_WITH_RC4_40_MD5";
    public static final String SSL_RSA_EXPORT_WITH_DES40_CBC_SHA = "SSL_RSA_EXPORT_WITH_DES40_CBC_SHA";
    public static final String SSL_RSA_WITH_RC4_128_MD5 = "SSL_RSA_WITH_RC4_128_MD5";
    public static final String SSL_RSA_WITH_RC4_128_SHA = "SSL_RSA_WITH_RC4_128_SHA";
  
}
