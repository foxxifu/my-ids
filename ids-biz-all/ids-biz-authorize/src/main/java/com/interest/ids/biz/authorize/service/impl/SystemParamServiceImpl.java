package com.interest.ids.biz.authorize.service.impl;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import redis.clients.jedis.Jedis;

import com.interest.ids.common.project.bean.sm.SystemParam;
import com.interest.ids.commoninterface.dao.sm.SystemParamMapper;
import com.interest.ids.commoninterface.service.sm.SystemParamService;
import com.interest.ids.redis.utils.RedisUtil;

@Service
public class SystemParamServiceImpl implements SystemParamService
{
	private static final Logger logger = LoggerFactory.getLogger(SystemParamServiceImpl.class);
	@Resource
	private SystemParamMapper paramMapper;
	
	@Override
	public int saveParam(SystemParam param) 
	{
		return paramMapper.saveParam(param);
	}

	@Override
	public SystemParam getSystemParam() {
		SystemParam param = paramMapper.getSystemParam();
		if(param == null){
			logger.info("param is null.");
			param = new SystemParam();
		}
		return getVersionInfo(param);		 
	}

	@Override
	public int updateParam(SystemParam param) {
		return paramMapper.updateParam(param);
	}
	
	private SystemParam getVersionInfo(SystemParam param){
		Jedis jedis = RedisUtil.getJedis();
		if (null != jedis) {
		    try {
                String version = jedis.get("MySystemVersion");
                String installDate = jedis.get("MySystemInstallDate");
               	param.setVersion(version);
                param.setInstallDate(installDate);
            } catch (Exception e) {
                logger.error(e.getMessage(), e);
            } finally{
                if (jedis != null){
                    RedisUtil.closeJeids(jedis);
                }
            }
		}else{
			logger.error("jedis is null,can't get version info.");
		}
		return param;
	}
}
