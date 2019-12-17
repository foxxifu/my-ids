package com.interest.ids.biz.data.service.station;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.interest.ids.common.project.bean.TreeModel;
import com.interest.ids.common.project.bean.analysis.PowerPriceM;
import com.interest.ids.common.project.bean.device.DeviceInfoDto;
import com.interest.ids.common.project.bean.phalanx.PhalanxInfoBean;
import com.interest.ids.common.project.bean.signal.DeviceInfo;
import com.interest.ids.common.project.bean.sm.Page;
import com.interest.ids.common.project.bean.sm.QueryStationInfo;
import com.interest.ids.common.project.bean.sm.StationInfoM;
import com.interest.ids.common.project.bean.sm.StationShareemi;
import com.interest.ids.common.project.bean.sm.UserInfo;
import com.interest.ids.common.project.bean.subarray.SubarrayInfoBean;
import com.interest.ids.common.project.constant.DevTypeConstant;
import com.interest.ids.common.project.constant.StationInfoConstant;
import com.interest.ids.common.project.utils.CommonUtil;
import com.interest.ids.commoninterface.dao.sm.DomainInfoMapper;
import com.interest.ids.commoninterface.dao.station.PhalanxInfoMapper;
import com.interest.ids.commoninterface.dao.station.StationInfoMMapper;
import com.interest.ids.commoninterface.dao.station.StationShareemiMapper;
import com.interest.ids.commoninterface.dao.station.SubarrayInfoMapper;
import com.interest.ids.commoninterface.dto.StationInfoDto;
import com.interest.ids.commoninterface.service.device.IDeviceInfoService;
import com.interest.ids.commoninterface.service.filemanager.IFileManagerService;
import com.interest.ids.commoninterface.service.station.IStationHeathInspectionService;
import com.interest.ids.commoninterface.service.station.StationInfoMService;
import com.interest.ids.redis.caches.StationCache;
import com.interest.ids.redis.caches.StationPowerPriceCache;

@Service("stationInfoMService")
public class StationInfoMServiceImpl implements StationInfoMService {

	private static final Logger logger = LoggerFactory
			.getLogger(StationInfoMServiceImpl.class);

	@Autowired
	private DomainInfoMapper domainMMapper;

	@Autowired
	private StationInfoMMapper stationInfoMMapper;

	@Autowired
	private IDeviceInfoService deviceInfoService;

	@Autowired
	private StationShareemiMapper stationShareemiMapper;

	@Autowired
	private IStationHeathInspectionService stationHeathInspectionService;

	@Autowired
	IFileManagerService fileManagerService;
	
	@Autowired
	private PhalanxInfoMapper phalanxInfoMapper;
	
	@Autowired
	private SubarrayInfoMapper subarrayInfoMapper;

	@Override
	public int insertStation(StationInfoM station) {
		int result = stationInfoMMapper.insertstation(station);
		// 判断是否添加成功，如果成功则添加到redis缓存
		if (result == 1) {
			StationCache.addStation(station);
		}
		return result;
	}

	@Override
	public StationInfoM selectStationInfoMById(Long id) {
		return stationInfoMMapper.selectStationInfoMById(id);
	}

	@Override
	public List<StationInfoM> selcetStationInfoMsByCondition(
			QueryStationInfo queryStation) {
		return stationInfoMMapper.selcetStationInfoMsByCondition(queryStation);
	}

	@Override
	public List<StationInfoM> selectStationInfoMByPage(Page<StationInfoM> page) {
		return stationInfoMMapper.selectStationInfoMByPage(page);
	}

	@Override
	public void updateStationInfoMById(StationInfoM stationInfo) {
		stationInfoMMapper.updateStationInfoMById(stationInfo);
		List<StationInfoM> list = new ArrayList<StationInfoM>();
		list.add(stationInfo);
		StationCache.updateStationInfo(list);
	}

	@Override
	public boolean deleteStationInfoMById(Long id) {
		StationInfoM station = stationInfoMMapper.selectStationInfoMById(id);
		boolean result = stationInfoMMapper.deleteStationInfoMById(id);
		StationCache.deleteStation(station);
		return result;
	}

	@Override
	@Transactional
	public void deleteStationInfosByCodes(String ids) {
		String[] idArray = new String[ids.split(",").length];
		for (int i = 0; i < ids.split(",").length; i++) {
			idArray[i] = ids.split(",")[i];
		}
		this.stationInfoMMapper.deleteStationInfosByCodes(idArray);
		for(String stationCode : idArray){
			StationCache.deleteStationByCode(stationCode);
		}
	}

	@Override
	public StationInfoM selectStationInfoMByStationCode(String stationCode) {
		StationInfoM station = StationCache.getStation(stationCode); // 先从换成中获取数�?
		if (null != station) {
			return station;
		} else // 获取不到则自己查询，然后放入到换成中
		{
			station = stationInfoMMapper
					.selectStationInfoMByStationCode(stationCode);
			StationCache.addStation(station);
			return station;
		}
	}

	@Override
	public String selectStationInfoMNameByStationCode(String stationCode) {
		String stationName = null;

		if (null != stationCode) // 从redis缓存中获取数�?
		{
			stationName = StationCache.getStationName(stationCode);
		} else {
			StationInfoM station = stationInfoMMapper
					.selectStationInfoMNameByStationCode(stationCode);
			StationCache.addStation(station);
			stationName = station.getStationName();
		}
		return stationName;
	}

	@Override
	public boolean deleteStationInfoMByStationCode(String stationCode) {
		boolean result = false;
		if (null != stationCode) {
			result = stationInfoMMapper
					.deleteStationInfoMByStationCode(stationCode);

			if (result) // 删除成功，清除redis换成对应数据
			{
				StationInfoM stationInfoM = new StationInfoM();
				stationInfoM.setStationCode(stationCode);
				StationCache.deleteStation(stationInfoM);
			}
		}
		return false;
	}

	@Override
	public int deleteStationInfoMsByStationCode(String[] stationCodes) {
		int result = stationInfoMMapper
				.deleteStationInfoMsByStationCode(stationCodes);
		if (result > 0) {
			List<StationInfoM> stationList = new ArrayList<StationInfoM>();
			StationInfoM stationInfoM = null;
			for (String code : stationCodes) {
				stationInfoM = new StationInfoM();
				stationInfoM.setStationCode(code);
				stationList.add(stationInfoM);
			}
			StationCache.deleteStations(stationList);
		}
		return result;
	}

	@Override
	public List<StationInfoM> selectStationInfoMByEnterpriseId(Long enterpriseId) {
		return stationInfoMMapper
				.selectStationInfoMByEnterpriseId(enterpriseId);
	}

	@Override
	public Integer selectStationNumberByEnterprise(Long enterpriseId) {
		return stationInfoMMapper.selectStationNumberByEnterprise(enterpriseId);
	}

	@Override
	public List<StationInfoM> selectStationInfoMByUserId(
			Map<String, Object> userId) {
		return stationInfoMMapper.selectStationInfoMByUserId(userId);
	}

	@Override
	public Integer selectStationCountByUserId(Long userId) {
		return stationInfoMMapper.selectStationCountByUserId(userId);
	}

	@Override
	public List<StationInfoM> selectStationInfoMByUserIdAndPage(
			Page<StationInfoM> page) {
		return stationInfoMMapper.selectStationInfoMByUserIdAndPage(page);
	}

	@Override
	public List<StationInfoM> listStationsByStationCodes(
			Collection<String> stationCodes) {

		return stationInfoMMapper
				.selectStationInfoMByStationCodes(stationCodes);
	}

	@Override
	public Map<String, Long> getShareEmiByStationCodes(
			Collection<String> stationCodes) {
		return getShareEmiByStationCodes(stationCodes, null);
	}

	@Override
	public Map<String, Long> getShareEmiByStationCodes(
			Collection<String> stationCodes, Map<String, String> emiStationMap) {

		Map<String, Long> result = new HashMap<>();
		Map<String, Long> stationSharedDevicesMap = new HashMap<String, Long>();

		if (emiStationMap == null) {
			emiStationMap = new HashMap<String, String>();
		}

		// 1. 从设备表中查询未删除电站的环境监测仪设备
		if (CommonUtil.isNotEmpty(stationCodes)) {
			Map<String, List<Long>> stationDevicesMap = deviceInfoService
					.queryStationDevicesMap(stationCodes,
							DevTypeConstant.EMI_DEV_TYPE_ID);

			if (stationDevicesMap != null) {
				for (Map.Entry<String, List<Long>> ele : stationDevicesMap
						.entrySet()) {
					if (CommonUtil.isNotEmpty(ele.getValue())) {
						// 环境监测仪一般只有一个，如果有脏数据查询出多个，取第一个进行数据的运算�?
						result.put(ele.getKey(), ele.getValue().get(0));
					}
				}
			}
		}

		// 2. 查询共享的环境监测仪并添加到返回结果中（后期可从缓存中查询）
		if (CommonUtil.isNotEmpty(stationCodes)) {
			List<StationShareemi> queryResult = stationShareemiMapper
					.selectStationSharedEMI(stationCodes);
			if (CommonUtil.isNotEmpty(queryResult)) {
				for (StationShareemi ele : queryResult) {
					String stationCode = ele.getStationCode();
					String sharedStationCode = ele.getShareStationCode();
					Long deviceId = ele.getShareDeviceId();

					if (StringUtils.isNotEmpty(stationCode)
							&& StringUtils.isNotEmpty(sharedStationCode)
							&& deviceId != null) {
						// 电站没有环境监测仪并且共享的环境监测仪存在，同时排除共享电站被删除及共享设备被删除的情况
						if (!result.containsKey(stationCode)
								&& result.containsKey(sharedStationCode)
								&& result.get(stationCode).equals(deviceId)) {

							result.put(stationCode, deviceId);
						}
						stationSharedDevicesMap.put(stationCode, deviceId);
					}
				}
			}
		}

		stationCodes = result.keySet();

		return result;
	}

	@Override
	public void updateStationStata() {
		// 电站健康状态检查更新
		stationHeathInspectionService.inspectStationStatus();
	}

	@Override
	public List<StationInfoM> getAllStations() {

		return stationInfoMMapper.selectAllStations();
	}

	@Override
	public List<String> getStationWithCriticalAlarm(List<String> stationCodes) {

		return stationInfoMMapper.selectStationWithCriticalAlarm(stationCodes);
	}
	
	public List<PowerPriceM> getAllStationPowerPrices(){
		return this.stationInfoMMapper.getAllStationPowerPrices();
	}

	/**
	 * 初始化电站缓存
	 */
	public void initStationCache() {

		try {
			// 清空缓存
			StationCache.clearCache();
			// 获取所有的未被逻辑删除
			
			List<StationInfoM> allStations = getAllStations();
			Map<String, StationInfoM> stationInfoMMap = new HashMap<>();

			if (CommonUtil.isNotEmpty(allStations)) {
				for (StationInfoM stationInfoM : allStations) {
					stationInfoMMap.put(stationInfoM.getStationCode(), stationInfoM);
				}
			}

			StationCache.putAll(stationInfoMMap);
			// 同时将环境监测仪共享信息进行缓存
			StationCache.initShareEmi(getAllSharedEmis());
			
			//初始化电站健康状态
			updateStationStata();
			
			//初始化电站分时电价缓存
			List<PowerPriceM> powerPriceList = this.getAllStationPowerPrices();
			StationPowerPriceCache.refreshAllData(powerPriceList);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
	}

	@Override
	public Map<String, Long> getSharedDeviceByStationCodes(
			List<String> stationCodes) {
		List<Map<String, Long>> list = stationShareemiMapper.getSharedDeviceByStationCodes(stationCodes);
		Map<String, Long> map = new HashMap<String, Long>();
		String key = null;
		Iterator<String> it = null;
		if (null != list && list.size() > 0) {
			for (int i = 0; i < list.size(); i++) {
				it = list.get(i).keySet().iterator();
				if (it.hasNext()) {
					key = it.next();
					map.put(key, list.get(i).get(key));
				}
			}
		}
		return map;
	}

	@Override
	public List<StationInfoM> getStationInfoByEmiId(
			Map<String, Object> queryParams) {
		return stationInfoMMapper.getStationInfoByEmiId(queryParams);
	}

	@Override
	public List<DeviceInfoDto> getEmiInfoByStationCode(String stationCode) {
		return stationShareemiMapper.getEmiInfoByStationCode(stationCode);
	}

	@Override
	public void insertDeviceShare(StationShareemi share) {
		this.stationShareemiMapper.insertDeviceShare(share);
	}

	@Override
	public List<StationShareemi> getAllSharedEmis() {

		return stationShareemiMapper.selectAllSharedEmis();
	}

	@Override
	public StationInfoM checkStationNameIsExists(String stationName) {
		return stationInfoMMapper.checkStationNameIsExists(stationName);
	}

	@Override
	public Page<StationInfoM> getStationInfoByPage(
			Map<String, Object> queryParams) {
		Page<StationInfoM> pageInfo = new Page<StationInfoM>();
		// 查询总数
		int totalCount = this.stationInfoMMapper.getStationInfoTotalCount(queryParams);
		// 查询分页显示数据
		List<StationInfoM> queryList = this.stationInfoMMapper.getStationInfo(queryParams);
		if(queryList != null && queryList.size() > 0){
			List<String> sids = new ArrayList<String>();
			for (StationInfoM station : queryList) {
				sids.add(station.getStationCode());
			}
			Map<String, String> stationStatus = StationCache.getStationHealthState(sids);
			for(StationInfoM stationInfo : queryList){
				stationInfo.setStationCurrentState(stationStatus.get(stationInfo.getStationCode()));
				if (StringUtils.isEmpty(stationInfo.getSafeOprDate())) {
					stationInfo.setSafeOprDate("0");
				} else {
					SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
					long safeRunTime = 0L;// 安全运行开始时间毫秒
					try {
						safeRunTime = sdf.parse(stationInfo.getSafeOprDate()).getTime();
					} catch (ParseException e) {
						logger.error("parse error, " + e);
					}
					long currentTime = System.currentTimeMillis();// 当前时间毫秒
					// 计算天数
					long days = (currentTime - safeRunTime) / 1000 / 3600 / 24;
					stationInfo.setSafeOprDate(String.valueOf(days));
				}
			}
		}
		pageInfo.setList(queryList);
		pageInfo.setAllSize(totalCount);
		pageInfo.setCount(totalCount);
		pageInfo.setIndex(Integer.valueOf(queryParams.get("index").toString()));
		pageInfo.setPageSize(Integer.valueOf(queryParams.get("pageSize").toString()));
		int totalSize = 0;
		if (totalCount % Integer.valueOf(queryParams.get("pageSize").toString()) > 0) {
			totalSize = totalCount / Integer.valueOf(queryParams.get("pageSize").toString()) + 1;
		} else {
			totalSize = totalCount / Integer.valueOf(queryParams.get("pageSize").toString());
		}
		pageInfo.setAllSize(totalSize);
		return pageInfo;
	}

	@Override
	public List<TreeModel> getUserDomainTree(Map<String, Object> queryParams) {
		return this.domainMMapper.getUserDomainTree(queryParams);
	}

	@Override
	@Transactional
	public void insertStationAndDevice(StationInfoDto stationDto) {
		StationInfoM station = new StationInfoM();

		BeanUtils.copyProperties(stationDto, station);

		// 新增电站信息
		this.insertStation(station);
		
		// 如果新建分布式(1)电站，默认创建1#分区和1#子阵
		if(station.getOnlineType() == Integer.valueOf(StationInfoConstant.COMBINE_TYPE_DISTRIBUTE)){
			PhalanxInfoBean phalanx = new PhalanxInfoBean();
			phalanx.setCreateTime(new Date());
			phalanx.setPhalanxName("1#分区");
			phalanx.setStationCode(station.getStationCode());
			this.phalanxInfoMapper.insertUseGeneratedKeys(phalanx);
			SubarrayInfoBean subarray = new SubarrayInfoBean();
			subarray.setCreateTime(new Date());
			subarray.setPhalanxId(phalanx.getId());
			subarray.setPhalanxName(phalanx.getPhalanxName());
			subarray.setStationCode(station.getStationCode());
			subarray.setSubarrayName("1#子阵");
			this.subarrayInfoMapper.insertUseGeneratedKeys(subarray);
		}

		// 新增设备信息
		List<DeviceInfo> devList = stationDto.getDevInfoList();
		if (devList != null && devList.size() > 0) {
			this.deviceInfoService.insertDevInfos(devList);
		}
		
		//新增电价
		for(int i=0;i<stationDto.getPriceList().size();i++){
			stationDto.getPriceList().get(i).setId(UUID.randomUUID().toString().replaceAll("-", ""));
			stationDto.getPriceList().get(i).setDomainId(stationDto.getDomainId());
			stationDto.getPriceList().get(i).setEnterpriseId(stationDto.getEnterpriseId());
			stationDto.getPriceList().get(i).setStationCode(stationDto.getStationCode());
		}
		if(stationDto.getPriceList().size() > 0){
			stationInfoMMapper.insertPowerPrices(stationDto.getPriceList());
			StationPowerPriceCache.refreshStationData(stationDto.getPriceList());
		}
	}

	@Override
	public int getShareRmiSize(String stationCode) {
		return this.stationShareemiMapper.getShareRmiSize(stationCode);
	}

	@Override
	public void updateDeviceShare(StationShareemi share) {
		this.stationShareemiMapper.updateDeviceShare(share);
	}

	@Override
	public StationInfoM getStationByCode(String stationCode) {
		StationInfoM station = this.stationInfoMMapper.getStationByCode(stationCode);
		if(null == station) {
			station =  this.stationInfoMMapper.getStationByStationCode(stationCode);
		}
		if (station != null) {
			List<String> sids = new ArrayList<String>();
			sids.add(station.getStationCode());
			Map<String, String> stationStatus = StationCache.getStationHealthState(sids);
			station.setStationCurrentState(stationStatus.get(station.getStationCode()));
		}
		return station;
	}

	@Override
	public Double getPowerPriceById(String queryType, String queryId) {
		return this.stationInfoMMapper.getPowerPriceById(queryType, queryId);
	}

	@Override
	public List<DeviceInfo> getDevicesByStationCode(String stationCode) {
		return this.deviceInfoService.getDevicesByStationCode(stationCode);
	}

	@Override
	@Transactional
	public void updateStationAndDeviceInfo(StationInfoDto stationDto,List<Long> emiChanged) {
		StationInfoM stationInfo = new StationInfoM();
		BeanUtils.copyProperties(stationDto, stationInfo);
		
		// 判断电站图片是否改变
		StationInfoM oldStation = this.getStationByCode(stationDto.getStationCode());
		if(!StringUtils.isEmpty(oldStation.getStationFileId()) && 
				!oldStation.getStationFileId().equals(stationInfo.getStationFileId())){
			// 电站照片文件id改变，删除原有电站图片id对应数据
			this.fileManagerService.deleteFileById(oldStation.getStationFileId());
		}
		
		// 更新电站信息
		this.updateStationInfoMById(stationInfo);
		/**
		 * 当前新增电站暂不支持操作设备，暂时屏蔽
		 */
//		// 更新设备信息
//		List<DeviceInfo> devList = stationDto.getDevInfoList();
//		// 1、先查询该电站ID下挂的设备信息
//		List<DeviceInfo> oldDeviceInfoList = this.deviceInfoService.getDevicesByStationCode(stationDto.getStationCode());
//		List<Long> oldDevIds = new ArrayList<Long>();
//		for (DeviceInfo dev : oldDeviceInfoList) {
//			oldDevIds.add(dev.getId());
//		}
//		// 2、定义需要删除、更新、新增的设备信息
//		List<DeviceInfo> insertDeviceList = new ArrayList<DeviceInfo>();
//		List<DeviceInfo> updateDeviceList = new ArrayList<DeviceInfo>();
//		for (DeviceInfo deviceInfo : devList) {
//			if (deviceInfo.getId() != null) {
//				// 如果设备id不为空需要判断原有设备是否包含现有设备id，如果包含的需要更新，剩余的则需要删除
//				if (oldDevIds.contains(deviceInfo.getId())) {
//					updateDeviceList.add(deviceInfo);
//					// 存在的id不能删除
//					oldDevIds.remove(deviceInfo.getId());
//				}
//			} else {
//				// 如果设备id为空则新增设备信息
//				deviceInfo.setCreateDate(new Date());
//				insertDeviceList.add(deviceInfo);
//			}
//		}
//		// 3、新增设备信息
//		if (insertDeviceList != null && insertDeviceList.size() > 0) {
//			this.deviceInfoService.insertDevInfos(insertDeviceList);
//		}
//		// 4、更新设备信息
//		for (DeviceInfo deviceInfo : updateDeviceList) {
//			this.deviceInfoService.updateDevInfos(deviceInfo);
//		}
//		// 5、删除设备信息
//		if (oldDevIds.size() > 0) {
//			Object[] devIds = oldDevIds.toArray();
//			this.deviceInfoService.deleteDeviceInfos(devIds);
//		}
//		emiChanged.addAll(oldDevIds);
//		if(emiChanged != null && emiChanged.size() > 0){
//			this.stationShareemiMapper.deleteShareEmi(emiChanged);
//		}
//		for(DeviceInfo d : insertDeviceList){
//			if(d.getDevTypeId() == DevTypeConstant.EMI_DEV_TYPE_ID){
//				this.stationShareemiMapper.deleteShareEmiBystationCode(d.getStationCode());
//			}
//		}
		
		//新增电价
		for(int i=0;i<stationDto.getPriceList().size();i++){
			stationDto.getPriceList().get(i).setId(UUID.randomUUID().toString().replaceAll("-", ""));
			stationDto.getPriceList().get(i).setDomainId(stationDto.getDomainId());
			stationDto.getPriceList().get(i).setEnterpriseId(stationDto.getEnterpriseId());
			stationDto.getPriceList().get(i).setStationCode(stationDto.getStationCode());
		}
		stationInfoMMapper.deletePowerPrices(stationDto.getStationCode());
		if(stationDto.getPriceList().size() > 0){
			stationInfoMMapper.insertPowerPrices(stationDto.getPriceList());
			StationPowerPriceCache.refreshStationData(stationDto.getPriceList());
		}
		
		List<StationInfoM> sl = new ArrayList<StationInfoM>();
		sl.add(stationInfo);
		StationCache.updateStationInfo(sl);
	}

	@Override
	public void insertAndUpdateShareEmi(List<StationShareemi> shareEmiList) {

		for (StationShareemi share : shareEmiList) {
			// 查询该电站编号是否已经绑定共享环境检测仪
			int shareRmiSize = this.getShareRmiSize(share.getStationCode());
			if (shareRmiSize > 0) {
				// 通过电站编号更新绑定的环境检测仪
				this.updateDeviceShare(share);
			} else {
				// 如果没有查询到绑定的环境检测仪则新增
				this.insertDeviceShare(share);
			}
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Map<String, Object>> getPriceByStationCode(String stationCode) {
		List<Map<String, Object>> result = new ArrayList<Map<String,Object>>();
		List<PowerPriceM> prices = this.stationInfoMMapper.getPriceByStationCode(stationCode);
		Map<String, Object> map = null;
		List<Map<String, Object>> time = null;
		for(PowerPriceM price : prices){
			if(map != null && map.get("startDate").equals(price.getStartDate())){
				Map<String, Object> map1 = new HashMap<String, Object>();
				map1.put("startTime", price.getStartTime());
				map1.put("endTime", price.getEndTime());
				map1.put("price", price.getPrice());
				((List<Map<String, Object>>)map.get("hoursPrices")).add(map1);
			} else {
				if(map != null){
					result.add(map);
				}
				map = new HashMap<String, Object>();
				map.put("startDate", price.getStartDate());
				map.put("endDate", price.getEndDate());
				time = new ArrayList<Map<String,Object>>();
				Map<String, Object> map1 = new HashMap<String, Object>();
				map1.put("startTime", price.getStartTime());
				map1.put("endTime", price.getEndTime());
				map1.put("price", price.getPrice());
				time.add(map1);
				map.put("hoursPrices", time);
			}
			
		}
		result.add(map);
		return result;
	}

	@Override
	public List<StationInfoM> getStationByUser(UserInfo user) {
		return this.stationInfoMMapper.getStationByUser(user);
	}

    @Override
    public List<StationInfoM> getStationByStationName(String stationName) {
        List<StationInfoM> result = null;
        
        if (stationName != null && stationName.trim().length() > 0){
            result = stationInfoMMapper.getStationByStationName(stationName);
        }
        
        return result;
    }

	@Override
	public Page<StationInfoM> getNoBindingStationInfoByPage(
			Map<String, Object> queryParams) {
		Page<StationInfoM> pageInfo = new Page<StationInfoM>();
		// 查询总数
		int totalCount = this.stationInfoMMapper.getNoBindingStationInfoTotalCount(queryParams);
		// 查询分页显示数据
		List<StationInfoM> queryList = this.stationInfoMMapper.getNoBindingStationInfo(queryParams);
		if(queryList != null && queryList.size() > 0){
			List<String> sids = new ArrayList<String>();
			for (StationInfoM station : queryList) {
				sids.add(station.getStationCode());
			}
			Map<String, String> stationStatus = StationCache.getStationHealthState(sids);
			for(StationInfoM stationInfo : queryList){
				stationInfo.setStationCurrentState(stationStatus.get(stationInfo.getStationCode()));
				if (StringUtils.isEmpty(stationInfo.getSafeOprDate())) {
					stationInfo.setSafeOprDate("0");
				} else {
					SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
					long safeRunTime = 0L;// 安全运行开始时间毫秒
					try {
						safeRunTime = sdf.parse(stationInfo.getSafeOprDate()).getTime();
					} catch (ParseException e) {
						logger.error("parse error, " + e);
					}
					long currentTime = System.currentTimeMillis();// 当前时间毫秒
					// 计算天数
					long days = (currentTime - safeRunTime) / 1000 / 3600 / 24;
					stationInfo.setSafeOprDate(String.valueOf(days));
				}
			}
		}
		pageInfo.setList(queryList);
		pageInfo.setAllSize(totalCount);
		pageInfo.setCount(totalCount);
		pageInfo.setIndex(Integer.valueOf(queryParams.get("index").toString()));
		pageInfo.setPageSize(Integer.valueOf(queryParams.get("pageSize").toString()));
		int totalSize = 0;
		if (totalCount % Integer.valueOf(queryParams.get("pageSize").toString()) > 0) {
			totalSize = totalCount / Integer.valueOf(queryParams.get("pageSize").toString()) + 1;
		} else {
			totalSize = totalCount / Integer.valueOf(queryParams.get("pageSize").toString());
		}
		pageInfo.setAllSize(totalSize);
		return pageInfo;
	}
}
