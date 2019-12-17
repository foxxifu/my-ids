/*=====================================*/
/* 初始化系统用户 */
/*=====================================*/
DELETE FROM `ids_sm_user_info_t` WHERE `login_name` = 'admin';
INSERT INTO `ids_sm_user_info_t` (`id`, `type_`, `login_name`, `password`, `user_name`, `qq`, `email`, `gender`, `phone`, `status`, `user_type`, `enterprise_id`, `create_user_id`, `create_date`, `modify_user_id`, `modify_date`) 
VALUES ('1', 'system', 'admin', 'e6e061838856bf47e1de730719fb2609', 'admin', NULL, NULL, NULL, NULL, '0', '1', NULL, NULL, NULL, NULL, NULL);

delete from ids_sm_role_info_t;
insert into `ids_sm_role_info_t` (`id`, `name`, `description`, `role_type`, `status`, `enterprise_id`, `create_user_id`, `create_date`, `modify_user_id`, `modify_date`) values('1','系统管理员','给自己公司使用的超级管理员','system','0','0','0','2018-02-08 13:10:52',NULL,NULL);
insert into `ids_sm_role_info_t` (`id`, `name`, `description`, `role_type`, `status`, `enterprise_id`, `create_user_id`, `create_date`, `modify_user_id`, `modify_date`) values('2','企业管理员','给公司使用的超级管理员','enterprise','0','0','0','2018-02-08 13:11:43',NULL,NULL);
insert into `ids_sm_role_info_t` (`id`, `name`, `description`, `role_type`, `status`, `enterprise_id`, `create_user_id`, `create_date`, `modify_user_id`, `modify_date`) values('3','运维','运维人员','operator','0','0','0','2018-02-08 13:11:43',NULL,NULL);

delete from ids_sm_user_role_t;
INSERT INTO ids_sm_user_role_t VALUES(1,1);

/*=============================================*/
/* 初始化权限*/
/* 用户默认拥有权限：大屏、首页、待办、密码修改 */
/*=============================================*/
delete from ids_sm_auth_t;
insert into ids_sm_auth_t(id,auth_name,description) values(1001,'总览','总览');
insert into ids_sm_auth_t(id,auth_name,description) values(1002,'运维中心','运维中心');
insert into ids_sm_auth_t(id,auth_name,description) values(1006,'报表管理','报表管理');

insert into ids_sm_auth_t(id,auth_name,description) values(1009,'个人任务','个人任务');
insert into ids_sm_auth_t(id,auth_name,description) values(1010,'大数据可视化','大数据可视化');
insert into ids_sm_auth_t(id,auth_name,description) values(1012,'单站视图','单站视图');

insert into ids_sm_auth_t(id,auth_name,description) values(1004,'企业管理','企业信息管理');

insert into ids_sm_auth_t(id,auth_name,description) values(1015,'区域管理','区域管理');
insert into ids_sm_auth_t(id,auth_name,description) values(1016,'角色管理','角色管理');
insert into ids_sm_auth_t(id,auth_name,description) values(1017,'账号管理','账号管理');

insert into ids_sm_auth_t(id,auth_name,description) values(1018,'数据补采','数据补采');
insert into ids_sm_auth_t(id,auth_name,description) values(1019,'KPI修正','KPI修正');
insert into ids_sm_auth_t(id,auth_name,description) values(1020,'KPI重计算','KPI重计算');

insert into ids_sm_auth_t(id,auth_name,description) values(1021,'点表管理','点表管理');
insert into ids_sm_auth_t(id,auth_name,description) values(1022,'告警设置','告警设置');
insert into ids_sm_auth_t(id,auth_name,description) values(1023,'电站参数','电站参数');

insert into ids_sm_auth_t(id,auth_name,description) values(1007,'电站管理','电站创建、设备绑定等相关操作权限');
insert into ids_sm_auth_t(id,auth_name,description) values(1024,'设备管理','设备管理');
insert into ids_sm_auth_t(id,auth_name,description) values(1025,'设备通信','设备通信');
insert into ids_sm_auth_t(id,auth_name,description) values(1031,'设备绑定','设备绑定');
insert into ids_sm_auth_t(id,auth_name,description) values(1032,'设备升级','设备升级');
insert into ids_sm_auth_t(id,auth_name,description) values(1026,'系统参数配置','系统参数配置');

insert into ids_sm_auth_t(id,auth_name,description) values(1027,'License管理','License管理');
insert into ids_sm_auth_t(id,auth_name,description) values(1028,'组织架构','组织架构');
insert into ids_sm_auth_t(id,auth_name,description) values(1029,'智慧应用','智慧应用');
-- insert into ids_sm_auth_t(id,auth_name,description) values(1041,'第一种工作票','第一种工作票');
-- insert into ids_sm_auth_t(id,auth_name,description) values(1042,'第二种工作票','第二种工作票');
-- insert into ids_sm_auth_t(id,auth_name,description) values(1043,'操作票','操作票');
-- insert into ids_sm_auth_t(id,auth_name,description) values(1044,'操作票模板','操作票模板');

-- EPC模块权限
insert into ids_sm_auth_t(id,auth_name,description) values(1030,'合作伙伴','合作伙伴');
-- 设备升级
insert into ids_sm_auth_t(id,auth_name,description) values(1035,'设备升级','设备升级');

-- APP模块权限
insert into ids_sm_auth_t(id,auth_name,description) values(2001,'电站管理','电站管理');
insert into ids_sm_auth_t(id,auth_name,description) values(2002,'设备监控','设备监控');
insert into ids_sm_auth_t(id,auth_name,description) values(2003,'报表管理','报表管理');
insert into ids_sm_auth_t(id,auth_name,description) values(2004,'任务中心','任务中心');


-- 初始化角色的对应权限
delete from ids_sm_role_auth_t;
insert into ids_sm_role_auth_t(role_id,auth_id) values(2,1001);
insert into ids_sm_role_auth_t(role_id,auth_id) values(2,1002);
insert into ids_sm_role_auth_t(role_id,auth_id) values(2,1006);
insert into ids_sm_role_auth_t(role_id,auth_id) values(2,1009);
insert into ids_sm_role_auth_t(role_id,auth_id) values(2,1010);
insert into ids_sm_role_auth_t(role_id,auth_id) values(2,1011);
insert into ids_sm_role_auth_t(role_id,auth_id) values(2,1012);
insert into ids_sm_role_auth_t(role_id,auth_id) values(2,1004);
insert into ids_sm_role_auth_t(role_id,auth_id) values(2,1015);
insert into ids_sm_role_auth_t(role_id,auth_id) values(2,1016);
insert into ids_sm_role_auth_t(role_id,auth_id) values(2,1017);
insert into ids_sm_role_auth_t(role_id,auth_id) values(2,1018);
insert into ids_sm_role_auth_t(role_id,auth_id) values(2,1019);
insert into ids_sm_role_auth_t(role_id,auth_id) values(2,1020);
insert into ids_sm_role_auth_t(role_id,auth_id) values(2,1021);
insert into ids_sm_role_auth_t(role_id,auth_id) values(2,1022);
insert into ids_sm_role_auth_t(role_id,auth_id) values(2,1023);
insert into ids_sm_role_auth_t(role_id,auth_id) values(2,1007);
insert into ids_sm_role_auth_t(role_id,auth_id) values(2,1024);
insert into ids_sm_role_auth_t(role_id,auth_id) values(2,1025);
insert into ids_sm_role_auth_t(role_id,auth_id) values(2,1026);
insert into ids_sm_role_auth_t(role_id,auth_id) values(2,1027);
insert into ids_sm_role_auth_t(role_id,auth_id) values(2,1030);
insert into ids_sm_role_auth_t(role_id,auth_id) values(2,1040);
insert into ids_sm_role_auth_t(role_id,auth_id) values(2,1028);
insert into ids_sm_role_auth_t(role_id,auth_id) values(2,1029);
insert into ids_sm_role_auth_t(role_id,auth_id) values(2,1031);
insert into ids_sm_role_auth_t(role_id,auth_id) values(2,1032);
insert into ids_sm_role_auth_t(role_id,auth_id) values(2,1035);
-- insert into ids_sm_role_auth_t(role_id,auth_id) values(2,1041);
-- insert into ids_sm_role_auth_t(role_id,auth_id) values(2,1042);
-- insert into ids_sm_role_auth_t(role_id,auth_id) values(2,1043);
-- insert into ids_sm_role_auth_t(role_id,auth_id) values(2,1044);

insert into ids_sm_role_auth_t(role_id,auth_id) values(2,2001);
insert into ids_sm_role_auth_t(role_id,auth_id) values(2,2002);
insert into ids_sm_role_auth_t(role_id,auth_id) values(2,2003);
insert into ids_sm_role_auth_t(role_id,auth_id) values(2,2004);

-- 运维角色默认权限
insert into ids_sm_role_auth_t(role_id,auth_id) values(3,1001);
insert into ids_sm_role_auth_t(role_id,auth_id) values(3,1002);
insert into ids_sm_role_auth_t(role_id,auth_id) values(3,1006);
insert into ids_sm_role_auth_t(role_id,auth_id) values(3,1009);
insert into ids_sm_role_auth_t(role_id,auth_id) values(3,1010);
insert into ids_sm_role_auth_t(role_id,auth_id) values(3,1011);
insert into ids_sm_role_auth_t(role_id,auth_id) values(3,1012);
insert into ids_sm_role_auth_t(role_id,auth_id) values(3,1018);
insert into ids_sm_role_auth_t(role_id,auth_id) values(3,1019);
insert into ids_sm_role_auth_t(role_id,auth_id) values(3,1020);
insert into ids_sm_role_auth_t(role_id,auth_id) values(3,1021);
insert into ids_sm_role_auth_t(role_id,auth_id) values(3,1022);
insert into ids_sm_role_auth_t(role_id,auth_id) values(3,1023);
insert into ids_sm_role_auth_t(role_id,auth_id) values(3,1007);
insert into ids_sm_role_auth_t(role_id,auth_id) values(3,1035);

insert into ids_sm_role_auth_t(role_id,auth_id) values(3,2001);
insert into ids_sm_role_auth_t(role_id,auth_id) values(3,2002);
insert into ids_sm_role_auth_t(role_id,auth_id) values(3,2003);
insert into ids_sm_role_auth_t(role_id,auth_id) values(3,2004);

/*=============================================*/
/* 智能运维资源初始化开始*/
/*=============================================*/

-- 运维工作台权限资源
delete from  ids_sm_resource_info_t;
insert into ids_sm_resource_info_t(resource_key,auth_id,resource_name,description,auth_url,request_type) values (REPLACE(UUID(), '-', ''),1002,"运维工作台任务分配合理性检验","运维工作台任务分配合理性检验",'/biz/operation/worksite/validTaskToUser','POST');
insert into ids_sm_resource_info_t(resource_key,auth_id,resource_name,description,auth_url,request_type) values (REPLACE(UUID(), '-', ''),1002,"运维工作台总览","运维工作台电站总览",'/biz/operation/worksite/userStationProf','POST');
insert into ids_sm_resource_info_t(resource_key,auth_id,resource_name,description,auth_url,request_type) values (REPLACE(UUID(), '-', ''),1002,"电站检索","电站快速定位检索",'/biz/operation/worksite/getStations','POST');
insert into ids_sm_resource_info_t(resource_key,auth_id,resource_name,description,auth_url,request_type) values (REPLACE(UUID(), '-', ''),1002,"电站列表","电站列表",'/biz/operation/worksite/getStationProfile','POST');
insert into ids_sm_resource_info_t(resource_key,auth_id,resource_name,description,auth_url,request_type) values (REPLACE(UUID(), '-', ''),1002,"电站详情","电站详情",'/biz/operation/worksite/getStationDetail','POST');
insert into ids_sm_resource_info_t(resource_key,auth_id,resource_name,description,auth_url,request_type) values (REPLACE(UUID(), '-', ''),1002,"地图汇聚","地图汇聚",'/biz/operation/worksite/getMapData','POST');
insert into ids_sm_resource_info_t(resource_key,auth_id,resource_name,description,auth_url,request_type) values (REPLACE(UUID(), '-', ''),1002,"告警列表","告警列表",'/biz/operation/worksite/getAlarmProfile','POST');
insert into ids_sm_resource_info_t(resource_key,auth_id,resource_name,description,auth_url,request_type) values (REPLACE(UUID(), '-', ''),1002,"任务列表","任务列表",'/biz/operation/worksite/getDefectTasks','POST');
insert into ids_sm_resource_info_t(resource_key,auth_id,resource_name,description,auth_url,request_type) values (REPLACE(UUID(), '-', ''),1002,"运维人员任务列表","运维人员任务列表",'/biz/operation/worksite/getOperatorTasks','POST');



insert into ids_sm_resource_info_t(resource_key,auth_id,resource_name,description,auth_url,request_type) values (REPLACE(UUID(), '-', ''),1002,"保存一種工作票","保存一種工作票",'/biz/workTicket/save','POST');
insert into ids_sm_resource_info_t(resource_key,auth_id,resource_name,description,auth_url,request_type) values (REPLACE(UUID(), '-', ''),1002,"根据processDefinitionId查询流程","根据processDefinitionId查询流程",'/biz/workTicket/getWorkTicketByDefinitionId','POST');
insert into ids_sm_resource_info_t(resource_key,auth_id,resource_name,description,auth_url,request_type) values (REPLACE(UUID(), '-', ''),1002,"修改流程数据","修改流程数据",'/biz/workTicket/updateWorkTicket','POST');
insert into ids_sm_resource_info_t(resource_key,auth_id,resource_name,description,auth_url,request_type) values (REPLACE(UUID(), '-', ''),1002,"执行流程节点","执行流程节点",'/biz/workTicket/executeWorkTicket','POST');
insert into ids_sm_resource_info_t(resource_key,auth_id,resource_name,description,auth_url,request_type) values (REPLACE(UUID(), '-', ''),1002,"列出相关流程","列出相关流程",'/biz/workTicket/getWorkTicketList','POST');
insert into ids_sm_resource_info_t(resource_key,auth_id,resource_name,description,auth_url,request_type) values (REPLACE(UUID(), '-', ''),1002,"导出和打印","导出和打印",'/biz/workTicket/export','GET');
insert into ids_sm_resource_info_t(resource_key,auth_id,resource_name,description,auth_url,request_type) values (REPLACE(UUID(), '-', ''),1002,"查询告警","查询告警",'/biz/alarm/list','POST');
insert into ids_sm_resource_info_t(resource_key,auth_id,resource_name,description,auth_url,request_type) values (REPLACE(UUID(), '-', ''),1002,"查询企业用户","查询企业用户",'/biz/user/getEnterpriseUser','POST');

-- 运维中心 下的 告警管理                                                                                                     
	-- 设备告警                                                                                                 
insert into ids_sm_resource_info_t(resource_key,auth_id,resource_name,description,auth_url,request_type) values (REPLACE(UUID(), '-', ''),1002,"设备告警确认","设备告警确认",'/biz/alarm/confirm/ack','POST');
insert into ids_sm_resource_info_t(resource_key,auth_id,resource_name,description,auth_url,request_type) values (REPLACE(UUID(), '-', ''),1002,"设备告警清除","设备告警清除",'/biz/alarm/confirm/cleared','POST');
insert into ids_sm_resource_info_t(resource_key,auth_id,resource_name,description,auth_url,request_type) values (REPLACE(UUID(), '-', ''),1002,"设备告警查询","设备告警查询",'/biz/alarm/list','POST');
insert into ids_sm_resource_info_t(resource_key,auth_id,resource_name,description,auth_url,request_type) values (REPLACE(UUID(), '-', ''),1002,"设备告警信息查询","设备告警信息查询",'/biz/alarm/list','POST');
insert into ids_sm_resource_info_t(resource_key,auth_id,resource_name,description,auth_url,request_type) values (REPLACE(UUID(), '-', ''),1002,"设备告警修复建议","设备告警修复建议",'/biz/alarm/repair','GET');
insert into ids_sm_resource_info_t(resource_key,auth_id,resource_name,description,auth_url,request_type) values (REPLACE(UUID(), '-', ''),1002,"智能运维告警信息","智能运维告警信息",'/biz/alarm/list/specified','POST');
insert into ids_sm_resource_info_t(resource_key,auth_id,resource_name,description,auth_url,request_type) values (REPLACE(UUID(), '-', ''),1002,"设备告警信息查询","设备告警信息查询",'/biz/alarm/alarmOnlineList','POST');
insert into ids_sm_resource_info_t(resource_key,auth_id,resource_name,description,auth_url,request_type) values (REPLACE(UUID(), '-', ''),1002,"告警详情","告警详情",'/biz/operation/worksite/getAlarmDetail','POST');
	-- 智能告警                                                                                                 
insert into ids_sm_resource_info_t(resource_key,auth_id,resource_name,description,auth_url,request_type) values (REPLACE(UUID(), '-', ''),1002,"智能告警确认","智能告警确认",'/biz/alarm/confirm/ackzl','POST');
insert into ids_sm_resource_info_t(resource_key,auth_id,resource_name,description,auth_url,request_type) values (REPLACE(UUID(), '-', ''),1002,"智能告警清除","智能告警清除",'/biz/alarm/confirm/clearedzl','POST');
insert into ids_sm_resource_info_t(resource_key,auth_id,resource_name,description,auth_url,request_type) values (REPLACE(UUID(), '-', ''),1002,"智能告警信息查询","智能告警信息查询",'/biz/alarm/list/analysis','POST');
insert into ids_sm_resource_info_t(resource_key,auth_id,resource_name,description,auth_url,request_type) values (REPLACE(UUID(), '-', ''),1002,"智能告警修复建议","智能告警修复建议",'/biz/alarm/repair/analysis','GET');
insert into ids_sm_resource_info_t(resource_key,auth_id,resource_name,description,auth_url,request_type) values (REPLACE(UUID(), '-', ''),1002,"告警统计级别","告警统计级别",'/biz/alarm/statistics/level','POST');
insert into ids_sm_resource_info_t(resource_key,auth_id,resource_name,description,auth_url,request_type) values (REPLACE(UUID(), '-', ''),1002,"告警转工单","告警转工单",'/biz/workFlow/alarmToDefect','POST');
-- 运维中心缺陷管理                                                                                                     
insert into ids_sm_resource_info_t(resource_key,auth_id,resource_name,description,auth_url,request_type) values (REPLACE(UUID(), '-', ''),1002,"获取缺陷四种状态的缺陷数","获取缺陷四种状态的缺陷数",'/biz/workFlow/getWorkFlowCount','POST');
insert into ids_sm_resource_info_t(resource_key,auth_id,resource_name,description,auth_url,request_type) values (REPLACE(UUID(), '-', ''),1002,"根据条件查询缺陷数据","根据条件查询缺陷数据",'/biz/workFlow/getWorkFlowDefectsByCondition','POST');
insert into ids_sm_resource_info_t(resource_key,auth_id,resource_name,description,auth_url,request_type) values (REPLACE(UUID(), '-', ''),1002,"根据id获取缺陷详情","根据id获取缺陷详情",'/biz/workFlow/getWorkFlowDefectDetails','POST');
insert into ids_sm_resource_info_t(resource_key,auth_id,resource_name,description,auth_url,request_type) values (REPLACE(UUID(), '-', ''),1002,"新建和复制缺陷","新建和复制缺陷",'/biz/workFlow/saveDefect','POST');
insert into ids_sm_resource_info_t(resource_key,auth_id,resource_name,description,auth_url,request_type) values (REPLACE(UUID(), '-', ''),1002,"文件上传","文件上传",'/biz/fileManager/fileUpload','POST');
insert into ids_sm_resource_info_t(resource_key,auth_id,resource_name,description,auth_url,request_type) values (REPLACE(UUID(), '-', ''),1002,"获取所有同事","获取所有同事",'/biz/user/getEnterpriseUser','POST');
insert into ids_sm_resource_info_t(resource_key,auth_id,resource_name,description,auth_url,request_type) values (REPLACE(UUID(), '-', ''),1002,"根据条件查询设备","根据条件查询设备",'/dev/device/getDeviceByCondition','POST');
insert into ids_sm_resource_info_t(resource_key,auth_id,resource_name,description,auth_url,request_type) values (REPLACE(UUID(), '-', ''),1002,"执行缺陷的流程","执行缺陷的流程",'/biz/workFlow/executeWorkFlowDefect','POST');
insert into ids_sm_resource_info_t(resource_key,auth_id,resource_name,description,auth_url,request_type) values (REPLACE(UUID(), '-', ''),1002,"查询缺陷管理告警","查询缺陷管理告警",'/biz/workFlow/getDefectAlarm','POST');
insert into ids_sm_resource_info_t(resource_key,auth_id,resource_name,description,auth_url,request_type) values (REPLACE(UUID(), '-', ''),1002,"根据ID查询设备数据","根据ID查询设备数据",'/dev/device/getDeviceById','POST');

-- 缺陷管理                                                                                                     
insert into ids_sm_resource_info_t(resource_key,auth_id,resource_name,description,auth_url,request_type) values (REPLACE(UUID(), '-', ''),1002,"获取缺陷四种状态的缺陷数","获取缺陷四种状态的缺陷数",'/biz/workFlow/getWorkFlowCount','POST');
insert into ids_sm_resource_info_t(resource_key,auth_id,resource_name,description,auth_url,request_type) values (REPLACE(UUID(), '-', ''),1002,"根据条件查询缺陷数据","根据条件查询缺陷数据",'/biz/workFlow/getWorkFlowDefectsByCondition','POST');
insert into ids_sm_resource_info_t(resource_key,auth_id,resource_name,description,auth_url,request_type) values (REPLACE(UUID(), '-', ''),1002,"根据id获取缺陷详情","根据id获取缺陷详情",'/biz/workFlow/getWorkFlowDefectDetails','POST');
insert into ids_sm_resource_info_t(resource_key,auth_id,resource_name,description,auth_url,request_type) values (REPLACE(UUID(), '-', ''),1002,"新建和复制缺陷","新建和复制缺陷",'/biz/workFlow/saveDefect','POST');
insert into ids_sm_resource_info_t(resource_key,auth_id,resource_name,description,auth_url,request_type) values (REPLACE(UUID(), '-', ''),1002,"文件上传","文件上传",'/biz/fileManager/fileUpload','POST');
insert into ids_sm_resource_info_t(resource_key,auth_id,resource_name,description,auth_url,request_type) values (REPLACE(UUID(), '-', ''),1002,"获取所有同事","获取所有同事",'/biz/user/getEnterpriseUser','POST');
insert into ids_sm_resource_info_t(resource_key,auth_id,resource_name,description,auth_url,request_type) values (REPLACE(UUID(), '-', ''),1002,"根据条件查询设备","根据条件查询设备",'/dev/device/getDeviceByCondition','POST');
insert into ids_sm_resource_info_t(resource_key,auth_id,resource_name,description,auth_url,request_type) values (REPLACE(UUID(), '-', ''),1002,"执行缺陷的流程","执行缺陷的流程",'/biz/workFlow/executeWorkFlowDefect','POST');
insert into ids_sm_resource_info_t(resource_key,auth_id,resource_name,description,auth_url,request_type) values (REPLACE(UUID(), '-', ''),1002,"查询缺陷管理告警","查询缺陷管理告警",'/biz/workFlow/getDefectAlarm','POST'); 
insert into ids_sm_resource_info_t(resource_key,auth_id,resource_name,description,auth_url,request_type) values (REPLACE(UUID(), '-', ''),1002,"查询电站下的用户","查询电站下的用户",'/biz/workFlow/getStationUser','POST'); 

-- 智慧应用
insert into ids_sm_resource_info_t(resource_key,auth_id,resource_name,description,auth_url,request_type) values (REPLACE(UUID(), '-', ''),1029,"查询电站","查询电站",'/biz/station/getStationInfo','POST');                                                                                                                
insert into ids_sm_resource_info_t(resource_key,auth_id,resource_name,description,auth_url,request_type) values (REPLACE(UUID(), '-', ''),1029,"查询电站月度分析表表","查询电站月度分析表表",'/biz/compontAnalysis/getStationMouthAnalysis','POST');
insert into ids_sm_resource_info_t(resource_key,auth_id,resource_name,description,auth_url,request_type) values (REPLACE(UUID(), '-', ''),1029,"查询子阵月度分析报表","查询子阵月度分析报表",'/biz/compontAnalysis/getCompontAnalysisMouth','POST');
insert into ids_sm_resource_info_t(resource_key,auth_id,resource_name,description,auth_url,request_type) values (REPLACE(UUID(), '-', ''),1029,"查询电站年度分析报表","查询电站年度分析报表",'/biz/compontAnalysis/getStationYearAnalysis','POST');
insert into ids_sm_resource_info_t(resource_key,auth_id,resource_name,description,auth_url,request_type) values (REPLACE(UUID(), '-', ''),1029,"查询子阵年度分析报表","查询子阵年度分析报表",'/biz/compontAnalysis/getCompontAnalysisYear','POST');
insert into ids_sm_resource_info_t(resource_key,auth_id,resource_name,description,auth_url,request_type) values (REPLACE(UUID(), '-', ''),1029,"查询电站日分析报表","查询电站日分析报表",'/biz/compontAnalysis/getStationAnalysisDay','POST');
insert into ids_sm_resource_info_t(resource_key,auth_id,resource_name,description,auth_url,request_type) values (REPLACE(UUID(), '-', ''),1029,"查询子阵日分析报表","查询子阵日分析报表",'/biz/compontAnalysis/getCompontAnalysisDay','POST');
insert into ids_sm_resource_info_t(resource_key,auth_id,resource_name,description,auth_url,request_type) values (REPLACE(UUID(), '-', ''),1029,"数据导出","数据导出",'/biz/compontAnalysis/exportData','GET');
insert into ids_sm_resource_info_t(resource_key,auth_id,resource_name,description,auth_url,request_type) values (REPLACE(UUID(), '-', ''),1029,"逆变器离散率分析","逆变器离散率分析",'/biz/compontAnalysis/getInverterDiscreteRate','POST');
insert into ids_sm_resource_info_t(resource_key,auth_id,resource_name,description,auth_url,request_type) values (REPLACE(UUID(), '-', ''),1029,"直流汇率箱离散率分析","直流汇率箱离散率分析",'/biz/compontAnalysis/getCombinerdcDiscreteRate','POST');
                                                                                                                

                                                                                                                
-- 个人任务跟踪                                                                                                 
insert into ids_sm_resource_info_t(resource_key,auth_id,resource_name,description,auth_url,request_type) values (REPLACE(UUID(), '-', ''),1009,"查询个人未办任务总数","查询个人未办任务总数",'/biz/workFlow/getWorkFlowTaskCountByUserId','POST');
insert into ids_sm_resource_info_t(resource_key,auth_id,resource_name,description,auth_url,request_type) values (REPLACE(UUID(), '-', ''),1009,"查询待办和已办任务","查询待办和已办任务",'/biz/workFlow/getWorkFlowTaskByCondition','POST');
insert into ids_sm_resource_info_t(resource_key,auth_id,resource_name,description,auth_url,request_type) values (REPLACE(UUID(), '-', ''),1009,"任务转派","任务转派查询对应的人员",'/biz/workFlow/getStationUser','POST');                                                                                
/*=============================================*/                                                               
/* 智能运维资源初始化结束*/                                                                                     
/*=============================================*/                                                               
                                                                                                                
/*=============================================*/                                                               
/* 生产管理初始化开始*/                                                                                         
/*=============================================*/                                                               
                                                                                                                
-- 报表管理                                                                                                     
insert into ids_sm_resource_info_t(resource_key,auth_id,resource_name,description,auth_url,request_type) values (REPLACE(UUID(), '-', ''),1006,"电站运行报表","电站运行报表",'/biz/produceMng/listStationRpt','POST');
insert into ids_sm_resource_info_t(resource_key,auth_id,resource_name,description,auth_url,request_type) values (REPLACE(UUID(), '-', ''),1006,"获取时间","获取时间",'/biz/produceMng/getYestodayTime','POST');
insert into ids_sm_resource_info_t(resource_key,auth_id,resource_name,description,auth_url,request_type) values (REPLACE(UUID(), '-', ''),1006,"电站运行报表下载","电站运行报表下载",'/biz/produceMng/report/download/StationRuning','GET');
insert into ids_sm_resource_info_t(resource_key,auth_id,resource_name,description,auth_url,request_type) values (REPLACE(UUID(), '-', ''),1006,"设备故障分类统计报表","设备故障分类统计报表",'/biz/produceMng/listDeviceTroubleRpt','POST');
insert into ids_sm_resource_info_t(resource_key,auth_id,resource_name,description,auth_url,request_type) values (REPLACE(UUID(), '-', ''),1006,"设备故障分类统计报表下载","设备故障分类统计报表下载",'/biz/produceMng/report/download/DevAlarm','GET');
insert into ids_sm_resource_info_t(resource_key,auth_id,resource_name,description,auth_url,request_type) values (REPLACE(UUID(), '-', ''),1006,"逆变器运行报表","逆变器运行报表",'/biz/produceMng/listInverterRpt','POST');
insert into ids_sm_resource_info_t(resource_key,auth_id,resource_name,description,auth_url,request_type) values (REPLACE(UUID(), '-', ''),1006,"逆变器运行报表下载","逆变器运行报表下载",'/biz/produceMng/exportInverterRpt','GET');
insert into ids_sm_resource_info_t(resource_key,auth_id,resource_name,description,auth_url,request_type) values (REPLACE(UUID(), '-', ''),1006,"子阵运行报表","子阵运行报表",'/biz/produceMng/listSubarrayRpt','POST');
insert into ids_sm_resource_info_t(resource_key,auth_id,resource_name,description,auth_url,request_type) values (REPLACE(UUID(), '-', ''),1006,"子阵运行报表下载","子阵运行报表下载",'/biz/produceMng/exportSubarrayRpt','GET');
insert into ids_sm_resource_info_t(resource_key,auth_id,resource_name,description,auth_url,request_type) values (REPLACE(UUID(), '-', ''),1012,"查询逆变器详细报表的数据","查询逆变器详细报表的数据",'/biz/produceMng/listInverterDetailRpt','POST');	   
insert into ids_sm_resource_info_t(resource_key,auth_id,resource_name,description,auth_url,request_type) values (REPLACE(UUID(), '-', ''),1012,"导出逆变器详细报表的数据","导出逆变器详细报表的数据",'/biz/produceMng/exportInverterDetailRpt','GET');	   
insert into ids_sm_resource_info_t(resource_key,auth_id,resource_name,description,auth_url,request_type) values (REPLACE(UUID(), '-', ''),1012,"电站详细逆变器数据","电站详细逆变器数据",'/biz/produceMng/listInverterDevices','POST');	   
                                                                                                                
/*=============================================*/                                                               
/* 生产管理初始化结束*/                                                                                         
/*=============================================*/                                                               
                                                                                                                
/*=============================================*/                                                               
/* 人员管理初始化开始*/                                                                                         
/*=============================================*/                                                               
                                                                                                                
-- 区域管理初始化资源                                                                                           
insert into ids_sm_resource_info_t(resource_key,auth_id,resource_name,description,auth_url,request_type) values (REPLACE(UUID(), '-', ''),1015,"新建区域","新建区域",'/biz/domain/insertDomain','POST');
insert into ids_sm_resource_info_t(resource_key,auth_id,resource_name,description,auth_url,request_type) values (REPLACE(UUID(), '-', ''),1015,"更新保存区域","更新保存区域",'/biz/domain/updateDomain','POST');
insert into ids_sm_resource_info_t(resource_key,auth_id,resource_name,description,auth_url,request_type) values (REPLACE(UUID(), '-', ''),1015,"删除区域","删除区域",'/biz/domain/deleteDomainById','POST');
insert into ids_sm_resource_info_t(resource_key,auth_id,resource_name,description,auth_url,request_type) values (REPLACE(UUID(), '-', ''),1015,"修改查询区域","修改查询区域",'/biz/domain/getDomainById','POST');
insert into ids_sm_resource_info_t(resource_key,auth_id,resource_name,description,auth_url,request_type) values (REPLACE(UUID(), '-', ''),1015,"所属企业","所属企业",'/biz/enterprise/getEnterpriseByUserId','POST');
insert into ids_sm_resource_info_t(resource_key,auth_id,resource_name,description,auth_url,request_type) values (REPLACE(UUID(), '-', ''),1015,"修改区域的时候获取区域树","修改区域的时候获取区域树",'/biz/domain/editDomainTree','POST');
insert into ids_sm_resource_info_t(resource_key,auth_id,resource_name,description,auth_url,request_type) values (REPLACE(UUID(), '-', ''),1015,"查询所有区域","根据企业id或者某一个区域id查询所有的区域",'/biz/domain/getDomainTree1','POST');
                                                                                                                
-- 角色管理初始化资源                                                                                           
insert into ids_sm_resource_info_t(resource_key,auth_id,resource_name,description,auth_url,request_type) values (REPLACE(UUID(), '-', ''),1016,"修改查询角色","修改查询角色",'/biz/role/getRoleMById','POST');
insert into ids_sm_resource_info_t(resource_key,auth_id,resource_name,description,auth_url,request_type) values (REPLACE(UUID(), '-', ''),1016,"获取企业所有角色","获取企业所有角色",'/biz/role/getRoleByEnterpriseId','POST');
insert into ids_sm_resource_info_t(resource_key,auth_id,resource_name,description,auth_url,request_type) values (REPLACE(UUID(), '-', ''),1016,"删除角色","删除角色",'/biz/role/deleteRoleById','POST');
insert into ids_sm_resource_info_t(resource_key,auth_id,resource_name,description,auth_url,request_type) values (REPLACE(UUID(), '-', ''),1016,"修改保存角色","修改保存角色",'/biz/role/updateRoleMById','POST');
insert into ids_sm_resource_info_t(resource_key,auth_id,resource_name,description,auth_url,request_type) values (REPLACE(UUID(), '-', ''),1016,"新建角色","新建角色",'/biz/role/insertRole','POST');
insert into ids_sm_resource_info_t(resource_key,auth_id,resource_name,description,auth_url,request_type) values (REPLACE(UUID(), '-', ''),1016,"更新角色状态","更新角色状态",'/biz/role/updateRoleMStatus','POST');
insert into ids_sm_resource_info_t(resource_key,auth_id,resource_name,description,auth_url,request_type) values (REPLACE(UUID(), '-', ''),1016,"查询角色","角色列表和分页查询角色信息",'/biz/role/getRoleMByPage','POST');
insert into ids_sm_resource_info_t(resource_key,auth_id,resource_name,description,auth_url,request_type) values (REPLACE(UUID(), '-', ''),1016,"查询角色对应的权限","查询角色对应的权限",'/biz/authorize/getUserAuthorize','POST');
insert into ids_sm_resource_info_t(resource_key,auth_id,resource_name,description,auth_url,request_type) values (REPLACE(UUID(), '-', ''),1016,"查询企业","分页查询企业和根据条件分页查询企业信息",'/biz/enterprise/getEnterpriseMByCondition','POST');
                                                                                                                
-- 账号管理 用户管理初始化资源                                                                                           
insert into ids_sm_resource_info_t(resource_key,auth_id,resource_name,description,auth_url,request_type) values (REPLACE(UUID(), '-', ''),1017,"查询企业","分页查询企业和根据条件分页查询企业信息",'/biz/enterprise/getEnterpriseMByCondition','POST');
insert into ids_sm_resource_info_t(resource_key,auth_id,resource_name,description,auth_url,request_type) values (REPLACE(UUID(), '-', ''),1017,"查询公司下的第一级部门","查询公司下的第一级部门",'/biz/departmentController/getEnterprise','POST');
insert into ids_sm_resource_info_t(resource_key,auth_id,resource_name,description,auth_url,request_type) values (REPLACE(UUID(), '-', ''),1017,"查询用户的所有角色","查询用户的所有角色",'/biz/role/getRolesByUserId','POST');
insert into ids_sm_resource_info_t(resource_key,auth_id,resource_name,description,auth_url,request_type) values (REPLACE(UUID(), '-', ''),1017,"获取公司下的所有电站","获取公司下的所有电站",'/biz/station/getStationInfo','POST');
insert into ids_sm_resource_info_t(resource_key,auth_id,resource_name,description,auth_url,request_type) values (REPLACE(UUID(), '-', ''),1017,"新建用户","新增用户",'/biz/user/insertUser','POST');
insert into ids_sm_resource_info_t(resource_key,auth_id,resource_name,description,auth_url,request_type) values (REPLACE(UUID(), '-', ''),1017,"查询用户","查询用户",'/biz/user/getUserByConditions','POST');
insert into ids_sm_resource_info_t(resource_key,auth_id,resource_name,description,auth_url,request_type) values (REPLACE(UUID(), '-', ''),1017,"修改查询用户","修改查询用户",'/biz/user/getUserById','POST');
insert into ids_sm_resource_info_t(resource_key,auth_id,resource_name,description,auth_url,request_type) values (REPLACE(UUID(), '-', ''),1017,"删除用户","删除用户",'/biz/user/deleteUserById','POST');
insert into ids_sm_resource_info_t(resource_key,auth_id,resource_name,description,auth_url,request_type) values (REPLACE(UUID(), '-', ''),1017,"修改保存用户","修改保存用户",'/biz/user/updateUser','POST');
insert into ids_sm_resource_info_t(resource_key,auth_id,resource_name,description,auth_url,request_type) values (REPLACE(UUID(), '-', ''),1017,"更新用户状态","更新用户状态",'/biz/user/updateUserMStatus','POST');
insert into ids_sm_resource_info_t(resource_key,auth_id,resource_name,description,auth_url,request_type) values (REPLACE(UUID(), '-', ''),1017,"查询公司部门信息","查询公司部门信息",'/biz/departmentController/getDepartmentByParentId','POST');
insert into ids_sm_resource_info_t(resource_key,auth_id,resource_name,description,auth_url,request_type) values (REPLACE(UUID(), '-', ''),1017,"新增部门信息","新增部门信息",'/biz/departmentController/insertDepartment','POST');
insert into ids_sm_resource_info_t(resource_key,auth_id,resource_name,description,auth_url,request_type) values (REPLACE(UUID(), '-', ''),1017,"检查用户登录名是否使用过","检查用户登录名是否使用过",'/biz/user/checkLoginName','POST');
insert into ids_sm_resource_info_t(resource_key,auth_id,resource_name,description,auth_url,request_type) values (REPLACE(UUID(), '-', ''),1017,"获取企业所有角色","获取企业所有角色",'/biz/role/getRoleByEnterpriseId','POST');
insert into ids_sm_resource_info_t(resource_key,auth_id,resource_name,description,auth_url,request_type) values (REPLACE(UUID(), '-', ''),1017,"重置密码","重置密码",'/biz/user/resetPassword','POST');


-- 数据补采
insert into ids_sm_resource_info_t(resource_key,auth_id,resource_name,description,auth_url,request_type) values (REPLACE(UUID(), '-', ''),1018,"数据补采获取列表","数据补采获取列表",'/dev/dataMining/findPage','POST');
insert into ids_sm_resource_info_t(resource_key,auth_id,resource_name,description,auth_url,request_type) values (REPLACE(UUID(), '-', ''),1018,"保存数据补采的信息","保存数据补采的信息",'/dev/dataMining/saveData','POST');
insert into ids_sm_resource_info_t(resource_key,auth_id,resource_name,description,auth_url,request_type) values (REPLACE(UUID(), '-', ''),1018,"查询设备列表","查询设备列表",'/dev/device/getDeviceByCondition','POST');
insert into ids_sm_resource_info_t(resource_key,auth_id,resource_name,description,auth_url,request_type) values (REPLACE(UUID(), '-', ''),1018,"所有数采设备","所有数采设备",'/dev/device/findAllBindScDevs','GET');
insert into ids_sm_resource_info_t(resource_key,auth_id,resource_name,description,auth_url,request_type) values (REPLACE(UUID(), '-', ''),1018,"下发补采命令","下发补采命令",'/dev/dataMining/reloadData','POST');
insert into ids_sm_resource_info_t(resource_key,auth_id,resource_name,description,auth_url,request_type) values (REPLACE(UUID(), '-', ''),1018,"删除补采任务","删除补采任务",'/dev/dataMining/deleteTask','POST');
                                                                                                            
/*=============================================*/                                                               
/* 人员管理初始化结束*/                                                                                         
/*=============================================*/                                                               
                                                                                                                
/*=============================================*/                                                               
/* 企业管理初始化开始*/                                                                                         
/*=============================================*/                                                               
insert into ids_sm_resource_info_t(resource_key,auth_id,resource_name,description,auth_url,request_type) values (REPLACE(UUID(), '-', ''),1004,"新建企业","新建企业",'/biz/enterprise/insertEnterprise','POST');
insert into ids_sm_resource_info_t(resource_key,auth_id,resource_name,description,auth_url,request_type) values (REPLACE(UUID(), '-', ''),1004,"更新保存企业","更新保存企业",'/biz/enterprise/updateEnterpriseM','POST');
insert into ids_sm_resource_info_t(resource_key,auth_id,resource_name,description,auth_url,request_type) values (REPLACE(UUID(), '-', ''),1004,"删除企业","删除企业",'/biz/enterprise/deleteEnterpriseMById','POST');
insert into ids_sm_resource_info_t(resource_key,auth_id,resource_name,description,auth_url,request_type) values (REPLACE(UUID(), '-', ''),1004,"查询企业详情","查询企业详情",'/biz/enterprise/selectEnterpriseMById','POST');
insert into ids_sm_resource_info_t(resource_key,auth_id,resource_name,description,auth_url,request_type) values (REPLACE(UUID(), '-', ''),1004,"查询企业","分页查询企业和根据条件分页查询企业信息",'/biz/enterprise/getEnterpriseMByCondition','POST');
insert into ids_sm_resource_info_t(resource_key,auth_id,resource_name,description,auth_url,request_type) values (REPLACE(UUID(), '-', ''),1004,"企业图片上传","企业图片上传",'biz/fileManager/fileUpload','POST');
insert into ids_sm_resource_info_t(resource_key,auth_id,resource_name,description,auth_url,request_type) values (REPLACE(UUID(), '-', ''),1004,"下载企业图片文件","下载企业图片文件",'biz/fileManager/downloadFile','GET');
/*=============================================*/                                                               
/* 企业管理初始化结束*/                                                                                         
/*=============================================*/                                                               
                                                                                                                
/*=============================================*/                                                               
/* 参数配置初始化开始*/                                                                                         
/*=============================================*/                                                               
-- 点表管理                                                                                                     
insert into ids_sm_resource_info_t(resource_key,auth_id,resource_name,description,auth_url,request_type) values (REPLACE(UUID(), '-', ''),1021,"点表导入","点表导入",'/biz/settings/signal/import/list','POST');
insert into ids_sm_resource_info_t(resource_key,auth_id,resource_name,description,auth_url,request_type) values (REPLACE(UUID(), '-', ''),1021,"点表列表查询","点表列表查询",'/biz/settings/signal/import/list','POST');
insert into ids_sm_resource_info_t(resource_key,auth_id,resource_name,description,auth_url,request_type) values (REPLACE(UUID(), '-', ''),1021,"点表删除","点表删除",'/biz/settings/signal/delete','POST');
insert into ids_sm_resource_info_t(resource_key,auth_id,resource_name,description,auth_url,request_type) values (REPLACE(UUID(), '-', ''),1021,"信号点模型适配","信号点模型适配",'/biz/settings/signal/unificationAdapter','POST');
insert into ids_sm_resource_info_t(resource_key,auth_id,resource_name,description,auth_url,request_type) values (REPLACE(UUID(), '-', ''),1021,"信号点名称查询","信号点名称查询",'/biz/settings/signal/unificationAdapter/check','GET');
insert into ids_sm_resource_info_t(resource_key,auth_id,resource_name,description,auth_url,request_type) values (REPLACE(UUID(), '-', ''),1021,"归一化适配清除","归一化适配清除",'/biz/settings/signal/unificationAdapter/cleared','POST');
insert into ids_sm_resource_info_t(resource_key,auth_id,resource_name,description,auth_url,request_type) values (REPLACE(UUID(), '-', ''),1021,"归一化适配删除","归一化适配删除",'/biz/settings/signal/unificationAdapter/delete','POST');
insert into ids_sm_resource_info_t(resource_key,auth_id,resource_name,description,auth_url,request_type) values (REPLACE(UUID(), '-', ''),1021,"信号点模型适配查询","信号点模型适配查询",'/biz/settings/signal/unificationAdapter/query','GET');
insert into ids_sm_resource_info_t(resource_key,auth_id,resource_name,description,auth_url,request_type) values (REPLACE(UUID(), '-', ''),1021,"通知修改通讯配置","通知修改通讯配置",'/dev/device/notifyTnDevUpdateChange','POST');
insert into ids_sm_resource_info_t(resource_key,auth_id,resource_name,description,auth_url,request_type) values (REPLACE(UUID(), '-', ''),1021,"通过版本的id获取设备的id","通过版本的id获取设备的id",'/dev/device/getTnDevIdByVersionId','POST');
insert into ids_sm_resource_info_t(resource_key,auth_id,resource_name,description,auth_url,request_type) values (REPLACE(UUID(), '-', ''),1021,"通知清除缓存中的归一化","通知清除缓存中的归一化",'/dev/device/clearCacheOfUnificationByModelVersion','POST');
                                                                                                               

                                                                                                                
                                                                                                             
/*=============================================*/                                                               
/* 参数配置初始化结束*/                                                                                         
/*=============================================*/                                                               
                                                                                                                
/*=============================================*/                                                               
/* 电站管理初始化开始*/                                                                                         
/*=============================================*/                                                               
-- 电站管理                                                                                                     
insert into ids_sm_resource_info_t(resource_key,auth_id,resource_name,description,auth_url,request_type) values (REPLACE(UUID(), '-', ''),1007,"校验电站名称是否重复","校验电站名称是否重复",'/biz/station/checkNameIsExists','POST');
insert into ids_sm_resource_info_t(resource_key,auth_id,resource_name,description,auth_url,request_type) values (REPLACE(UUID(), '-', ''),1007,"新增电站信息","新增电站信息",'/biz/station/insertStation','POST');
insert into ids_sm_resource_info_t(resource_key,auth_id,resource_name,description,auth_url,request_type) values (REPLACE(UUID(), '-', ''),1007,"更新电站信息","更新电站信息",'/biz/station/updateStationInfoMById','POST');
insert into ids_sm_resource_info_t(resource_key,auth_id,resource_name,description,auth_url,request_type) values (REPLACE(UUID(), '-', ''),1007,"删除电站信息","删除电站信息,多个id之间使用逗号来分割",'/biz/station/deleteStationInfosByIds','POST');
insert into ids_sm_resource_info_t(resource_key,auth_id,resource_name,description,auth_url,request_type) values (REPLACE(UUID(), '-', ''),1007,"分页查询电站信息","分页查询电站信息",'/biz/station/getStationInfo','POST');
insert into ids_sm_resource_info_t(resource_key,auth_id,resource_name,description,auth_url,request_type) values (REPLACE(UUID(), '-', ''),1007,"查询所有拥有环境检测仪的电站","查询所有拥有环境检测仪的电站",'/biz/station/getStationInfoByEmiId','POST');
insert into ids_sm_resource_info_t(resource_key,auth_id,resource_name,description,auth_url,request_type) values (REPLACE(UUID(), '-', ''),1007,"根据电站编号查询电站下面的所有的环境检测仪","根据电站编号查询电站下面的所有的环境检测仪",'/biz/station/getEmiInfoByStationCode','POST');
insert into ids_sm_resource_info_t(resource_key,auth_id,resource_name,description,auth_url,request_type) values (REPLACE(UUID(), '-', ''),1007,"保存电站共享环境检测仪数据","保存电站共享环境检测仪数据",'/biz/station/saveShareEmi','POST');
insert into ids_sm_resource_info_t(resource_key,auth_id,resource_name,description,auth_url,request_type) values (REPLACE(UUID(), '-', ''),1007,"获取用户区域企业树","获取用户区域企业树",'/biz/station/getUserDomainTree','POST');
insert into ids_sm_resource_info_t(resource_key,auth_id,resource_name,description,auth_url,request_type) values (REPLACE(UUID(), '-', ''),1007,"通过sn号查询设备信息","通过sn号查询设备信息",'/biz/station/getDeviceInfoBySN','POST');
insert into ids_sm_resource_info_t(resource_key,auth_id,resource_name,description,auth_url,request_type) values (REPLACE(UUID(), '-', ''),1007,"通过电站编号查询电站信息","通过电站编号查询电站信息",'/biz/station/getStationByCode','POST');
insert into ids_sm_resource_info_t(resource_key,auth_id,resource_name,description,auth_url,request_type) values (REPLACE(UUID(), '-', ''),1007,"查询电价","查询电价",'/biz/station/getPowerPriceById','POST');
insert into ids_sm_resource_info_t(resource_key,auth_id,resource_name,description,auth_url,request_type) values (REPLACE(UUID(), '-', ''),1007,"查询设备列表","查询设备列表",'/biz/station/getDevicesByStationCode','POST');
insert into ids_sm_resource_info_t(resource_key,auth_id,resource_name,description,auth_url,request_type) values (REPLACE(UUID(), '-', ''),1007,"查询所有设备的moduleVersionCode","查询所有设备的moduleVersionCode",'/biz/station/getModelVersionCodeList','POST');
insert into ids_sm_resource_info_t(resource_key,auth_id,resource_name,description,auth_url,request_type) values (REPLACE(UUID(), '-', ''),1007,"根据用户id查询所有的站点","根据用户id查询所有的站点",'/biz/station/getStationByUserId','POST');
insert into ids_sm_resource_info_t(resource_key,auth_id,resource_name,description,auth_url,request_type) values (REPLACE(UUID(), '-', ''),1007,"查询分时电价","查询分时电价",'/biz/station/getPricesByStationCode','POST');
insert into ids_sm_resource_info_t(resource_key,auth_id,resource_name,description,auth_url,request_type) values (REPLACE(UUID(), '-', ''),1007,"获取未绑定的Modbus数采","获取未绑定的Modbus数采",'/dev/device/findAllUnBindSCdevs','GET');
insert into ids_sm_resource_info_t(resource_key,auth_id,resource_name,description,auth_url,request_type) values (REPLACE(UUID(), '-', ''),1007,"绑定电站和Modbus协议数采的关系","绑定电站和Modbus协议数采的关系",'/dev/device/bindScToStation','POST');
                                                                                                                
-- 设备管理                                                                                                     
insert into ids_sm_resource_info_t(resource_key,auth_id,resource_name,description,auth_url,request_type) values (REPLACE(UUID(), '-', ''),1024,"获取所有行政区域","获取所有行政区域",'/biz/district/getAllDistrict','POST');
insert into ids_sm_resource_info_t(resource_key,auth_id,resource_name,description,auth_url,request_type) values (REPLACE(UUID(), '-', ''),1024,"根据条件分页查询设备数据","根据条件分页查询设备数据",'/dev/device/getDeviceByCondition','POST');
insert into ids_sm_resource_info_t(resource_key,auth_id,resource_name,description,auth_url,request_type) values (REPLACE(UUID(), '-', ''),1024,"根据ID查询设备数据","根据ID查询设备数据",'/dev/device/getDeviceById','POST');
insert into ids_sm_resource_info_t(resource_key,auth_id,resource_name,description,auth_url,request_type) values (REPLACE(UUID(), '-', ''),1024,"更新设备数据","更新设备数据",'/dev/device/updateDeviceById','POST');
insert into ids_sm_resource_info_t(resource_key,auth_id,resource_name,description,auth_url,request_type) values (REPLACE(UUID(), '-', ''),1024,"查询配置的组串容量","根查询配置的组串容量",'/dev/device/checkCapaty','POST');
insert into ids_sm_resource_info_t(resource_key,auth_id,resource_name,description,auth_url,request_type) values (REPLACE(UUID(), '-', ''),1024,"保存组串容量配置","保存组串容量配置",'/dev/device/saveDeviceCapacity','POST');
insert into ids_sm_resource_info_t(resource_key,auth_id,resource_name,description,auth_url,request_type) values (REPLACE(UUID(), '-', ''),1024,"查询组串容量配置","查询组串容量配置",'/dev/device/findFactorys','POST');
insert into ids_sm_resource_info_t(resource_key,auth_id,resource_name,description,auth_url,request_type) values (REPLACE(UUID(), '-', ''),1024,"保存组串详情设置","保存组串详情设置",'/dev/device/saveStationPvModule','POST');
insert into ids_sm_resource_info_t(resource_key,auth_id,resource_name,description,auth_url,request_type) values (REPLACE(UUID(), '-', ''),1024,"保存组串容量配置","保存组串容量配置",'/dev/device/saveDeviceCapacity','POST');
insert into ids_sm_resource_info_t(resource_key,auth_id,resource_name,description,auth_url,request_type) values (REPLACE(UUID(), '-', ''),1024,"设备删除","设备删除",'/biz/dev/remove','POST');
insert into ids_sm_resource_info_t(resource_key,auth_id,resource_name,description,auth_url,request_type) values (REPLACE(UUID(), '-', ''),1024,"设备实时数据获取","设备实时数据获取",'/biz/dev/data','POST');
insert into ids_sm_resource_info_t(resource_key,auth_id,resource_name,description,auth_url,request_type) values (REPLACE(UUID(), '-', ''),1024,"设备删除","设备删除",'/biz/dev/removeDev','POST');
insert into ids_sm_resource_info_t(resource_key,auth_id,resource_name,description,auth_url,request_type) values (REPLACE(UUID(), '-', ''),1024,"通知修改通讯配置","通知修改通讯配置",'/dev/device/notifyTnDevUpdateChange','POST');
insert into ids_sm_resource_info_t(resource_key,auth_id,resource_name,description,auth_url,request_type) values (REPLACE(UUID(), '-', ''),1024,"获取MQQT的版本信息","获取MQQT的版本信息",'/biz/devAccess/getMqqtVersionList','POST');
insert into ids_sm_resource_info_t(resource_key,auth_id,resource_name,description,auth_url,request_type) values (REPLACE(UUID(), '-', ''),1024,"新增mqqt设备","新增mqqt设备",'/biz/devAccess/insertMqqtDevInfo','POST');
insert into ids_sm_resource_info_t(resource_key,auth_id,resource_name,description,auth_url,request_type) values (REPLACE(UUID(), '-', ''),1024,"获取mqqt的数采SN号列表","获取mqqt的数采SN号列表",'/biz/devAccess/getMqqtParentSnList','GET');
insert into ids_sm_resource_info_t(resource_key,auth_id,resource_name,description,auth_url,request_type) values (REPLACE(UUID(), '-', ''),1024,"获取modbus协议的父设备信息SN号的信息","获取mqqt的数采SN号列表",'/biz/devAccess/getMqqtParentSnList','GET');
insert into ids_sm_resource_info_t(resource_key,auth_id,resource_name,description,auth_url,request_type) values (REPLACE(UUID(), '-', ''),1024,"新增MODBUS协议的设备","新增MODBUS协议的设备",'/biz/devAccess/insertModbusDevInfo','POST');
-- 设备通信                                                                                                     
insert into ids_sm_resource_info_t(resource_key,auth_id,resource_name,description,auth_url,request_type) values (REPLACE(UUID(), '-', ''),1025,"采集器设备配置","采集器设备配置",'/dev/settings/dc','POST');
insert into ids_sm_resource_info_t(resource_key,auth_id,resource_name,description,auth_url,request_type) values (REPLACE(UUID(), '-', ''),1025,"获取设备配置详情","获取设备配置详情",'/dev/settings/details','POST');
insert into ids_sm_resource_info_t(resource_key,auth_id,resource_name,description,auth_url,request_type) values (REPLACE(UUID(), '-', ''),1025,"设备端口配置列表","设备端口配置列表",'/dev/settings/list','POST');
insert into ids_sm_resource_info_t(resource_key,auth_id,resource_name,description,auth_url,request_type) values (REPLACE(UUID(), '-', ''),1025,"设备类型查询","设备类型查询",'/dev/settings/query','GET');
-- 设备绑定
insert into ids_sm_resource_info_t(resource_key,auth_id,resource_name,description,auth_url,request_type) values (REPLACE(UUID(), '-', ''),1031,"查询为绑定的设备","查询为绑定的设备",'/biz/station/getNoBindingStationInfoByPage','POST');  
insert into ids_sm_resource_info_t(resource_key,auth_id,resource_name,description,auth_url,request_type) values (REPLACE(UUID(), '-', ''),1031,"查询domainTree","查询domainTree",'/biz/station/getUserDomainTree','POST');
insert into ids_sm_resource_info_t(resource_key,auth_id,resource_name,description,auth_url,request_type) values (REPLACE(UUID(), '-', ''),1031,"查询设备下面的环境检测仪","查询设备下面的环境检测仪",'/biz/station/getEmiInfoByStationCode','POST');                                                                                                             
insert into ids_sm_resource_info_t(resource_key,auth_id,resource_name,description,auth_url,request_type) values (REPLACE(UUID(), '-', ''),1031,"查询电站的信息","查询电站的信息",'/biz/station/getPricesByStationCode','POST');
insert into ids_sm_resource_info_t(resource_key,auth_id,resource_name,description,auth_url,request_type) values (REPLACE(UUID(), '-', ''),1031,"绑定电站","绑定电站",'/biz/station//updateStationInfoMById','POST');

-- 设备升级
insert into ids_sm_resource_info_t(resource_key,auth_id,resource_name,description,auth_url,request_type) values (REPLACE(UUID(), '-', ''),1032,"查询所有的MOBUS协议的绑定了电站的数采","查询所有的MOBUS协议的绑定了电站的数采",'/dev/device/findAllBindScDevs','GET');
insert into ids_sm_resource_info_t(resource_key,auth_id,resource_name,description,auth_url,request_type) values (REPLACE(UUID(), '-', ''),1032,"上传升级文件","上传升级文件",'/dev/device/uploadUpgradeFile','POST');
insert into ids_sm_resource_info_t(resource_key,auth_id,resource_name,description,auth_url,request_type) values (REPLACE(UUID(), '-', ''),1032,"查询升级的设备信息","查询升级的设备信息",'/dev/device/findUpgradeDevInfos','GET');
insert into ids_sm_resource_info_t(resource_key,auth_id,resource_name,description,auth_url,request_type) values (REPLACE(UUID(), '-', ''),1032,"执行设备升级","执行设备升级",'/dev/device/devUpgrade','POST');

/*=============================================*/                                                               
/* 电站管理初始化结束*/                                                                                         
/*=============================================*/                                                               
                                                                                                                
/*=============================================*/                                                               
/* 系统参数配置初始化开始*/                                                                                     
/*=============================================*/                                                               
insert into ids_sm_resource_info_t(resource_key,auth_id,resource_name,description,auth_url,request_type) values (REPLACE(UUID(), '-', ''),1008,"插入系统参数","插入系统参数",'/biz/systemParam/saveSystemParam','POST');
insert into ids_sm_resource_info_t(resource_key,auth_id,resource_name,description,auth_url,request_type) values (REPLACE(UUID(), '-', ''),1008,"获取系统参数配置","获取系统参数配置",'/biz/systemParam/getSystemParam','POST');
/*=============================================*/                                                               
/* 系统参数配置初始化结束*/                                                                                     
/*=============================================*/                                                               
                                                                                                                
/*=============================================*/                                                               
/* 数据完整性初始化开始 ----- 未做*/                                                                            
/*=============================================*/                                                               
insert into ids_sm_resource_info_t(resource_key,auth_id,resource_name,description,auth_url,request_type) values (REPLACE(UUID(), '-', ''),1009,"KPI计算新增任务","KPI计算新增任务",'/biz/kpiRevise/createCalcTask','POST');
insert into ids_sm_resource_info_t(resource_key,auth_id,resource_name,description,auth_url,request_type) values (REPLACE(UUID(), '-', ''),1009,"KPI计算删除任务","KPI计算删除任务",'/biz/kpiRevise/removeCalcTask','POST');
insert into ids_sm_resource_info_t(resource_key,auth_id,resource_name,description,auth_url,request_type) values (REPLACE(UUID(), '-', ''),1009,"KPI计算修改任务","KPI计算修改任务",'/biz/kpiRevise/modifyCalcTask','POST');
insert into ids_sm_resource_info_t(resource_key,auth_id,resource_name,description,auth_url,request_type) values (REPLACE(UUID(), '-', ''),1009,"KPI计算查询任务","KPI计算查询任务",'/biz/kpiRevise/queryCalcTask','POST');
insert into ids_sm_resource_info_t(resource_key,auth_id,resource_name,description,auth_url,request_type) values (REPLACE(UUID(), '-', ''),1009,"KPI计算执行任务","KPI计算执行任务",'/biz/kpiRevise/executeCalcTask','POST');
                                                                                                                
insert into ids_sm_resource_info_t(resource_key,auth_id,resource_name,description,auth_url,request_type) values (REPLACE(UUID(), '-', ''),1019,"KPI修正添加任务","KPI修正添加任务",'/biz/kpiRevise/saveKpiReviseT','POST');
insert into ids_sm_resource_info_t(resource_key,auth_id,resource_name,description,auth_url,request_type) values (REPLACE(UUID(), '-', ''),1019,"KPI修正更改任务","KPI修正更改任务",'/biz/kpiRevise/updateKpiReviseT','POST');
insert into ids_sm_resource_info_t(resource_key,auth_id,resource_name,description,auth_url,request_type) values (REPLACE(UUID(), '-', ''),1019,"KPI修正查询任务","KPI修正查询任务",'/biz/kpiRevise/getKpiReviseTByCondtion','POST');
insert into ids_sm_resource_info_t(resource_key,auth_id,resource_name,description,auth_url,request_type) values (REPLACE(UUID(), '-', ''),1019,"KPI修正按ID查询任务","KPI修正查询任务",'/biz/kpiRevise/getKpiReviseT','POST');
insert into ids_sm_resource_info_t(resource_key,auth_id,resource_name,description,auth_url,request_type) values (REPLACE(UUID(), '-', ''),1019,"KPI修正执行任务","KPI修正执行任务",'/biz/kpiRevise/kpiSyncronize','POST');
                                                                                                                
                                                                                                                
                                                                                                                
-- 系统大屏                                                                                                     
insert into ids_sm_resource_info_t(resource_key,auth_id,resource_name,description,auth_url,request_type) values (REPLACE(UUID(), '-', ''),1010,"供电负荷","供电负荷",'/biz/largeScreen/getActivePower','POST');
insert into ids_sm_resource_info_t(resource_key,auth_id,resource_name,description,auth_url,request_type) values (REPLACE(UUID(), '-', ''),1010,"发电趋势","发电趋势",'/biz/largeScreen/getPowerTrends','POST');
insert into ids_sm_resource_info_t(resource_key,auth_id,resource_name,description,auth_url,request_type) values (REPLACE(UUID(), '-', ''),1010,"地图数据查询","地图数据查询",'/biz/largeScreen/getMapShowData','POST');
insert into ids_sm_resource_info_t(resource_key,auth_id,resource_name,description,auth_url,request_type) values (REPLACE(UUID(), '-', ''),1010,"大屏基础数据","大屏基础数据",'/biz/largeScreen/getCommonData','POST');
insert into ids_sm_resource_info_t(resource_key,auth_id,resource_name,description,auth_url,request_type) values (REPLACE(UUID(), '-', ''),1010,"大屏底部数据","大屏底部数据",'/biz/largeScreen/getPowerGeneration','POST');
insert into ids_sm_resource_info_t(resource_key,auth_id,resource_name,description,auth_url,request_type) values (REPLACE(UUID(), '-', ''),1010,"电站告警统计","电站告警统计",'/biz/largeScreen/getAlarmStatistics','POST');
insert into ids_sm_resource_info_t(resource_key,auth_id,resource_name,description,auth_url,request_type) values (REPLACE(UUID(), '-', ''),1010,"电站规模","电站规模",'/biz/largeScreen/getListStationInfo','POST');
insert into ids_sm_resource_info_t(resource_key,auth_id,resource_name,description,auth_url,request_type) values (REPLACE(UUID(), '-', ''),1010,"社会贡献","社会贡献",'/biz/largeScreen/getSocialContribution','POST');
insert into ids_sm_resource_info_t(resource_key,auth_id,resource_name,description,auth_url,request_type) values (REPLACE(UUID(), '-', ''),1010,"设备分布","设备分布",'/biz/largeScreen/getDeviceCount','POST');
insert into ids_sm_resource_info_t(resource_key,auth_id,resource_name,description,auth_url,request_type) values (REPLACE(UUID(), '-', ''),1010,"电站任务工单统计","电站任务工单统计",'/biz/largeScreen/getTaskStatistics','POST');
                                                                                                                
                                                                                                                
-- 单站总览                                                                                                     
insert into ids_sm_resource_info_t(resource_key,auth_id,resource_name,description,auth_url,request_type) values (REPLACE(UUID(), '-', ''),1012,"发电量及收益接口","发电量及收益接口",'/biz/singleStation/getSingleStationPowerAndIncome','POST');
insert into ids_sm_resource_info_t(resource_key,auth_id,resource_name,description,auth_url,request_type) values (REPLACE(UUID(), '-', ''),1012,"告警统计","告警统计",'/biz/singleStation/getSingleStationAlarmStatistics','POST');
insert into ids_sm_resource_info_t(resource_key,auth_id,resource_name,description,auth_url,request_type) values (REPLACE(UUID(), '-', ''),1012,"实时数据接口","实时数据接口",'/biz/singleStation/getSingleStationCommonData','POST');
insert into ids_sm_resource_info_t(resource_key,auth_id,resource_name,description,auth_url,request_type) values (REPLACE(UUID(), '-', ''),1012,"日负荷曲线接口","日负荷曲线接口",'/biz/singleStation/getSingleStationActivePower','POST');
insert into ids_sm_resource_info_t(resource_key,auth_id,resource_name,description,auth_url,request_type) values (REPLACE(UUID(), '-', ''),1012,"电站总览社会贡献","电站总览社会贡献",'/biz/singleStation/getContribution','POST');
insert into ids_sm_resource_info_t(resource_key,auth_id,resource_name,description,auth_url,request_type) values (REPLACE(UUID(), '-', ''),1012,"电站概况信息","电站概况信息",'/biz/singleStation/getSingleStationInfo','POST');
insert into ids_sm_resource_info_t(resource_key,auth_id,resource_name,description,auth_url,request_type) values (REPLACE(UUID(), '-', ''),1012,"设备发电状态","设备发电状态",'/biz/singleStation/getDevPowerStatus','POST');

-- 单站视图
insert into ids_sm_resource_info_t(resource_key,auth_id,resource_name,description,auth_url,request_type) values (REPLACE(UUID(), '-', ''),1012,"设备总览","设备总览",' /biz/singleStation/getDevProfile','POST');
insert into ids_sm_resource_info_t(resource_key,auth_id,resource_name,description,auth_url,request_type) values (REPLACE(UUID(), '-', ''),1012,"设备列表","设备列表",'/biz/singleStation/getDevList','POST');
insert into ids_sm_resource_info_t(resource_key,auth_id,resource_name,description,auth_url,request_type) values (REPLACE(UUID(), '-', ''),1012,"设备监控","设备监控",'/biz/singleStation/getSingalData','POST');
insert into ids_sm_resource_info_t(resource_key,auth_id,resource_name,description,auth_url,request_type) values (REPLACE(UUID(), '-', ''),1012,"设备信息","设备信息",'/biz/singleStation/getDevDetail','POST');
insert into ids_sm_resource_info_t(resource_key,auth_id,resource_name,description,auth_url,request_type) values (REPLACE(UUID(), '-', ''),1012,"设备告警","设备告警",'/biz/singleStation/getDevAlarm','POST');       
-- 单站告警                                                                                                     
insert into ids_sm_resource_info_t(resource_key,auth_id,resource_name,description,auth_url,request_type) values (REPLACE(UUID(), '-', ''),1012,"设备告警确认","设备告警确认",'/biz/alarm/confirm/ack','POST');
insert into ids_sm_resource_info_t(resource_key,auth_id,resource_name,description,auth_url,request_type) values (REPLACE(UUID(), '-', ''),1012,"设备告警清除","设备告警清除",'/biz/alarm/confirm/cleared','POST');
insert into ids_sm_resource_info_t(resource_key,auth_id,resource_name,description,auth_url,request_type) values (REPLACE(UUID(), '-', ''),1012,"设备告警信息查询","设备告警信息查询",'/biz/settings/alarm/list','POST');
insert into ids_sm_resource_info_t(resource_key,auth_id,resource_name,description,auth_url,request_type) values (REPLACE(UUID(), '-', ''),1012,"设备告警修复建议","设备告警修复建议",'/biz/alarm/repair','GET');
insert into ids_sm_resource_info_t(resource_key,auth_id,resource_name,description,auth_url,request_type) values (REPLACE(UUID(), '-', ''),1012,"智能告警确认","智能告警确认",'/biz/alarm/confirm/ackzl','POST');
insert into ids_sm_resource_info_t(resource_key,auth_id,resource_name,description,auth_url,request_type) values (REPLACE(UUID(), '-', ''),1012,"智能告警清除","智能告警清除",'/biz/alarm/confirm/clearedzl','POST');
insert into ids_sm_resource_info_t(resource_key,auth_id,resource_name,description,auth_url,request_type) values (REPLACE(UUID(), '-', ''),1012,"智能告警信息查询","智能告警信息查询",'/biz/alarm/list/analysis','POST');
insert into ids_sm_resource_info_t(resource_key,auth_id,resource_name,description,auth_url,request_type) values (REPLACE(UUID(), '-', ''),1012,"智能告警修复建议","智能告警修复建议",'/biz/alarm/repair/analysis','GET');
insert into ids_sm_resource_info_t(resource_key,auth_id,resource_name,description,auth_url,request_type) values (REPLACE(UUID(), '-', ''),1012,"告警统计级别","告警统计级别",'/biz/alarm/statistics/level','POST');
insert into ids_sm_resource_info_t(resource_key,auth_id,resource_name,description,auth_url,request_type) values (REPLACE(UUID(), '-', ''),1012,"告警设置","告警设置",'/biz/settings/alarm/list','POST');
insert into ids_sm_resource_info_t(resource_key,auth_id,resource_name,description,auth_url,request_type) values (REPLACE(UUID(), '-', ''),1012,"告警列表","告警列表",'/biz/alarm/list','POST');	   
insert into ids_sm_resource_info_t(resource_key,auth_id,resource_name,description,auth_url,request_type) values (REPLACE(UUID(), '-', ''),1012,"查询实时告警","查询实时告警",'/biz/alarm/alarmOnlineList','POST');	   
insert into ids_sm_resource_info_t(resource_key,auth_id,resource_name,description,auth_url,request_type) values (REPLACE(UUID(), '-', ''),1012,"查询一条告警的详情","查询一条告警的详情",'/biz/operation/worksite/getAlarmDetail','POST');	   
insert into ids_sm_resource_info_t(resource_key,auth_id,resource_name,description,auth_url,request_type) values (REPLACE(UUID(), '-', ''),1012,"告警转工单","告警转工单",'/biz/workFlow/alarmToDefect','POST');
insert into ids_sm_resource_info_t(resource_key,auth_id,resource_name,description,auth_url,request_type) values (REPLACE(UUID(), '-', ''),1012,"文件上传","文件上传",'/biz/fileManager/fileUpload','POST');

-- 单站报表
insert into ids_sm_resource_info_t(resource_key,auth_id,resource_name,description,auth_url,request_type) values (REPLACE(UUID(), '-', ''),1012,"查询电站报表的数据","查询电站报表的数据",'/biz/produceMng/listStationRpt','POST');	   
insert into ids_sm_resource_info_t(resource_key,auth_id,resource_name,description,auth_url,request_type) values (REPLACE(UUID(), '-', ''),1012,"导出电站报表的数据","导出电站报表的数据",'/biz/produceMng/report/download/StationRuning','GET');	   
insert into ids_sm_resource_info_t(resource_key,auth_id,resource_name,description,auth_url,request_type) values (REPLACE(UUID(), '-', ''),1012,"查询逆变器报表的数据","查询逆变器报表的数据",'/biz/produceMng/listInverterRpt','POST');	   
insert into ids_sm_resource_info_t(resource_key,auth_id,resource_name,description,auth_url,request_type) values (REPLACE(UUID(), '-', ''),1012,"导出逆变器报表的数据","导出逆变器报表的数据",'biz/produceMng/exportInverterRpt','GET');	   

-- 多站总览                                                                                                     
insert into ids_sm_resource_info_t(resource_key,auth_id,resource_name,description,auth_url,request_type) values (REPLACE(UUID(), '-', ''),1001,"分布统计","分布统计",'/biz/stationOverview/getStationDistribution','POST');
insert into ids_sm_resource_info_t(resource_key,auth_id,resource_name,description,auth_url,request_type) values (REPLACE(UUID(), '-', ''),1001,"发电量收益","发电量收益",'/biz/stationOverview/getPowerAndIncome','POST');
insert into ids_sm_resource_info_t(resource_key,auth_id,resource_name,description,auth_url,request_type) values (REPLACE(UUID(), '-', ''),1001,"告警统计","告警统计",'/biz/stationOverview/getAlarmStatistics','POST');
insert into ids_sm_resource_info_t(resource_key,auth_id,resource_name,description,auth_url,request_type) values (REPLACE(UUID(), '-', ''),1001,"实时数据","实时数据",'/biz/stationOverview/getRealtimeKPI','POST');
insert into ids_sm_resource_info_t(resource_key,auth_id,resource_name,description,auth_url,request_type) values (REPLACE(UUID(), '-', ''),1001,"总览社会贡献","总览社会贡献",'/biz/stationOverview/getContribution','POST');
insert into ids_sm_resource_info_t(resource_key,auth_id,resource_name,description,auth_url,request_type) values (REPLACE(UUID(), '-', ''),1001,"电站PR","电站PR",'/biz/stationOverview/getPRList','POST');
insert into ids_sm_resource_info_t(resource_key,auth_id,resource_name,description,auth_url,request_type) values (REPLACE(UUID(), '-', ''),1001,"电站状态","电站状态",'/biz/stationOverview/getStationStatus','POST');
insert into ids_sm_resource_info_t(resource_key,auth_id,resource_name,description,auth_url,request_type) values (REPLACE(UUID(), '-', ''),1001,"等效利用小时数","等效利用小时数",'/biz/stationOverview/getPPRList','POST');
insert into ids_sm_resource_info_t(resource_key,auth_id,resource_name,description,auth_url,request_type) values (REPLACE(UUID(), '-', ''),1001,"获取电站列表","获取电站列表",'/biz/stationOverview/getPowerStationList','POST');
insert into ids_sm_resource_info_t(resource_key,auth_id,resource_name,description,auth_url,request_type) values (REPLACE(UUID(), '-', ''),1001,"设备统计","设备统计",'/biz/stationOverview/getDevDistrition','POST');



-- 点表管理
insert into ids_sm_resource_info_t(resource_key,auth_id,resource_name,description,auth_url,request_type) values (REPLACE(UUID(), '-', ''),1021,"点表导入","点表导入",'/biz/signal/importSignalVersion','POST');
insert into ids_sm_resource_info_t(resource_key,auth_id,resource_name,description,auth_url,request_type) values (REPLACE(UUID(), '-', ''),1021,"点表列表查询","点表列表查询",'/biz/signal/listSignalVersionInfo','POST');
insert into ids_sm_resource_info_t(resource_key,auth_id,resource_name,description,auth_url,request_type) values (REPLACE(UUID(), '-', ''),1021,"删除点表","删除点表",'/biz/signal/deleteSignalVersionById','POST');
insert into ids_sm_resource_info_t(resource_key,auth_id,resource_name,description,auth_url,request_type) values (REPLACE(UUID(), '-', ''),1021,"保存归一化配置","保存归一化配置",'/biz/signal/normalizedAdapter','POST');
insert into ids_sm_resource_info_t(resource_key,auth_id,resource_name,description,auth_url,request_type) values (REPLACE(UUID(), '-', ''),1021,"删除归一化配置","删除归一化配置",'/biz/signal/deleteNormalizedAdapter','POST');
insert into ids_sm_resource_info_t(resource_key,auth_id,resource_name,description,auth_url,request_type) values (REPLACE(UUID(), '-', ''),1021,"归一化适配清除","归一化适配清除",'/biz/signal/clearNormalizedAdapter','POST');
insert into ids_sm_resource_info_t(resource_key,auth_id,resource_name,description,auth_url,request_type) values (REPLACE(UUID(), '-', ''),1021,"查询已经配置的归一化信息","查询已经配置的归一化信息",'/biz/signal/getNormalizedInfo','POST');
insert into ids_sm_resource_info_t(resource_key,auth_id,resource_name,description,auth_url,request_type) values (REPLACE(UUID(), '-', ''),1021,"通过设备类型查询信号点信息","通过设备类型查询信号点信息",'/biz/signal/queryNormalizedByDevType','POST');
insert into ids_sm_resource_info_t(resource_key,auth_id,resource_name,description,auth_url,request_type) values (REPLACE(UUID(), '-', ''),1021,"告警转遥信","告警转遥信",'/biz/signal/convert/false','POST');
insert into ids_sm_resource_info_t(resource_key,auth_id,resource_name,description,auth_url,request_type) values (REPLACE(UUID(), '-', ''),1021,"更新信号点信息","更新信号点信息",'/biz/signal/updateSignalInfo','POST');
insert into ids_sm_resource_info_t(resource_key,auth_id,resource_name,description,auth_url,request_type) values (REPLACE(UUID(), '-', ''),1021,"查询信号点列表","查询信号点列表",'/biz/signal/listSignalInfo','POST');
insert into ids_sm_resource_info_t(resource_key,auth_id,resource_name,description,auth_url,request_type) values (REPLACE(UUID(), '-', ''),1021,"获取点表模型","获取点表模型",'/biz/signal/getSignalModel','POST');
insert into ids_sm_resource_info_t(resource_key,auth_id,resource_name,description,auth_url,request_type) values (REPLACE(UUID(), '-', ''),1021,"遥信转告警","遥信转告警",'/biz/signal/convert/true','POST');
insert into ids_sm_resource_info_t(resource_key,auth_id,resource_name,description,auth_url,request_type) values (REPLACE(UUID(), '-', ''),1021,"通知这个连接信号点的告警转换信息改变","通知这个连接信号点的告警转换信息改变",'/dev/device/resetMasterServiceSignals','POST');

-- 告警设置
insert into ids_sm_resource_info_t(resource_key,auth_id,resource_name,description,auth_url,request_type) values (REPLACE(UUID(), '-', ''),1022,"告警设置列表查询","告警设置列表查询",'/biz/settings/alarm/list','POST');
insert into ids_sm_resource_info_t(resource_key,auth_id,resource_name,description,auth_url,request_type) values (REPLACE(UUID(), '-', ''),1022,"告警设置修改","告警设置修改",'/biz/settings/alarm/update','POST');

-- 电站参数配置                                                                                                 
insert into ids_sm_resource_info_t(resource_key,auth_id,resource_name,description,auth_url,request_type) values (REPLACE(UUID(), '-', ''),1023,"根据条件分页查询电站参数配置","根据条件分页查询电站参数配置",'/biz/param/getParamByCondition','POST');
insert into ids_sm_resource_info_t(resource_key,auth_id,resource_name,description,auth_url,request_type) values (REPLACE(UUID(), '-', ''),1023,"保存电站参数配置修改数据","保存电站参数配置修改数据",'/biz/param/saveOrUpdateParam','POST');
insert into ids_sm_resource_info_t(resource_key,auth_id,resource_name,description,auth_url,request_type) values (REPLACE(UUID(), '-', ''),1023,"导航树","导航树",'/biz/district/getAllDistrict','POST');   

-- License管理
insert into ids_sm_resource_info_t(resource_key,auth_id,resource_name,description,auth_url,request_type) values (REPLACE(UUID(), '-', ''),1027,"License导入","License导入",'/biz/license/licenseImport','POST');
insert into ids_sm_resource_info_t(resource_key,auth_id,resource_name,description,auth_url,request_type) values (REPLACE(UUID(), '-', ''),1027,"License信息获取","License信息获取",'/biz/license/getLicenseInfo','POST');

-- 组织架构
insert into ids_sm_resource_info_t(resource_key,auth_id,resource_name,description,auth_url,request_type) values (REPLACE(UUID(), '-', ''),1028,"插入子公司","插入子公司",'/biz/departmentController/insertDepartment','POST');
insert into ids_sm_resource_info_t(resource_key,auth_id,resource_name,description,auth_url,request_type) values (REPLACE(UUID(), '-', ''),1028,"删除","删除",'/biz/departmentController/deleteDepartment','POST');
insert into ids_sm_resource_info_t(resource_key,auth_id,resource_name,description,auth_url,request_type) values (REPLACE(UUID(), '-', ''),1028,"查询部门树形结构列表","查询部门树形结构列表",'/biz/departmentController/getDepartmentByParentId','POST');
insert into ids_sm_resource_info_t(resource_key,auth_id,resource_name,description,auth_url,request_type) values (REPLACE(UUID(), '-', ''),1028,"移动","移动",'/biz/departmentController/moveDepartment','POST');
insert into ids_sm_resource_info_t(resource_key,auth_id,resource_name,description,auth_url,request_type) values (REPLACE(UUID(), '-', ''),1028,"查询企业的数据","查询企业的数据",'/biz/departmentController/getEnterprise','POST');

-- 系统参数配置
insert into ids_sm_resource_info_t(resource_key,auth_id,resource_name,description,auth_url,request_type) values (REPLACE(UUID(), '-', ''),1026,"系统参数配置查询","系统参数配置查询",'/biz/systemParam/getSystemParam','POST');
insert into ids_sm_resource_info_t(resource_key,auth_id,resource_name,description,auth_url,request_type) values (REPLACE(UUID(), '-', ''),1026,"系统参数配置保存","系统参数配置保存",'/biz/systemParam/saveSystemParam','POST');

/* ================================================================================================================= */
/* APP模块权限资源 */
/* ================================================================================================================= */
-- APP电站管理
insert into ids_sm_resource_info_t(resource_key,auth_id,resource_name,description,auth_url,request_type) values (REPLACE(UUID(), '-', ''),2001,"运维人员位置信息上报","运维人员位置信息上报",'/biz/app/common/realLocation','POST');
insert into ids_sm_resource_info_t(resource_key,auth_id,resource_name,description,auth_url,request_type) values (REPLACE(UUID(), '-', ''),2001,"电站总览发电量及收益功能","电站总览发电量及收益功能",'/biz/app/stationOverview/getPowerAndIncome','POST');
insert into ids_sm_resource_info_t(resource_key,auth_id,resource_name,description,auth_url,request_type) values (REPLACE(UUID(), '-', ''),2001,"电站总览等效利用小时数排名","电站总览等效利用小时数排名",'/biz/app/stationOverview/getPPRList','POST');
insert into ids_sm_resource_info_t(resource_key,auth_id,resource_name,description,auth_url,request_type) values (REPLACE(UUID(), '-', ''),2001,"电站总览查询电站状态","电站总览查询电站状态",'/biz/app/stationOverview/getStationStatus','POST');
insert into ids_sm_resource_info_t(resource_key,auth_id,resource_name,description,auth_url,request_type) values (REPLACE(UUID(), '-', ''),2001,"获取人员管理电站的实时数据合计","获取人员管理电站的实时数据合计",'/biz/app/stationOverview/getRealtimeKPI','POST');
insert into ids_sm_resource_info_t(resource_key,auth_id,resource_name,description,auth_url,request_type) values (REPLACE(UUID(), '-', ''),2001,"获取人员管理电站的列表","获取人员管理电站的列表",'/biz/app/stationOverview/getPowerStationList','POST');
insert into ids_sm_resource_info_t(resource_key,auth_id,resource_name,description,auth_url,request_type) values (REPLACE(UUID(), '-', ''),2001,"电站总览社会贡献","电站总览社会贡献",'/biz/app/stationOverview/getContribution','POST');
insert into ids_sm_resource_info_t(resource_key,auth_id,resource_name,description,auth_url,request_type) values (REPLACE(UUID(), '-', ''),2001,"单电站总览查询电站概况信息","单电站总览查询电站概况信息",'/biz/app/singleStation/getSingleStationInfo','POST');
insert into ids_sm_resource_info_t(resource_key,auth_id,resource_name,description,auth_url,request_type) values (REPLACE(UUID(), '-', ''),2001,"单电站总览查询电站实时数据","单电站总览查询电站实时数据",'/biz/app/singleStation/getSingleStationCommonData','POST');
insert into ids_sm_resource_info_t(resource_key,auth_id,resource_name,description,auth_url,request_type) values (REPLACE(UUID(), '-', ''),2001,"单电站总览查询发电量及收益","单电站总览查询发电量及收益",'/biz/app/singleStation/getSingleStationPowerAndIncome','POST');
insert into ids_sm_resource_info_t(resource_key,auth_id,resource_name,description,auth_url,request_type) values (REPLACE(UUID(), '-', ''),2001,"获取单电站告警分类统计","获取单电站告警分类统计",'/biz/app/singleStation/getSingleStationAlarmStatistics','POST');
insert into ids_sm_resource_info_t(resource_key,auth_id,resource_name,description,auth_url,request_type) values (REPLACE(UUID(), '-', ''),2001,"获取单电站日负荷曲线","获取单电站日负荷曲线",'/biz/app/singleStation/getSingleStationActivePower','POST');
insert into ids_sm_resource_info_t(resource_key,auth_id,resource_name,description,auth_url,request_type) values (REPLACE(UUID(), '-', ''),2001,"获取单电站","获取单电站",'/biz/app/singleStation/getStationInfo','POST');
insert into ids_sm_resource_info_t(resource_key,auth_id,resource_name,description,auth_url,request_type) values (REPLACE(UUID(), '-', ''),2001,"电站总览社会贡献","电站总览社会贡献",'/biz/app/singleStation/getContribution','POST');
insert into ids_sm_resource_info_t(resource_key,auth_id,resource_name,description,auth_url,request_type) values (REPLACE(UUID(), '-', ''),2001,"分页获取当期用户关联的电站","分页获取当期用户关联的电站",'/biz/app/appStationInfoController/getStationInfo','POST');
                                                                                                                
-- APP报表管理                                                                                                  
insert into ids_sm_resource_info_t(resource_key,auth_id,resource_name,description,auth_url,request_type) values (REPLACE(UUID(), '-', ''),2003,"逆变器运行报表","逆变器运行报表",'/biz/app/inverterRunReport','POST');

-- 设备监管
insert into ids_sm_resource_info_t(resource_key,auth_id,resource_name,description,auth_url,request_type) values (REPLACE(UUID(), '-', ''),2002,"设备一览","设备一览",'/biz/app/appDevInfoController/getDevProfile','POST');
insert into ids_sm_resource_info_t(resource_key,auth_id,resource_name,description,auth_url,request_type) values (REPLACE(UUID(), '-', ''),2002,"设备列表","设备列表",'/biz/app/appDevInfoController/getDeviceList','POST');
insert into ids_sm_resource_info_t(resource_key,auth_id,resource_name,description,auth_url,request_type) values (REPLACE(UUID(), '-', ''),2002,"设备监控","设备监控",'/biz/app/appDevInfoController/data','POST');
insert into ids_sm_resource_info_t(resource_key,auth_id,resource_name,description,auth_url,request_type) values (REPLACE(UUID(), '-', ''),2002,"设备信息","设备信息",'/biz/app/appDevInfoController/getDeviceDetail','POST');
insert into ids_sm_resource_info_t(resource_key,auth_id,resource_name,description,auth_url,request_type) values (REPLACE(UUID(), '-', ''),2002,"设备告警","设备告警",'/biz/app/appDevInfoController/getDeviceAlarm','POST');

-- 任务中心
insert into ids_sm_resource_info_t(resource_key,auth_id,resource_name,description,auth_url,request_type) values (REPLACE(UUID(), '-', ''),2004,"统计缺陷数量","统计缺陷数量",'/biz/app/appWorkFlowController/getWorkFlowCount','POST');
insert into ids_sm_resource_info_t(resource_key,auth_id,resource_name,description,auth_url,request_type) values (REPLACE(UUID(), '-', ''),2004,"执行缺陷","执行缺陷",'/biz/app/appWorkFlowController/executeWorkFlowDefect','POST');
insert into ids_sm_resource_info_t(resource_key,auth_id,resource_name,description,auth_url,request_type) values (REPLACE(UUID(), '-', ''),2004,"根据条件查询个人任务信息","根据条件查询个人任务信息",'/biz/app/appWorkFlowController/getWorkFlowTaskByCondition','POST');
insert into ids_sm_resource_info_t(resource_key,auth_id,resource_name,description,auth_url,request_type) values (REPLACE(UUID(), '-', ''),2004,"根据条件查询缺陷数据","根据条件查询缺陷数据",'/biz/app/appWorkFlowController/getWorkFlowDefectsByCondition','POST');
insert into ids_sm_resource_info_t(resource_key,auth_id,resource_name,description,auth_url,request_type) values (REPLACE(UUID(), '-', ''),2004,"查询某一条消缺详情","查询某一条消缺详情",'/biz/app/appWorkFlowController/getWorkFlowDefectDetails','POST');
insert into ids_sm_resource_info_t(resource_key,auth_id,resource_name,description,auth_url,request_type) values (REPLACE(UUID(), '-', ''),2004,"查询缺陷关联的告警","查询缺陷关联的告警",'/biz/app/appWorkFlowController/getDefectAlarm','POST');



