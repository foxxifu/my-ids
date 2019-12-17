insert into ids_sm_resource_info_t(resource_key,auth_id,resource_name,description,auth_url,request_type) values (REPLACE(UUID(), '-', ''),1024,"获取modbus协议的父设备信息SN号的信息","获取mqqt的数采SN号列表",'/biz/devAccess/getMqqtParentSnList','GET');
insert into ids_sm_resource_info_t(resource_key,auth_id,resource_name,description,auth_url,request_type) values (REPLACE(UUID(), '-', ''),1024,"新增MODBUS协议的设备","新增MODBUS协议的设备",'/biz/devAccess/insertModbusDevInfo','POST');

-- 数据补采  创建补采任务的表，由这个数据来判断是否执行补采和补采的是否需要执行kpi重计算的执行
DROP TABLE IF EXISTS `ids_data_mining_t`;
CREATE TABLE `ids_data_mining_t` (
	`id` bigint(16) NOT NULL AUTO_INCREMENT COMMENT '主键',
	`dev_id` bigint(16) NOT NULL COMMENT '设备id',
	`dev_name` varchar(64) NOT NULL COMMENT '设备名称,这里取不能修改的名称',
	`start_time` bigint(16) NOT NULL COMMENT '补采的开始时间',
	`end_time` bigint(16) NOT NULL COMMENT '补采的结束时间',
	`progress_num` int(3) DEFAULT 0 COMMENT '补采的进度',
	`station_code` varchar(64) DEFAULT NULL COMMENT '电站编号，虽然是冗余字段，方便查询',
	`station_name` varchar(64) DEFAULT NULL COMMENT '电站名称',
	`exe_status` int(2) DEFAULT 0 COMMENT '执行的结果，0:已启动,创建默认，1:执行成功，-1:执行失败',
	`collect_time` bigint(16) DEFAULT NULL COMMENT '当前补采进行到的时间节点',
	`kpi_re_comp_status` int(2) DEFAULT 0 COMMENT 'KPI重计算的状态  -1：失败，0：未开始，默认，1:成功',
	`create_time` bigint(16) DEFAULT NULL COMMENT '创建任务的时间',
	PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='数据补采记录创建了的补采任务表，记录设备的补采状态和确定是否执行kpi重计算的依据';

-- 数据补采权限的sql
insert into ids_sm_resource_info_t(resource_key,auth_id,resource_name,description,auth_url,request_type) values (REPLACE(UUID(), '-', ''),1018,"数据补采获取列表","数据补采获取列表",'/dev/dataMining/findPage','POST');
insert into ids_sm_resource_info_t(resource_key,auth_id,resource_name,description,auth_url,request_type) values (REPLACE(UUID(), '-', ''),1018,"保存数据补采的信息","保存数据补采的信息",'/dev/dataMining/saveData','POST');
insert into ids_sm_resource_info_t(resource_key,auth_id,resource_name,description,auth_url,request_type) values (REPLACE(UUID(), '-', ''),1018,"查询设备列表","查询设备列表",'/dev/device/getDeviceByCondition','POST');

-- 设备升级
insert into ids_sm_auth_t(id,auth_name,description) values(1035,'设备升级','设备升级');
insert into ids_sm_role_auth_t(role_id,auth_id) values(2,1035);
insert into ids_sm_role_auth_t(role_id,auth_id) values(3,1035);

-- 修改组串容量的串数的配置最多24串,之前是最多20串，现在修改为最多24串
ALTER TABLE `ids_pv_capacity_t` 
 ADD COLUMN `pv21` smallint(6) DEFAULT NULL,
 ADD COLUMN `pv22` smallint(6) DEFAULT NULL,
 ADD COLUMN `pv23` smallint(6) DEFAULT NULL,
 ADD COLUMN `pv24` smallint(6) DEFAULT NULL;
																										
