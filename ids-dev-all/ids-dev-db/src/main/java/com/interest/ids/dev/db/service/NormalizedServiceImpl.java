package com.interest.ids.dev.db.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import tk.mybatis.mapper.entity.Example;

import com.interest.ids.common.project.bean.signal.DeviceInfo;
import com.interest.ids.common.project.bean.signal.NormalizedInfo;
import com.interest.ids.common.project.bean.signal.NormalizedModel;
import com.interest.ids.common.project.bean.signal.SignalInfo;
import com.interest.ids.common.project.mapper.signal.NormalizedInfoMapper;
import com.interest.ids.common.project.mapper.signal.NormalizedModelMapper;
import com.interest.ids.dev.api.service.NormalizedService;
import com.interest.ids.dev.api.service.SignalService;


/**
 * 
 * @author lhq
 *
 *
 */

@Service("unificationService")
public class NormalizedServiceImpl implements NormalizedService{

	
	@Resource
	private NormalizedModelMapper uModelMapper;
	
	@Resource
	private NormalizedInfoMapper infoMapper;
	
	@Resource
	private SignalService signalService;

	
	@Override
	public List<NormalizedModel> getUnModelsByDeviceType(Integer deviceType) {
		
		Example ex = new Example(NormalizedModel.class);
		Example.Criteria criteria = ex.createCriteria();
		criteria.andEqualTo("devType", deviceType);
		return uModelMapper.selectByExample(ex);
	}

	@Override
	public List<NormalizedInfo> getUnInfoBySignalVersion(String signalVersion) {
		
		Example ex = new Example(NormalizedInfo.class);
		Example.Criteria criteria = ex.createCriteria();
		criteria.andEqualTo("signalVersion", signalVersion);
		return infoMapper.selectByExample(ex);
	}

	@Override
	public Map<NormalizedInfo, NormalizedModel> getInfo2ModelMap(
			Integer deviceType, String signalVersion) {
		List<NormalizedModel> models = getUnModelsByDeviceType(deviceType);
		List<NormalizedInfo> infos = getUnInfoBySignalVersion(signalVersion);
		if(infos != null && infos.size() > 0){
			Map<NormalizedInfo, NormalizedModel> map = new HashMap<>(64);
			for(int i=0;i<models.size();i++){
				NormalizedModel model = models.get(i);
				for(int j=0;j<infos.size();j++){
					NormalizedInfo info = infos.get(j);
					if(model.getIsPersistent().equals(Boolean.TRUE)){
						if(model.getId().equals(info.getNormalizedSignalId())){
							map.put(info, model);
						}
					}
				}
			}
			return map;
		}
		return null;
	}

	@Override
	public Map<Integer, String> getAddress2Column(Integer deviceType,
			String signalVersion) {
		Map<NormalizedInfo, NormalizedModel> map = getInfo2ModelMap(deviceType,signalVersion);
		if(map != null && map.size() > 0){
			Map<Integer, String> data = new HashMap<>(map.size());
			for(Map.Entry<NormalizedInfo, NormalizedModel> entry:map.entrySet()){
				if(entry.getValue().getIsPersistent().equals(Boolean.TRUE)){
					data.put(entry.getKey().getSignalAddress(), entry.getValue().getColumnName());
				}
			}
			return data;
		}
		return null;
	}

	@Override
	public Map<Integer, List<String>> getModelId2Column(Integer deviceType,
			String signalVersion) {
		Map<NormalizedInfo, NormalizedModel> map = getInfo2ModelMap(deviceType,signalVersion);
		if(map != null && map.size() > 0){
			Map<Integer, List<String>> data = new HashMap<>(map.size());
			for(Map.Entry<NormalizedInfo, NormalizedModel> entry:map.entrySet()){
				Integer signalModelId = entry.getKey().getSiganlModelId();
				if(!data.containsKey(signalModelId)) {
					data.put(signalModelId, new ArrayList<String>());
				}
				data.get(signalModelId).add(entry.getValue().getColumnName());
				//data.put(entry.getKey().getSiganlModelId(), entry.getValue().getColumnName());
			}
			return data;
		}
		return null;
	}

	@Override
	public Map<Integer, List<String>> getAllModelId2Column(Integer deviceType, String signalVersion) {
		List<NormalizedModel> models = getUnModelsByDeviceType(deviceType);
		List<NormalizedInfo> infos = getUnInfoBySignalVersion(signalVersion);
		Map<Integer, List<String>> result = null;
		if (infos != null && infos.size() > 0) {
			result = new HashMap<>();
			for (int i = 0; i < models.size(); i++) {
				NormalizedModel model = models.get(i);
				for (int j = 0; j < infos.size(); j++) {
					NormalizedInfo info = infos.get(j);
					if (model.getId().equals(info.getNormalizedSignalId())) {
						if (!result.containsKey(info.getSiganlModelId())) {
							result.put(info.getSiganlModelId(), new ArrayList<String>());
						}
						result.get(info.getSiganlModelId()).add(model.getColumnName());
					}
				}
			}
		}
		return result;
	}

	/*public Map<Integer, String> getModelId2Column(String signalVersion) {
		List<NormalizedInfo> list = getUnInfoBySignalVersion(signalVersion);
		if(list != null && list.size() > 0){
			Map<Integer, String> data = new HashMap<>(list.size());
			for(NormalizedInfo info : list){
				data.put();
			}
		}
		return null;
	}*/
	
	public Map<Integer,List<String>> getAddress2Column(DeviceInfo dev){
		Map<Integer, List<String>> map = getModelId2Column(dev.getDevTypeId(),dev.getSignalVersion());
		List<SignalInfo> signals = signalService.getSignalsByDeviceId(dev.getId());
		
		if(map != null && signals != null){
			Map<Integer,List<String>> data = new HashMap<>();
			for(Map.Entry<Integer, List<String>> entry : map.entrySet()){
				Integer id = entry.getKey();
				
				for(SignalInfo signal : signals){
					if(signal.getModelId().intValue() == id.intValue()){
						data.put(signal.getSignalAddress(), entry.getValue());
					}
				}
			}
			return data;
		}
		
		return null;
	}


}
