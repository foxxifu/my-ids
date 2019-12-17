package com.interest.ids.common.project.constant;

/**
 * @author sunbjx
 * @title ResponseConstants.java
 * @update 2017年12月4日 上午9:40:13
 */
public interface ResponseConstants {

    /*---- http ----*/
    int CODE_AUTH = -1; // 无权限
    int CODE_FAILED = 0; // 请求失败
    int CODE_SUCCESS = 1; // 请求成功

    int CODE_ERROR_PARAM = 2;// 参数错误
    int CODE_ERROR_NODEVICE = 3;// 无此sn的设备信息，电站管理使用
    int CODE_ERROR_INUSERDEVICE = 4;// 此sn的设备信息已经使用
    int CODE_PARAMS_CROSSING = 5;    // 参数值越界
    int CODE_PARAMS_USING = 6;    // 参数值被占用
    
    int CODE_FILETYPE_NOTMATCH = 20; // 文件类型不匹配
    
    int CODE_LOGIN_FAIL = 1001; //登陆失败
    int CODE_ACCESS_FAIL = 1002; //用户访问的url没有访问权限
    int CODE_SESSION_TIMEOUT = 1003; // session过期
    int CODE_NO_ROLE = 1004; //用户没有任何的权限

    String CODE_FAILED_SESSION_FAILURE = "SESSION FAILURE";    // 不涉及负载均衡
    String CODE_FAILED_TOKEN_FAILURE = "TOKEN FAILURE";
    String CODE_SUCCESS_VALUE = "SUCCESS";
    String CODE_FAILED_VALUE = "FAILED";
    String CODE_EXCEPTION_VALUE = "EXCEPTION";
    String CODE_ERROR_PARAM_VALUE = "ERROR PARAM";
    String CODE_ERROR_NODEVICE_VALUE = "NO DEVICE";
    String CODE_ERROR_INUSERDEVICE_VALUE = "INUSER DEVICE";
    String CODE_PARAMS_CROSSING_VALUE = "PARAMS CROSSING";
    String CODE_PARAMS_USING_VALUE = "PARAMS USING";
    String CODE_FILETYPE_NOTMATCH_VALUE = "FILE TYPE NOT MATCH";
    
    /**
     * 请求携带的当前请求数据的语言参数的key = lang
     */
    String REQ_LANG = "lang";
    /**
     * 英文： en
     */
    String EN_LANG = "en"; // 英文语言的请求方式
    /**
     * 中文： zh
     */
    String ZH_LANG = "zh"; // 中文语言的请求头
}
