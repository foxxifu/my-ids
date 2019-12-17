package com.interest.ids.biz.data.service.station;

import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import com.interest.ids.common.project.bean.sm.StationParam;
import com.interest.ids.common.project.utils.CommonUtil;
import com.interest.ids.commoninterface.dao.station.StationParamMapper;
import com.interest.ids.commoninterface.service.station.StationParamService;

@Service("stationParamService")
public class StationParamServiceImpl implements StationParamService {
    @Resource
    private StationParamMapper stationParamMapper;

    @Override
    public int insertStationParam(StationParam stationParam) 
    {
        return stationParamMapper.insertStationParam(stationParam);
    }

    @Override
    public StationParam selectStationParamById(Long id) {
        return stationParamMapper.selectStationParamById(id);
    }


    @Override
    public int updateStationParamById(StationParam stationParam) 
    {
        return stationParamMapper.updateStationParamById(stationParam);
    }

    public Map<String, String> queryParamsValMapByKey(Collection<String> stationCodes, String paramKey) {
        Map<String, String> result = null;
        
        if(CommonUtil.isNotEmpty(stationCodes) && StringUtils.isNotEmpty(paramKey)){
            result = new HashMap<>();
            List<StationParam> queryResult = stationParamMapper.selectStationParamMap(stationCodes, paramKey);
            
            for(StationParam param : queryResult){
                String key = param.getStationCode();
                if(StringUtils.isNotEmpty(key)){
                    result.put(key, param.getParamValue());
                }
            }
            
            //有电站未对该参数进行配置，则取系统参数
            if (stationCodes.size() > result.size()){
                StationParam systemParam = stationParamMapper.selectSystemParamMap(paramKey);
                stationCodes.removeAll(result.keySet());
                for (String stationCode : stationCodes){
                    result.put(stationCode, systemParam.getParamValue());
                }
            }
        }
        
        return result;
    }

    
    @Override
    public StationParam queryStationParamByStationCodeAndKey(
            String stationCode, String paramKey) {
        
        if(stationCode == null || paramKey == null){
            return null;
        }
        
        return stationParamMapper.selectStationParamByStationCodeAndKey(stationCode, paramKey);
    }

    @Override
    public List<StationParam> selectStationParamByStationCode(String stationCode) 
    {
        List<StationParam> list = stationParamMapper.selectStationParamByStationCode(stationCode);
        //去除重复的
        Map<String,StationParam> map = new LinkedHashMap<>();
        for (StationParam stationParam : list)
        {
            if(map.containsKey(stationParam.getParamKey()))
            {
                if(stationParam.getStationCode()!=null)
                {
                    map.put(stationParam.getParamKey(), stationParam);
                }
            }else
            {
                map.put(stationParam.getParamKey(), stationParam);
            }
        }
        list.clear();
        for (String key : map.keySet()) 
        {
            list.add(map.get(key));
        }
        return list;
    }

    @Override
    public Integer selectParamByCodeAndName(Map<String, Object> condition) {
        
        return stationParamMapper.selectParamByCodeAndName(condition);
    }
}
