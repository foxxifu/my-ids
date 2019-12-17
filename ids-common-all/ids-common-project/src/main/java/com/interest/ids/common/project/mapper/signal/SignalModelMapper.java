package com.interest.ids.common.project.mapper.signal;


import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.interest.ids.common.project.bean.signal.SignalModelInfo;
import com.interest.ids.common.project.utils.CommonMapper;


public interface SignalModelMapper extends CommonMapper<SignalModelInfo> {

    /**
     * 插入并返回主键
     * @param needInsertModel
     * @return
     */
    int insertAndGetId(List<SignalModelInfo> needInsertModel);
    /**
     * 判断是否存在mqtt数采sn号的信息
     * @param value
     * @return
     */
    int isExistMqqtUser(String value);
    /**
     * 新增mqtt用户新
     * @param params
     */
    void insertMqqtUser(Map<String, Object> params);
    /**
     * 根据用户名查询mqtt用户密码
     * @param userName 用户名
     */
    String getMqttUserPassword(String username);
    /**
     * 根据mqtt用户名更新密码
     * @param username
     * @param password
     * @return
     */
    int updateMqttPWByUsername(@Param("username")String username,@Param("password")String password);

}