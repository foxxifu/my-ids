package com.interest.ids.common.project.bean;

/**
 * @Author: sunbjx
 * @Description: 用户通用DTO
 * @Date Created in 10:07 2018/1/18
 * @Modified By:
 */
public class UserParams {

    // 用户ID
    private Long userId;
    // 用户类型
    private String userType;

    private Long enterpriseId;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public Long getEnterpriseId() {
        return enterpriseId;
    }

    public void setEnterpriseId(Long enterpriseId) {
        this.enterpriseId = enterpriseId;
    }

}
