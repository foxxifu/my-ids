package com.interest.ids.biz.authorize.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.interest.ids.common.project.bean.sm.District;
import com.interest.ids.common.project.bean.sm.StationInfoM;
import com.interest.ids.common.project.dto.DomainTreeDto;
import com.interest.ids.commoninterface.dao.sm.DistrictMapper;
import com.interest.ids.commoninterface.dao.sm.DomainInfoMapper;
import com.interest.ids.commoninterface.dao.station.StationInfoMMapper;
import com.interest.ids.commoninterface.service.sm.DistrictService;

@Service("districtService")
public class DistrictServiceImpl implements DistrictService 
{
    @Resource
    private DistrictMapper districtMapper;
    
    @Resource
    private StationInfoMMapper stationInfoMMapper;
    @Autowired
    private DomainInfoMapper domainInfoMapper;
    
    /**
     * 获取当前用户的所有的行政区域
     */
    @Override
    public List<District> getAllDistrict(Long userId,String type_) 
    {
    	return getDistrictAndStation(userId, type_,false) ;
    }

    /**
     * 根据id统计每个行政区域的市有多少电站
     */
    @Override
    public Map<String,Integer> selectDistrictCount(Long userId,String type_) 
    {
        Map<String, Object> userInfo = new HashMap<String, Object>();
        userInfo.put("id", userId);
        userInfo.put("type_", type_);
        List<StationInfoM> stations = stationInfoMMapper.selectStationInfoMByUserId(userInfo);
        Map<String,Integer> map = new HashMap<String,Integer>();
        for (StationInfoM stationInfoM : stations) 
        {
            String areaCode = stationInfoM.getAreaCode();
            if(null != areaCode)
            {
                String[] codes = areaCode.split("@");
                String key = null;
                if(null != codes && codes.length == 2)
                {
                    key = codes[0];
                }else if(null != codes && codes.length == 3)
                {
                    key = codes[1];
                }
                District district = districtMapper.selectDistrictById(Long.parseLong(key));
                if(null != district)
                {
                    if(map.containsKey(district.getName()))
                    {
                        map.put(district.getName(), map.get(district.getName())+1);
                    }else
                    {
                        map.put(district.getName(), 1);
                    }
                }
            }
        }
        
        return map;
    }

    @Override
    public List<District> getDistrictAndStation(Long id, String type_,boolean hasStation) 
    {
    	List<District> list = new ArrayList<>();
        //1. 根据用户id得到所有的电站信息
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("id", id);
        map.put("type_", type_);
        List<StationInfoM> stations = stationInfoMMapper.selectStationInfoMByUserId(map);
        Set<String> codeSet = new HashSet<>();
        for(StationInfoM s:stations){//需要查询的行政区域的code
        	codeSet.addAll(Arrays.asList(s.getAreaCode().split("@")));
        }
        if(codeSet.size()==0){
        	return list;
        }
        //TODO 根据行政区域的code查询到对应的区域信息
        List<District> disList = districtMapper.findDisListByAreaCodes(codeSet.toArray());
        Map<String,District> codeToDisMap = new HashMap<>();
        for(District dis:disList){
        	codeToDisMap.put(dis.getCode(), dis);
        }
        
        Map<String,District> recoeCodeMap = new HashMap<>();//
        for(StationInfoM s:stations){//需要查询的行政区域的code
        	if(StringUtils.isEmpty(s.getAreaCode())){
        		continue;
        	}
        	String[]tmpCodeArr = s.getAreaCode().split("@");
        	if(tmpCodeArr== null || tmpCodeArr.length<2){
        		continue;
        	}
        	District first = recoeCodeMap.get(tmpCodeArr[0]);
        	if(first == null){
        		first = codeToDisMap.get(tmpCodeArr[0]);
        		if(first==null){
        			continue;
        		}
        		recoeCodeMap.put(tmpCodeArr[0], first);
        	}
        	District dl = codeToDisMap.get(tmpCodeArr[1]);
        	if(dl==null){
        		continue;
        	}
        	List<District> tempDisList = first.getChildren();
        	if(tempDisList == null){
        		tempDisList = new ArrayList<>();
        		first.setChildren(tempDisList);
        	}
        	if(tempDisList.contains(dl)){
        		if(tmpCodeArr.length==3){
        			List<District> clList = dl.getChildren();
        			if(clList==null){
        				clList = new ArrayList<>();
        				dl.setChildren(clList);
        			}
        			District dl3 = codeToDisMap.get(tmpCodeArr[2]);
        			if(clList.contains(dl3)){
        				addStation(hasStation, s, dl3);
        				continue;
        			}
        			clList.add(dl3);
        			addStation(hasStation, s, dl3);
        		}else{
        			List<StationInfoM> stList = dl.getStations();
        			if(stList==null){
        				stList = new ArrayList<>();
        				dl.setStations(stList);
        			}
        			stList.add(s);
        		}
        	}else{
        		tempDisList.add(dl);
        		if(tmpCodeArr.length==3){
        			District dl2 = codeToDisMap.get(tmpCodeArr[2]);
        			List<District> clList = dl.getChildren();
        			if(clList==null){
        				clList = new ArrayList<>();
        				dl.setChildren(clList);
        			}
        			clList.add(dl2);
        			addStation(hasStation, s, dl2);
        		}else{
        			addStation(hasStation, s, dl);
        		}
        	}
        	
        }
        List<District> result = new ArrayList<>();
        for(District tmp:recoeCodeMap.values()){
        	result.add(tmp);
        }
        return result;
    }

	private void addStation(boolean hasStation, StationInfoM s, District dl) {
		if(hasStation){
			List<StationInfoM> stList = dl.getStations();
			if(stList==null){
				stList = new ArrayList<>();
				dl.setStations(stList);
			}
			stList.add(s);
		}
	}

	@Override
	public List<DomainTreeDto> getDomains(Long id, String type_, boolean hasStation) {
		List<DomainTreeDto> result = new ArrayList<>(); // 返回的结果
		//1. 根据用户id得到所有的电站信息
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("id", id);
        map.put("type_", type_);
        List<StationInfoM> stations = stationInfoMMapper.selectStationInfoMByUserId(map);
        if (CollectionUtils.isEmpty(stations)) { // 没有任何的数据
        	return result;
        }
        Set<Long> domainIds = new HashSet<>();
        // 区域id和对应电站的关系 <区域id, 当前区域下具有的电站集合>
        Map<Long, List<StationInfoM>> domainIdToStationListMap = new HashMap<>();
        for(StationInfoM s : stations) {
        	Long domainId = s.getDomainId();
        	if (domainId == null) { // 错误的电站信息，就不处理
        		continue;
        	}
        	domainIds.add(domainId);
        	if (!domainIdToStationListMap.containsKey(domainId)) { // 如果没有对应的key，就添加对应的内容
        		domainIdToStationListMap.put(domainId, new ArrayList<StationInfoM>());
        	}
        	domainIdToStationListMap.get(domainId).add(s);
        }
        // 2.根据电站所属的区域id获取区域的信息
        List<String> domainPaths = domainInfoMapper.getDomainPathByIds(domainIds);
        if (CollectionUtils.isEmpty(domainPaths)) {
        	return result;
        }
        // 需要查询的父节点的id
        Set<Long> domainParentIds = new HashSet<>();
        for(String path : domainPaths) {
        	String[] tmpPathArr =  StringUtils.split(path, "@");
        	int len = tmpPathArr.length;
        	if (len <= 1) {
        		continue;
        	}
        	for (int i = 1; i < len; i++) {
        		String tmpPath = tmpPathArr[i];
        		if (StringUtils.isBlank(tmpPath)) {
        			continue;
        		}
        		try {
        			domainParentIds.add(Long.valueOf(tmpPath));
        		}catch(Exception e) {
        		}
        	}
        }
        domainParentIds.addAll(domainIds);
        // 3.获取当前需要查询的所有区域
        List<DomainTreeDto> domainTreeList = domainInfoMapper.getDomainByIds(domainParentIds);
        // 4.拼接返回结果
        getDomainTree(domainTreeList, result, domainIdToStationListMap);
		return result;
	}
	
	private void getDomainTree(List<DomainTreeDto> domainTreeList, List<DomainTreeDto> result, 
			Map<Long, List<StationInfoM>> domainIdToStationListMap) {
		// 父节点-》对应的子节点的中间集合
		Map<Long, List<DomainTreeDto>> parentIdToChildrenList = new HashMap<>();
		for (DomainTreeDto dto : domainTreeList) {
			Long parentId = dto.getParentId();
			if (!parentIdToChildrenList.containsKey(parentId)) {
				parentIdToChildrenList.put(parentId, new ArrayList<DomainTreeDto>());
			}
			parentIdToChildrenList.get(parentId).add(dto);
		}
		result.addAll(parentIdToChildrenList.get(0L)); // 存放第一级
		for(DomainTreeDto d : result) {
			doChildrenDomainTree(domainIdToStationListMap, parentIdToChildrenList, d);
		}
	}
	
	private void doChildrenDomainTree(Map<Long, List<StationInfoM>> domainIdToStationListMap,
			Map<Long, List<DomainTreeDto>> parentIdToChildrenList,DomainTreeDto dto) {
		Long id = dto.getId();
		if (domainIdToStationListMap.containsKey(id)) { // 电站信息
			dto.setStations(domainIdToStationListMap.get(id));
		}
		List<DomainTreeDto> list = parentIdToChildrenList.get(id);
		if (CollectionUtils.isEmpty(list)) {
			return;
		}
		dto.setChildren(list); // 设置子节点
		for(DomainTreeDto d : list) { // 循环递归调用,设置子节点和电站
			doChildrenDomainTree(domainIdToStationListMap, parentIdToChildrenList, d);
		}
	}
}
