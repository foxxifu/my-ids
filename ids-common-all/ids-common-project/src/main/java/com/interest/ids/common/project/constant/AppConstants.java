package com.interest.ids.common.project.constant;

/**
 * @Author: sunbjx
 * @Description: 系统数据常量
 * @Date Created in 10:11 2018/1/19
 * @Modified By:
 */
public interface AppConstants {

    /**
     * 用户类型
     */
    String SYSTEM_USER = "system";
    String ENTERPRISE_USER = "enterprise";

    /**
     * 点表数据创建类型
     */
    int SIGNAL_DATA_BY_SYSTEM = 1;  // 系统初始化
    int SIGNAL_DATA_BY_USER = 2;    // 用户导入
    String TOKEN_KEY = "tokenId";
}
