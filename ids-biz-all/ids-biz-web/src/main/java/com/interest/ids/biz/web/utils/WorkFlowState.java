package com.interest.ids.biz.web.utils;

public class WorkFlowState 
{
    public static final String PROCEEE_STATE_EXAMINE = "1"; //待审核
    public static final String PROCEEE_STATE_EXAMINE_CONTENT = "等待审核"; //待审核
    
    public static final String PROCEEE_STATE_DEALING = "2"; //消缺中
    public static final String PROCEEE_STATE_DEALING_CONTENT = "正在处理"; //消缺中
    
    public static final String PROCEEE_STATE_WAIT_SURE = "3"; //待确认
    public static final String PROCEEE_STATE_WAIT_SURE_CONTENT = "处理完成等待确认"; //待确认
    
    public static final String PROCEEE_STATE_WAIT_FINISH = "4"; //已完成
    public static final String PROCEEE_STATE_WAIT_FINISH_CONTENT = "确认完成"; //待确认
    
    public static final String DEAL_STATE_NOT_DEAL = "1"; //不处理
    
    public static final String DEAL_STATE_NOT_COMPLETE = "2"; //不完全处理
    
    public static final String DEAL_STATE_FINISH = "3"; //完全处理
    
    
}
