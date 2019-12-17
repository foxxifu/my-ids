package com.interest.ids.gatekeeper.server.parse;

import java.io.File;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.interest.ids.dev.api.utils.JdbcExecutorUtils;
import com.interest.ids.gatekeeper.server.utils.GateKeeperConstant;
import com.interest.ids.gatekeeper.server.utils.GatekeeperServerUtils;


/**
 * 
 * @author lhq
 *
 * 5分钟数据
 */
@Component("serviceDataParse")
public class ServiceDataParser implements FileDataParser{

	private static final Logger log = LoggerFactory.getLogger(ServiceDataParser.class);

	@Override
	public void parse(File file) {
		String sql = null;
		try {
			String fileName = file.getName();
			//拿到tablename
			String tableName = fileName.substring(fileName.indexOf("_")+1);
			String absPath = file.getAbsolutePath();
			sql = GatekeeperServerUtils.generateImportSql(tableName, absPath);
			boolean b = JdbcExecutorUtils.executeSql(sql);
			if(b){
				log.info("import file {} success",fileName);
				//导入redis
				//删除文件
				file.delete();
			} else { // 是否考虑删除这个文件
				log.warn("excute failed::fileName=" + fileName, "sql=" + sql);
			}
		} catch (Exception e) {
			log.error("excute sql error:: sql = ",sql , e);
		}
	}
	


	@Override
	public byte getDataType() {
		
		return GateKeeperConstant.SERVICEDATA_TYPE;
	}
}
