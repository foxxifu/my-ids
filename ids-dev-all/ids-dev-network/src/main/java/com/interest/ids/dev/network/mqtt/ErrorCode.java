/*
 *
 *  * iot.chinamobil.com Inc.
 *  * Copyright(c) 2012-2018 All Rights Reserved.
 *
 */
package com.interest.ids.dev.network.mqtt;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;



/**
 * 协议错误消息枚举
 *
 * @author chenshiqiang
 * @version TypeId.java, v0.1  2018/10/17 10:48 chenshiqiang Exp $
 */
public enum ErrorCode {

    /**
     * 成功
     */
    SUCCESS(0, "SUCCESS", "SUCCESS", "成功"),

    /**
     * 认证失败
     */
    AUTH_FAILED(101, "AUTH_FAILED", "AUTH_FAILED", "认证失败，该设备不存在"),

    /**
     * 服务器内部错误
     */
    INTERNAL_ERROR(102, "INTERNAL_ERROR", "INTERNAL_ERROR", "服务器内部错误"),

    /**
     * 消息头验证失败
     */
    MESSAGE_HEADER_ERROR(103, "MESSAGE_HEADER_ERROR", "MESSAGE_HEADER_ERROR", "消息头验证失败"),

    /**
     * 消息体不满足约束
     */
    MESSAGE_BODY_NOT_SATISFIED(104, "MESSAGE_BODY_NOT_SATISFIED", "MESSAGE_BODY_NOT_SATISFIED", "消息体不满足约束"),

    /**
     * 消息体大小超限
     */
    MESSAGE_BODY_OUT_OF_LIMIT(105, "MESSAGE_BODY_OUT_OF_LIMIT", "MESSAGE_BODY_OUT_OF_LIMIT", "消息体大小超限"),;

    /**
     * 编码
     */
    private final int code;

    /**
     * 英文名
     */
    private final String englishName;

    /**
     * 中文名
     */
    private final String chineseName;

    /**
     * 描述说明
     */
    private final String description;

    /**
     * 枚举构造函数
     *
     * @param code        编码
     * @param englishName 英文名称
     * @param chineseName 中文名称
     * @param description 描述
     */
    ErrorCode(int code, String englishName, String chineseName, String description) {
        this.code = code;
        this.englishName = englishName;
        this.chineseName = chineseName;
        this.description = description;
    }

    /**
     * 根据编码获取枚举
     *
     * @param code 编码
     * @return TypeId 枚举
     */
    public static ErrorCode getErrorCode(int code) {
        for (ErrorCode typeId : ErrorCode.values()) {
            if (typeId.getCode() == code) {
                return typeId;
            }
        }
        return null;
    }

    /**
     * Getter method for property <tt>code</tt>.
     *
     * @return property value of code
     */
    public int getCode() {
        return code;
    }

    /**
     * Getter method for property <tt>englishName</tt>.
     *
     * @return property value of englishName
     */
    public String getEnglishName() {
        return englishName;
    }

    /**
     * Getter method for property <tt>chineseName</tt>.
     *
     * @return property value of chineseName
     */
    public String getChineseName() {
        return chineseName;
    }

    /**
     * Getter method for property <tt>description</tt>.
     *
     * @return property value of description
     */
    public String getDescription() {
        return description;
    }

    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }
}
