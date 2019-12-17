package com.interest.ids.biz.web.appsettings.service.impl;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import org.apache.commons.collections.CollectionUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.interest.ids.biz.web.appsettings.controller.params.DeviceSignalParams;
import com.interest.ids.biz.web.appsettings.controller.params.SignalUpdateParams;
import com.interest.ids.biz.web.appsettings.dao.SignalDao;
import com.interest.ids.biz.web.appsettings.service.SignalConfigService;
import com.interest.ids.biz.web.appsettings.vo.SignalConfigVO;
import com.interest.ids.biz.web.appsettings.vo.SignalModelVo;
import com.interest.ids.biz.web.constant.ProtocolConstant;
import com.interest.ids.biz.web.license.service.ILicenseService;
import com.interest.ids.common.project.bean.Pagination;
import com.interest.ids.common.project.bean.alarm.AlarmModel;
import com.interest.ids.common.project.bean.device.DcConfig;
import com.interest.ids.common.project.bean.signal.DeviceInfo;
import com.interest.ids.common.project.bean.signal.SignalInfo;
import com.interest.ids.common.project.bean.signal.SignalModelInfo;
import com.interest.ids.common.project.bean.signal.SignalVersionInfo;
import com.interest.ids.common.project.bean.sm.StationInfoM;
import com.interest.ids.common.project.bean.subarray.SubarrayInfoBean;
import com.interest.ids.common.project.constant.AppConstants;
import com.interest.ids.common.project.constant.DataTypeConstant;
import com.interest.ids.common.project.constant.DevTypeConstant;
import com.interest.ids.common.project.enums.AlarmLevelEnum;
import com.interest.ids.common.project.enums.AlarmTypeEnum;
import com.interest.ids.common.project.enums.SignalTypeEnum;
import com.interest.ids.common.project.mapper.device.DcConfigMapper;
import com.interest.ids.common.project.mapper.signal.AlarmModelMapper;
import com.interest.ids.common.project.mapper.signal.DevInfoMapper;
import com.interest.ids.common.project.mapper.signal.NormalizedInfoMapper;
import com.interest.ids.common.project.mapper.signal.SignalInfoMapper;
import com.interest.ids.common.project.mapper.signal.SignalModelMapper;
import com.interest.ids.common.project.mapper.signal.SignalVersionInfoMapper;
import com.interest.ids.common.project.utils.RegularUtil;
import com.interest.ids.commoninterface.dao.station.StationInfoMMapper;
import com.interest.ids.commoninterface.dao.station.SubarrayInfoMapper;
import com.interest.ids.redis.caches.DeviceCache;

import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.entity.Example.Criteria;

/**
 * @Author: sunbjx
 * 
 * @Description: 接口实现：系统设置-参数配置-点表
 * 
 * @Date Created in 14:38 2018/1/2
 * 
 * @Modified By:
 */
@Service
public class SignalConfigServiceImpl implements SignalConfigService {

	private static final Logger logger = LoggerFactory.getLogger(SignalConfigServiceImpl.class);

	@Autowired
	private DevInfoMapper devInfoMapper;
	@Autowired
	private SignalVersionInfoMapper versionInfoMapper;
	@Autowired
	private SignalModelMapper modelMapper;
	@Autowired
	private SignalInfoMapper infoMapper;
	@Autowired
	private AlarmModelMapper alarmModelMapper;
	@Autowired
	private StationInfoMMapper stationInfoMMapper;
	@Autowired
	private NormalizedInfoMapper normalizedInfoMapper;
	@Autowired
	private SignalDao signalDao;
	@Autowired
	private DcConfigMapper dcConfigMapper;
	@Autowired
	private ILicenseService licenseService;
	@Autowired
	private SubarrayInfoMapper subarrayInfoMapper;

	@Transactional
	@Override
	public String importExcel(MultipartFile file, final String userType,
			final Long enterpriseId, Map<String, String> map) {
		String result = null;

		// 获取文件名
		String fileName = file.getOriginalFilename();

		// 验证文件格式
//		if (!RegularUtil.isExcel2007(fileName))
//			return ProtocolConstant.EXCEL_TYPE_ERROR;

		// 进一步判断文件是否为空(即判断其大小是否为0或其名是否为null)
		long size = file.getSize();
		if (StringUtils.isEmpty(fileName) || 0 == size)
			return map.get("tableNoData"); // 点表无数据

		// 初始化输入流
		InputStream is = null;
		try {
			is = file.getInputStream();
			Workbook wb = null;
			if (RegularUtil.isExcel2007(fileName)) {
				wb = new XSSFWorkbook(is);
			} else {
				wb = new HSSFWorkbook(is);
			}
			Row row1 = wb.getSheetAt(0).getRow(1);
			if (row1 == null) {
				return map.get("seccondNotNull"); //"第二行不能为空！"
			}
			// 校验第一行数据
			String rowValueFlag = this.checkRowValue(2, row1, map);
			if(rowValueFlag != null){
				throw new Exception(rowValueFlag);
			}
			// 根据第一页第一行第三列获取协议类型
			String protocolType = row1.getCell(2).getStringCellValue();
			if (!ProtocolConstant.SUPPORTED_PROTOCOLS.contains(protocolType)) {
				return map.get("protocolsNotSupported").replace("{0}", protocolType); //不支持此类(" + protocolType + ")协议!
			}
			String stationCode = null;
			if(ProtocolConstant.OZF.equals(protocolType)){
				// 判断此通管机是否已经导入过
				String devName = row1.getCell(5).getStringCellValue();
				String signalVersion = row1.getCell(1).getStringCellValue().trim() + "_" + row1.getCell(3).getStringCellValue().trim();
				// 名称和版本号都相同则执行扩容
				DeviceInfo devInfo = new DeviceInfo();
				devInfo.setDevName(devName);
				devInfo.setSignalVersion(signalVersion);
				int count = devInfoMapper.selectCount(devInfo);
				
				// 判断此电站是否存在
				String stationName = row1.getCell(6).getStringCellValue();
				StationInfoM station = this.stationInfoMMapper.getStationInfoByStationName(stationName);
				if (station == null) {
					return map.get("plantNotexistence").replace("{0}", stationName); //"此电站(" + stationName + ")不存在!"
				} else {
					stationCode = station.getStationCode();
				}
				if (count > 0) {
					result = this.read104AddDev(wb, stationCode, map);
					return result;
				}
				DeviceInfo devInfoName = new DeviceInfo();
				devInfoName.setDevName(devName);
				int countName = devInfoMapper.selectCount(devInfoName);
				if(countName > 0){
					return map.get("devNameNotRepeat").replace("{0}", devName); //设备名称(" + devName + ")不能重复!
				}
				DeviceInfo devInfoVersion = new DeviceInfo();
				devInfoVersion.setSignalVersion(signalVersion);
				int countVersion = devInfoMapper.selectCount(devInfoVersion);
				if(countVersion > 0){
					return map.get("versionNotRepeat").replace("{0}", signalVersion); //版本号(" + signalVersion + ")不能重复!
				}
			}else if (ProtocolConstant.SNMODBUS.equals(protocolType)){
				// 判断上能modbus点表是否已经导入
				String signalVersion = row1.getCell(1).getStringCellValue().trim() + "_" + row1.getCell(3).getStringCellValue().trim();
				SignalVersionInfo svi = new SignalVersionInfo();
				svi.setSignalVersion(signalVersion);
				int count = versionInfoMapper.selectCount(svi);
				if (count > 0) {
					return map.get("pointTableModelExists").replace("{0}", signalVersion); //该点表(" + signalVersion + ")模型已经存在!
				}
			}else if (ProtocolConstant.MQTT.equals(protocolType)) {
				// 判断之前的mqtt的点表是否导入
				String signalVersion = row1.getCell(1).getStringCellValue()
						.trim()
						+ "_" + row1.getCell(3).getStringCellValue().trim();
				SignalVersionInfo svi = new SignalVersionInfo();
				svi.setSignalVersion(signalVersion);
				int count = versionInfoMapper.selectCount(svi);
				// 进行mqtt点表扩容
				if (count > 0) {
					String mqttExtendResult = this.readMqttAndExtend(wb,
							signalVersion, map);
					return mqttExtendResult;
				}
			}
			
			// 校验sheet页是否准确
			int sheetNum = wb.getNumberOfSheets();
			if(sheetNum < 2){
				return map.get("excelPagesIncorrect"); //excel页数不正确!
			}

			switch (protocolType) {

			case ProtocolConstant.OZF:
				result = this.read104(wb, enterpriseId, stationCode, userType, map);
				break;
				

			case ProtocolConstant.SNMODBUS:
				result = this.readSnmodbus(wb, enterpriseId, userType, map);
				break;
			// MQTT和modbus协议相同
			case ProtocolConstant.MQTT:
			case ProtocolConstant.MODBUS:
				result = this.readMQTT(wb, enterpriseId, userType, protocolType, map);
				break;
			}
			

		} catch (Exception e) {
			logger.info("Excel read error: ", e);
		} finally {
			if (null != is) {
				try {
					is.close();
				} catch (IOException e) {
					is = null;
					logger.error("IO error: ", e);
				}
			}
		}
		return result;
	}
	// 对mqtt点表扩容
		private String readMqttAndExtend(Workbook wb, String signalVersion,
				Map<String, String> map) throws Exception{
			// TODO Auto-generated method stub
			String rowValueFlag = null;
			String returnResult = null;
			Sheet sheet = wb.getSheetAt(1);
			// 遍历行
			List<SignalInfo> signalList = new ArrayList<>();
			List<SignalModelInfo> signalModelList = new ArrayList<>();
			SignalModelInfo signalModelInfo = null;
			//已有的mqttmodel的名称
			List<String> exitMqttModelName = new ArrayList<>();
			//已有Mqtt设备的Id
			List<Long> exitMqttDevId = new ArrayList<>();
			try{
				//首先获取所有的点表模型信息
				Example example = new Example(SignalModelInfo.class);
				Example.Criteria criteria = example.createCriteria();
				criteria.andEqualTo("protocolCode", DevTypeConstant.MQTT);
				criteria.andEqualTo("signalVersion", signalVersion);
				List<SignalModelInfo> mqttModelList = modelMapper.selectByExample(example);
				for(SignalModelInfo mqttModel:mqttModelList){
					exitMqttModelName.add(mqttModel.getSignalName());
				}
				//获取mqtt对应的设备的id
				Example example2 = new Example(DeviceInfo.class);
				Example.Criteria criteria2 = example2.createCriteria();
				criteria2.andEqualTo("signalVersion", signalVersion);
				criteria2.andEqualTo("isLogicDelete", 0);
				List<DeviceInfo> mqttDevList = devInfoMapper.selectByExample(example2);
				for(DeviceInfo mqttDev:mqttDevList){
					exitMqttDevId.add(mqttDev.getId());
				}
				for (int r = sheet.getFirstRowNum() + 1; r <= sheet.getLastRowNum(); r++) {
					signalModelInfo = new SignalModelInfo();
					Row row = sheet.getRow(r);

					if (null == row)
						continue;
					rowValueFlag = checkRowValue(r + 1, row, map);
					if (rowValueFlag != null) {
						throw new Exception(rowValueFlag);
					}
					// 信号名称
					signalModelInfo.setSignalName(row.getCell(0).getStringCellValue().trim());
					signalModelInfo.setSignalAlias(row.getCell(0).getStringCellValue().trim());
					// 功能类型
					signalModelInfo.setSignalType(Integer.valueOf(row.getCell(1)
							.getStringCellValue().trim()));
					//数据类型
					signalModelInfo.setDataType(Integer.valueOf(row.getCell(2).getStringCellValue().trim()));
					// 单位
					if (!"N/A".equals(row.getCell(3).getStringCellValue().trim())) {
						signalModelInfo.setSignalUnit(row.getCell(3).getStringCellValue().trim());
					}
					// 增益
					signalModelInfo.setGain(Double.valueOf(row.getCell(4).getStringCellValue()
							.trim()));
					// 地址
					signalModelInfo.setSignalAddress(Integer.valueOf(row.getCell(5)
							.getStringCellValue().trim()));
					// 寄存器个数
					signalModelInfo.setRegisterNum(Short.valueOf(row.getCell(6).getStringCellValue().trim()));
					// 偏移量
					signalModelInfo.setOffset(Double.valueOf(row.getCell(7).getStringCellValue()
							.trim()));
					signalModelInfo.setSignalVersion(signalVersion);
					signalModelInfo.setProtocolCode(DevTypeConstant.MQTT);

					if (!exitMqttModelName.contains(signalModelInfo.getSignalName())) {
						signalModelList.add(signalModelInfo);
					}
				}
				// 查询数据库中已经存在模型
				List<SignalModelInfo> allModel = this.modelMapper.selectAll();
				List<SignalModelInfo> needInsertModel = new ArrayList<SignalModelInfo>();

				for (SignalModelInfo model : signalModelList) {
					boolean found = false;
					for (SignalModelInfo dbModel : mqttModelList) {
						if (dbModel.getSignalVersion().equals(model.getSignalVersion())
								&& dbModel.getSignalName()
										.equals(model.getSignalName())) {
							found = true;
						}
					}
					if (!found) {
						needInsertModel.add(model);
					}
				}
				if (needInsertModel.size() > 0) {
					this.modelMapper.insertAndGetId(needInsertModel);
				}
				if(exitMqttDevId!=null){
					//生成signalInfo
					for(Long id : exitMqttDevId){
						Date date = new Date();
						List<SignalInfo> infoT = new ArrayList<>();
						for(SignalModelInfo insertModel : needInsertModel){
							SignalInfo infoS = new SignalInfo();
							infoS.setSignalVersion(insertModel.getSignalVersion());
							infoS.setSignalName(insertModel.getSignalName());
							infoS.setSignalAlias(insertModel.getSignalAlias());
							infoS.setDataType(insertModel.getDataType());
							infoS.setSignalUnit(insertModel.getSignalUnit());
							infoS.setGain(insertModel.getGain());
							infoS.setSignalAddress(insertModel.getSignalAddress());
							infoS.setRegisterNum(insertModel.getRegisterNum());
							infoS.setOffset(insertModel.getOffset());
							infoS.setDeviceId(id);
							infoS.setSignalType(insertModel.getSignalType());
							infoS.setCreateDate(date);
							infoS.setModelId(insertModel.getId());
							infoT.add(infoS);
						}
						// 新增信号点信息
						if (infoT != null && infoT.size() > 0) {
							infoMapper.insertList(infoT);
						}
					}
				}
				
				return "1";
			}catch(Exception e){
				logger.error("read Mqtt error:error msg : " + e.getMessage());
				TransactionAspectSupport.currentTransactionStatus()
						.setRollbackOnly();
				returnResult = map.get("mqttResolutionFailure");
				return returnResult;
			}
		}
	@Transactional
	@Override
	public boolean deleteByVersionId(List<String> ids, Map<String, String> map) {
		try {
			for (String id : ids) {
				Long versionId = Long.valueOf(id);
				SignalVersionInfo versionInfo = versionInfoMapper.selectByPrimaryKey(versionId);

				// 版本
				Example versionExample = new Example(SignalVersionInfo.class);

				if (versionInfo.getProtocolCode().equals(ProtocolConstant.SNMODBUS)) {
					// 设备
					Example devExample = new Example(DeviceInfo.class);
					// 模型
					Example modelExample = new Example(SignalModelInfo.class);
					// 模型实例
					Example infoExample = new Example(SignalInfo.class);
					// 告警
					Example alarmExample = new Example(AlarmModel.class);

					devExample.createCriteria().andEqualTo("signalVersion", versionInfo.getSignalVersion());
					List<DeviceInfo> deviceInfoList = devInfoMapper.selectByExample(devExample);

					for (DeviceInfo di : deviceInfoList) {
						di.setSignalVersion(null);
						devInfoMapper.updateByExample(di, devExample);
					}

					modelExample.createCriteria().andEqualTo("signalVersion", versionInfo.getSignalVersion());
					infoExample.createCriteria().andEqualTo("signalVersion", versionInfo.getSignalVersion());
					alarmExample.createCriteria().andEqualTo("signalVersion", versionInfo.getSignalVersion());
					modelMapper.deleteByExample(modelExample);
					infoMapper.deleteByExample(infoExample);
					alarmModelMapper.deleteByExample(alarmExample);
					versionInfoMapper.deleteByPrimaryKey(Long.valueOf(id.toString()));
				}else {
					// 1、删除signal_version
					versionExample.createCriteria().andEqualTo("parentId", versionInfo.getId());
					List<SignalVersionInfo> childrenVersionList = versionInfoMapper.selectByExample(versionExample);
					Example deleteModel = new Example(SignalVersionInfo.class);
					Criteria model = deleteModel.createCriteria().orEqualTo("id", versionInfo.getId());
					model.orEqualTo("parentId", versionInfo.getId());
					versionInfoMapper.deleteByExample(deleteModel);
					// 2、删除设备
					// 根据versionId查询主设备
					Example parentVersionId = new Example(DeviceInfo.class);
					parentVersionId.createCriteria().andEqualTo("parentSignalVersionId", versionInfo.getId());
					List<DeviceInfo> dev = devInfoMapper.selectByExample(parentVersionId);
					// 告警
					Example alarmExample = new Example(AlarmModel.class);
					alarmExample.createCriteria().andEqualTo("signalVersion", versionInfo.getSignalVersion());
					alarmModelMapper.deleteByExample(alarmExample);
					boolean needDeleteDev = false;
					Long parentDevId = null;
					if(dev !=null && dev.size() > 0){
						// 有设备需要删除
						parentDevId = dev.get(0).getId();
						needDeleteDev = true;
					}
					// 3、根据设备删除对应的信号点
					Example parentDev = new Example(DeviceInfo.class);
					if (parentDevId != null){
						parentDev.createCriteria().andEqualTo("parentId", parentDevId);
					} else if (versionInfo.getProtocolCode().equals(ProtocolConstant.MQTT)) {
						parentDev.createCriteria().andEqualTo("signalVersion", versionInfo.getSignalVersion())
						.andEqualTo("protocolCode", ProtocolConstant.MQTT);
						needDeleteDev = true;
					} else {
						throw new RuntimeException(map.get("needQueryConditions")); //缺少查询条件
					}
					List<DeviceInfo> devs = devInfoMapper.selectByExample(parentDev);
					List<Long> devIds = new ArrayList<Long>();
					for(DeviceInfo d : devs){
						devIds.add(d.getId());
					}
					if(devIds.size() > 0){
						Example signalInfo = new Example(SignalInfo.class);
						signalInfo.createCriteria().andIn("deviceId", devIds);
						infoMapper.deleteByExample(signalInfo);
					}
					// 4、删除通讯配置
					Example dcConfig = new Example(DcConfig.class);
					dcConfig.createCriteria().andEqualTo("devId", parentDevId);
					dcConfigMapper.deleteByExample(dcConfig);
					
					if(needDeleteDev){
						Example deleteDev = new Example(DeviceInfo.class);
						if (parentDevId != null){
							Criteria devc = deleteDev.createCriteria().orEqualTo("id", parentDevId);
							devc.orEqualTo("parentId", parentDevId);
						} else if (versionInfo.getProtocolCode().equals(ProtocolConstant.MQTT)) {
							deleteDev.createCriteria().andEqualTo("signalVersion", versionInfo.getSignalVersion())
							.andEqualTo("protocolCode", ProtocolConstant.MQTT);
							needDeleteDev = true;
						}
						devInfoMapper.deleteByExample(deleteDev);
						DeviceCache.removeDevs(devs);// 清空缓存中的子设备
						DeviceCache.removeDevs(dev);// 清空缓存中的主设备
					}
					// 模型删除
					if (versionInfo.getProtocolCode().equals(ProtocolConstant.MQTT)){
						Example modelExample = new Example(SignalModelInfo.class);
						modelExample.createCriteria().andEqualTo("signalVersion", versionInfo.getSignalVersion());
						modelExample.createCriteria().andEqualTo("protocolCode", versionInfo.getProtocolCode());
						modelMapper.deleteByExample(modelExample);
					}else{ //这里先限定不是MQTT就是 104
						Example modelExample1 = new Example(SignalModelInfo.class);
						List<String> versions = new ArrayList<>();
						for(SignalVersionInfo modelInfo:childrenVersionList){
							String version = modelInfo.getSignalVersion();
							versions.add(version);
						}
						if(versions.size()>0){
							modelExample1.createCriteria().andIn("signalVersion", versions);//andEqualTo是累加的，不能循环使用
							modelMapper.deleteByExample(modelExample1);
						}
					}
				}
			}

		} catch (Exception e) {
			logger.error("Update failed: ", e);
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			return false;
		}
		return true;
	}

	@Override
	public List<SignalConfigVO> listSignalInfo(Pagination params) {
		List<SignalConfigVO> result;
		try {
			PageHelper.startPage(params.getIndex(), params.getPageSize());
			result = signalDao.listSignalInfo(params);
		} catch (Exception e) {
			logger.error("Failed to query", e);
			result = null;
		}
		return result;
	}

	@Override
	public boolean updateSignal(ArrayList<SignalUpdateParams> params, Long modeVersionId) {
		SignalInfo signal;
		boolean ok = true;
		try {
			// 根据版本的id获取对应的版本信息
			SignalVersionInfo version = versionInfoMapper.selectByPrimaryKey(modeVersionId);
			if(ProtocolConstant.MQTT.equals(version.getProtocolCode())) { // 如果是mqqt的协议类型的保存，就保存导入的点表的模板的增益和偏移量
				for(SignalUpdateParams s: params) {
					SignalModelInfo model = new SignalModelInfo();
					model.setId(s.getId());
					model.setGain(s.getSigGain());
					model.setOffset(s.getSigOffset());
					modelMapper.updateByPrimaryKeySelective(model); // 只修改增益和偏移量
				}
			} else { // 104协议保存到对应的设备的信号点的增益和偏移量
				for (SignalUpdateParams o : params) {
					signal = infoMapper.selectByPrimaryKey(o.getId());
					signal.setGain(o.getSigGain());
					signal.setOffset(o.getSigOffset());
					infoMapper.updateByPrimaryKey(signal);
				}
			}
			
			// signalDao.updateByParams(params);
		} catch (Exception e) {
			logger.error("Update signal error: ", e);
			ok = false;
		}
		return ok;
	}

	@Override
	public void convertToSignal(String modelIds, Long signalVersionId) {
		SignalModelVo vo = new SignalModelVo();
		vo.setSignalVersionId(signalVersionId);
		vo.setSignalType(SignalTypeEnum.GJ.getValue());
		List<SignalModelVo> all = this.signalDao.getSignalModel(vo);
		String stationCode = null;
		if(all != null && all.size() > 0){
			stationCode = all.get(0).getStationCode();
		}
		Example alarmExample = new Example(AlarmModel.class);
		Criteria c = alarmExample.createCriteria();
		c.andIn("modelId", Arrays.asList(modelIds.split(",")));
		c.andEqualTo("stationCode", stationCode);
		this.alarmModelMapper.deleteByExample(alarmExample);
		this.signalDao.updateAlarmToYx(false,Arrays.asList(modelIds.split(",")),stationCode);
	}

	@Override
	public List<SignalInfo> listSignalInfoByVersion(DeviceSignalParams params) {
		Example example = null;
		Example.Criteria criteria = null;
		List<SignalInfo> signalInfoList = new ArrayList<SignalInfo>();
		try {
			// 根据versionId查询点表信息
			SignalVersionInfo v = versionInfoMapper.selectByPrimaryKey(params.getVersionId());

			if (v == null){
				return signalInfoList;
			}

			if (ProtocolConstant.SNMODBUS.equals(v.getProtocolCode())) {
				// snmodbus协议没有遥信等信息
				return signalInfoList;
			} else if(ProtocolConstant.MQTT.equals(v.getProtocolCode()) || ProtocolConstant.MODBUS.equals(v.getProtocolCode())){
				signalInfoList = new Page<SignalInfo>();
				//查询模型点，组装为实例点
				Example vExample = new Example(SignalModelInfo.class);
				vExample.createCriteria().andEqualTo("signalVersion", v.getSignalVersion());
				criteria= vExample.createCriteria();
				if (null != params.getSignalName()) {
					criteria.andLike("signalName", "%".concat(params.getSignalName()).concat("%"));
				}
				if (null != params.getVersion()) {
					criteria.andLike("signalVersion", "%".concat(params.getVersion()).concat("%"));
				}
				if (null != params.getSignalType()) {
					criteria.andEqualTo("signalType", params.getSignalType());
				}
				PageHelper.startPage(params.getIndex(), params.getPageSize());
				List<SignalModelInfo> models = modelMapper.selectByExample(vExample);
				PageInfo<SignalModelInfo> pageInfo = new PageInfo<SignalModelInfo>(models);
				Page<SignalInfo> infos = (Page)signalInfoList;
				infos.setTotal(pageInfo.getTotal());
				if(CollectionUtils.isNotEmpty(models)){
					for (SignalModelInfo model : models) {
						SignalInfo info=new SignalInfo();
						info.setId(model.getId()); // 信号点的id是当前导入点表模板的信号点id，后面修改增益和偏移量就是根据他修改
						info.setBit(model.getBit());
						info.setDataType(model.getDataType());
						info.setGain(model.getGain());
						info.setModelId(model.getId());
						info.setOffset(model.getOffset());
						info.setProtocolCode(model.getProtocolCode());
						info.setRegisterNum(model.getRegisterNum());
						info.setSignalAddress(model.getSignalAddress());
						info.setRegisterType(model.getSignalType());
						info.setSignalName(model.getSignalName());
						info.setSignalAlias(model.getSignalAlias());
						info.setSignalUnit(model.getSignalUnit());
						info.setSignalVersion(model.getSignalVersion());
						info.setSignalType(model.getSignalType());
						infos.add(info);
					}
				}
				return infos;
			}else {
				if (v.getSignalVersion() == null){
					return signalInfoList;
				}

				Example vExample = new Example(SignalVersionInfo.class);
				vExample.createCriteria().andEqualTo("parentId", v.getId());
				List<SignalVersionInfo> vList = versionInfoMapper.selectByExample(vExample);

				if (vList == null || vList.size() == 0){
					return signalInfoList;
				}

				List<String> versionCodeList = new ArrayList<String>();
				for (SignalVersionInfo o : vList) {
					versionCodeList.add(o.getSignalVersion());
				}
				example = new Example(SignalInfo.class);
				criteria = example.createCriteria().andIn("signalVersion", versionCodeList);

			}

			
			List<Long> devIds = new ArrayList<>();
			// 存在设备名称查询只能搜索该通管机下的设备
			Example devTGJ = new Example(DeviceInfo.class);
			devTGJ.createCriteria().andEqualTo("parentSignalVersionId", params.getVersionId());
			List<DeviceInfo> tgjInfoList = devInfoMapper.selectByExample(devTGJ);
			if(tgjInfoList != null && tgjInfoList.size() > 0){
				DeviceInfo tgj = tgjInfoList.get(0);
				Example dev = new Example(DeviceInfo.class);
				Criteria devC = dev.createCriteria();
				if (params.getDeviceName() != null) {
				devC.andLike("devName", "%".concat(params.getDeviceName()).concat("%"));
				}
				devC.andEqualTo("parentId", tgj.getId());
				List<DeviceInfo> deviceInfoList = devInfoMapper.selectByExample(dev);
				
				for (DeviceInfo o : deviceInfoList) {
					devIds.add(o.getId());
				}
			
				criteria.andIn("deviceId", devIds);
			}
			if (null != params.getSignalName()) {
				criteria.andLike("signalName", "%".concat(params.getSignalName()).concat("%"));
			}
			if (null != params.getVersion()) {
				criteria.andLike("signalVersion", "%".concat(params.getVersion()).concat("%"));
			}
			if (null != params.getSignalType()) {
				criteria.andEqualTo("signalType", params.getSignalType());
			}

			PageHelper.startPage(params.getIndex(), params.getPageSize());
			signalInfoList = infoMapper.selectByExample(example);
		} catch (Exception e) {
			logger.error("Failed to query", e);
			signalInfoList = null;
		}
		return signalInfoList;
	}

	@Override
	public String getDeviceNameById(Long deviceId) {
		String deviceName = null;
		try {
			DeviceInfo deviceInfo = devInfoMapper.selectByPrimaryKey(deviceId);
			if (null != deviceInfo)
				deviceName = deviceInfo.getDevName();
		} catch (Exception e) {
			logger.error("Query deviceName error： ", e);
		}
		return deviceName;
	}

	/**
	 * 读取上能modbus协议
	 * @param wb
	 * @param enterpriseId
	 * @param userType
	 * @return
	 */
	@Transactional
	private String readMQTT(Workbook wb, Long enterpriseId, String userType,String protocolType, Map<String, String> map){
		String returnResult = null;
		try {
			String rowValueFlag = null;
			String signalVersion = null;
			String protocolCode = null;
			Integer devTypeId = null;
			// 遍历 sheet
			for (int numSheet = 0; numSheet < wb.getNumberOfSheets(); numSheet++) {
				Sheet sheet = wb.getSheetAt(numSheet);
				if (sheet == null) {
					continue;
				}
				if (0 == numSheet) {// 设备信号版本信息
					Row r2 = sheet.getRow(1);
					rowValueFlag = checkRowValue(2,r2,map);
					if(rowValueFlag != null){
						throw new Exception(rowValueFlag);
					}
	
					String devType = r2.getCell(0).getStringCellValue().trim();
					devTypeId = DevTypeConstant.DEV_TYPE_I18N.get(devType);
					protocolCode = r2.getCell(2).getStringCellValue().trim();
					signalVersion = r2.getCell(1).getStringCellValue().trim()
							+ "_" + r2.getCell(3).getStringCellValue().trim();
					Date date = new Date();
					// 电表类型1：系统初始化，2：用户导入
					int type = AppConstants.SYSTEM_USER.equals(userType) ? AppConstants.SIGNAL_DATA_BY_SYSTEM
							: AppConstants.SIGNAL_DATA_BY_USER;
	
					// 父版本
					SignalVersionInfo pversion = new SignalVersionInfo();
					pversion.setName(sheet.getSheetName().trim());
					pversion.setSignalVersion(signalVersion);
					pversion.setEnterpriseId(enterpriseId);
					pversion.setDevTypeId(devTypeId);
					pversion.setVenderName(r2.getCell(4).getStringCellValue().trim());
					pversion.setProtocolCode(protocolCode);
					pversion.setInterfaceVersion(r2.getCell(3).getStringCellValue().trim());
					pversion.setCreateDate(date);
					pversion.setType(type);
					// 新增父版本
					this.versionInfoMapper.insert(pversion);
				}
				List<SignalModelInfo> modelList = new ArrayList<>();
				if (1 == numSheet) {// 信号点信息
					// 遍历行
					SignalModelInfo model = null;
					for (int r = sheet.getFirstRowNum() + 1; r <= sheet.getLastRowNum(); r++) {
						model = new SignalModelInfo();
						Row row = sheet.getRow(r);
						if (null == row)
							continue;
						rowValueFlag = checkRowValue(r+1,row,map);
						if(rowValueFlag != null){
							throw new Exception(rowValueFlag);
						}
						model.setProtocolCode(protocolCode);
						model.setSignalVersion(signalVersion);
						// 信号名称
						model.setSignalName(row.getCell(0).getStringCellValue().trim());
						model.setSignalAlias(model.getSignalName());
						// 功能类型
						model.setSignalType(Integer.valueOf(row.getCell(1).getStringCellValue().trim()));
						// 数据类型
						String dataType = row.getCell(2).getStringCellValue().trim();
						model.setDataType(Integer.valueOf(dataType));
						// 单位
						if(!"N/A".equals(row.getCell(3).getStringCellValue().trim())){
							model.setSignalUnit(row.getCell(3).getStringCellValue().trim());
						}
						// 增益
						model.setGain(Double.valueOf(row.getCell(4).getStringCellValue().trim()));
						// 地址
						model.setSignalAddress(Integer.valueOf(row.getCell(5).getStringCellValue().trim()));
						// 寄存器个数
						model.setRegisterNum(Short.valueOf(row.getCell(6).getStringCellValue().trim()));
						// 偏移量
						model.setOffset(Double.valueOf(row.getCell(7).getStringCellValue().trim()));
						modelList.add(model);
					}
					
					// 查询数据库中已经存在的该信号点版本的模型
					SignalModelInfo condition=new SignalModelInfo();
					condition.setSignalVersion(signalVersion);
					List<SignalModelInfo> exsitModel =this.modelMapper.select(condition);
					List<SignalModelInfo> needInsertModel = new ArrayList<SignalModelInfo>();
					Set<Integer> set=new HashSet<>();
					for (SignalModelInfo signalModel:exsitModel) {
						set.add(signalModel.getSignalAddress());
					}
					for (SignalModelInfo modelToInsert : modelList) {
						if(!set.contains(modelToInsert.getSignalAddress())){
							needInsertModel.add(modelToInsert);
						}
					}
					if(needInsertModel.size() > 0){
						this.modelMapper.insertAndGetId(needInsertModel);
					}
				}
				if (2 == numSheet) {// 告警信息
					List<AlarmModel> alarmModelList = new ArrayList<AlarmModel>();
					AlarmModel alarmModel = null;
					for (int r = sheet.getFirstRowNum() + 1; r <= sheet.getLastRowNum(); r++) {
						alarmModel = new AlarmModel();
						Row row = sheet.getRow(r);
						if (row == null)
							continue;
						alarmModel.setSignalVersion(signalVersion);
						alarmModel.setAlarmId(Math.abs(UUID.randomUUID().hashCode()));
						// 父告警名称
						alarmModel.setAlarmName(row.getCell(0).getStringCellValue().trim());
						alarmModel.setAlarmSubName(row.getCell(0).getStringCellValue().trim());
						// 告警级别
						row.getCell(1).setCellType(CellType.STRING);
						Byte alarmLevel = Byte.valueOf(row.getCell(1).getStringCellValue().trim());
						alarmModel.setSeverityId(alarmLevel);
						// 告警类型
						row.getCell(2).setCellType(CellType.STRING);
						alarmModel.setAlarmType(AlarmTypeEnum.getValue(row.getCell(2).getStringCellValue().trim()));
						// 寄存器地址
						row.getCell(3).setCellType(CellType.STRING);
						alarmModel.setSigAddress(Long.valueOf(row.getCell(3).getStringCellValue().trim()));
						alarmModel.setAlarmId(Integer.valueOf(row.getCell(3).getStringCellValue().trim()));
						alarmModel.setIsDeleted(false);
						// bit位
						row.getCell(4).setCellType(CellType.STRING);
						Byte bit = Byte.valueOf(row.getCell(4).getStringCellValue().trim());
						alarmModel.setBitIndex(bit);
						alarmModel.setCauseId(Integer.valueOf(bit));
						// 告警原因
						alarmModel.setAlarmCause(row.getCell(5).getStringCellValue().trim());
						// 修复建议
						alarmModel.setRepairSuggestion(row.getCell(6).getStringCellValue().trim());
						alarmModel.setDevTypeId(Short.valueOf(devTypeId.toString()));
//						alarmModel.setStationCode(ProtocolConstant.MQTT);
						alarmModel.setStationCode(protocolType); // 协议类型为当前的电站编号
						alarmModelList.add(alarmModel);
					}
					AlarmModel condition=new AlarmModel();
					condition.setSignalVersion(signalVersion);
					Set<String> set=new HashSet<>();
					List<AlarmModel> alarmModels=this.alarmModelMapper.select(condition);
					List<AlarmModel> needInsertAlarmModels=new ArrayList<>();
					for (AlarmModel model:alarmModels) {
						set.add(model.getSigAddress()+"#"+model.getBitIndex());
					}
					for (AlarmModel model : alarmModelList) {
						if(!set.contains(model.getSigAddress()+"#"+model.getBitIndex())){
							needInsertAlarmModels.add(model);
						}
					}
					if(needInsertAlarmModels.size()>0){
						this.alarmModelMapper.insertList(needInsertAlarmModels);
					}
				}
			}
			returnResult = "1";
		} catch (Exception e) {
			logger.error("read SNMODBUS error:error msg : " + e.getMessage());
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			returnResult = map.get("parsePointTableFail"); //"解析SNMODBUS点表失败"
		}
		return returnResult;
	}
	
	
	/**
	 * 读取上能modbus协议
	 * @param wb
	 * @param enterpriseId
	 * @param userType
	 * @return
	 */
	@Transactional
	private String readSnmodbus(Workbook wb, Long enterpriseId, String userType, Map<String, String> map){
		String returnResult = null;
		try {
			String rowValueFlag = null;
			String signalVersion = null;
			String protocolCode = null;
			Integer devTypeId = null;
			// 遍历 sheet
			for (int numSheet = 0; numSheet < wb.getNumberOfSheets(); numSheet++) {
				Sheet sheet = wb.getSheetAt(numSheet);
				if (sheet == null) {
					continue;
				}
				if (0 == numSheet) {// 设备信号版本信息
					Row r2 = sheet.getRow(1);
					rowValueFlag = checkRowValue(2,r2,map);
					if(rowValueFlag != null){
						throw new Exception(rowValueFlag);
					}
	
					String devType = r2.getCell(0).getStringCellValue().trim();
					devTypeId = DevTypeConstant.DEV_TYPE_I18N.get(devType);
					protocolCode = r2.getCell(2).getStringCellValue().trim();
					signalVersion = r2.getCell(1).getStringCellValue().trim()
							+ "_" + r2.getCell(3).getStringCellValue().trim();
					Date date = new Date();
					// 电表类型1：系统初始化，2：用户导入
					int type = AppConstants.SYSTEM_USER.equals(userType) ? AppConstants.SIGNAL_DATA_BY_SYSTEM
							: AppConstants.SIGNAL_DATA_BY_USER;
	
					// 父版本
					SignalVersionInfo pversion = new SignalVersionInfo();
					pversion.setName(sheet.getSheetName().trim());
					pversion.setSignalVersion(signalVersion);
					pversion.setEnterpriseId(enterpriseId);
					pversion.setDevTypeId(devTypeId);
					pversion.setVenderName(r2.getCell(4).getStringCellValue().trim());
					pversion.setProtocolCode(protocolCode);
					pversion.setInterfaceVersion(r2.getCell(3).getStringCellValue().trim());
					pversion.setCreateDate(date);
					pversion.setType(type);
					// 新增父版本
					this.versionInfoMapper.insert(pversion);
				}
	
				if (1 == numSheet) {// 信号点信息
					
					// 遍历行
					List<SignalInfo> signalList = new ArrayList<>();
					SignalInfo info = null;
					for (int r = sheet.getFirstRowNum() + 1; r <= sheet.getLastRowNum(); r++) {
						info = new SignalInfo();
						Row row = sheet.getRow(r);
	
						if (null == row)
							continue;
						rowValueFlag = checkRowValue(r+1,row,map);
						if(rowValueFlag != null){
							throw new Exception(rowValueFlag);
						}
						
						info.setProtocolCode(protocolCode);
						info.setSignalVersion(signalVersion);
						// 信号名称
						info.setSignalName(row.getCell(0).getStringCellValue().trim());
						info.setSignalAlias(row.getCell(0).getStringCellValue().trim());
						// 功能类型
						info.setSignalType(SignalTypeEnum.getValue(row.getCell(1).getStringCellValue().trim()));
						// 数据类型
						String dataType = row.getCell(2).getStringCellValue().trim();
						info.setDataType(DataTypeConstant.DATA_TYPE.get(dataType));
						// 单位
						if(!"N/A".equals(row.getCell(3).getStringCellValue().trim())){
							info.setSignalUnit(row.getCell(3).getStringCellValue().trim());
						}
						// 增益
						info.setGain(Double.valueOf(row.getCell(4).getStringCellValue().trim()));
						// 地址
						info.setSignalAddress(Integer.valueOf(row.getCell(5).getStringCellValue().trim()));
						// 寄存器个数
						info.setRegisterNum(Short.valueOf(row.getCell(6).getStringCellValue().trim()));
						// 偏移量
						info.setOffset(Double.valueOf(row.getCell(7).getStringCellValue().trim()));
						// 寄存器类型
						info.setRegisterType(Integer.valueOf(row.getCell(8).getStringCellValue().trim()));
						// bit位
						String bit = row.getCell(9).getStringCellValue().trim();
						if(!StringUtils.isEmpty(bit) && !"N/A".equals(bit)){
							info.setBit(Integer.valueOf(row.getCell(9).getStringCellValue().trim()));
						}
	
						signalList.add(info);
					}
					// 通过信号点信息生成信号点模型
					List<SignalModelInfo> modelList = this.generateModel(signalList);
					
					// 查询数据库中已经存在模型
					List<SignalModelInfo> allModel = this.modelMapper.selectAll();
					List<SignalModelInfo> needInsertModel = new ArrayList<SignalModelInfo>();
					
					for(SignalModelInfo model : modelList){
						boolean found = false;
						for(SignalModelInfo dbModel : allModel){
							if(dbModel.getSignalVersion().equals(model.getSignalVersion()) &&
									dbModel.getSignalName().equals(model.getSignalName())){
								found = true;
								break;
							}
						}
						if(!found){
							needInsertModel.add(model);
						}
					}
					if(needInsertModel.size() > 0){
						this.modelMapper.insertAndGetId(needInsertModel);
					}
					
					// 更新modelId
					for(SignalInfo signalInfo : signalList){
						
						for(SignalModelInfo model : needInsertModel){
							if(signalInfo.getSignalVersion().equals(model.getSignalVersion()) &&
									signalInfo.getSignalName().equals(model.getSignalName())){
								signalInfo.setModelId(model.getId());
								continue;
							}
						}
						for(SignalModelInfo model : allModel){
							if(signalInfo.getSignalVersion().equals(model.getSignalVersion()) &&
									signalInfo.getSignalName().equals(model.getSignalName())){
								signalInfo.setModelId(model.getId());
							}
						}
					}
					
					// 新增信号点信息
					if (signalList != null && signalList.size() > 0) {
						infoMapper.insertList(signalList);
					}
				}
				if (2 == numSheet) {// 告警信息
					List<AlarmModel> alarmModelList = new ArrayList<AlarmModel>();
					AlarmModel alarmModel = null;
					for (int r = sheet.getFirstRowNum() + 1; r <= sheet.getLastRowNum(); r++) {
						alarmModel = new AlarmModel();
						Row row = sheet.getRow(r);
	
						if (row == null)
							continue;
						alarmModel.setSignalVersion(signalVersion);
						alarmModel.setAlarmId(Math.abs(UUID.randomUUID().hashCode()));
						// 父告警名称
						alarmModel.setAlarmName(row.getCell(0).getStringCellValue().trim());
						alarmModel.setAlarmSubName(row.getCell(0).getStringCellValue().trim());
						// 告警级别
						String alarmLevelName = row.getCell(1).getStringCellValue().trim();
						Byte alarmLevel = Byte.valueOf(String.valueOf(AlarmLevelEnum.getValue(alarmLevelName)));
						alarmModel.setSeverityId(alarmLevel);
						// 告警类型
						alarmModel.setAlarmType(AlarmTypeEnum.getValue(row.getCell(2).getStringCellValue().trim()));
						// 寄存器地址
						alarmModel.setSigAddress(Long.valueOf(row.getCell(3).getStringCellValue().trim()));
						alarmModel.setIsDeleted(false);
						// bit位
//						Byte bit = Byte.valueOf(row.getCell(4).getStringCellValue().trim());
//						alarmModel.setBitIndex(bit);
						// 告警原因
						alarmModel.setAlarmCause(row.getCell(5).getStringCellValue().trim());
						// 修复建议
						alarmModel.setRepairSuggestion(row.getCell(6).getStringCellValue().trim());
						alarmModel.setDevTypeId(Short.valueOf(devTypeId.toString()));
						alarmModelList.add(alarmModel);
					}
					this.alarmModelMapper.insertList(alarmModelList);
				}
			}
			returnResult = "1";
		} catch (Exception e) {
			logger.error("read SNMODBUS error:error msg : " + e.getMessage());
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			returnResult = map.get("parsePointTableFail"); //"解析SNMODBUS点表失败"
		}

		return returnResult;
	}
	
	/**
	 * 读取 通管机&104  协议的数据
	 * 
	 * @param numSheet
	 * @param sheet
	 */
	@Transactional
	private String read104(Workbook wb, Long enterpriseId, String stationCode,String userType, Map<String, String> map) {
		String returnResult = null;
		try {
			// 根据电站code查询子阵和方阵id
			Example ex = new Example(SubarrayInfoBean.class);
			ex.createCriteria().andEqualTo("stationCode", stationCode);
			List<SubarrayInfoBean> subarrayList = this.subarrayInfoMapper.selectByExample(ex);
			Long subarrayId = null;
			Long phalanxId = null;
			if(subarrayList != null && subarrayList.size() > 0){
				subarrayId = subarrayList.get(0).getId();
				phalanxId = subarrayList.get(0).getPhalanxId();
			}
			String rowValueFlag = null;
			// 获取设备新增成功后返回的设备id及版本信息
			Map<String, String> devInfoMap = new HashMap<String, String>();
			// 遍历 sheet
			for (int numSheet = 0; numSheet < wb.getNumberOfSheets(); numSheet++) {
				Sheet sheet = wb.getSheetAt(numSheet);
				if (sheet == null) {
					continue;
				}
				if (0 == numSheet) {// 新增设备和版本信息
					// 104 类型 隐含父设备
					Row r2 = sheet.getRow(1);
					rowValueFlag = checkRowValue(2,r2, map);
					if(rowValueFlag != null){
						throw new Exception(rowValueFlag);
					}
	
					String protocolCode = r2.getCell(2).getStringCellValue().trim();
					String devType = r2.getCell(0).getStringCellValue().trim();
					Integer devTypeId = DevTypeConstant.DEV_TYPE_I18N.get(devType);
					String dataModelName = r2.getCell(5).getStringCellValue().trim();
					String signalVersion = r2.getCell(1).getStringCellValue().trim()
							+ "_" + r2.getCell(3).getStringCellValue().trim();
					Date date = new Date();
	
					// 父设备
					DeviceInfo pdev = new DeviceInfo();
					pdev.setDevTypeId(devTypeId);
					pdev.setCreateDate(date);
					pdev.setProtocolCode(protocolCode);
					pdev.setDevAlias(dataModelName);
					pdev.setDevName(dataModelName);
					pdev.setStationCode(stationCode);
					pdev.setEnterpriseId(enterpriseId);
					pdev.setSignalVersion(signalVersion);
					pdev.setIsLogicDelete(false);
					pdev.setPhalanxId(phalanxId);
					pdev.setMatrixId(subarrayId);
	
					// 电表类型1：系统初始化，2：用户导入
					int type = AppConstants.SYSTEM_USER.equals(userType) ? AppConstants.SIGNAL_DATA_BY_SYSTEM
							: AppConstants.SIGNAL_DATA_BY_USER;
	
					// 父版本
					SignalVersionInfo pversion = new SignalVersionInfo();
					pversion.setName(sheet.getSheetName().trim());
					pversion.setSignalVersion(signalVersion);
					pversion.setEnterpriseId(enterpriseId);
					pversion.setStationCode(stationCode);
					pversion.setDevTypeId(devTypeId);
					pversion.setVenderName(r2.getCell(4).getStringCellValue().trim());
					pversion.setProtocolCode(protocolCode);
					pversion.setInterfaceVersion(r2.getCell(3).getStringCellValue().trim());
					pversion.setCreateDate(date);
					pversion.setType(type);
					// 新增父版本
					this.versionInfoMapper.insertAndGetId(pversion);
					Long versionParentId = pversion.getId();
					
					// 入库父设备并获取父设备id
					pdev.setParentSignalVersionId(versionParentId);
					devInfoMapper.insertAndGetId(pdev);
					DeviceCache.putDev(pdev);
					Long parentId = pdev.getId();
					// 新增父设备断连的告警模型
					this.alarmModelMapper.insertList(getParentDisconnectedAlarmModel(stationCode, signalVersion, devTypeId,map));
					
					// 子版本信息
					List<SignalVersionInfo> versionList = new ArrayList<SignalVersionInfo>();
					// 子设备信息
					List<DeviceInfo> devList = new ArrayList<DeviceInfo>();
					SignalVersionInfo versionInfo = null;
					DeviceInfo deviceInfo = null;
					String devSignalVersion = null;
					// 遍历行 设置子设备子版本
					for (int r = sheet.getFirstRowNum() + 2; r <= sheet.getLastRowNum(); r++) {
						Row row = sheet.getRow(r);
						if (null == row)
							continue;
						rowValueFlag = checkRowValue(r+1,row,map);
						if(rowValueFlag != null){
							throw new Exception(rowValueFlag);
						}
						devSignalVersion = row.getCell(1).getStringCellValue().trim()
								+ "_" + row.getCell(3).getStringCellValue().trim();
						versionInfo = new SignalVersionInfo();
						// 设备类型
						versionInfo.setDevTypeId(DevTypeConstant.DEV_TYPE_I18N.get(row.getCell(0).getStringCellValue().trim()));
						// 版本号
						versionInfo.setSignalVersion(devSignalVersion);
						// 协议类型
						versionInfo.setProtocolCode(row.getCell(2).getStringCellValue().trim());
						// 协议版本号
						versionInfo.setInterfaceVersion(row.getCell(3).getStringCellValue().trim());
						// 厂商名称
						versionInfo.setVenderName(row.getCell(4).getStringCellValue().trim());
						// 电站名称
						versionInfo.setStationCode(stationCode);
						// 父版本id
						versionInfo.setParentId(versionParentId);
	
						versionInfo.setEnterpriseId(enterpriseId);
						versionInfo.setCreateDate(date);
						versionInfo.setType(type);
						boolean isExistVersion = false;
						for(SignalVersionInfo version : versionList){
							if(version.getSignalVersion().equals(versionInfo.getSignalVersion())){
								isExistVersion = true;
							}
						}
						if(!isExistVersion){
							versionList.add(versionInfo);
						}
	
						// 子设备
						deviceInfo = new DeviceInfo();
						// 版本
						deviceInfo.setSignalVersion(devSignalVersion);
						// 通管机名称
						deviceInfo.setDevTypeId(DevTypeConstant.DEV_TYPE_I18N.get(row.getCell(0).getStringCellValue().trim()));
						// 业务编号
						deviceInfo.setDevName(row.getCell(5).getStringCellValue().trim());
						// 别名
						deviceInfo.setDevAlias(row.getCell(5).getStringCellValue().trim());
						// 协议
						deviceInfo.setProtocolCode(row.getCell(2).getStringCellValue().trim());
	
						deviceInfo.setCreateDate(date);
						deviceInfo.setIsLogicDelete(false);
						deviceInfo.setStationCode(stationCode);
						deviceInfo.setEnterpriseId(enterpriseId);
						deviceInfo.setParentId(parentId);
	
						deviceInfo.setPhalanxId(phalanxId);
						deviceInfo.setMatrixId(subarrayId);
						devList.add(deviceInfo);
					}
					// 新增子设备和子版本信息
					if (devList != null && devList.size() > 0) {
						int inverterCount = 0;
						for(DeviceInfo devInfo : devList){
							if(devInfo.getDevTypeId().equals(DevTypeConstant.INVERTER_DEV_TYPE)
									|| devInfo.getDevTypeId().equals(DevTypeConstant.CENTER_INVERT_DEV_TYPE)){
								inverterCount++;
							}
						}
//						boolean isOverload = this.licenseService.beyondDevNumLimit(inverterCount);
//						if(isOverload){
//							throw new Exception("the inverter count is overload!");
//						}
						this.devInfoMapper.insertDevInfos(devList);
						DeviceCache.putDevs(devList);
					}
					// 设备新增成功后需要获取id和信号版本
					for (DeviceInfo devInfo : devList) {
						devInfoMap.put(devInfo.getDevName(), devInfo.getId() + "," + devInfo.getSignalVersion()
								+ "," + devInfo.getProtocolCode());
					}
					if (versionList != null && versionList.size() > 0) {
						this.versionInfoMapper.insertList(versionList);
					}
				}
	
				if (1 == numSheet) {
					// 遍历行
					List<SignalInfo> signalList = new ArrayList<>();
					SignalInfo info = null;
					String devIdAndSignalVersion = null;
					for (int r = sheet.getFirstRowNum() + 1; r <= sheet.getLastRowNum(); r++) {
						info = new SignalInfo();
						Row row = sheet.getRow(r);
	
						if (null == row)
							continue;
						rowValueFlag = checkRowValue(r+1,row,map);
						if(rowValueFlag != null){
							throw new Exception(rowValueFlag);
						}
						// 信号名称
						info.setSignalName(row.getCell(7).getStringCellValue().trim());
						info.setSignalAlias(row.getCell(0).getStringCellValue().trim());
						// 功能类型
						info.setSignalType(Integer.valueOf(row.getCell(1).getStringCellValue().trim()));
						// 单位
						if(!"N/A".equals(row.getCell(2).getStringCellValue().trim())){
							info.setSignalUnit(row.getCell(2).getStringCellValue().trim());
						}
						// 增益
						info.setGain(Double.valueOf(row.getCell(3).getStringCellValue().trim()));
						// 地址
						info.setSignalAddress(Integer.valueOf(row.getCell(4).getStringCellValue().trim()));
						// 偏移量
						info.setOffset(Double.valueOf(row.getCell(5).getStringCellValue().trim()));
						// 设备名称
						String devName = row.getCell(6).getStringCellValue().trim();
						info.setDevName(devName);
						devIdAndSignalVersion = devInfoMap.get(devName);
	
						if (!StringUtils.isEmpty(devIdAndSignalVersion)
								&& devIdAndSignalVersion.split(",").length == 3) {
							info.setDeviceId(Long.valueOf(devIdAndSignalVersion.split(",")[0]));
							info.setSignalVersion(devIdAndSignalVersion.split(",")[1]);
							info.setProtocolCode(devIdAndSignalVersion.split(",")[2]);
						} else {
							return map.get("devNotExistence").replace("{0}", devName); //该设备(" + devName + ")不存在！
						}
						signalList.add(info);
					}
					// 通过信号点信息生成信号点模型
					List<SignalModelInfo> modelList = this.generateModel(signalList);
					
					// 查询数据库中已经存在模型
					List<SignalModelInfo> allModel = this.modelMapper.selectAll();
					List<SignalModelInfo> needInsertModel = new ArrayList<SignalModelInfo>();
					
					for(SignalModelInfo model : modelList){
						boolean found = false;
						for(SignalModelInfo dbModel : allModel){
							if(dbModel.getSignalVersion().equals(model.getSignalVersion()) &&
									dbModel.getSignalName().equals(model.getSignalName())){
								found = true;
							}
						}
						if(!found){
							needInsertModel.add(model);
						}
					}
					if(needInsertModel.size() > 0){
						this.modelMapper.insertAndGetId(needInsertModel);
					}
					
					// 更新modelId
					for(SignalInfo signalInfo : signalList){
						
						for(SignalModelInfo model : needInsertModel){
							if(signalInfo.getSignalVersion().equals(model.getSignalVersion()) &&
									signalInfo.getSignalName().equals(model.getSignalName())){
								signalInfo.setModelId(model.getId());
								continue;
							}
						}
						for(SignalModelInfo model : allModel){
							if(signalInfo.getSignalVersion().equals(model.getSignalVersion()) &&
									signalInfo.getSignalName().equals(model.getSignalName())){
								signalInfo.setModelId(model.getId());
							}
						}
					}
					
					// 新增信号点信息
					if (signalList != null && signalList.size() > 0) {
						infoMapper.insertList(signalList);
					}
				}
			}
			returnResult = "1";
		} catch (Exception e) {
			logger.error("read 104 error:error msg : " + e.getMessage());
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			returnResult = map.get("parsePointTableFail"); //解析104点表失败
		}

		return returnResult;
	}
	
	/**
	 * 读取 通管机&104  协议的数据(扩容场景)
	 * 
	 * @param numSheet
	 * @param sheet
	 */
	private String read104AddDev(Workbook wb, String stationCode,Map<String, String> map) {
		String returnResult = null;
		List<String> existDevNameList = new ArrayList<String>();
		List<String> existSignalVersionList = new ArrayList<String>();
		try {
			// 根据电站code查询子阵和方阵id
			Example ex11 = new Example(SubarrayInfoBean.class);
			ex11.createCriteria().andEqualTo("stationCode", stationCode);
			List<SubarrayInfoBean> subarrayList = this.subarrayInfoMapper.selectByExample(ex11);
			Long subarrayId = null;
			Long phalanxId = null;
			if(subarrayList != null && subarrayList.size() > 0){
				subarrayId = subarrayList.get(0).getId();
				phalanxId = subarrayList.get(0).getPhalanxId();
			}
			String rowValueFlag = null;
			// 获取设备新增成功后返回的设备id及版本信息
			Map<String, String> devInfoMap = new HashMap<String, String>();
			// 遍历 sheet
			for (int numSheet = 0; numSheet < wb.getNumberOfSheets(); numSheet++) {
				Sheet sheet = wb.getSheetAt(numSheet);
				if (sheet == null) {
					continue;
				}
				if (0 == numSheet) {// 新增设备和版本信息
					// 104 类型 隐含父设备
					Row r2 = sheet.getRow(1);
					rowValueFlag = checkRowValue(2,r2,map);
					if(rowValueFlag != null){
						throw new Exception(rowValueFlag);
					}
	
					String protocolCode = r2.getCell(2).getStringCellValue().trim();
					String devType = r2.getCell(0).getStringCellValue().trim();
					Integer devTypeId = DevTypeConstant.DEV_TYPE_I18N.get(devType);
					String dataModelName = r2.getCell(5).getStringCellValue().trim();
					String signalVersion = r2.getCell(1).getStringCellValue().trim()
							+ "_" + r2.getCell(3).getStringCellValue().trim();
					Date date = new Date();
	
					// 父设备
					DeviceInfo pdev = new DeviceInfo();
					pdev.setDevTypeId(devTypeId);
					pdev.setCreateDate(date);
					pdev.setProtocolCode(protocolCode);
					pdev.setDevAlias(dataModelName);
					pdev.setDevName(dataModelName);
					pdev.setStationCode(stationCode);
					pdev.setSignalVersion(signalVersion);
					pdev.setIsLogicDelete(false);
					pdev.setPhalanxId(phalanxId);
					pdev.setMatrixId(subarrayId);
	
					// 父版本
					SignalVersionInfo pversion = new SignalVersionInfo();
					pversion.setName(sheet.getSheetName().trim());
					pversion.setSignalVersion(signalVersion);
					pversion.setStationCode(stationCode);
					pversion.setDevTypeId(devTypeId);
					pversion.setVenderName(r2.getCell(4).getStringCellValue().trim());
					pversion.setProtocolCode(protocolCode);
					pversion.setInterfaceVersion(r2.getCell(3).getStringCellValue().trim());
					pversion.setCreateDate(date);
					pversion.setType(2); // 用户导入
					// 新增父版本
					Example ex = new Example(SignalVersionInfo.class);
					ex.createCriteria().andEqualTo("signalVersion",pversion.getSignalVersion());
					List<SignalVersionInfo> parentVersion = this.versionInfoMapper.selectByExample(ex);
					Long versionParentId = parentVersion.get(0).getId();
					Example ex4 = new Example(SignalVersionInfo.class);
					ex4.createCriteria().andEqualTo("parentId",versionParentId);
					List<SignalVersionInfo> existSignalVersion = this.versionInfoMapper.selectByExample(ex4);
					for(SignalVersionInfo s : existSignalVersion){
						existSignalVersionList.add(s.getSignalVersion());
					}
					// 入库父设备并获取父设备id
					pdev.setParentSignalVersionId(versionParentId);
					Example ex1 = new Example(DeviceInfo.class);
					ex1.createCriteria().andEqualTo("devName",pdev.getDevName());
					List<DeviceInfo> parentDev = devInfoMapper.selectByExample(ex1);
					Long parentId = parentDev.get(0).getId();
					// 获取已存在的设备
					Example ex2 = new Example(DeviceInfo.class);
					ex2.createCriteria().andEqualTo("parentId", parentId);
					List<DeviceInfo> existDevList = this.devInfoMapper.selectByExample(ex2);
					for(DeviceInfo dev : existDevList){
						existDevNameList.add(dev.getDevName());
					}
					// 子版本信息
					List<SignalVersionInfo> versionList = new ArrayList<SignalVersionInfo>();
					// 子设备信息
					List<DeviceInfo> devList = new ArrayList<DeviceInfo>();
					SignalVersionInfo versionInfo = null;
					DeviceInfo deviceInfo = null;
					String devSignalVersion = null;
					// 遍历行 设置子设备子版本
					for (int r = sheet.getFirstRowNum() + 2; r <= sheet.getLastRowNum(); r++) {
						Row row = sheet.getRow(r);
						if (null == row)
							continue;
						rowValueFlag = checkRowValue(r+1,row,map);
						if(rowValueFlag != null){
							throw new Exception(rowValueFlag);
						}
						devSignalVersion = row.getCell(1).getStringCellValue().trim()
								+ "_" + row.getCell(3).getStringCellValue().trim();
						versionInfo = new SignalVersionInfo();
						// 设备类型
						versionInfo.setDevTypeId(DevTypeConstant.DEV_TYPE_I18N.get(row.getCell(0).getStringCellValue().trim()));
						// 版本号
						versionInfo.setSignalVersion(devSignalVersion);
						// 协议类型
						versionInfo.setProtocolCode(row.getCell(2).getStringCellValue().trim());
						// 协议版本号
						versionInfo.setInterfaceVersion(row.getCell(3).getStringCellValue().trim());
						// 厂商名称
						versionInfo.setVenderName(row.getCell(4).getStringCellValue().trim());
						// 电站名称
						versionInfo.setStationCode(stationCode);
						// 父版本id
						versionInfo.setParentId(versionParentId);
						versionInfo.setType(2); // 用户导入
	
						versionInfo.setCreateDate(date);
						boolean isExistVersion = false;
						for(SignalVersionInfo version : versionList){
							if(version.getSignalVersion().equals(versionInfo.getSignalVersion())){
								isExistVersion = true;
							}
						}
						if(!isExistVersion && !existSignalVersionList.contains(versionInfo.getSignalVersion())){
							versionList.add(versionInfo);
						}
	
						// 子设备
						deviceInfo = new DeviceInfo();
						// 版本
						deviceInfo.setSignalVersion(devSignalVersion);
						// 通管机名称
						deviceInfo.setDevTypeId(DevTypeConstant.DEV_TYPE_I18N.get(row.getCell(0).getStringCellValue().trim()));
						// 业务编号
						deviceInfo.setDevName(row.getCell(5).getStringCellValue().trim());
						// 别名
						deviceInfo.setDevAlias(row.getCell(5).getStringCellValue().trim());
						// 协议
						deviceInfo.setProtocolCode(row.getCell(2).getStringCellValue().trim());
	
						deviceInfo.setCreateDate(date);
						deviceInfo.setIsLogicDelete(false);
						deviceInfo.setStationCode(stationCode);
						deviceInfo.setParentId(parentId);

						deviceInfo.setPhalanxId(phalanxId);
						deviceInfo.setMatrixId(subarrayId);
						if(!existDevNameList.contains(deviceInfo.getDevName())){
							devList.add(deviceInfo);
						}
					}
					// 新增子设备和子版本信息
					if (devList != null && devList.size() > 0) {
						int inverterCount = 0;
						for(DeviceInfo devInfo : devList){
							if(devInfo.getDevTypeId().equals(DevTypeConstant.INVERTER_DEV_TYPE)
									|| devInfo.getDevTypeId().equals(DevTypeConstant.CENTER_INVERT_DEV_TYPE)){
								inverterCount++;
							}
						}
						 boolean isOverload = this.licenseService.beyondDevNumLimit(inverterCount);
						 if(isOverload){
						 	throw new Exception("the inverter count is overload!");
						 }
						this.devInfoMapper.insertDevInfos(devList);
					}
					// 设备新增成功后需要获取id和信号版本
					for (DeviceInfo devInfo : devList) {
						devInfoMap.put(devInfo.getDevName(), devInfo.getId() + "," + devInfo.getSignalVersion()
								+ "," + devInfo.getProtocolCode());
					}
					if (versionList != null && versionList.size() > 0) {
						this.versionInfoMapper.insertList(versionList);
					}
				}
	
				if (1 == numSheet) {
					// 遍历行
					List<SignalInfo> signalList = new ArrayList<>();
					SignalInfo info = null;
					String devIdAndSignalVersion = null;
					for (int r = sheet.getFirstRowNum() + 1; r <= sheet.getLastRowNum(); r++) {
						info = new SignalInfo();
						Row row = sheet.getRow(r);
	
						if (null == row)
							continue;
						rowValueFlag = checkRowValue(r+1,row,map);
						if(rowValueFlag != null){
							throw new Exception(rowValueFlag);
						}
						// 信号名称
						info.setSignalName(row.getCell(7).getStringCellValue().trim());
						info.setSignalAlias(row.getCell(0).getStringCellValue().trim());
						// 功能类型
						info.setSignalType(Integer.valueOf(row.getCell(1).getStringCellValue().trim()));
						// 单位
						if(!"N/A".equals(row.getCell(2).getStringCellValue().trim())){
							info.setSignalUnit(row.getCell(2).getStringCellValue().trim());
						}
						// 增益
						info.setGain(Double.valueOf(row.getCell(3).getStringCellValue().trim()));
						// 地址
						info.setSignalAddress(Integer.valueOf(row.getCell(4).getStringCellValue().trim()));
						// 偏移量
						info.setOffset(Double.valueOf(row.getCell(5).getStringCellValue().trim()));
						// 设备名称
						String devName = row.getCell(6).getStringCellValue().trim();
						info.setDevName(devName);
						devIdAndSignalVersion = devInfoMap.get(devName);
	
						if (!StringUtils.isEmpty(devIdAndSignalVersion)
								&& devIdAndSignalVersion.split(",").length == 3) {
							info.setDeviceId(Long.valueOf(devIdAndSignalVersion.split(",")[0]));
							info.setSignalVersion(devIdAndSignalVersion.split(",")[1]);
							info.setProtocolCode(devIdAndSignalVersion.split(",")[2]);
						}
						if(!existDevNameList.contains(info.getDevName())){
							signalList.add(info);
						}
					}
					// 通过信号点信息生成信号点模型
					List<SignalModelInfo> modelList = this.generateModel(signalList);
					
					// 查询数据库中已经存在模型
					List<SignalModelInfo> allModel = this.modelMapper.selectAll();
					List<SignalModelInfo> needInsertModel = new ArrayList<SignalModelInfo>();
					
					for(SignalModelInfo model : modelList){
						boolean found = false;
						for(SignalModelInfo dbModel : allModel){
							if(dbModel.getSignalVersion().equals(model.getSignalVersion()) &&
									dbModel.getSignalName().equals(model.getSignalName())){
								found = true;
							}
						}
						if(!found){
							needInsertModel.add(model);
						}
					}
					if(needInsertModel.size() > 0){
						this.modelMapper.insertAndGetId(needInsertModel);
					}
					
					// 更新modelId
					for(SignalInfo signalInfo : signalList){
						
						for(SignalModelInfo model : needInsertModel){
							if(signalInfo.getSignalVersion().equals(model.getSignalVersion()) &&
									signalInfo.getSignalName().equals(model.getSignalName())){
								signalInfo.setModelId(model.getId());
								continue;
							}
						}
						for(SignalModelInfo model : allModel){
							if(signalInfo.getSignalVersion().equals(model.getSignalVersion()) &&
									signalInfo.getSignalName().equals(model.getSignalName())){
								signalInfo.setModelId(model.getId());
							}
						}
					}
					
					// 新增信号点信息
					if (signalList != null && signalList.size() > 0) {
						infoMapper.insertList(signalList);
					}
				}
			}
			returnResult = "1";
		} catch (Exception e) {
			logger.error("read 104 error:error msg : " + e.getMessage());
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			returnResult = map.get("104resolutionFailure"); //104解析失败！
		}

		return returnResult;
	}
	
	/**
	 * 通过信号点信息生成model
	 * 
	 * @param signalList
	 * @return
	 */
	private List<SignalModelInfo> generateModel(List<SignalInfo> signalList) {
		List<SignalModelInfo> modelList = null;
		if(signalList != null && signalList.size() > 0){
			modelList = new ArrayList<SignalModelInfo>();
			SignalModelInfo modelInfo = null;
			for(SignalInfo signalInfo :  signalList){
				boolean isExist = false;
				for(SignalModelInfo model : modelList){
					if(model.getSignalVersion().equals(signalInfo.getSignalVersion()) && 
							model.getSignalName().equals(signalInfo.getSignalName())){
						isExist = true;// 已存在模型，不增加
						break;
					}
				}
				if(!isExist){
					modelInfo = new SignalModelInfo();
					modelInfo.setSignalName(signalInfo.getSignalName());
					modelInfo.setSignalAlias(signalInfo.getSignalName());
					modelInfo.setSignalVersion(signalInfo.getSignalVersion());
					modelInfo.setSignalUnit(signalInfo.getSignalUnit());
					modelInfo.setSignalType(signalInfo.getSignalType());
					modelInfo.setDataType(signalInfo.getDataType());
					modelInfo.setGain(signalInfo.getGain());
					modelInfo.setOffset(signalInfo.getOffset());
					modelInfo.setRegisterNum(signalInfo.getRegisterNum());
					modelInfo.setSignalGroup(signalInfo.getSignalGroup());
					modelInfo.setBit(signalInfo.getBit());
					modelInfo.setProtocolCode(signalInfo.getProtocolCode());
					modelInfo.setCreateDate(new Date());
					modelList.add(modelInfo);
				}
			}
		}
		return modelList;
	}

	/**
	 * 检验行数据是否正确
	 * 
	 * @param index
	 * 				行号
	 * @param r
	 * 				行数据
	 * @return null为正确
	 */
	private String checkRowValue(int index,Row r,Map<String, String> map){
		// 遍历列
		for (int c = r.getFirstCellNum(); c < r.getLastCellNum(); c++) {
			Cell cell = r.getCell(c);
			if (cell != null) {
				cell.setCellType(CellType.STRING);
				if (StringUtils.isEmpty(cell.getStringCellValue()
						.trim())) {
					return map.get("notNull").replace("{0}", index + "").replace("{1}", (c + 1) + ""); // "第" + index + "行" + (c + 1) + "列不能为空"
				}
			} else {
				return map.get("notNull").replace("{0}", index + "").replace("{1}", (c + 1) + ""); // "第" + index + "行" + (c + 1) + "列不能为空"
			}
		}
		return null;
	}

	@Override
	public List<SignalModelVo> getSignalModel(SignalModelVo vo) {
		PageHelper.startPage(vo.getIndex(), vo.getPageSize());
		return this.signalDao.getSignalModel(vo);
	}

	@Override
	public void convertToAlarm(byte alarmLevel, String modelIds,
			Long signalVersionId,byte teleType) {
		String stationCode = "";
		// 查询所有单点遥信信号点
		SignalModelVo vo = new SignalModelVo();
		vo.setSignalVersionId(signalVersionId);
		vo.setSignalType(SignalTypeEnum.DDYX.getValue());
		List<SignalModelVo> all = this.signalDao.getSignalModel(vo);
		// 匹配需要配置的遥信转告警信息
		List<SignalModelVo> needConvert = new ArrayList<SignalModelVo>();
		for(SignalModelVo model : all){
			stationCode = model.getStationCode();
			if(modelIds.contains(String.valueOf(model.getModelId()))){
				needConvert.add(model);
			}
		}
		// 查询数据库看是否已存在已经遥信转告警的信息
		Example alarmExample = new Example(AlarmModel.class);
		alarmExample.createCriteria().andIn("modelId", Arrays.asList(modelIds.split(",")));
		List<AlarmModel> alarmModelList = this.alarmModelMapper.selectByExample(alarmExample);
		if(alarmModelList != null && alarmModelList.size() > 0){
			for(AlarmModel model : alarmModelList){
				// 已存在的遥信转告警执行更新操作
				model.setSeverityId(alarmLevel);
				model.setTeleType(teleType);
				alarmModelMapper.updateByPrimaryKey(model);
				// 在新增中去除更新的遥信
				for(SignalModelVo modelVo : needConvert){
					if(modelVo.getModelId().equals(model.getModelId()) && modelVo.getStationCode().equals(model.getStationCode())){
						needConvert.remove(modelVo);
						break;
					}
				}
			}
		}
		if(needConvert != null && needConvert.size() > 0){
			
			List<AlarmModel> insertList = new ArrayList<AlarmModel>();
			AlarmModel model = null;
			for(SignalModelVo modelVo : needConvert){
				model = new AlarmModel();
				model.setModelId(modelVo.getModelId());
				model.setAlarmName(modelVo.getSignalName());
				model.setAlarmSubName(modelVo.getSignalName());
				model.setAlarmType((byte)AlarmTypeEnum.ALARM.getValue());
				model.setDevTypeId(modelVo.getDevTypeId());
				model.setIsDeleted(false);
				model.setSeverityId(alarmLevel);
				model.setSignalVersion(modelVo.getSignalVersion());
				model.setStationCode(modelVo.getStationCode());
				model.setTeleType(teleType);
				model.setAlarmId(Integer.valueOf(modelVo.getModelId().toString()));
				model.setUpdateDate(new Date());
				insertList.add(model);
			}
			alarmModelMapper.insertList(insertList);
		}
		// 更新遥信为告警
		this.signalDao.updateIsAlarmFlagById(true, Arrays.asList(modelIds.split(",")), stationCode);
	}

	@Override
	public List<Object[]> selectSignalDev(String id, String signalVersion) {
		List<Object[]> data = null;
		List<Map<String, Object>> result = this.signalDao.selectSignalDev(id, signalVersion);
		if(result != null && result.size() > 0){
			data = new ArrayList<Object[]>();
			for (Map<String, Object> map : result) {
				Object[] o = new Object[7];
				// 设备类型
				o[0] = DevTypeConstant.DEV_TYPE_I18N_ID.get(map.get("dev_type_id"));
				// 设备型号
				o[1] = map.get("signal_version").toString().split("_")[0];
				// 协议类型
				o[2] = map.get("protocol_code");
				// 设备版本
				o[3] = map.get("signal_version").toString().split("_")[1];
				// 厂商名称
				o[4] = map.get("vender_name");
				// 设备名称
				o[5] = map.get("dev_name");
				// 电站名称
				o[6] = map.get("station_name");
				data.add(o);
			}
		}
		return data;
	}

	@Override
	public SignalVersionInfo getSignalVersionById(Long id) {
		
		return this.versionInfoMapper.selectByPrimaryKey(id);
	}

	@Override
	public List<Object[]> selectSignalInfo(String id) {
		List<Object[]> data = null;
		List<Map<String, Object>> result = this.signalDao.selectSignalInfo(id);
		if(result != null && result.size() > 0){
			data = new ArrayList<Object[]>();
			for (Map<String, Object> map : result) {
				Object[] o = new Object[8];
				// 设备信号名称
				o[0] = map.get("dev_signal_name");
				// 功能类型
				if(!StringUtils.isEmpty(map.get("signal_type"))){
					if(Integer.valueOf(map.get("signal_type").toString()) == SignalTypeEnum.GJ.getValue()){
						o[1] = SignalTypeEnum.DDYX.getValue();
					} else {
						o[1] = map.get("signal_type");
					}
				} else {
					o[1] = map.get("signal_type");
				}
				
				// 单位
				o[2] = map.get("signal_unit");
				// 增益
				o[3] = map.get("gain");
				// 信号地址
				o[4] = map.get("signal_address");
				// 偏移量
				o[5] = map.get("offset");
				// 设备名称
				o[6] = map.get("dev_name");
				// 信号名称
				o[7] = map.get("signal_name");
				data.add(o);
			}
		}
		return data;
	}
	/**
	 * 创建一个通管机或者输出设备的告警模型
	 * @param stationCode
	 * @param versionCode
	 * @param devTypeId
	 * @return
	 */
	private List<AlarmModel> getParentDisconnectedAlarmModel(String stationCode, String versionCode, Integer devTypeId, Map<String, String> map) {
		AlarmModel model = new AlarmModel();
		model.setAlarmId(65535);
		model.setCauseId(1);
		model.setAlarmCause(map.get("interruptionEquipmentCommunication")); //设备通讯中断
		model.setAlarmName(map.get("interruptionEquipmentCommunication")); //设备通讯中断
		model.setAlarmType((byte)1);
		model.setDevTypeId(devTypeId.shortValue());
		model.setStationCode(stationCode);
		model.setSignalVersion(versionCode);
		model.setSeverityId((byte)1);
		model.setRepairSuggestion(map.get("pleaseCheckEquipment"));//1.请检查设备是否连接\r\n2.请检查通讯配置是否正确
		List<AlarmModel> list = new ArrayList<>();
		list.add(model);
		return list;
	}

	@Override
	public List<SignalVersionInfo> getMqttVersions(String protocal) {
		SignalVersionInfo search = new SignalVersionInfo();
		search.setProtocolCode(protocal);
		return versionInfoMapper.select(search);
	}

	@Override
	public List<Object[]> selecMqttUsers(Map<String, String> map) {
		List<Map<String, Object>> list = this.signalDao.queryMqttUsers();
		List<Object[]> result = new ArrayList<Object[]>();
		if(CollectionUtils.isEmpty(list)) {
			return result;
		}
		for(Map<String, Object> u : list) {
			try {
				Object[] objArr = new Object[6];
				objArr[0] = u.get("id");
				objArr[1] = u.get("username");
				objArr[2] = u.get("password");
				objArr[3] = u.get("salt");
				objArr[4] = (Boolean)u.get("is_superuser")? map.get("yes"): map.get("no");//是  否
				objArr[5] = u.get("clientid");
				result.add(objArr);
			} catch(Exception e) {
				logger.error("coust exception:", e);
			}
		}
		return result;
	}

	@Override
	public List<Object[]> selectMqttSignalDev(String signalVersion) {
		List<Object[]> data = null;
		List<Map<String, Object>> result = this.signalDao.selectMqttSignalDev(signalVersion);
		if(result != null && result.size() > 0){
			data = new ArrayList<Object[]>();
			for (Map<String, Object> map : result) {
				Object[] o = new Object[5];
				// 设备类型
				o[0] = DevTypeConstant.DEV_TYPE_I18N_ID.get(map.get("dev_type_id"));
				// 设备型号
				o[1] = map.get("signal_version").toString().split("_")[0];
				// 协议类型
				o[2] = map.get("protocol_code");
				// 设备版本
				o[3] = map.get("signal_version").toString().split("_")[1];
				// 厂商名称
				o[4] = map.get("vender_name");
				data.add(o);
			}
		}
		return data;
	}

	@Override
	public List<Object[]> selectMqttSignalInfo(String signalVersion) {
		List<Object[]> data = null;
		List<Map<String, Object>> result = this.signalDao.selectMqttSignalInfo(signalVersion);
		if(result != null && result.size() > 0){
			data = new ArrayList<Object[]>();
			for (Map<String, Object> map : result) {
				Object[] o = new Object[8];
				// 设备信号名称
				o[0] = map.get("signal_name");
				// 功能类型
				if(!StringUtils.isEmpty(map.get("signal_type"))){
					if(Integer.valueOf(map.get("signal_type").toString()) == SignalTypeEnum.GJ.getValue()){
						o[1] = SignalTypeEnum.DDYX.getValue();
					} else {
						o[1] = map.get("signal_type");
					}
				} else {
					o[1] = map.get("signal_type");
				}
				//数据类型
				o[2] = map.get("data_type");
				// 单位
				o[3] = map.get("signal_unit");
				// 增益
				o[4] = map.get("gain");
				// 信号地址
				o[5] = map.get("signal_address");
				// 寄存器个数
				o[6] = map.get("register_num");
				// 偏移量
				o[7] = map.get("offset");
				data.add(o);
			}
		}
		return data;
	}

	@Override
	public List<Object[]> selectMqttAlarm(String signalVersion) {
		List<Object[]> data = null;
		List<Map<String, Object>> result = this.signalDao.selectMqttAlarm(signalVersion);
		if(result != null && result.size() > 0){
			data = new ArrayList<Object[]>();
			for (Map<String, Object> map : result) {
				Object[] o = new Object[7];
				// 告警名称
				o[0] = map.get("alarm_name");
				/*// 功能类型
				if(!StringUtils.isEmpty(map.get("signal_type"))){
					if(Integer.valueOf(map.get("signal_type").toString()) == SignalTypeEnum.GJ.getValue()){
						o[1] = SignalTypeEnum.DDYX.getValue();
					} else {
						o[1] = map.get("signal_type");
					}
				} else {
					o[1] = map.get("signal_type");
				}*/
				//告警级别
				o[1] = map.get("severity_id");
				//告警类型
				o[2] = map.get("alarm_type");
				// 寄存器地址
				o[3] = map.get("sig_address");
				// bit位
				o[4] = map.get("bit_index");
				// 告警原因
				o[5] = map.get("alarm_cause");
				// 修复意见
				o[6] = map.get("repair_suggestion");
				data.add(o);
			}
		}
		return data;
	}

}
