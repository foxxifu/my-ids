package com.interest.ids.biz.authorize.interceptors;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import redis.clients.jedis.Jedis;

import com.alibaba.fastjson.JSONObject;
import com.interest.ids.biz.authorize.utils.AccessUtils;
import com.interest.ids.common.project.constant.ResponseConstants;
import com.interest.ids.common.project.support.Response;
import com.interest.ids.redis.utils.RedisUtil;

public class AccessInterceptor implements HandlerInterceptor {
    
    private static final Logger logger = LoggerFactory.getLogger(AccessInterceptor.class);
	
	@Resource
	private AccessUtils accessUtils;

	private List<String> skipUri = null;

	@Override
	public void afterCompletion(HttpServletRequest arg0,
			HttpServletResponse arg1, Object arg2, Exception arg3)
			throws Exception {

	}

	@Override
	public void postHandle(HttpServletRequest arg0, HttpServletResponse arg1,
			Object arg2, ModelAndView arg3) throws Exception {

	}

	private void init() {
		if (null == skipUri) {
			skipUri = new ArrayList<>();
		}
		skipUri.add("/biz/app/appUserController/login");
		skipUri.add("/biz/app/appUserController/logout");
		skipUri.add("/biz/app/appUserController/getEnterpriseUser");
		skipUri.add("/biz/app/appUserController/resetPassword");
		skipUri.add("/biz/app/appUserController/getUserAuthorize");
		
		skipUri.add("/biz/user/login");
		skipUri.add("/biz/user/logout");
		skipUri.add("/biz/authorize/getUserAuthorize");
		// 图片上传和下载
		skipUri.add("/biz/fileManager/fileUpload"); 
		skipUri.add("/biz/fileManager/downloadFile");
		// 天气不需要配置权限，直接跳过
		skipUri.add("/biz/weather/getWeatherDaily");
		skipUri.add("/biz/weather/getWeatherNow");
		// 多电站总览不需要配置权限，直接跳过
		/*skipUri.add("/biz/stationOverview/getStationDistribution");
		skipUri.add("/biz/stationOverview/getPowerAndIncome");
		skipUri.add("/biz/stationOverview/getAlarmStatistics");
		skipUri.add("/biz/stationOverview/getPRList");
		skipUri.add("/biz/stationOverview/getPPRList");
		skipUri.add("/biz/stationOverview/stationStatus");
		skipUri.add("/biz/stationOverview/getRealtimeKPI");
		skipUri.add("/biz/stationOverview/getPowerStationList");
		// 单电站总览不需要配置权限，直接跳过
		skipUri.add("/biz/singleStation/getSingleStationInfo");
		skipUri.add("/biz/singleStation/getSingleStationCommonData");
		skipUri.add("/biz/singleStation/getSingleStationPowerAndIncome");
		skipUri.add("/biz/singleStation/getSingleStationAlarmStatistics");
		skipUri.add("/biz/singleStation/getSingleStationActivePower");
		// 大屏不需要配置权限，直接跳过
		skipUri.add("/biz/largeScreen/getActivePower");
		skipUri.add("/biz/largeScreen/getCompdisc");
		skipUri.add("/biz/largeScreen/getPowerTrends");
		skipUri.add("/biz/largeScreen/getMapShowData");
		skipUri.add("/biz/largeScreen/getCommonData");
		skipUri.add("/biz/largeScreen/getListStationInfo");
		skipUri.add("/biz/largeScreen/getSocialContribution");
		skipUri.add("/biz/largeScreen/getDeviceCount");
		skipUri.add("/biz/largeScreen/getAlarmStatistics");*/
		skipUri.add("/biz/websocket");
		skipUri.add("/biz/sockjs");
		// 获取登录用户企业的LOGO也不需要配置权限，直接跳过
		skipUri.add("/biz/enterprise/getLoginUserLogo");
		// 通知改变状态的接口不做拦截
		skipUri.add("/dev/device/clearMqqtToDbCache");
	}

	@Override
	public boolean preHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler) throws Exception {
		//TODO 返回json状态 3为未登录
		response.setHeader("contentType", "text/plain; charset=utf-8");
		response.setCharacterEncoding("utf-8");
		String uri = request.getRequestURI();
		init();
		if (skipUri.contains(uri)) {
			return true;
		}
		Cookie[] cookies = request.getCookies();
		Object obj = request.getSession().getAttribute("user");
		if (null == obj) {
			Cookie c = new Cookie("tokenId", "");
			c.setPath("/");
			c.setMaxAge(0);
			response.addCookie(c);
			Jedis jedis = RedisUtil.getJedis();
			if (null != jedis) {
			    try {
                    jedis.del("user");
                } catch (Exception e) {
                    logger.error(e.getMessage(), e);
                } finally{
                    if (jedis != null){
                        RedisUtil.closeJeids(jedis);
                    }
                }
			}  
			Response content = new Response();
			content.setCode(ResponseConstants.CODE_SESSION_TIMEOUT);
			content.setMessage("no user login or session timeout");
			response.getWriter().println(JSONObject.toJSON(content));
			return false;
		}
		if (null != cookies && cookies.length > 0) {
			for (Cookie cookie : cookies) {
				if (cookie.getName().equals("tokenId") || request.getParameter("tokenId") != null) {
					boolean status = accessUtils.access(uri, cookie.getName().equals("tokenId")? cookie.getValue() : request.getParameter("tokenId").toString());
					if(!status)
					{
						Response content = new Response();
						content.setCode(ResponseConstants.CODE_ACCESS_FAIL);
						content.setMessage("no user login or session timeout");
						response.getWriter().println(JSONObject.toJSON(content));
					}
					return status;
				}
			}
		}
		response.getWriter().println("no user login or session timeout");
		return false;
	}
}
