package com.interest.ids.dev.network.handler;

import com.interest.ids.common.project.bean.signal.DeviceInfo;
import com.interest.ids.common.project.mapper.signal.DevInfoMapper;
import com.interest.ids.dev.network.modbus.command.UpgradeCmd;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.List;

@Component
public class DeviceUpgradeJob {

	
	private static final Logger log = LoggerFactory.getLogger(DeviceUpgradeJob.class);

	//5分钟一次
	@Scheduled(cron = "0 0/5 * * * ?")
	public void execute(){
//		log.info("DeviceUpgradeJob  task start");
//		Example ex = new Example(DeviceInfo.class);
//		Example.Criteria criteria = ex.createCriteria();
//		criteria.andEqualTo("upgradeStatus", 1);
//		//除了逻辑删除之外的设备
//		criteria.andEqualTo("isLogicDelete",0);
//		List<DeviceInfo> devs = devMapper.selectByExample(ex);
//		for (DeviceInfo deviceInfo : devs) {
//			try {
//				UpgradeCmd.apply(deviceInfo);
//			} catch (Exception e) {
//				log.error("upgrade dev error..{}",e);
//			}
//		}
//		log.info("DeviceUpgradeJob task end");
	}
}
