package com.interest.ids.common.project.constant;

public class WorkFlowConstant 
{
    /**
     * 待分配
     */
    public static final String PROC_STATUS_WAITING = "0";
    
    /**
     * 消缺中
     */
    public static final String PROC_STATUS_DEALING = "1";
    
    /**
     * 待审核
     */
    public static final String PROC_STATUS_NOT_SURE = "2";
    
    /**
     * 已完成
     */
    public static final String PROC_STATUS_FINISHED = "3";
    
    /**
     * 未操作标记
     */
    public static final String TASK_NOT_OPERATION_STATUS = "0";
    
    /**
     * 已操作标记
     */
    public static final String TASK_OPERATIONED_STATUS = "1";
    
    /**
     * 任务状态 - 未完成
     */
    public static final String TASK_STATE_NOT_FINISHED = "0";
    
    /**
     * 任务状态-已完成
     */
    public static final String TASK_STATE_FINISHED = "1";
    
    /**
     * 处理结果-未处理1:未完全消除 2:已完全消除 3:不处理
     */
    public static final String TASK_DEAL_RESULT_NOT_START = "1";
    
    public static final String TASK_DEAL_RESULT_FINISHED = "2";
    
    public static final String TASK_DEAL_RESULT_NOT_DEAL = "3";
}
