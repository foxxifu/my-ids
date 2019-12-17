package com.interest.ids.biz.web.utils;

import com.interest.ids.common.project.bean.sm.UserInfo;
import com.interest.ids.common.project.utils.MathUtil;

import javax.servlet.http.HttpServletRequest;

import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;

/**
 * web工程通用工具类
 *
 * @author claude
 */
public class CommonUtils {

    /**
     * 通过HttpServletRequest获取当前登录用户信息
     *
     * @param request HttpServletRequest
     * @return map<key   (   用户属性   )   ,   value   (   属性值   )>
     * @throws Exception 当获取当前用户为NULL是抛出异常
     */
    public static Map<String, Object> getCurrentUser(HttpServletRequest request)
            throws Exception {
        Map<String, Object> userInfo = new HashMap<String, Object>();
        Object userO = request.getSession().getAttribute("user");
        if (userO != null) {
            UserInfo user = (UserInfo) userO;
            userInfo.put("id", user.getId());
            userInfo.put("type_", user.getType_());// type_:system(系统用户)，enterprise(企业用户)
        } else {
            throw new Exception("get current user error! current user is null!");
        }
        return userInfo;
    }

    /**
     * 从 session 中获取当前登陆用户信息
     *
     * @param request
     * @return
     */
    public static UserInfo getSigninUser(HttpServletRequest request) {
        Object userO = request.getSession().getAttribute("user");
        return (null != userO) ? (UserInfo) userO : null;
    }
    
    /**
	 * 获取timezoneId
	 * 
	 * @param timeZone
	 * @return
	 */
	public static int getTimeZoneId(TimeZone timeZone) {
		int offset = timeZone.getRawOffset() / 3600000;
		return MathUtil.formatInteger(offset);
	}

}
