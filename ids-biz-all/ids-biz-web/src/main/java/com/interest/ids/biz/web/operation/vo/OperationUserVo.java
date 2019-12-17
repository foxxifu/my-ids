package com.interest.ids.biz.web.operation.vo;

public class OperationUserVo {

    /**
     * 用户编号
     */
    private Long userId;

    /**
     * 用户名
     */
    private String userName;

    /**
     * 纬度
     */
    private Double latitude;

    /**
     * 经度
     */
    private Double longitude;

    /**
     * 登录状态
     */
    private Byte loginStatus;

    /**
     * 运维人员正消缺任务数
     */
    private Integer totalTask;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public Byte getLoginStatus() {
        return loginStatus;
    }

    public void setLoginStatus(Byte loginStatus) {
        this.loginStatus = loginStatus;
    }

    public Integer getTotalTask() {
        return totalTask;
    }

    public void setTotalTask(Integer totalTask) {
        this.totalTask = totalTask;
    }

    public static enum LoginStatus {
        LOGING((byte) 0, "登录"), LOGINOUT((byte) 1, "退出");

        private byte code;

        private String desc;

        private LoginStatus(byte code, String desc) {
            this.code = code;
            this.desc = desc;
        }

        public byte getCode() {
            return code;
        }

        public void setCode(byte code) {
            this.code = code;
        }

        public String getDesc() {
            return desc;
        }

        public void setDesc(String desc) {
            this.desc = desc;
        }
    }
}
