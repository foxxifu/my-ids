package com.interest.ids.gatekeeper.server.parse;

import java.io.File;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.interest.ids.common.project.utils.SpringBeanContext;
import com.interest.ids.commoninterface.service.station.StationInfoMService;
import com.interest.ids.dev.api.utils.JdbcExecutorUtils;
import com.interest.ids.gatekeeper.server.utils.GateKeeperConstant;
import com.interest.ids.gatekeeper.server.utils.GatekeeperServerUtils;

/**
 * 
 * 
 * @author lhq
 *
 */
public class DefaultFileParser implements FileDataParser {
	
	private static final Logger log = LoggerFactory.getLogger(DefaultFileParser.class);

	private byte fileType;
	/**
	 * 电站信息的服务
	 */
	private StationInfoMService stationInfoMService;

	public DefaultFileParser(byte fileType) {
		this.fileType = fileType;
	}

	@Override
	public void parse(File file) {

		String tableName = GateKeeperConstant.getTable(fileType);
		String importSql = null;
		if (tableName != null) {
			try {
				importSql = GatekeeperServerUtils.generateImportSql(
						tableName, file.getAbsolutePath());
				boolean flag = JdbcExecutorUtils.executeSql(importSql);
				if (flag) { // 导入成功考虑删除文件
					file.delete();
					if (GateKeeperConstant.STATION_TYPE == fileType) { // 更新电站信息
						getStationInfoMService().initStationCache();
					}
				} else { // 如果导入失败是否考虑删除这个文件？
					log.warn("import failed,sql=", importSql);
				}
			} catch (Exception e) {
				log.error("has exception::", importSql, e);
			}
		}

	}

	@Override
	public byte getDataType() {

		return fileType;
	}
	
	private StationInfoMService getStationInfoMService() {
		if (stationInfoMService == null) {
			stationInfoMService =  (StationInfoMService) SpringBeanContext.getBean("stationInfoMService");
		}
		return stationInfoMService;
	}

}
