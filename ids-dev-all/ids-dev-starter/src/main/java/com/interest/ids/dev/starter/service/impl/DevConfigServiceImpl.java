package com.interest.ids.dev.starter.service.impl;


import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import tk.mybatis.mapper.entity.Example;

import com.github.pagehelper.PageHelper;
import com.interest.ids.common.project.bean.device.DcConfig;
import com.interest.ids.common.project.bean.signal.NormalizedModel;
import com.interest.ids.common.project.constant.DevTypeConstant;
import com.interest.ids.common.project.mapper.device.DcConfigMapper;
import com.interest.ids.common.project.utils.SpringBeanContext;
import com.interest.ids.dev.api.service.Iec104Service;
import com.interest.ids.dev.api.service.NormalizedService;
import com.interest.ids.dev.starter.controller.params.DevConfigParams;
import com.interest.ids.dev.starter.dao.DevConfigDao;
import com.interest.ids.dev.starter.service.DevConfigService;
import com.interest.ids.dev.starter.vo.DevConfigVO;
import com.interest.ids.redis.client.service.SignalCacheClient;

/**
 * @Author: sunbjx
 * @Description:
 * @Date Created in 16:54 2018/1/16
 * @Modified By:
 */
@Service
public class DevConfigServiceImpl implements DevConfigService {

    private static final Logger LOGGER = LoggerFactory.getLogger(DevConfigServiceImpl.class);

    @Autowired
    private DcConfigMapper dcConfigMapper;
    @Autowired
    private DevConfigDao devConfigDao;
    @Resource
    private Iec104Service iec104Service;
    @Autowired
    private NormalizedService normalizedService;

    /**
     * 重复检查
     *
     * @param dcConfig
     * @return
     */
    @Override
    public int config(DcConfig dcConfig) {

        int result = 1;
        try {
        	if(isExist(dcConfig)){
        		
        		result = 0 ;
        	}
        	DcConfig record = getConfigInfo(dcConfig.getDevId());
        	//新增的情况
            if (null == record) {
                dcConfigMapper.insert(dcConfig);
                iec104Service.newConfig(dcConfig);
            } else {
            	//修改的情况
            	record.setChannelType(dcConfig.getChannelType());
            	record.setIp(dcConfig.getIp());
            	record.setLogicalAddres(dcConfig.getLogicalAddres());
            	record.setPort(dcConfig.getPort());
            	dcConfigMapper.updateByPrimaryKey(record);
                //dcConfigMapper.selectOne(record);
                iec104Service.updateConfig(record);
            }
        } catch (Exception e) {
            LOGGER.info("Query failed: ", e);
            result = 0;
        }

        return result;
    }
    
    
    private boolean isExist(DcConfig dcConfig){
    	Example ex = new Example(DcConfig.class);
		Example.Criteria criteria = ex.createCriteria();
		criteria.andEqualTo("channelType",dcConfig.getChannelType());
		criteria.andEqualTo("ip",dcConfig.getIp());
		criteria.andEqualTo("port",dcConfig.getPort());
    	List<DcConfig> configs = dcConfigMapper.selectByExample(ex);
    	if(configs != null && configs.size() > 0){
    		return true;
    	}else{
    		return false;
    	}
    }
    
    private DcConfig getConfigInfo(Long devId){
    	Example ex = new Example(DcConfig.class);
		Example.Criteria criteria = ex.createCriteria();
		criteria.andEqualTo("devId",devId);
    	List<DcConfig> configs = dcConfigMapper.selectByExample(ex);
    	if(configs != null && configs.size() > 0){
    		return configs.get(0);
    	}else{
    		return null;
    	}
    }
    

    @Override
    public List<DevConfigVO> list(DevConfigParams params) {
        List<DevConfigVO> result;
        try {
            PageHelper.startPage(params.getIndex(), params.getPageSize());
            result = devConfigDao.listDcConfig(params);
        } catch (Exception e) {
            LOGGER.info("Query failed: ", e);
            result = null;
        }
        return result;
    }

    @Override
    public DevConfigVO getByDevId(Long devId) {
        DevConfigVO result;
        try {
            result = devConfigDao.getByDevId(devId);
        } catch (Exception e) {
            LOGGER.info("Query failed: ", e);
            result = null;
        }
        return result;
    }


	@Override
	public List<Map<String, Object>> deviceComparisonTable(String[] devArray) {
		List<Map<String, Object>> result = this.devConfigDao.deviceComparisonTable(devArray);
		if(result != null && result.size() > 0){
			SignalCacheClient signalCacheClient = (SignalCacheClient) SpringBeanContext.getBean("signalCacheClient");
			List<NormalizedModel> list = this.normalizedService.getUnModelsByDeviceType(DevTypeConstant.INVERTER_DEV_TYPE);
			for(int i=0;i<result.size();i++){
				for(NormalizedModel model : list){
					result.get(i).put(model.getColumnName(), 
							signalCacheClient.get(Long.valueOf(result.get(i).get("dev_id").toString()), model.getColumnName()));
				}
			}
		}
		return result;
	}


	@Override
	public List<Map<String, Object>> deviceComparisonChart(Map<String, Object> param) {
		List<Map<String, Object>> result = this.devConfigDao.deviceComparisonChart(param);
		return result;
	}


	@Override
	public List<Map<String, Object>> getDeviceSignal(int devTypeId) {
		List<Map<String, Object>> result = this.devConfigDao.getDeviceSignal(devTypeId);
		return result;
	}
}
