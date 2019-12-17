package com.interest.ids.redis.caches;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.Pipeline;
import redis.clients.jedis.Response;

import com.interest.ids.common.project.bean.sm.UserInfo;
import com.interest.ids.common.project.utils.UUIDUtils;
import com.interest.ids.redis.client.JedisClient;
import com.interest.ids.redis.utils.RedisUtil;

public class SessionCache {
    
	private final static String JSESSIONID = "tokenId";
	
	public static final String LOGIN_STATUS = "LOGIN_STATUS";
	
	public static String generatorLoginStatusKey(Long userId){
	    StringBuilder sb = new StringBuilder(LOGIN_STATUS);
	    sb.append(":").append(userId);
	    return sb.toString();
	}
	
	@Resource
	private  JedisClient jedClient;

	public void setJedClient(JedisClient jedClient) {
		this.jedClient = jedClient;
	}

	private static Logger log = LoggerFactory.getLogger(SessionCache.class);

	public  UserInfo getAttribute(HttpServletRequest request, String key) {
		Cookie cookie = getJsessionid(request);
		Jedis j = null;
		try {
			if (null != jedClient) {
				j = jedClient.getJedis();
			}
			if (j != null) {
				byte[] json = j.get((key+cookie.getValue()).getBytes());
				if (null != json && json.length > 0) {
					ByteArrayInputStream inputStream = new ByteArrayInputStream(json);
					ObjectInputStream input = new ObjectInputStream(inputStream);
					Object obj = input.readObject();
					if(obj != null)
					{
						return (UserInfo)obj;
					}
					return null;
				}
			}
		} catch (Exception e) {
			log.error("error", e);
		} finally {
			RedisUtil.closeJeids(j);
		}
		return null;
	}
	
	public UserInfo getAttribute(
			String tokenId, String key) 
	{
		Jedis j = null;
		try {
			if (null != jedClient) {
				j = jedClient.getJedis();
			}
			if (j != null) {
				byte[] json = j.get((key+tokenId).getBytes());
				if (null != json && json.length > 0) {
					ByteArrayInputStream inputStream = new ByteArrayInputStream(json);
					ObjectInputStream input = new ObjectInputStream(inputStream);
					Object obj = input.readObject();
					if(obj != null)
					{
						j.expire((key+tokenId).getBytes(), 30 * 60);
						 Set<String> keys = j.keys(LOGIN_STATUS + "*");
						 if(null != keys && keys.size() > 0) {
							 for (String k : keys) {
								 j.expire(k, 30 * 60);
							 }
						 }
						return (UserInfo)obj;
					}
					return null;
				}
			}
		} catch (Exception e) {
			log.error("error", e);
		} finally {
			RedisUtil.closeJeids(j);
		}
		return null;
	}

	public  void setAttribute(HttpServletRequest request,
			HttpServletResponse response, String key, UserInfo value) {
		Cookie cookie = getJsessionid(request);
		Jedis j = null;
		try {
			if (null != jedClient) {
				j = jedClient.getJedis();
			}
			if(null != j)
			{
				ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
				ObjectOutputStream out = new ObjectOutputStream(byteOut);
				out.writeObject(value);
				byte[] userKey = ("user"+cookie.getValue()).getBytes();
				j.set(userKey,byteOut.toByteArray());
				j.expire(userKey, 30 * 60);
				response.addCookie(cookie);
				
				//将用户登录存入缓存，以便检索用户登录状态
				String userLoginKey = generatorLoginStatusKey(value.getId());
				j.set(userLoginKey, value.getId().toString());
				j.expire(userLoginKey, 30 * 60);
			}
		} catch (Exception e) {
			log.error("error", e);
		} finally {
			RedisUtil.closeJeids(j);
		}
	}
	
	public void setAttribute(String string, String tokenId, UserInfo user) {
		Jedis j = null;
		try {
			if (null != jedClient) {
				j = jedClient.getJedis();
			}
			if (j != null) {
				ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
				ObjectOutputStream out = new ObjectOutputStream(byteOut);
				out.writeObject(user);
				byte[] userKey = ("user"+tokenId).getBytes();
                j.set(userKey,byteOut.toByteArray());
                j.expire(userKey, 30 * 60);
                
                //将用户登录存入缓存，以便检索用户登录状态
                String userLoginKey = generatorLoginStatusKey(user.getId());
                j.set(userLoginKey, user.getId().toString());
                j.expire(userLoginKey, 30 * 60);
			}
		} catch (Exception e) {
			log.error("error", e);
		} finally {
			RedisUtil.closeJeids(j);
		}		
	}
	
	/**
	 * 用于检测给定用户集是否处于在线状态
	 * @return
	 */
	public List<Long> getLoginUserIds(List<Long> userIds){
	    Jedis jedis = null;
	    List<Long> result = null;
        try {
            if (null != jedClient) {
                jedis = jedClient.getJedis();
            }
            
            if (jedis != null) {
                Pipeline pipeLine = jedis.pipelined();
                Set<String> keys = jedis.keys(LOGIN_STATUS + "*");
                if (keys != null && keys.size() > 0){
                    Map<String, Response<String>> response = new HashMap<>();
                    for (String key : keys){
                        response.put(key, pipeLine.get(key));
                    }
                    
                    pipeLine.sync();
                    result = new ArrayList<>();
                    for (String key : keys){
                        try {
                            result.add(Long.valueOf(response.get(key).get()));
                        } catch (Exception e) {
                            log.error("can't format user id: " + key, e);
                        }
                    }
                    
                    pipeLine.close();
                }
            }
        } catch (Exception e) {
            log.error("error", e);
        } finally {
            RedisUtil.closeJeids(jedis);
        } 
        
        return result;
	}

	public  Cookie getJsessionid(HttpServletRequest request) {
		Cookie[] cookies = request.getCookies();
		Cookie cookie = null;
		if (null != cookies && cookies.length > 0) {
			for (int i = 0; i < cookies.length; i++) {
				if (JSESSIONID.equals(cookies[i].getName())) {
					cookie = cookies[i];
					break;
				}
			}
		}
		if (cookie == null) {
			cookie = new Cookie(JSESSIONID, UUIDUtils.getUUID());
			cookie.setPath("/");
			//cookie.setMaxAge(60 * 30);
		}
		return cookie;
	}

}
