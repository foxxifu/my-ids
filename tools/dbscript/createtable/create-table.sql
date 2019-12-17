/*==============================================================*/
/* 系统管理模块相关表，包括：企业、用户、区域、角色、权限资源 等*/
/*==============================================================*/
drop table if exists ids_poverty_relief_object_t;

drop table if exists ids_alarm_model_t;

drop table if exists ids_alarm_t;

drop table if exists ids_analysis_alarm_model_t;

drop table if exists ids_analysis_alarm_t;

drop table if exists ids_array_intelligentclean_t;

drop table if exists ids_cleared_alarm_t;

drop table if exists ids_data_range_config_t;

drop table if exists ids_dc_config_t;

drop table if exists ids_dev_info_t;

drop table if exists ids_device_pv_module_t;

drop table if exists ids_district_t;

drop table if exists ids_file_manager_t;

drop table if exists ids_inverter_intelligentclean_h;

drop table if exists ids_inverter_intelligentclean_t;

drop table if exists ids_inverter_string_t;

drop table if exists ids_kpi_day_combinerdc_t;

drop table if exists ids_kpi_hour_emi_t;

drop table if exists ids_kpi_day_inverter_t;

drop table if exists ids_kpi_hour_inverter_t;

drop table if exists ids_kpi_month_inverter_t;

drop table if exists ids_kpi_year_inverter_t;

drop table if exists ids_kpi_day_meter_t;

drop table if exists ids_kpi_hour_meter_t;

drop table if exists ids_kpi_month_meter_t;

drop table if exists ids_kpi_year_meter_t;

drop table if exists ids_kpi_day_station_t;

drop table if exists ids_kpi_hour_station_t;

drop table if exists ids_kpi_month_station_t;

drop table if exists ids_kpi_year_station_t;

drop table if exists ids_overlimit_alarm_t;

drop table if exists ids_power_price_t;

drop table if exists ids_pv_capacity_t;

drop table if exists ids_pv_module_t;

drop table if exists ids_signal_info_t;

drop table if exists ids_signal_model_t;

drop table if exists ids_signal_version_t;

drop table if exists ids_sm_auth_t;

drop table if exists ids_sm_domain_info_t;

drop table if exists ids_sm_enterprise_info_t;

drop table if exists ids_sm_intelligentclean_param_t;

drop table if exists ids_sm_resource_info_t;

drop table if exists ids_sm_role_auth_t;

drop table if exists ids_sm_role_info_t;

drop table if exists ids_station_info_t;

drop table if exists ids_sm_user_auth_t;

drop table if exists ids_sm_user_role_t;

drop table if exists ids_sm_user_station_t;

drop table if exists ids_sm_user_info_t;

drop table if exists ids_station_realtime_data_h;

drop table if exists ids_station_realtime_data_t;

drop table if exists ids_station_shareemi_t;

drop table if exists ids_station_weather_day_t;

drop table if exists ids_station_weather_statistics_t;

drop table if exists ids_statistics_schedule_task_t;

drop table if exists ids_system_param;

drop table if exists ids_telesignal_t;

drop table if exists ids_transformer_data_t;

drop table if exists ids_workflow_defect_t;

drop table if exists ids_workflow_process_t;

drop table if exists ids_workflow_task_t;

drop table if exists ids_workflow_task_user_t;

drop table if exists ids_sm_department_t;

drop table if exists user_department;

drop table if exists ids_center_vert_detail_t;

drop table if exists ids_first_workticket_business;

-- ----------------------------
-- Table structure for `ids_first_workticket_business`
-- ----------------------------
create table ids_first_workticket_business
(
	id		BIGINT(16) NOT NULL auto_increment COMMENT '主键id',
	charge_id	bigint(16) not null comment '负责人的id',
	group_id	varchar(50) not null comment '工作成员的id,使用逗号隔开',
	work_content	varchar(500) not null comment '工作内容',
	alarm_ids		varchar(300) comment '关联告警的id',
	alarm_type		varchar(1) comment '告警类型-1设备告警2智能告警',
	work_address	varchar(500) not null comment '工作地点',
	start_work_time	bigint(16) not null comment '计划开始时间',
	end_work_time	bigint(16) not null comment '计划结束时间',
	safety_technical	varchar(500) comment '安全技术措施',
	personal_safety_technical	varchar(500) comment '个人安全措施',
	ground_wire		varchar(500) comment '已安装接地线',
	safety_warning_signs	varchar(500) comment '已安设置安全警示标识牌',
	live_electrified_equipment	varchar(500) comment '工作地点保留带电设备',
	issuer_id	bigint(16) not null comment '签发人id',
	dev_id		bigint(16) not null comment '设备id',
	create_time bigint(16) not null comment '创建时间',
	act_state	varchar(100) not null comment '流程的状态',
	primary key (id)
)ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='第一种工作票关联业务数据表';
-- ----------------------------
-- Table structure for `ids_subarray_info_t`
-- ----------------------------
drop table if exists ids_subarray_info_t;
CREATE TABLE ids_subarray_info_t
(
	id                   BIGINT(16) NOT NULL auto_increment COMMENT '主键id',
	subarray_name           VARCHAR(255) NOT NULL COMMENT '子阵名称',
	phalanx_code            BIGINT(16) NOT NULL COMMENT '方阵编号',
	station_code         VARCHAR(32) NOT NULL COMMENT '方阵编号',
	create_time          DATETIME DEFAULT NULL COMMENT '创建时间',
	create_user_id       BIGINT(16) DEFAULT NULL COMMENT '创建人id',
	update_time          DATETIME DEFAULT NULL COMMENT '创建时间',
	update_user_id       BIGINT(16) DEFAULT NULL COMMENT '创建人id',
	primary key (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='子阵信息表';


-- ----------------------------
-- Table structure for `ids_phalanx_info_t`
-- ----------------------------
drop table if exists ids_phalanx_info_t;
CREATE TABLE ids_phalanx_info_t
(
	id                   BIGINT(16) NOT NULL auto_increment COMMENT '主键id',
	phalanx_name            VARCHAR(255) NOT NULL COMMENT '方阵名称',
	station_code         VARCHAR(32) NOT NULL COMMENT '方阵编号',
	create_time          DATETIME DEFAULT NULL COMMENT '创建时间',
	create_user_id       BIGINT(16) DEFAULT NULL COMMENT '创建人id',
	update_time          DATETIME DEFAULT NULL COMMENT '创建时间',
	update_user_id       BIGINT(16) DEFAULT NULL COMMENT '创建人id',
	primary key (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='方阵信息表';

create table ids_center_vert_detail_t
(
	id					bigint(16) not null auto_increment,
	center_vert_id		bigint(16) NOT NULL comment '集中式逆变器的id',
	dcjs_dev_id			bigint(16) NOT NULL comment '直流汇率箱的id',
	primary key (id),
	UNIQUE (dcjs_dev_id)
);

/*==============================================================*/
/* Table: ids_poverty_relief_object_t                                      */
/*==============================================================*/
create table ids_poverty_relief_object_t
(
   id                   bigint(16) not null auto_increment,
   station_code         varchar(64),
   user_name            varchar(64),
   gender               char(2),
   county               varchar(32),
   poverty_status           tinyint default 0 comment '扶贫状态：0:未完成 1:已完成',
   poverty_addr_code          varchar(246),
   contact_phone        varchar(32),
   detail_addr          varchar(128),
   create_user_id       bigint(16),
   create_time          bigint(16),
   modify_user_id       bigint(16),
   modify_time          bigint(16),
   primary key (id)
);

/*==============================================================*/
/* Table: ids_alarm_model_t                                     */
/*==============================================================*/
create table ids_alarm_model_t
(
   id                   bigint(16) not null auto_increment comment '映射建，唯一识别。用于系统内部。',
   enterprise_id        bigint(16) default NULL comment '企业编号',
   station_code         varchar(64) default NULL comment '电站编号',
   signal_version       varchar(64) not null comment '归属型号版本编号',
   alarm_id             int(11) default null comment '告警编号:点表中提供的告警编号，该编号不是由系统生成的，而是来源于点表的，稳定的字典信息',
   model_id             bigint(11) default null comment '信号点模型id',
   cause_id             int(11) default null comment '原因id:来源于点表',
   dev_type_id          smallint(6) default NULL comment '设备类型id',
   severity_id          tinyint(4) not null comment '告警级别',
   metro_unit           varchar(32) default NULL comment '计量单位:要求使用英文规范的计量单位信息',
   alarm_name           varchar(128) default NULL comment '告警名称',
   alarm_sub_name       varchar(128) default NULL,
   sig_address          bigint(16) default NULL comment '信号地址：信号地址是和具体设备相关的某一个信号的存取地址，一般的规范，比如104，modbus等都有这个概念',
   bit_index            tinyint(4) default NULL comment '信号bit位位置：有的信号会占据某一个寄存器的一个位，这个时候需要用到该信息。确定一个信号到底是取一个bit位还是取几个寄存器，需要根据信号的数据类型来确定',
   alarm_cause          varchar(2000) default NULL,
   repair_suggestion    varchar(2000) default NULL comment '修复建议',
   alarm_type           tinyint(4) default NULL comment '告警类型, 1:告警，2：时间，3：自检',
   tele_type            tinyint(4) default 2 comment '替代alarmtype 展示，遥信信号拆分为  1:变位信号  2：异常告警  3：保护事件  4：通信状态  5：告知信息',
   is_subscribed        tinyint(1) default 0 comment '是否推送',
   is_deleted           tinyint(1) default 0 comment '是否被删除，用户删除信号点模型时该列置为true',
   is_station_lev       tinyint(1) default 0 comment '是否为电站级别设置，该级别具有最高优先级，不会被托管域及二级域设置覆盖',
   source_id            tinyint(4) default 1 comment '告警来源， 一般告警，遥测告警，bit位告警等',
   update_user_id       bigint(16) default NULL comment '更新用户',
   update_date          timestamp default CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP comment '最后更新时间',
   primary key (id),
   key idx_ids_alarm_model (enterprise_id, signal_version, alarm_id, cause_id)
);

/*==============================================================*/
/* Table: ids_alarm_t                                           */
/*==============================================================*/
DROP TABLE IF EXISTS `ids_alarm_t`;
CREATE TABLE `ids_alarm_t` (
  `id` bigint(16) NOT NULL AUTO_INCREMENT,
  `alarm_name` varchar(64) DEFAULT '' COMMENT '告警名称',
  `alarm_id` int(11) NOT NULL COMMENT '告警id',
  `caused_id` int(11) DEFAULT NULL COMMENT '原因id',
  `sequence_num` int(11) DEFAULT NULL COMMENT '告警流水号',
  `enterprise_id` bigint(16) DEFAULT NULL COMMENT '企业编号',
  `station_code` varchar(64) DEFAULT NULL COMMENT '电站编号',
  `station_name` varchar(32) DEFAULT NULL COMMENT '名称:不同领域下对名称可能有不同的规范要求。名称一般要求具有唯一性',
  `last_happen_time` bigint(16) NOT NULL COMMENT '最后一次发生时间',
  `station_happen_time` bigint(16) DEFAULT NULL COMMENT '产生时间对应的电站时区时间',
  `dev_id` bigint(16) NOT NULL COMMENT '设备id',
  `dev_alias` varchar(64) DEFAULT '' COMMENT '业务编号',
  `dev_type_id` tinyint(4) DEFAULT NULL COMMENT '告警类型',
  `status_id` tinyint(4) NOT NULL COMMENT '1、未处理（活动）； 2、已确认（用户确认）； 3、处理中（转缺陷票）； 4：已处理（缺陷处理结束）； 5、已清除（用户清除）；6、已恢复（设备自动恢复）；',
  `act_id` tinyint(4) DEFAULT NULL COMMENT '直接来源于设备(1), 设备恢复(2), 遥信转告警(3), 遥信转告警(4),手动清除(5,用状态标识，已废弃)\n	, 通过工作流恢复(6,用状态标识，已废弃), 遥测告警产生(7), 遥测告警恢复(8),通过缺陷恢复(9),工作一票(10),工作二票(11) ',
  `first_happen_time` bigint(16) DEFAULT NULL COMMENT '第一次接受到告警时间',
  `confirm_time` bigint(16) DEFAULT NULL COMMENT '确认时间',
  `recover_time` bigint(16) DEFAULT NULL COMMENT '恢复时间',
  `level_id` tinyint(4) DEFAULT NULL COMMENT '告警级别 1 一般 2 严重 3 提示',
  `tele_type` tinyint(4) DEFAULT 2 COMMENT '替代alarmtype 展示，遥信信号拆分为  1:变位信号  2：异常告警  3：保护事件  4：通知状态  5：告警信息, 原有告警模型归一化为 2：异常告警',
  `alarm_type` tinyint(4) DEFAULT NULL COMMENT '告警类型：1、告警 2、事件 3、自检',
  `signal_version` varchar(32) NOT NULL COMMENT '归属型号版本编号',
  `work_flow_id` bigint(16) DEFAULT NULL COMMENT '工作流id，与缺陷相关',
  `create_time` bigint(16) DEFAULT NULL COMMENT '记录创建时间',
  `update_time` timestamp NULL DEFAULT NULL ON UPDATE current_timestamp() COMMENT '最后更新时间',
  `dev_interval` varchar(128) DEFAULT NULL COMMENT '间隔(分区子阵名称之间使用空格隔开)',
  PRIMARY KEY (`id`),
  KEY `ids_fm_alarm_idx` (`dev_id`,`alarm_id`,`caused_id`,`sequence_num`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT;

/*==============================================================*/
/* Table: ids_analysis_alarm_model_t                            */
/*==============================================================*/
DROP TABLE IF EXISTS `ids_analysis_alarm_model_t`;
CREATE TABLE `ids_analysis_alarm_model_t` (
  `alarm_id` tinyint(2) NOT NULL COMMENT '告警编号',
  `alarm_name` varchar(200) DEFAULT '' COMMENT '告警定义名称',
  `repair_suggestion` varchar(1000) DEFAULT NULL COMMENT '修复建议',
  `severity_id` tinyint(2) DEFAULT NULL COMMENT '告警级别',
  `alarm_cause` varchar(2000) DEFAULT NULL,
  PRIMARY KEY (`alarm_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*==============================================================*/
/* Table: ids_analysis_alarm_t                                  */
/*==============================================================*/
create table ids_analysis_alarm_t
(
   id                   bigint(20) not null auto_increment,
   dev_id               bigint(20) not null default 0 comment '设备id',
   station_id           char(32) default '' comment '电站ID',
   alarm_id             tinyint(2) default NULL comment '告警编号',
   alarm_pv_num         int(11) default NULL comment '故障组串序号',
   alarm_name           varchar(50) default NULL comment '告警名称',
   dev_alias             varchar(64) default NULL comment '设备名称',
   dev_type_id          int(11) default NULL comment '设备类型ID',
   dev_type_name        varchar(255) default NULL comment '设备类型名称',
   create_time          bigint(20) default NULL comment '入库时间',
   happen_time          bigint(20) default NULL comment '发生时间',
   deal_time            bigint(20) default NULL comment '处理时间',
   recovered_time       bigint(20) default NULL comment '恢复时间',
   alarm_state          tinyint(2) default NULL comment '告警状态',
   relate_table         varchar(50) default NULL comment '关联业务',
   relate_keyId         bigint(20) default NULL comment '业务表Id',
   sn_code             char(32) default NULL comment 'esn号',
   repair_suggestion    varchar(1000) default NULL comment '修复建议',
   severity_id          tinyint(2) default NULL comment '告警级别id',
   alarm_location       varchar(1000) default NULL comment '告警定位',
   primary key (id)
);

/*==============================================================*/
/* Table: ids_array_intelligentclean_t                          */
/*==============================================================*/
create table ids_array_intelligentclean_t
(
   station_name         VARCHAR (255) default NULL comment '电站名称',
   station_code         VARCHAR (32) not null comment '电站编号',
   array_name           VARCHAR (255) default NULL comment '子阵名称',
   array_code           VARCHAR (32) not null comment '子阵编号',
   capacity             DECIMAL (20, 4) default NULL comment '装机容量',
   realized_return      DECIMAL (19, 4) default NULL comment '上次清洗已获得收益',
   estimate_real_returns DECIMAL (19, 4) default NULL comment '预估实际收益',
   power_profit_thirty  DECIMAL (19, 4) default NULL comment '30天预估收益',
   power_profit_thirty_ratio DECIMAL (5, 2) default NULL comment '30天投资收益比',
   power_profit_sixty   DECIMAL (19, 4) default NULL comment '60天预估收益',
   power_profit_sixty_ratio DECIMAL (5, 2) default NULL comment '60天投资收益比',
   start_time           BIGINT (16) not null,
   end_time             BIGINT (16),
   primary key (array_code, station_code, start_time)
);

/*==============================================================*/
/* Table: ids_cleared_alarm_t                                   */
/*==============================================================*/
DROP TABLE IF EXISTS `ids_cleared_alarm_t`;
CREATE TABLE `ids_cleared_alarm_t` (
  `id` bigint(16) NOT NULL AUTO_INCREMENT,
  `alarm_name` varchar(64) DEFAULT '' COMMENT '告警名称',
  `alarm_id` int(11) NOT NULL COMMENT '告警id',
  `caused_id` int(11) DEFAULT NULL COMMENT '原因码',
  `sequence_num` int(11) DEFAULT NULL COMMENT '告警流水号',
  `enterprise_id` bigint(16) DEFAULT NULL COMMENT '企业编号',
  `station_code` varchar(64) DEFAULT NULL COMMENT '电站编号',
  `station_name` varchar(32) DEFAULT NULL COMMENT '名称:不同领域下对名称可能有不同的规范要求。名称一般要求具有唯一性',
  `last_happen_time` bigint(16) DEFAULT NULL,
  `station_happen_time` bigint(16) DEFAULT NULL COMMENT '产生时间对应的电站时区时间',
  `dev_id` bigint(16) NOT NULL COMMENT '设备id',
  `dev_alias` varchar(48) DEFAULT '' COMMENT '业务编号',
  `dev_type_id` tinyint(4) DEFAULT NULL COMMENT '告警类型',
  `status_id` tinyint(4) NOT NULL COMMENT '告警状态,活动(1), 确认(2), 清除(3),all(4), none(5);',
  `act_id` tinyint(4) DEFAULT NULL COMMENT '直接来源于设备(1), 设备恢复(2), 遥信转告警(3), 遥信转告警(4),手动清除(5), 通过工作流恢复(6); ',
  `first_happen_time` bigint(16) DEFAULT NULL COMMENT '第一次接受到告警时间',
  `confirm_time` bigint(16) DEFAULT NULL COMMENT '确认时间',
  `recover_time` bigint(16) DEFAULT NULL COMMENT '恢复时间',
  `level_id` tinyint(4) DEFAULT NULL COMMENT '告警级别',
  `tele_type` tinyint(4) DEFAULT 2 COMMENT '替代alarmtype 展示，遥信信号拆分为  1:变位信号  2：异常告警  3：保护事件  4：通知状态  5：告警信息, 原有告警模型归一化为 2：异常告警',
  `alarm_type` tinyint(4) DEFAULT NULL COMMENT '告警类型',
  `signal_version` varchar(32) NOT NULL COMMENT '归属型号版本编号',
  `work_flow_id` bigint(16) DEFAULT NULL COMMENT '工作流id，如缺陷，工作票等',
  `create_time` bigint(16) DEFAULT NULL COMMENT '记录创建时间',
  `update_time` timestamp NULL DEFAULT NULL ON UPDATE current_timestamp() COMMENT '最后更新时间',
  `dev_interval` varchar(128) DEFAULT NULL COMMENT '间隔(分区子阵名称之间使用空格隔开)',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT;

/*==============================================================*/
/* Table: ids_combiner_dc_t                                     */
/*==============================================================*/
DROP TABLE IF EXISTS `ids_combiner_dc_data_t`;
CREATE TABLE `ids_combiner_dc_data_t` (
  `dev_id` bigint(16) NOT NULL,
  `dev_name` varchar(48) DEFAULT '',
  `station_code` varchar(64) NOT NULL,
  `collect_time` bigint(16) NOT NULL,
  `valid` varchar(256) DEFAULT NULL COMMENT '判断当前指标是否有效：0代表有效，1代表无效  格式010000000000 : 代表第2位也就是bc_u无效',
  `dc_i1` decimal(10,3) DEFAULT NULL COMMENT '第1路电流值',
  `dc_i2` decimal(10,3) DEFAULT NULL,
  `dc_i3` decimal(10,3) DEFAULT NULL,
  `dc_i4` decimal(10,3) DEFAULT NULL,
  `dc_i5` decimal(10,3) DEFAULT NULL,
  `dc_i6` decimal(10,3) DEFAULT NULL,
  `dc_i7` decimal(10,3) DEFAULT NULL,
  `dc_i8` decimal(10,3) DEFAULT NULL,
  `dc_i9` decimal(10,3) DEFAULT NULL,
  `dc_i10` decimal(10,3) DEFAULT NULL,
  `dc_i11` decimal(10,3) DEFAULT NULL,
  `dc_i12` decimal(10,3) DEFAULT NULL,
  `dc_i13` decimal(10,3) DEFAULT NULL,
  `dc_i14` decimal(10,3) DEFAULT NULL,
  `dc_i15` decimal(10,3) DEFAULT NULL,
  `dc_i16` decimal(10,3) DEFAULT NULL,
  `dc_i17` decimal(10,3) DEFAULT NULL,
  `dc_i18` decimal(10,3) DEFAULT NULL,
  `dc_i19` decimal(10,3) DEFAULT NULL,
  `dc_i20` decimal(10,3) DEFAULT NULL,
  `dc_i21` decimal(10,3) DEFAULT NULL,
  `dc_i22` decimal(10,3) DEFAULT NULL,
  `dc_i23` decimal(10,3) DEFAULT NULL,
  `dc_i24` decimal(10,3) DEFAULT NULL,
  `photc_i` decimal(12,3) DEFAULT NULL COMMENT '光伏电流',
  `photc_u` decimal(12,3) DEFAULT NULL COMMENT '光伏电压',
  `temprature` decimal(12,3) DEFAULT NULL COMMENT '温度',
  `struck_count` int(11) DEFAULT NULL COMMENT '雷击次数',
  PRIMARY KEY (`collect_time`,`dev_id`,`station_code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='直流汇流箱原始数据采集表';

/*==============================================================*/
/* Table: ids_data_range_config_t                               */
/*==============================================================*/
create table ids_data_range_config_t
(
   id                   bigint(16) not null auto_increment comment '映射建，唯一识别。用于系统内部。',
   enterprise_id        bigint(16) default NULL comment '企业编号',
   station_code         varchar(64) default NULL comment '电站编号',
   model_version_code   varchar(64) not null comment '归属型号版本编号',
   sig_address          int(8) default NULL comment '信息体地址',
   start_time           varchar(64) default NULL comment '开始时间',
   end_time             varchar(64) default NULL comment '结束时间',
   higher               decimal(24,4) default NULL comment '上上限',
   high                 decimal(24,4) default NULL comment '上限',
   low                  decimal(24,4) default NULL comment '下限',
   lower                decimal(24,4) default NULL comment '下下限',
   is_alter             tinyint(1) default NULL comment '是否可修改',
   update_user_id       bigint(16) default NULL comment '最后修改该记录的用户id',
   update_date          timestamp default CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP comment '最后更新时间',
   is_first             tinyint(1) default 0 comment '是否为该信号点的第一条',
   time_zone            tinyint(4) default 8 comment '时区',
   primary key (id),
   key idx_ids_data_range_config (enterprise_id, model_version_code, sig_address)
);

alter table ids_data_range_config_t comment '数据越线配置表';

/*==============================================================*/
/* Table: ids_dc_config_t                                       */
/*==============================================================*/
create table ids_dc_config_t
(
   id                   bigint(16) unsigned not null auto_increment,
   dev_id               bigint(16) unsigned not null comment '设备ID',
   channel_type         tinyint(4) unsigned default NULL comment '通道类型 1: TCP 客户端；2: TCP服务端',
   ip                   varchar(45) default NULL,
   port                 int(10) unsigned default NULL,
   logical_addres       tinyint(4) unsigned default NULL comment '逻辑地址',
   primary key (id)
);

alter table ids_dc_config_t comment '采集器配置';

/*==============================================================*/
/* Table: ids_dev_info_t                                        */
/*==============================================================*/

CREATE TABLE `ids_dev_info_t` (
  `id` bigint(16) NOT NULL AUTO_INCREMENT COMMENT '设备id',
  `dev_name` varchar(64) DEFAULT '' COMMENT '设备名称',
  `dev_alias` varchar(64) DEFAULT '' COMMENT '设备别名',
  `station_code` varchar(64) DEFAULT NULL COMMENT '电站编码',
  `enterprise_id` bigint(16) DEFAULT NULL COMMENT '企业编码',
  `signal_version` varchar(32) DEFAULT '' COMMENT '点表版本，唯一标识',
  `parent_signal_version_id` bigint(16) DEFAULT NULL COMMENT '主设备信号版本id',
  `dev_type_id` int(8) DEFAULT NULL COMMENT '设备类型id',
  `parent_id` bigint(16) DEFAULT NULL COMMENT '父设备id，针对通管机下挂类型',
  `parent_sn` varchar(32) DEFAULT NULL COMMENT '父设备esn',
  `related_dev_id` bigint(16) DEFAULT NULL COMMENT '关联设备id',
  `matrix_id` bigint(16) DEFAULT NULL COMMENT '子阵id',
  `phalanx_id` bigint(16) DEFAULT NULL COMMENT '方阵id',
  `sn_code` varchar(32) DEFAULT NULL COMMENT 'esn号',
  `pn_code` varchar(32) DEFAULT NULL COMMENT 'pn码,生产制造厂商',
  `kks_code` varchar(32) DEFAULT NULL COMMENT 'kks编码，在地面站中有用到',
  `ne_version` varchar(32) DEFAULT '' COMMENT '网元版本号',
  `dev_ip` varchar(32) DEFAULT '' COMMENT '设备ip',
  `dev_port` int(4) DEFAULT NULL COMMENT '端口号',
  `linked_host` varchar(32) DEFAULT '' COMMENT '接入服务器的host，针对分布式场景到字段',
  `second_address` int(4) DEFAULT NULL COMMENT '二级地址,通常在交互报文中带有此地址',
  `protocol_code` varchar(20) DEFAULT '' COMMENT '协议编码:104 103 hwmodbus',
  `longitude` decimal(10,6) DEFAULT NULL COMMENT '经度',
  `latitude` decimal(10,6) DEFAULT NULL COMMENT '纬度',
  `is_logic_delete` tinyint(1) DEFAULT 0 COMMENT '设备是否被逻辑删除 0表示否',
  `old_sn` varchar(32) DEFAULT '' COMMENT '记录设备替换时换掉的设备esn',
  `create_date` datetime DEFAULT NULL COMMENT '创建日期',
  `modified_date` datetime DEFAULT NULL COMMENT '修改日期',
  `is_monitor_dev` varchar(1) DEFAULT '0' COMMENT '设备是否来源于监控 1:监控导入设备的 ；0：集维侧的设备',
  `upgrade_file_name` varchar(255) DEFAULT NULL COMMENT '升级的文件名称',
  `upgrade_status` int(3) DEFAULT 0 COMMENT '设备升级状态 0:未上传, 1:文件上传, 2:升级中,  3：升级成功,  -1：升级失败',
  `upgrade_process` int(3) DEFAULT 0 COMMENT '设备升级进度 0~100 升级失败-1',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='设备表';

/*==============================================================*/
/* Table: ids_device_pv_module_t                                */
/*==============================================================*/
create table ids_device_pv_module_t
(
   dev_id            bigint(16) not null comment '设备id',
   station_code         varchar(64) default NULL comment '电站编号',
   fixed_power          numeric(10,5) default NULL comment '组串容量(Wp)',
   sn_code             varchar(32) default NULL comment '设备esn号',
   pv_index             int not null comment 'pv下标，如1,2,..20',
   pv_model_id          bigint(16) comment '组件信息编号，关联组件信息',
   modules_num_per_string int default 0 comment '组串组件串联数量(块)[0~50]',
   module_production_date bigint default NULL comment '组件投产日期',
   create_time          bigint default NULL comment '创建时间',
   update_time          bigint default NULL comment '修改时间',
   is_default           varchar(100),
   primary key (dev_id, pv_index)
);

/*==============================================================*/
/* Table: ids_district_t                                        */
/*==============================================================*/
create table ids_district_t
(
   id                   smallint (5) not null,
   name                 varchar (270),
   parent_id            smallint (5),
   `initial`              char (3),
   initials             varchar (30),
   pinyin               varchar (600),
   extra                varchar (60),
   suffix               varchar (15),
   code                 char (30),
   area_code            varchar (30),
   cuntry_code          varchar(16) default NULL comment '用于区分国家',
   `order`              tinyint (2),
   primary key (id)
);

/*==============================================================*/
/* Table: ids_emi_data_t                                     */
/*==============================================================*/
DROP TABLE IF EXISTS `ids_emi_data_t`;
CREATE TABLE `ids_emi_data_t` (
  `dev_id` bigint(16) NOT NULL,
  `dev_name` varchar(48) DEFAULT '',
  `collect_time` bigint(16) NOT NULL,
  `station_code` char(32) NOT NULL,
  `valid` varchar(256) DEFAULT NULL COMMENT '判断当前指标是否有效：0代表有效，1代表无效  格式010000000000 : 代表第2位无效',
  `wind_speed` decimal(9,3) DEFAULT NULL COMMENT '风速',
  `wind_direction` decimal(9,3) DEFAULT NULL COMMENT '风向',
  `pv_temperature` decimal(8,3) DEFAULT NULL COMMENT 'pv温度',
  `temperature` decimal(8,3) DEFAULT NULL COMMENT '温度',
  `irradiation_intensity` decimal(12,3) DEFAULT NULL COMMENT '辐照强度',
  `total_irradiation` decimal(12,3) DEFAULT NULL COMMENT '总辐射量',
  `horiz_irradiation_intensity` decimal(12,3) DEFAULT NULL COMMENT '水平辐照强度',
  `horiz_total_irradiation` decimal(12,3) DEFAULT NULL COMMENT '水平辐照量',
  PRIMARY KEY (`collect_time`,`dev_id`,`station_code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='环境监测仪原始数据采集表';

/*==============================================================*/
/* Table: ids_file_manager_t                                    */
/*==============================================================*/
create table ids_file_manager_t
(
   file_id              VARCHAR(32) not null comment '文件id',
   file_name            varchar(255) not null comment '文件名称',
   file_ext             varchar(500) default NULL comment '文件后缀',
   file_mime            varchar(500) default NULL comment '文件类型',
   original_name        varchar(500) default NULL comment '文件类型',
   primary key (file_id)
);

/*==============================================================*/
/* Table: ids_inverter_conc_data_t                                   */
/*==============================================================*/
DROP TABLE IF EXISTS `ids_inverter_conc_data_t`;
CREATE TABLE `ids_inverter_conc_data_t` (
  `collect_time` bigint(16) NOT NULL,
  `station_code` varchar(64) NOT NULL,
  `dev_id` bigint(16) NOT NULL COMMENT '设备唯一标识',
  `dev_name` varchar(48) DEFAULT '',
  `inverter_state` int(11) DEFAULT NULL COMMENT '0x0000:待机：初始化;idle:initializing 0x0001:待机：绝缘阻抗检测;idle:iso detecting 0x0002:待机：光照检测;idle:irradiation detecting 0x0100:启动;starting 0x0200:并网;on-grid 0x0201:并网:限功率;on-grid:limited 0x0300:关机：异常关机; shutdown:abnormal 0x0301:关机：指令关机shutdown:forced 0x0401：电网调度：cosψ-p 曲线;grid dispatch:cosψ-p curve 0x0402：电网调度：q-u 曲线;grid dispatch:q-u curve 0xa000:待机：无光照；idle:no irradiation',
  `valid` varchar(256) DEFAULT NULL COMMENT '判断当前指标是否有效：0代表有效，1代表无效  格式010000000000 : 代表第2位也就是bc_u无效',
  `day_capacity` decimal(12,3) DEFAULT NULL COMMENT '当日发电量',
  `total_capacity` decimal(12,3) DEFAULT NULL COMMENT '累计发电量',
  `temperature` decimal(7,3) DEFAULT NULL COMMENT '机内温度',
  `center_u` decimal(8,3) DEFAULT NULL COMMENT '直流电压',
  `center_i` decimal(8,3) DEFAULT NULL COMMENT '直流电流',
  `center_i_1` decimal(8,3) DEFAULT NULL COMMENT '第1路电流值',
  `center_i_2` decimal(8,3) DEFAULT NULL,
  `center_i_3` decimal(8,3) DEFAULT NULL,
  `center_i_4` decimal(8,3) DEFAULT NULL,
  `center_i_5` decimal(8,3) DEFAULT NULL,
  `center_i_6` decimal(8,3) DEFAULT NULL,
  `center_i_7` decimal(8,3) DEFAULT NULL,
  `center_i_8` decimal(8,3) DEFAULT NULL,
  `center_i_9` decimal(8,3) DEFAULT NULL,
  `center_i_10` decimal(8,3) DEFAULT NULL,
  `center_i_11` decimal(8,3) DEFAULT NULL,
  `center_i_12` decimal(8,3) DEFAULT NULL,
  `center_i_13` decimal(8,3) DEFAULT NULL,
  `center_i_14` decimal(8,3) DEFAULT NULL,
  `center_i_15` decimal(8,3) DEFAULT NULL,
  `center_i_16` decimal(8,3) DEFAULT NULL,
  `center_i_17` decimal(8,3) DEFAULT NULL,
  `center_i_18` decimal(8,3) DEFAULT NULL,
  `center_i_19` decimal(8,3) DEFAULT NULL,
  `center_i_20` decimal(8,3) DEFAULT NULL,
  `center_i_21` decimal(8,3) DEFAULT NULL,
  `center_i_22` decimal(8,3) DEFAULT NULL,
  `center_i_23` decimal(8,3) DEFAULT NULL,
  `center_i_24` decimal(8,3) DEFAULT NULL,
  `mppt_power` decimal(12,3) DEFAULT NULL COMMENT '直流输入功率',
  `a_u` decimal(9,3) DEFAULT NULL COMMENT 'a箱电压',
  `b_u` decimal(9,3) DEFAULT NULL,
  `c_u` decimal(9,3) DEFAULT NULL,
  `a_i` decimal(9,3) DEFAULT NULL COMMENT '电网a箱电流',
  `b_i` decimal(9,3) DEFAULT NULL,
  `c_i` decimal(9,3) DEFAULT NULL,
  `power_factor` decimal(5,3) DEFAULT NULL COMMENT '功率因数',
  `grid_frequency` decimal(7,3) DEFAULT NULL COMMENT '电网频率',
  `active_power` decimal(9,3) DEFAULT NULL COMMENT '交流输出功率',
  `reactive_power` decimal(9,3) DEFAULT NULL COMMENT '输出无功功率',
  `on_time` bigint(16) DEFAULT NULL COMMENT '逆变器开机时间',
  `off_time` bigint(16) DEFAULT NULL COMMENT '逆变器关机时间',
  `aop` varchar(4) DEFAULT NULL COMMENT '生产可靠性',
  `ext1` decimal(12,3) DEFAULT NULL COMMENT '预留字段1',
  `ext2` decimal(12,3) DEFAULT NULL COMMENT '预留字段2',
  `ext_str_1` varchar(255) DEFAULT NULL COMMENT '预留字段3',
  PRIMARY KEY (`collect_time`,`dev_id`,`station_code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='集中式逆变器原始数据采集表';
/*==============================================================*/
/* Table: ids_inverter_intelligentclean_h                       */
/*==============================================================*/
create table ids_inverter_intelligentclean_h
(
   station_name         VARCHAR (255) default NULL comment '电站名称',
   station_code         VARCHAR (32) not null comment '电站编号',
   array_name           VARCHAR (255) default NULL comment '子阵名称',
   array_code           VARCHAR (32) not null comment '子阵编号',
   dev_id            BIGINT (16) not null comment '设备id',
   capacity             DECIMAL (20, 4) default NULL comment '装机容量',
   base_pr              DECIMAL (7, 3) default NULL comment '基准效率',
   current_pr           DECIMAL (7, 3) default NULL comment '当前效率',
   power_profit_thirty  DECIMAL (19, 4) default NULL comment '30天预估收益',
   power_profit_thirty_ratio DECIMAL (4, 2) default NULL comment '30天投资收益比',
   power_profit_sixty   DECIMAL (19, 4) default NULL comment '60天预估收益',
   power_profit_sixty_ratio DECIMAL (4, 2) default NULL comment '60天投资收益比',
   start_time           BIGINT (16) not null,
   end_time             BIGINT (16),
   primary key (dev_id, station_code, start_time)
);

/*==============================================================*/
/* Table: ids_inverter_intelligentclean_t                       */
/*==============================================================*/
create table ids_inverter_intelligentclean_t
(
   station_name         VARCHAR (255) default NULL comment '电站名称',
   station_code         VARCHAR (32) not null comment '电站编号',
   array_name           VARCHAR (255) default NULL comment '子阵名称',
   array_code           VARCHAR (32) not null comment '子阵编号',
   dev_id            BIGINT (16) not null comment '设备id',
   capacity             DECIMAL (20, 4) default NULL comment '装机容量',
   base_pr              DECIMAL (7, 3) default NULL comment '基准效率',
   current_pr           DECIMAL (7, 3) default NULL comment '当前效率',
   power_profit_thirty  DECIMAL (19, 4) default NULL comment '30天预估收益',
   power_profit_thirty_ratio DECIMAL (4, 2) default NULL comment '30天投资收益比',
   power_profit_sixty   DECIMAL (19, 4) default NULL comment '60天预估收益',
   power_profit_sixty_ratio DECIMAL (4, 2) default NULL comment '60天投资收益比',
   start_time           BIGINT (16) not null,
   end_time             BIGINT (16),
   primary key (dev_id, station_code, start_time)
);

/*==============================================================*/
/* Table: ids_inverter_string_data_t                                 */
/*==============================================================*/
DROP TABLE IF EXISTS `ids_inverter_string_data_t`;
CREATE TABLE `ids_inverter_string_data_t` (
  `dev_id` bigint(16) NOT NULL COMMENT '设备唯一标识',
  `dev_name` varchar(32) DEFAULT '',
  `station_code` varchar(64) NOT NULL,
  `collect_time` bigint(16) NOT NULL,
  `valid` varchar(256) DEFAULT '' COMMENT '判断当前指标是否有效：0代表有效，1代表无效  格式010000000000 : 代表第2位也就是bc_u无效',
  `ab_u` decimal(9,3) DEFAULT NULL COMMENT '电网ab电压',
  `bc_u` decimal(9,3) DEFAULT NULL,
  `ca_u` decimal(9,3) DEFAULT NULL,
  `a_u` decimal(9,3) DEFAULT NULL COMMENT 'a箱电压',
  `b_u` decimal(9,3) DEFAULT NULL,
  `c_u` decimal(9,3) DEFAULT NULL,
  `a_i` decimal(9,3) DEFAULT NULL COMMENT '电网a箱电流',
  `b_i` decimal(9,3) DEFAULT NULL,
  `c_i` decimal(9,3) DEFAULT NULL,
  `pv1_u` decimal(8,3) DEFAULT NULL COMMENT 'pv1输入电压',
  `pv2_u` decimal(8,3) DEFAULT NULL,
  `pv3_u` decimal(8,3) DEFAULT NULL,
  `pv4_u` decimal(8,3) DEFAULT NULL,
  `pv5_u` decimal(8,3) DEFAULT NULL,
  `pv6_u` decimal(8,3) DEFAULT NULL,
  `pv7_u` decimal(8,3) DEFAULT NULL,
  `pv8_u` decimal(8,3) DEFAULT NULL,
  `pv9_u` decimal(8,3) DEFAULT NULL,
  `pv10_u` decimal(8,3) DEFAULT NULL,
  `pv11_u` decimal(8,3) DEFAULT NULL,
  `pv12_u` decimal(8,3) DEFAULT NULL,
  `pv13_u` decimal(8,3) DEFAULT NULL,
  `pv14_u` decimal(8,3) DEFAULT NULL,
  `pv15_u` decimal(8,3) DEFAULT NULL,
  `pv16_u` decimal(8,3) DEFAULT NULL,
  `pv17_u` decimal(8,3) DEFAULT NULL,
  `pv18_u` decimal(8,3) DEFAULT NULL,
  `pv19_u` decimal(8,3) DEFAULT NULL,
  `pv20_u` decimal(8,3) DEFAULT NULL,
  `pv21_u` decimal(8,3) DEFAULT NULL,
  `pv22_u` decimal(8,3) DEFAULT NULL,
  `pv23_u` decimal(8,3) DEFAULT NULL,
  `pv24_u` decimal(8,3) DEFAULT NULL,
  `pv1_i` decimal(8,3) DEFAULT NULL COMMENT 'pv1输入电流',
  `pv2_i` decimal(8,3) DEFAULT NULL,
  `pv3_i` decimal(8,3) DEFAULT NULL,
  `pv4_i` decimal(8,3) DEFAULT NULL,
  `pv5_i` decimal(8,3) DEFAULT NULL,
  `pv6_i` decimal(8,3) DEFAULT NULL,
  `pv7_i` decimal(8,3) DEFAULT NULL,
  `pv8_i` decimal(8,3) DEFAULT NULL,
  `pv9_i` decimal(8,3) DEFAULT NULL,
  `pv10_i` decimal(8,3) DEFAULT NULL,
  `pv11_i` decimal(8,3) DEFAULT NULL,
  `pv12_i` decimal(8,3) DEFAULT NULL,
  `pv13_i` decimal(8,3) DEFAULT NULL,
  `pv14_i` decimal(8,3) DEFAULT NULL,
  `pv15_i` decimal(8,3) DEFAULT NULL,
  `pv16_i` decimal(8,3) DEFAULT NULL,
  `pv17_i` decimal(8,3) DEFAULT NULL,
  `pv18_i` decimal(8,3) DEFAULT NULL,
  `pv19_i` decimal(8,3) DEFAULT NULL,
  `pv20_i` decimal(8,3) DEFAULT NULL,
  `pv21_i` decimal(8,3) DEFAULT NULL,
  `pv22_i` decimal(8,3) DEFAULT NULL,
  `pv23_i` decimal(8,3) DEFAULT NULL,
  `pv24_i` decimal(8,3) DEFAULT NULL,
  `inverter_state` int(11) DEFAULT NULL COMMENT '0x0000:待机：初始化;idle:initializing 0x0001:待机：绝缘阻抗检测;idle:iso detecting 0x0002:待机：光照检测;idle:irradiation detecting 0x0100:启动;starting 0x0200:并网;on-grid 0x0201:并网:限功率;on-grid:limited 0x0300:关机：异常关机; shutdown:abnormal 0x0301:关机：指令关机shutdown:forced 0x0401：电网调度：cosψ-p 曲线;grid dispatch:cosψ-p curve 0x0402：电网调度：q-u 曲线;grid dispatch:q-u curve 0xa000:待机：无光照；idle:no irradiation',
  `conversion_efficiency` decimal(7,3) DEFAULT NULL COMMENT '逆变器转换效率(厂家)',
  `temperature` decimal(7,3) DEFAULT NULL COMMENT '机内温度',
  `power_factor` decimal(5,3) DEFAULT NULL COMMENT '功率因数',
  `grid_frequency` decimal(7,3) DEFAULT NULL COMMENT '电网频率',
  `active_power` decimal(9,3) DEFAULT NULL COMMENT '有功功率',
  `reactive_power` decimal(9,3) DEFAULT NULL COMMENT '输出无功功率',
  `day_capacity` decimal(12,3) DEFAULT NULL COMMENT '当日发电量',
  `mppt_power` decimal(9,3) DEFAULT NULL COMMENT 'mppt输入总功率',
  `total_capacity` decimal(12,3) DEFAULT NULL COMMENT '累计发电量',
  `on_time` bigint(16) DEFAULT NULL COMMENT '逆变器开机时间',
  `off_time` bigint(16) DEFAULT NULL COMMENT '逆变器关机时间',
  `mppt_total_cap` decimal(12,3) DEFAULT NULL COMMENT '直流输入总电量',
  `mppt_1_cap` decimal(12,3) DEFAULT NULL COMMENT 'mppt1直流累计发电量',
  `mppt_2_cap` decimal(12,3) DEFAULT NULL,
  `mppt_3_cap` decimal(12,3) DEFAULT NULL,
  `mppt_4_cap` decimal(12,3) DEFAULT NULL,
  `dev_sn` varchar(64) DEFAULT NULL COMMENT '设备sn号',
  `ext1` decimal(12,3) DEFAULT NULL COMMENT '预留字段1',
  `ext2` decimal(12,3) DEFAULT NULL COMMENT '预留字段2',
  `ext3` decimal(12,3) DEFAULT NULL COMMENT '预留字段3',
  `ext4` decimal(12,3) DEFAULT NULL COMMENT '预留字段4',
  `ext5` varchar(255) DEFAULT '' COMMENT '预留字段5',
  PRIMARY KEY (`collect_time`,`dev_id`,`station_code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='组串逆变器原始数据采集表';

/*==============================================================*/
/* Table: ids_kpi_day_combinerdc_t                             */
/*==============================================================*/
create table ids_kpi_day_combinerdc_t
(
   collect_time         bigint(16) not null comment '采集时间',
   station_code         varchar(64) not null comment '电站编号',
   device_id            bigint(16) not null comment '设备编号',
   dev_name          varchar(255) default null comment '设备名称',
   discrete_rate        decimal(6,2) default null comment '组串离散率',
   equivalent_hour      decimal(12,2) default null comment '等效利用小时',
   pv1_avg_i            decimal(12,3) default null comment '组串1平均电流',
   pv2_avg_i            decimal(12,3) default null,
   pv3_avg_i            decimal(12,3) default null,
   pv4_avg_i            decimal(12,3) default null,
   pv5_avg_i            decimal(12,3) default null,
   pv6_avg_i            decimal(12,3) default null,
   pv7_avg_i            decimal(12,3) default null,
   pv8_avg_i            decimal(12,3) default null,
   pv9_avg_i            decimal(12,3) default null,
   pv10_avg_i           decimal(12,3) default null,
   pv11_avg_i           decimal(12,3) default null,
   pv12_avg_i           decimal(12,3) default null,
   pv13_avg_i           decimal(12,3) default null,
   pv14_avg_i           decimal(12,3) default null,
   pv15_avg_i           decimal(12,3) default null,
   pv16_avg_i           decimal(12,3) default null,
   pv17_avg_i           decimal(12,3) default null,
   pv18_avg_i           decimal(12,3) default null,
   pv19_avg_i           decimal(12,3) default null,
   pv20_avg_i          decimal(12,3) default null,
   avg_u                decimal(12,3) default null comment '平均电压',
   statistics_time      bigint(16),
   primary key (collect_time, station_code, device_id)
);

/*==============================================================*/
/* Table: ids_kpi_hour_emi_t                            */
/*==============================================================*/
create table ids_kpi_hour_emi_t
(
   collect_time         bigint(16) not null comment '采集时间',
   station_code         varchar(64) not null comment '电站编号',
   device_id            bigint(16) not null comment '设备编号',
   dev_name          varchar(255) default null comment '设备名称',
   temperature          decimal(6,3) default null comment '温度',
   pv_temperature       decimal(6,3) default null comment '组串温度',
   wind_speed           decimal(9,3) default null comment '风速',
   total_radiant        decimal(12,4) default null comment '总辐照量',
   horiz_radiant        decimal(12,4) default null comment '水平辐照量',
   max_radiant_point    decimal(12,4) default null comment '最大瞬时辐照',
   min_radiant_point    decimal(12,4) default null comment '最小瞬时辐照',
   statistics_time      bigint(16) default null comment '统计时间',
   primary key (collect_time, station_code, device_id)
);

/*==============================================================*/
/* Table: ids_kpi_day_inverter_t                                */
/*==============================================================*/
create table ids_kpi_day_inverter_t
(
   collect_time         bigint(16) not null comment '采集时间',
   station_code         varchar(64) not null comment '电站编号',
   device_id            bigint(16) not null comment '设备编号',
   dev_name          varchar(255) not null comment '设备名称',
   inverter_type        tinyint not null comment '逆变器类型：根据设备类型表定义确定',
   real_capacity        decimal(12,5) default null comment '实际运行容量',
   product_power        decimal(12,5) default null comment '发电量',
   discrete_rate        decimal(6,2) default null comment '组串离散率',
   equivalent_hour      decimal(12,2) default null comment '等效利用小时',
   peak_power           decimal(12,3) default null comment '峰值功率',
   efficiency           decimal(12,3) default null comment '逆变器转换效率',
   yield_deviation      decimal(12,3) default null comment '生产偏差',
   aoc_ratio            decimal(6,3) default null comment '生产可靠度',
   aop_ratio            decimal(6,3) default null comment '通信可靠度',
   pv1_avg_i            decimal(12,3) default null comment '组串1平均电流',
   pv2_avg_i            decimal(12,3) default null,
   pv3_avg_i            decimal(12,3) default null,
   pv4_avg_i            decimal(12,3) default null,
   pv5_avg_i            decimal(12,3) default null,
   pv6_avg_i            decimal(12,3) default null,
   pv7_avg_i            decimal(12,3) default null,
   pv8_avg_i            decimal(12,3) default null,
   pv9_avg_i            decimal(12,3) default null,
   pv10_avg_i           decimal(12,3) default null,
   pv11_avg_i           decimal(12,3) default null,
   pv12_avg_i           decimal(12,3) default null,
   pv13_avg_i           decimal(12,3) default null,
   pv14_avg_i           decimal(12,3) default null,
   pv1_avg_u            decimal(12,3) default null comment '组串1平均电压',
   pv2_avg_u            decimal(12,3) default null,
   pv3_avg_u            decimal(12,3) default null,
   pv4_avg_u            decimal(12,3) default null,
   pv5_avg_u            decimal(12,3) default null,
   pv6_avg_u            decimal(12,3) default null,
   pv7_avg_u            decimal(12,3) default null,
   pv8_avg_u            decimal(12,3) default null,
   pv9_avg_u            decimal(12,3) default null,
   pv10_avg_u           decimal(12,3) default null,
   pv11_avg_u           decimal(12,3) default null,
   pv12_avg_u           decimal(12,3) default null,
   pv13_avg_u           decimal(12,3) default null,
   pv14_avg_u           decimal(12,3) default null,
   statistics_time      bigint(16) default null comment '统计时间',
   primary key (collect_time, station_code, device_id)
);

/*==============================================================*/
/* Table: ids_kpi_hour_inverter_t                               */
/*==============================================================*/
create table ids_kpi_hour_inverter_t
(
   collect_time         bigint(16) not null comment '采集时间',
   station_code         varchar(64) not null comment '电站编号',
   device_id            bigint(16) not null comment '设备编号',
   dev_name          varchar(255) default null comment '设备名称',
   inverter_type        tinyint default null comment '逆变器类型：根据设备类型表定义确定',
   product_power        decimal(12,3) default null comment '当前小时发电量',
   total_power          decimal(12,3) default null comment '最近一次有效发电量',
   peak_power           decimal(12,3) default null comment '峰值功率',
   efficiency           decimal(12,3) default null comment '逆变器转换效率',
   aoc_conn_num         char(5) default null comment 'aoc有效连接数',
   aop_num_zero         char(5) default null comment '能量质量评价指标',
   aop_num_one          char(5) default null comment '能量质量衡量指标',
   statistics_time      bigint(16),
   primary key (collect_time, station_code, device_id)
);

/*==============================================================*/
/* Table: ids_kpi_month_inverter_t                              */
/*==============================================================*/
create table ids_kpi_month_inverter_t
(
   collect_time         bigint(16) not null comment '采集时间',
   station_code         varchar(64) not null comment '电站编号',
   device_id            bigint(16) not null comment '设备编号',
   dev_name          varchar(255) default null comment '设备名称',
   inverter_type        tinyint default null comment '逆变器类型：根据设备类型表定义确定',
   real_capacity        decimal(12,5) default null comment '实际运行容量',
   product_power        decimal(12,5) default null comment '发电量',
   equivalent_hour      decimal(12,2) default null comment '等效利用小时',
   peak_power           decimal(12,3) default null comment '峰值功率',
   efficiency           decimal(12,3) default null comment '逆变器转换效率',
   statistics_time      bigint(16) default null comment '统计时间',
   primary key (collect_time, station_code, device_id)
);

/*==============================================================*/
/* Table: ids_kpi_year_inverter_t                               */
/*==============================================================*/
create table ids_kpi_year_inverter_t
(
   collect_time         bigint(16) not null comment '采集时间',
   station_code         varchar(64) not null comment '电站编号',
   device_id            bigint(16) not null comment '设备编号',
   dev_name          varchar(255) default null comment '设备名称',
   inverter_type        tinyint default null comment '逆变器类型：根据设备类型表定义确定',
   real_capacity        decimal(12,5) default null comment '实际运行容量',
   product_power        decimal(12,5) default null comment '发电量',
   equivalent_hour      decimal(12,2) default null comment '等效利用小时',
   peak_power           decimal(12,3) default null comment '峰值功率',
   efficiency           decimal(12,3) default null comment '逆变器转换效率',
   statistics_time      bigint(16) default null comment '统计时间',
   primary key (collect_time, station_code, device_id)
);

/*==============================================================*/
/* Table: ids_kpi_day_meter_t                                   */
/*==============================================================*/
create table ids_kpi_day_meter_t
(
   collect_time         bigint(16) not null,
   station_code         varchar(64) not null,
   device_id            bigint(16) not null,
   dev_name          varchar(255) default null,
   meter_type           tinyint default null comment '电表类型',
   ongrid_power         decimal(12,3) default null comment '上网电量(输出电量)',
   buy_power            decimal(12,3) default null comment '网馈电量(反向输入电量)',
   statistics_time      bigint(16) default null,
   primary key (collect_time, station_code, device_id)
);

/*==============================================================*/
/* Table: ids_kpi_hour_meter_t                                  */
/*==============================================================*/
create table ids_kpi_hour_meter_t
(
   collect_time         bigint(16) not null,
   station_code         varchar(64) not null,
   device_id            bigint(16) not null,
   dev_name          varchar(255) default null,
   meter_type           tinyint default null comment '电表类型：关口表',
   ongrid_power         decimal(12,3) default null comment '关口表输出电量（上网电量）',
   buy_power            decimal(12,3) default null comment '网馈电量(反向输入电量)',
   valid_product_power  decimal(12,3) default null comment '有效发电量',
   valid_consume_power  decimal(12,3) default null comment '有效用电量',
   statistics_time      bigint(16) default null comment '统计时间',
   primary key (collect_time, station_code, device_id)
);

/*==============================================================*/
/* Table: ids_kpi_month_meter_t                                 */
/*==============================================================*/
create table ids_kpi_month_meter_t
(
   collect_time         bigint(16) not null,
   station_code         varchar(64) not null,
   device_id            bigint(16) not null,
   dev_name          varchar(255) default null,
   meter_type           tinyint default null,
   ongrid_power         decimal(12,3) default null,
   buy_power            decimal(12,3) default null comment '网馈电量(反向输入电量)',
   statistics_time      bigint(16) default null,
   primary key (collect_time, station_code, device_id)
);

/*==============================================================*/
/* Table: ids_kpi_year_meter_t                                  */
/*==============================================================*/
create table ids_kpi_year_meter_t
(
   collect_time         bigint(16) not null,
   station_code         varchar(64) not null,
   device_id            bigint(16) not null,
   dev_name          varchar(255) default null,
   meter_type           tinyint default null,
   ongrid_power         decimal(12,3) default null,
   buy_power            decimal(12,3) default null comment '网馈电量(反向输入电量)',
   statistics_time      bigint(16) default null,
   primary key (collect_time, station_code, device_id)
);

/*==============================================================*/
/* Table: ids_kpi_day_station_t                                 */
/*==============================================================*/
create table ids_kpi_day_station_t
(
   collect_time         bigint(16) not null comment '采集时间',
   station_code         varchar(64) not null comment '电站编号',
   enterprise_id        bigint(16) not null comment '区域编号',
   real_capacity        decimal(16,5) default null comment '实际运行容量',
   radiation_intensity  decimal(16,4) default null comment '辐照量',
   theory_power         decimal(12,3) default null comment '理论发电量',
   product_power        decimal(12,3) default null comment '发电量',
   ongrid_power         decimal(12,3) default null comment '上网电量（关口表输出电量）',
   buy_power            decimal(12,3) default null comment '网馈电量(反向输入电量)',
   self_use_power       decimal(12,3) default null comment '自发自用电量',
   consume_power        decimal(12,3) default null comment '综合用电量（总用电量）',
   limit_loss_power     decimal(12,3) default null comment '限电损失电量',
   trouble_loss_power   decimal(12,3) default null comment '故障损失电量',
   performance_ratio    decimal(7,3) default null comment '转换效率',
   equivalent_hour      decimal(12,2) default null comment '等效利用小时',
   power_profit         decimal(19,4) default null comment '收益',
   co2_reduction        decimal(12,3) default null comment '二氧化碳减排量',
   coal_reduction       decimal(12,3) default null comment '标准煤节约',
   tree_reduction       bigint(16) default null comment '等效植树棵数',
   statistic_time       bigint(16) default null comment '统计时间',
   primary key (collect_time, station_code)
);

/*==============================================================*/
/* Table: ids_kpi_hour_station_t                                */
/*==============================================================*/
create table ids_kpi_hour_station_t
(
   collect_time         bigint(16) not null comment '采集时间',
   station_code         varchar(64) not null comment '电站编号',
   enterprise_id        bigint(16) not null comment '区域编号',
   radiation_intensity  decimal(16,4) default null comment '辐照量',
   theory_power         decimal(12,3) default null comment '理论发电量',
   product_power        decimal(12,3) default null comment '发电量',
   ongrid_power         decimal(12,3) default null comment '上网电量（关口表输出电量）',
   buy_power            decimal(12,3) default null comment '网馈电量(反向输入电量)',
   self_use_power       decimal(12,3) default null comment '自发自用电量',
   consume_power        decimal(12,3) default null comment '综合用电量（总用电量）',
   limit_loss_power     decimal(12,3) default null comment '限电损失电量',
   trouble_loss_power   decimal(12,3) default null comment '故障损失电量',
   performance_ratio    decimal(7,3) default null comment '转换效率',
   power_profit         decimal(19,4) default null comment '收益',
   statistic_time       bigint(16) default null comment '统计时间',
   primary key (collect_time, station_code)
);

/*==============================================================*/
/* Table: ids_kpi_month_station_t                               */
/*==============================================================*/
create table ids_kpi_month_station_t
(
   collect_time         bigint(16) not null comment '采集时间',
   station_code         varchar(64) not null comment '电站编号',
   enterprise_id        bigint(16) not null comment '区域编号',
   real_capacity        decimal(16,5) default null comment '实际运行容量',
   radiation_intensity  decimal(16,4) default null comment '辐照量',
   theory_power         decimal(12,3) default null comment '理论发电量',
   product_power        decimal(12,3) default null comment '总发电量',
   ongrid_power         decimal(12,3) default null comment '上网电量（关口表输出电量）',
   buy_power            decimal(12,3) default null comment '网馈电量(反向输入电量)',
   self_use_power       decimal(12,3) default null comment '自发自用电量',
   consume_power        decimal(12,3) default null comment '综合用电量（总用电量）',
   limit_loss_power     decimal(12,3) default null comment '限电损失电量',
   trouble_loss_power   decimal(12,3) default null comment '故障损失电量',
   performance_ratio    decimal(7,3) default null comment '转换效率',
   equivalent_hour      decimal(12,2) default null comment '等效利用小时',
   power_profit         decimal(19,4) default null comment '收益',
   co2_reduction        decimal(12,3) default null comment '二氧化碳减排量',
   coal_reduction       decimal(12,3) default null comment '标准煤节约',
   tree_reduction       bigint(16) default null comment '等效植树棵数',
   statistic_time       bigint(16) default null comment '统计时间',
   primary key (collect_time, station_code)
);

/*==============================================================*/
/* Table: ids_kpi_year_station_t                                */
/*==============================================================*/
create table ids_kpi_year_station_t
(
   collect_time         bigint(16) not null comment '采集时间',
   station_code         varchar(64) not null comment '电站编号',
   enterprise_id        bigint(16) not null comment '区域编号',
   real_capacity        decimal(16,5) default null comment '实际运行容量',
   radiation_intensity  decimal(16,4) default null comment '辐照量',
   theory_power         decimal(12,3) default null comment '理论发电量',
   product_power        decimal(12,3) default null comment '发电量',
   ongrid_power         decimal(12,3) default null comment '上网电量（关口表输出电量）',
   buy_power            decimal(12,3) default null comment '网馈电量(反向输入电量)',
   self_use_power       decimal(12,3) default null comment '自发自用电量',
   consume_power        decimal(12,3) default null comment '综合用电量（总用电量）',
   limit_loss_power     decimal(12,3) default null comment '限电损失电量',
   trouble_loss_power   decimal(12,3) default null comment '故障损失电量',
   performance_ratio    decimal(7,3) default null comment '转换效率',
   equivalent_hour      decimal(12,2) default null comment '等效利用小时',
   power_profit         decimal(19,4) default null comment '收益',
   co2_reduction        decimal(12,3) default null comment '二氧化碳减排量',
   coal_reduction       decimal(12,3) default null comment '标准煤节约',
   tree_reduction       bigint(16) default null comment '等效植树棵数',
   statistic_time       bigint(16) default null comment '统计时间',
   primary key (collect_time, station_code)
);

/*==============================================================*/
/* Table: ids_meter_t                                           */
/*==============================================================*/
DROP TABLE IF EXISTS `ids_meter_data_t`;
CREATE TABLE `ids_meter_data_t` (
  `dev_id` bigint(16) NOT NULL,
  `dev_name` varchar(48) DEFAULT '',
  `collect_time` bigint(16) NOT NULL,
  `station_code` varchar(64) NOT NULL,
  `valid` varchar(256) DEFAULT NULL COMMENT '判断当前指标是否有效：0代表有效，1代表无效  格式010000000000 : 代表第2位也就是bc_u无效',
  `meter_type` int(11) DEFAULT NULL COMMENT '设备dev_type_id, 用于区分不同的电表, 17: 关口电表    47: 户用电表',
  `status` char(5) DEFAULT '' COMMENT '电表状态',
  `grid_u` decimal(9,3) DEFAULT NULL COMMENT '电网电压',
  `grid_i` decimal(9,3) DEFAULT NULL COMMENT '电网电流',
  `ab_u` decimal(9,3) DEFAULT NULL COMMENT '电网ab线电压',
  `bc_u` decimal(9,3) DEFAULT NULL COMMENT '电网bc线电压',
  `ca_u` decimal(9,3) DEFAULT NULL COMMENT '电网ca线电压',
  `a_u` decimal(9,3) DEFAULT NULL COMMENT 'a相电压（交流输出）',
  `b_u` decimal(9,3) DEFAULT NULL COMMENT 'b相电压（交流输出）',
  `c_u` decimal(9,3) DEFAULT NULL COMMENT 'c相电压（交流输出）',
  `a_i` decimal(9,3) DEFAULT NULL COMMENT '电网a相电流(ia)',
  `b_i` decimal(9,3) DEFAULT NULL COMMENT '电网b相电流(ib)',
  `c_i` decimal(9,3) DEFAULT NULL COMMENT '电网c相电流(ic)',
  `active_power` decimal(9,3) DEFAULT NULL COMMENT '有功功率',
  `power_factor` decimal(5,3) DEFAULT NULL COMMENT '功率因数',
  `active_capacity` decimal(14,3) DEFAULT NULL COMMENT '有功电量(正向有功电度)',
  `reactive_power` decimal(12,3) DEFAULT NULL COMMENT '无功功率',
  `reverse_active_cap` decimal(12,3) DEFAULT NULL COMMENT '反向有功电度',
  `forward_reactive_cap` decimal(12,3) DEFAULT NULL COMMENT '正向无功电度',
  `reverse_reactive_cap` decimal(12,3) DEFAULT NULL COMMENT '反向无功电度',
  `active_power_a` decimal(12,3) DEFAULT NULL COMMENT '有功功率pa(kw)',
  `active_power_b` decimal(12,3) DEFAULT NULL COMMENT '有功功率pb(kw)',
  `active_power_c` decimal(12,3) DEFAULT NULL COMMENT '有功功率pc(kw)',
  `reactive_power_a` decimal(12,3) DEFAULT NULL COMMENT '无功功率qa(kvar)',
  `reactive_power_b` decimal(12,3) DEFAULT NULL COMMENT '无功功率qb(kvar)',
  `reactive_power_c` decimal(12,3) DEFAULT NULL COMMENT '无功功率qc(kvar)',
  `total_apparent_power` decimal(12,3) DEFAULT NULL COMMENT '总视在功率(kva)',
  `grid_frequency` decimal(12,3) DEFAULT NULL COMMENT '电网频率(hz)',
  `reverse_active_peak` decimal(12,3) DEFAULT NULL COMMENT '反向有功电度（峰）(kwh)',
  `reverse_active_power` decimal(12,3) DEFAULT NULL COMMENT '反向有功电度（平）(kwh)',
  `reverse_active_valley` decimal(12,3) DEFAULT NULL COMMENT '反向有功电度（谷）(kwh)',
  `reverse_active_top` decimal(12,3) DEFAULT NULL COMMENT '反向有功电度（尖峰）(kwh)',
  `positive_active_peak` decimal(12,3) DEFAULT NULL COMMENT '正向有功电度（峰）(kwh)',
  `positive_active_power` decimal(12,3) DEFAULT NULL COMMENT '正向有功电度（平）(kwh)',
  `positive_active_valley` decimal(12,3) DEFAULT NULL COMMENT '正向有功电度（谷）(kwh)',
  `positive_active_top` decimal(12,3) DEFAULT NULL COMMENT '正向有功电度（尖峰）(kwh)',
  `reverse_reactive_peak` decimal(12,3) DEFAULT NULL COMMENT '反向无功电度（峰）(kvarh)',
  `reverse_reactive_power` decimal(12,3) DEFAULT NULL COMMENT '反向无功电度（平）(kvarh)',
  `reverse_reactive_valley` decimal(12,3) DEFAULT NULL COMMENT '反向无功电度（谷）(kvarh)',
  `reverse_reactive_top` decimal(12,3) DEFAULT NULL COMMENT '反向无功电度（尖峰）(kvarh)',
  `positive_reactive_peak` decimal(12,3) DEFAULT NULL COMMENT '正向无功电度（峰）(kvarh)',
  `positive_reactive_power` decimal(12,3) DEFAULT NULL COMMENT '正向无功电度（平）(kvarh)',
  `positive_reactive_valley` decimal(12,3) DEFAULT NULL COMMENT '正向无功电度（谷）(kvarh)',
  `positive_reactive_top` decimal(12,3) DEFAULT NULL COMMENT '正向无功电度（尖峰）(kvarh)',
  `ext1` decimal(12,3) DEFAULT NULL COMMENT '预留字段1',
  `ext2` decimal(12,3) DEFAULT NULL COMMENT '预留字段2',
  `ext3` varchar(255) DEFAULT '' COMMENT '预留字段3',
  PRIMARY KEY (`collect_time`,`dev_id`,`station_code`),
  KEY `idx_ids_meter_metertype` (`meter_type`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='电表原始数据采集表';

/*==============================================================*/
/* Table: ids_overlimit_alarm_t                                 */
/*==============================================================*/
create table ids_overlimit_alarm_t
(
   id                   bigint(16) not null,
   station_code         varchar(64) not null comment '电站编号',
   station_name         varchar(128) default NULL comment '名称:不同领域下对名称可能有不同的规范要求。名称一般要求具有唯一性',
   busi_code            varchar(128) default NULL comment '业务编号',
   busi_name            varchar(128) default NULL comment '业务名称',
   dev_type_id       smallint(6) default NULL comment '设备类型id',
   device_id            bigint(16) not null comment '设备id',
   status_id            tinyint(4) not null comment '告警状态,活动(1), 确认(2), 清除(3),all(4), none(5);',
   act_id               tinyint(4) default NULL comment '直接来源于设备(1), 设备恢复(2), 遥信转告警(3), 遥信转告警(4),手动清除(5), 通过工作流恢复(6); ',
   raised_date          bigint(16) not null comment '产生时间',
   create_date          bigint(16) default NULL comment '记录创建时间',
   ack_date             bigint(16) default NULL comment '确认时间',
   recover_date         bigint(16) default NULL comment '恢复时间',
   domain_id            bigint(16) not null comment '域id，用于区分安装商',
   enterprise_id        bigint(16) default NULL comment '企业编号',
   signal_id            bigint(16) not null comment '遥信信号点id',
   over_limit_name      varchar(128) default NULL comment '名称',
   severity_id          int(3) default NULL comment '级别id',
   limit_type_id        int(3) default NULL comment '越限类型，1：越上上限，2：越上限，3：越下限，4：越下下限',
   real_time_val        varchar(64) default NULL comment '实时值',
   up_target_val        varchar(64) default NULL comment '目标上界值,越下限则此项为空',
   lower_target_val     varchar(64) default NULL comment '目标下界值，越上限则此项为空',
   update_date          timestamp default CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP comment '最后更新时间',
   primary key (id)
);

/*==============================================================*/
/* Table: ids_power_price_t                                     */
/*==============================================================*/
create table ids_power_price_t
(
   id                   VARCHAR (32) not null comment '主键',
   enterprise_id        BIGINT (16) default NULL comment '企业id',
   domain_id            BIGINT (16) default NULL comment '区域id',
   station_code         VARCHAR (32) not null comment '电站编号',
   start_date           BIGINT (13) not null comment '分时电价开始日期(时间戳)',
   end_date             BIGINT (13) not null comment '分时电价结束日期(时间戳)',
   start_time           INT (2) not null comment '分时电价开始时间(整点0-23)',
   end_time             INT (2) not null comment '分时电价结束时间(整点1-23)',
   price                DECIMAL (3, 2) not null comment '分时电价',
   primary key (id)
);

/*==============================================================*/
/* Table: ids_pv_capacity_t                                     */
/*==============================================================*/
create table ids_pv_capacity_t
(
   id                   bigint(16) not null auto_increment,
   device_id            bigint(16) not null default 0 comment '组串所属设备id',
   station_code         varchar(64) default NULL comment '电站编码',
   enterprise_id        bigint(16) default NULL comment '企业编号',
   dev_name            varchar(48) default NULL comment '业务编号',
   dev_type_id       int(10) not null comment '设备类型',
   num                  int default 0,
   pv1                  smallint(6) default NULL comment 'pv1组串容量，单位 W',
   pv2                  smallint(6) default NULL comment 'pv2组串容量，单位 W',
   pv3                  smallint(6) default NULL,
   pv4                  smallint(6) default NULL,
   pv5                  smallint(6) default NULL,
   pv6                  smallint(6) default NULL,
   pv7                  smallint(6) default NULL,
   pv8                  smallint(6) default NULL,
   pv9                  smallint(6) default NULL,
   pv10                 smallint(6) default NULL,
   pv11                 smallint(6) default NULL,
   pv12                 smallint(6) default NULL,
   pv13                 smallint(6) default NULL,
   pv14                 smallint(6) default NULL,
   pv15                 smallint(6) default NULL,
   pv16                 smallint(6) default NULL,
   pv17                 smallint(6) default NULL,
   pv18                 smallint(6) default NULL,
   pv19                 smallint(6) default NULL,
   pv20                 smallint(6) default NULL,
  `pv21` 				smallint(6) DEFAULT NULL,
  `pv22` 				smallint(6) DEFAULT NULL,
  `pv23` 				smallint(6) DEFAULT NULL,
  `pv24` 				smallint(6) DEFAULT NULL,
   primary key (id),
   key idx_ids_pv_capacity_t_device_id (device_id)
);

/*==============================================================*/
/* Table: ids_pv_module_t                                       */
/*==============================================================*/
create table ids_pv_module_t
(
   id                   bigint(16) not null comment '主键id',
   manufacturer         varchar(255) default NULL comment '制造商',
   module_version       varchar(32) default NULL comment '组件型号',
   standard_power       numeric(10,5) default NULL comment '组件标称功率(Wp)',
   abbreviation         varchar(32) default NULL comment '简称',
   module_type          varchar(32) default NULL comment '组件类型1:多晶 2:单晶 3:N型单晶 4:PERC单晶(单晶PERC) 5:单晶双玻 6:多晶双玻 7:单晶四栅60片 8:单晶四栅72片 9:多晶四栅60片 10:多晶四栅72片',
   module_ratio         numeric(10,5) default NULL comment '效率(%)',
   components_nominal_voltage numeric(10,5) default NULL comment '组件标称开路电压,Voc(V) [0~80]',
   nominal_current_component numeric(10,5) default NULL comment '组件标称短路电流,Isc(A)[0~20]',
   max_power_point_voltage numeric(10,5) default NULL comment '组件最大功率点电压,Vm(V)[-1000~1000]',
   max_power_point_current numeric(10,5) default NULL comment '组件最大功率点电流,Im(A)[-1000~1000]',
   fill_factor          numeric(10,5) default NULL comment '填充因子 FF(%)[65~85]',
   max_power_temp_coef  numeric(10,5) default NULL comment '峰值功率温度系数(%)[-1~0]',
   voltage_temp_coef    numeric(10,5) default NULL comment '组件电压温度系数 (%/oC)[-1~0]',
   current_temp_coef    numeric(10,5) default NULL comment '组件电流温度系数(%/oC)[0~0.2]',
   first_degradation_drate numeric(10,5) default NULL comment '组件首年衰减率(%)[0~100]',
   second_degradation_drate numeric(10,5) default NULL comment '组件逐年衰减率(%)[0~100]',
   cells_num_per_module int default 0 comment '组件电池片数(片)[0~200]',
   min_work_temp        numeric(10,5) default NULL comment '工作温度(oC) (最小值)',
   max_work_temp        numeric(10,5) default NULL comment '工作温度(oC) (最小值)',
   create_time          bigint default NULL comment '创建时间',
   update_time          bigint default NULL comment '修改时间',
   primary key (id)
);

alter table ids_pv_module_t comment '组件信息表';

/*==============================================================*/
/* Table: ids_signal_info_t                                     */
/*==============================================================*/
CREATE TABLE `ids_signal_info_t` (
  `id` bigint(16) NOT NULL AUTO_INCREMENT COMMENT '信号点唯一id，全库唯一',
  `device_id` bigint(16) DEFAULT NULL COMMENT '设备id',
  `signal_name` varchar(32) NOT NULL COMMENT '信号实例名称',
  `signal_alias` varchar(32) DEFAULT '' COMMENT '信号别名',
  `signal_unit` varchar(32) DEFAULT NULL COMMENT '单位',
  `register_type` int(8) DEFAULT NULL COMMENT '寄存器类型',
  `bit` int(8) DEFAULT NULL COMMENT 'bit位',
  `signal_version` varchar(64) DEFAULT '' COMMENT '点表版本',
  `signal_group` int(8) DEFAULT 0 COMMENT '主信号地址，103的groupid',
  `signal_address` int(8) NOT NULL DEFAULT 0 COMMENT '信号点地址',
  `register_num` smallint(4) DEFAULT 0 COMMENT 'modbus协议的寄存器个数',
  `gain` decimal(10,6) DEFAULT NULL COMMENT '增益',
  `offset` decimal(12,6) DEFAULT NULL COMMENT '偏移量',
  `signal_type` int(8) DEFAULT NULL COMMENT '1：遥测；2：单点遥信；3：双点遥信；4：单点遥控；5：双点遥控,6：遥脉；7：遥调；8：事件；9：告警； 15：线圈；16：离散输入；17：保持寄存器；18：输入寄存器',
  `model_id` bigint(16) DEFAULT NULL COMMENT '模型点表的id',
  `data_type` int(8) DEFAULT NULL COMMENT '1：无符号整数；2：有符号整数；3：浮点数；4：字符串；5：时间',
  `is_alarm_flag` tinyint(1) DEFAULT 0 COMMENT '1表示该信号点为遥测告警标志位， 0表示否',
  `is_alarm_val` tinyint(1) DEFAULT 0 COMMENT '1表示该信号点为遥测告警有效值，0表示否',
  `is_limited` tinyint(1) DEFAULT 0 COMMENT '是否存在数据范围设置',
  `create_date` datetime DEFAULT NULL COMMENT '创建日期',
  `modified_date` datetime DEFAULT NULL COMMENT '修改日期',
  PRIMARY KEY (`id`),
  KEY `idx_ids_south_signal_key` (`device_id`,`signal_address`)
) ENGINE=InnoDB AUTO_INCREMENT=763 DEFAULT CHARSET=utf8 COMMENT='点表实例表';

/*==============================================================*/
/* Table: ids_signal_model_t                                    */
/*==============================================================*/
CREATE TABLE `ids_signal_model_t` (
  `id` bigint(16) NOT NULL AUTO_INCREMENT,
  `signal_name` varchar(64) DEFAULT NULL,
  `signal_address` int(8) DEFAULT NULL COMMENT '信息体地址',
  `protocol_code` varchar(64) DEFAULT NULL COMMENT '协议编码',
  `signal_version` varchar(64) DEFAULT '',
  `signal_alias` varchar(64) DEFAULT '' COMMENT '信号别名',
  `signal_unit` varchar(32) DEFAULT '' COMMENT '单位',
  `signal_type` int(8) DEFAULT NULL COMMENT '1：遥测；2：单点遥信；3：双点遥信；4：单点遥控；5：双点遥控,6：遥脉；7：遥调；8：事件；9：告警',
  `tele_type` int(8) DEFAULT 1 COMMENT ' 1:变位信号  2：异常告警  3：保护事件  4：通知状态  5：告警信息',
  `data_type` int(8) DEFAULT NULL COMMENT '1：无符号整数；2：有符号整数；3：浮点数；4：字符串；5：时间',
  `gain` double DEFAULT NULL COMMENT '增益',
  `offset` double DEFAULT NULL COMMENT '偏移量',
  `register_num` smallint(2) DEFAULT NULL COMMENT '寄存器个数',
  `signal_group` int(8) DEFAULT NULL COMMENT '主信号地址，103的groupid',
  `bit` int(8) DEFAULT NULL,
  `create_date` datetime DEFAULT NULL COMMENT '创建日期',
  `modified_date` datetime DEFAULT NULL COMMENT '修改日期',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=763 DEFAULT CHARSET=utf8 COMMENT='点表模型';

/*==============================================================*/
/* Table: ids_signal_version_info_t                             */
/*==============================================================*/
CREATE TABLE `ids_signal_version_t` (
  `id` bigint(16) NOT NULL AUTO_INCREMENT COMMENT '版本id',
  `parent_id` bigint(16) DEFAULT NULL COMMENT '父版本编码',
  `name` varchar(64) DEFAULT NULL COMMENT '电表名称',
  `signal_version` varchar(64) NOT NULL COMMENT '版本编码',
  `enterprise_id` bigint(16) DEFAULT NULL COMMENT '企业编码,归属企业,null表示属于共享信息',
  `station_code` varchar(32) DEFAULT NULL COMMENT '电站编码',
  `dev_type_id` int(8) NOT NULL DEFAULT 0 COMMENT '设备类型id',
  `vender_name` varchar(32) NOT NULL DEFAULT '0' COMMENT '设备供应商名称',
  `protocol_code` varchar(20) NOT NULL COMMENT '协议编码:104 103 hwmodbus',
  `interface_version` varchar(20) DEFAULT '' COMMENT '接口协议版本',
  `type` tinyint(4) NOT NULL COMMENT '1 系统内置，2 用户导入 —内置数据不可删',
  `create_date` datetime DEFAULT NULL COMMENT '创建日期',
  `modified_date` datetime DEFAULT NULL COMMENT '修改日期',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8 COMMENT='点表版本表';

/*==============================================================*/
/* Table: ids_sm_auth_t                                         */
/*==============================================================*/
create table ids_sm_auth_t
(
   id                   bigint(16) not null,
   auth_name            varchar(64) default NULL,
   description          varchar(255) default NULL,
   primary key (id)
);

/*==============================================================*/
/* Table: ids_sm_domain_info_t                                       */
/*==============================================================*/
create table ids_sm_domain_info_t
(
   id                   bigint(16) not null auto_increment,
   name                 varchar(64) not null comment '区域名',
   description          varchar(1000) default NULL comment '区域描述',
   parent_id            bigint(16) default NULL comment '父ID',
   enterprise_id        bigint(16) default NULL comment '企业编号',
   longitude            decimal(10,6) default NULL comment '区域经度，用于地图汇聚',
   latitude             decimal(10,6) default NULL comment '纬度',
   radius               decimal(10,3) default NULL comment '区域半径，地图呈现区域范围大小',
   domain_price         decimal(7,3) default NULL comment '区域电价，计算收益是会使用',
   currency             varchar(16) default NULL comment '货币：￥ $ €',
   create_user_id       bigint(16),
   create_date          datetime,
   modify_user_id       bigint(16),
   modify_date          datetime,
   path                 varchar(64),
   primary key (id)
);

/*==============================================================*/
/* Table: ids_sm_enterprise_info_t                                   */
/*==============================================================*/
create table ids_sm_enterprise_info_t
(
   id                   bigint(16) not null auto_increment comment '主键',
   name                 varchar(225) not null comment '企业名称',
   description          varchar(1000) default NULL comment '企业简介',
   parent_id            bigint(16) default NULL comment '父ID, 用于确定上级企业',
   avatar_path          varchar(255) default NULL comment '企业缩影图片',
   address              varchar(255) default NULL comment '企业地址',
   contact_people       varchar(64) default NULL comment '联系人',
   contact_phone        varchar(64) default NULL comment '联系方式',
   email                varchar(64) default NULL comment '邮箱',
   device_limit         int(8) default 0 comment '接入设备限制数',
   user_limit           int(6) default 0 comment '企业下属用户限制数',
   create_user_id       bigint(16) default NULL comment '创建用户ID',
   create_date          datetime default NULL comment '创建时间',
   modify_user_id       bigint(16) default NULL comment '修改用户ID',
   modify_date          datetime default NULL comment '修改时间',
   longitude            decimal(10,6) default NULL comment '区域经度，用于地图汇聚',
   latitude             decimal(10,6) default NULL comment '纬度',
   radius               decimal(10,3) default NULL comment '区域半径，地图呈现区域范围大小',
   primary key (id)
);
-- 添加企业的logo字段
ALTER TABLE `ids_sm_enterprise_info_t`
ADD COLUMN `logo`  varchar(255) NULL DEFAULT NULL COMMENT '企业LOGO' AFTER `radius`;
/*==============================================================*/
/* Table: ids_sm_department_t                                   */
/*==============================================================*/
create table ids_sm_department_t
(
	id                   bigint(16) not null auto_increment comment '主键',
	name                 varchar(225) not null comment '部门的名称',
	order_				 int(8) comment '顺序',
	enterprise_id        bigint(16) comment '企业id',
	parent_id			 bigint(16) comment '父id',
	primary key (id)
	
);

/*==============================================================*/
/* Table: ids_sm_department_t                                   */
/*==============================================================*/
create table user_department
(
	user_id 			bigint(16),
	department_id		bigint(16)
);

/*==============================================================*/
/* Table: ids_sm_intelligentclean_param_t                       */
/*==============================================================*/
create table ids_sm_intelligentclean_param_t
(
   id                   bigint(16) not null auto_increment,
   param_key            varchar(32) not null comment '参数编码(内置)',
   param_name           varchar(64) default NULL comment '参数名',
   description          varchar(255) default NULL comment '参数描述',
   param_value          varchar(255) default NULL comment '参数值',
   param_unit           varchar(32) default NULL comment '参数单位',
   param_type           varchar(64) default NULL comment '参数类型',
   param_order          tinyint default 0 comment '参数顺序号',
   station_code         varchar(32),
   enterprise_id        bigint(16),
   modify_user_id       bigint(16),
   modify_date          datetime,
   primary key (id)
);

/*==============================================================*/
/* Table: ids_sm_resource_info_t                                     */
/*==============================================================*/
create table ids_sm_resource_info_t
(
   id                   bigint(16) not null auto_increment,
   resource_key         varchar(64) not null comment '权限的key值，唯一',
   auth_id              bigint(16) default NULL comment '权限ID',
   resource_name        varchar(64) default NULL,
   description          varchar(255) default NULL,
   auth_url             varchar(255) not null,
   request_type         varchar(16) default NULL,
   unique               (resource_key),
   primary key (id)
);

/*==============================================================*/
/* Table: ids_sm_role_auth_t                                    */
/*==============================================================*/
create table ids_sm_role_auth_t
(
   role_id              bigint(16) not null,
   auth_id              bigint(16) not null,
   primary key (role_id, auth_id)
);

/*==============================================================*/
/* Table: ids_sm_role_info_t                                         */
/*==============================================================*/
create table ids_sm_role_info_t
(
   id                   bigint(16) not null auto_increment,
   name                 varchar(255) not null comment '角色名',
   role_type            varchar(32) default null comment '角色类型: sysadmin admin normal',
   description          varchar(500) default NULL comment '角色描述',
   status               tinyint default 0 comment '角色状态：0:启用 1:禁用',
   enterprise_id        bigint(16),
   create_user_id       bigint(16),
   create_date          datetime,
   modify_user_id       bigint(16),
   modify_date          datetime,
   primary key (id)
);

/*==============================================================*/
/* Table: ids_station_info_t                                 */
/*==============================================================*/
create table ids_station_info_t
(
   id                   bigint(16) not null auto_increment  comment '主键id',
   station_code         varchar(32) not null comment '电站编号',
   station_name         varchar(255) not null comment '电站名称',
   installed_capacity   decimal(20,4) default NULL comment '装机容量kW',
   station_build_status tinyint default NULL comment '电站状态：3:并网 2:在建 1:规划',
   produce_date         date default NULL comment '投产时间',
   online_type          tinyint default NULL comment '并网类型：1:地面式 2:分布式 3:户用',
   station_type         tinyint default NULL comment '电站类型：1:渔光、2:农光、3:牧光',
   inverter_type        tinyint default NULL comment '逆变器类型：0:集中式 1:组串式 2:户用',
   install_angle        decimal(6,2) default NULL comment '安装角度',
   assembly_layout      char(1) default NULL comment '组件布置方式：1:横排 2:竖排',
   floor_space          decimal(16,6) default NULL comment '占地面积平方千米',
   amsl                 decimal(20,2) default NULL comment '平均海拔',
   life_cycle           tinyint default NULL comment '计划运营年限',
   safe_run_datetime    bigint(16) default NULL comment '安全运行开始时间（精确到日的毫秒数）',
   is_poverty_relief    tinyint default 0 comment '是否扶贫站：0:不是 1:是',
   station_file_id      varchar(32) default NULL comment '电站缩略图',
   station_addr         varchar(255) default NULL comment '电站详细地址',
   station_desc         varchar(1000) default NULL comment '电站简介',
   contact_people       varchar(64) default NULL comment '联系人',
   phone                varchar(32) default NULL comment '联系方式',
   station_price        decimal(7,3) default NULL comment '电价',
   latitude             decimal(12,6) default NULL comment '纬度',
   longitude            decimal(12,6) default NULL comment '经度',
   time_zone            int default NULL comment '电站所在时区',
   area_code            varchar(64) default NULL comment '行政区域编号（对应省市县编号以@符号连接）',
   enterprise_id        bigint(16) default NULL comment '企业编号',
   domain_id            bigint(16) default NULL comment '区域编号',
   is_delete            char(1) default '0' comment '电站是否逻辑删除',
   create_user_id       bigint(16),
   create_date          datetime,
   update_user_id       bigint(16),
   update_date          datetime,
   `is_monitor` varchar(1) DEFAULT '0' COMMENT '设备是否来源于监控 1:监控的电站; 0：集维侧的电站',
   primary key (id),
   key idx_station_code (station_code),
   key idx_station_name (station_name)
);

/*==============================================================*/
/* Table: ids_sm_station_param_t                                */
/*==============================================================*/
drop table if exists ids_sm_station_param_t;
create table ids_sm_station_param_t
(
   id                   bigint(16) not null auto_increment,
   param_key            varchar(32) not null comment '参数编码(字符串常量)',
   param_name 			varchar(64) DEFAULT NULL COMMENT '参数名',
   description          varchar(255) default NULL comment '参数描述',
   param_value          varchar(255) default NULL comment '参数值',
   param_unit           varchar(32) default NULL comment '参数单位',
   param_order          tinyint default 0 comment '参数顺序号',
   station_code         varchar(64),
   enterprise_id        bigint(16),
   modify_user_id       bigint(16),
   modify_date          datetime,
   primary key (id)
);

/*==============================================================*/
/* Table: ids_sm_user_auth_t                                    */
/*==============================================================*/
create table ids_sm_user_auth_t
(
   user_id              bigint(16) not null,
   auth_id              bigint(16) not null,
   primary key (user_id, auth_id)
);

/*==============================================================*/
/* Table: ids_sm_user_role_t                                    */
/*==============================================================*/
create table ids_sm_user_role_t
(
   user_id              bigint(16) not null,
   role_id              bigint(16) not null,
   primary key (user_id, role_id)
);

/*==============================================================*/
/* Table: ids_sm_user_station_t                                 */
/*==============================================================*/
create table ids_sm_user_station_t
(
   station_code         varchar(64) not null,
   user_id              bigint(16) not null,
   primary key (station_code, user_id)
);

/*==============================================================*/
/* Table: ids_sm_user_info_t                                         */
/*==============================================================*/
create table ids_sm_user_info_t
(
   id                   bigint(16) not null auto_increment,
   type_                varchar(255) not null comment 'system:系统管理员；enterprise：企业管理员',
   login_name           varchar(64) not null comment '登录名',
   password             varchar(64) not null comment '登录密码',
   user_name            varchar(255) not null comment '用户名用于显示，类似昵称',
   qq                   varchar(16),
   email                varchar(255),
   gender               char(1),
   phone                varchar(32),
   status               tinyint default 0 comment '用户状态：0:正常 1:禁用',
   user_type            tinyint default 0 comment '用户类型：0:企业用户 1:注册用户',
   enterprise_id        bigint(16),
   create_user_id       bigint(16),
   create_date          datetime,
   modify_user_id       bigint(16),
   modify_date          datetime,
   primary key (id)
);

/*==============================================================*/
/* Table: ids_station_realtime_data_h                                */
/*==============================================================*/
create table ids_station_realtime_data_h
(
   collect_time         bigint(16) not null comment '采集时间',
   station_code         varchar(64) not null comment '电站编号',
   enterprise_id        bigint(16) not null comment '区域编号',
   radiation_intensity  decimal(16,4) default NULL comment '辐照量',
   product_power        decimal(12,3) default NULL comment '累计发电量',
   day_cap              decimal(12,3) default NULL comment '当日发电量',
   active_power         decimal(9,3) default NULL comment '有功功率',
   day_income           decimal(12,3) default NULL comment '当日收益',
   total_income         decimal(12,3) default NULL comment '累计收益',
   primary key (collect_time, station_code)
);

/*==============================================================*/
/* Table: ids_station_realtime_data_t                                */
/*==============================================================*/
create table ids_station_realtime_data_t
(
   collect_time         bigint(16) not null comment '采集时间',
   station_code         varchar(64) not null comment '电站编号',
   enterprise_id        bigint(16) not null comment '区域编号',
   radiation_intensity  decimal(16,4) default NULL comment '辐照量',
   product_power        decimal(12,3) default NULL comment '累计发电量',
   day_cap              decimal(12,3) default NULL comment '当日发电量',
   active_power         decimal(9,3) default NULL comment '有功功率',
   day_income           decimal(12,3) default NULL comment '当日收益',
   total_income         decimal(12,3) default NULL comment '累计收益',
   primary key (collect_time, station_code)
);

/*==============================================================*/
/* Table: ids_station_shareemi_t                                */
/*==============================================================*/
create table ids_station_shareemi_t
(
   id                   bigint(16) not null auto_increment comment '主键',
   share_station_code   varchar(100) default NULL comment '共享的电站编号',
   station_code         varchar(100) default NULL comment '电站编号',
   share_device_id      bigint(20) default NULL comment '共享的设备id',
   primary key (id)
);

/*==============================================================*/
/* Table: ids_station_weather_day_t                             */
/*==============================================================*/
create table ids_station_weather_day_t
(
   station_code         VARCHAR (32) not null comment '电站编号',
   collect_time         BIGINT (16) not null comment '统计时间',
   weather_text_day     VARCHAR (50) not null comment '白天天气情况',
   weather_text_night   VARCHAR (50) not null comment '晚上天气情况',
   primary key (station_code, collect_time)
);

/*==============================================================*/
/* Table: ids_station_weather_statistics_t                      */
/*==============================================================*/
create table ids_station_weather_statistics_t
(
   id                   VARCHAR (32) not null comment '主键id',
   station_code         VARCHAR (12) not null comment '电站编号',
   year_time            VARCHAR (4) not null comment '统计时间',
   month_time           VARCHAR (2) not null comment '统计时间',
   radiation_intensity  DECIMAL (10,4) default NULL comment '辐照量数据',
   heavy_rain_days      TINYINT (2) default NULL comment '大雨天数',
   middle_rain_days     TINYINT (2) default NULL comment '中雨/沙尘天数',
   primary key (id)
);

/*==============================================================*/
/* Table: ids_statistics_schedule_task_t                      */
/*==============================================================*/
create table ids_statistics_schedule_task_t
(
   station_code         varchar(64) not null,
   busi_type            varchar(20) not null comment '业务分区 ： 环境监测仪采集数据：environment  逆变器采集数据：inverter   交流汇流箱采集数据：combiner   箱变采集数据：transformer',
   stat_date            bigint(16) not null comment '统计时间',
   stat_type            tinyint(5) not null comment '执行周期：  0：hour  1：day',
   create_time          bigint(20) default NULL comment '生成时间',
   deal_state           tinyint(5) default NULL comment '0：未处理 1：处理中 2：已处理 ',
   primary key (station_code, busi_type, stat_date, stat_type)
);

/*==============================================================*/
/* Table: ids_subscribe_signal_t                                */
/*==============================================================*/
DROP TABLE IF EXISTS `ids_subscribe_signal_t`;
CREATE TABLE `ids_subscribe_signal_t` (
  `id` bigint(19) NOT NULL AUTO_INCREMENT COMMENT '主键Id',
  `signal_address` int(8) NOT NULL DEFAULT 0 COMMENT '信号点地址',
  `register_num` smallint(2) NOT NULL DEFAULT 0 COMMENT '寄存器个数',
  `signal_version` varchar(64) DEFAULT '' COMMENT '归属型号版本编号',
  `register_type` int(11) DEFAULT NULL COMMENT '0：离散量输入；1：线圈；2：输入寄存器；3：保持寄存器',
  `period` int(11) DEFAULT NULL COMMENT '订阅周期，单位秒，默认30秒',
  `subscribe_type` int(11) DEFAULT NULL COMMENT '0:周期上送；1：周期突变；2：突变',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COMMENT='信号点订阅列表';

/*==============================================================*/
/* Table: ids_system_param                                      */
/*==============================================================*/
create table ids_system_param
(
   id                   bigint(16) not null auto_increment,
   system_name			varchar(100) default NUll comment '系统名称',
   description          varchar(1000) default NULL comment '系统简介',
   file_id              VARCHAR(32) default NULL comment 'logo文件id',
   primary key (id)
);

/*==============================================================*/
/* Table: ids_telesignal_t                                      */
/*==============================================================*/
create table ids_telesignal_t
(
   id                   bigint(16) not null,
   station_code         varchar(128) not null comment '电站编号',
   station_name         varchar(128) default NULL comment '电站名称:名称一般要求具有唯一性',
   busi_code            varchar(128) default NULL comment '业务编号',
   busi_name            varchar(128) default NULL comment '业务名称',
   dev_type_id       smallint(6) default NULL comment '设备类型id',
   device_id            bigint(16) not null comment '设备id',
   status_id            tinyint(4) not null comment '告警状态,活动(1), 确认(2), 清除(3),all(4), none(5);',
   act_id               tinyint(4) default NULL comment '直接来源于设备(1), 设备恢复(2), 遥信转告警(3), 遥信转告警(4),手动清除(5), 通过工作流恢复(6); ',
   raised_date          bigint(16) not null comment '产生时间',
   create_date          bigint(16) default NULL comment '记录创建时间',
   ack_date             bigint(16) default NULL comment '确认时间',
   recover_date         bigint(16) default NULL comment '恢复时间',
   domain_id            bigint(16) not null comment '域id，用于区分安装商',
   enterprise_id        bigint(16) default NULL comment '企业编号',
   signal_id            bigint(16) not null comment '遥信信号点id',
   name                 varchar(128) default NULL comment '名称',
   signal_status        int(3) default NULL comment '遥信状态',
   update_date          timestamp default CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP comment '最后更新时间',
   primary key (id)
);

/*==============================================================*/
/* Table: ids_transformer_t                                     */
/*==============================================================*/
create table ids_transformer_data_t
(
   collect_time         bigint(16) not null,
   station_code         varchar(64) not null,
   dev_id               bigint(16) not null,
   dev_name             varchar(48) DEFAULT '',
   enterprise_id        bigint(16) default NULL comment '企业编号',
   valid                varchar(256) default NULL comment '判断当前指标是否有效：0代表有效，1代表无效  格式010000000000 : 代表第2位也就是bc_u无效',
   busi_code            varchar(48) default NULL,
   ab_u                 decimal(12,3) default NULL comment '电网ab线电压',
   bc_u                 decimal(12,3) default NULL comment '电网bc线电压',
   ca_u                 decimal(12,3) default NULL comment '电网ca线电压',
   a_u                  decimal(12,3) default NULL comment 'a相电压（交流输出）',
   b_u                  decimal(12,3) default NULL comment 'b相电压（交流输出）',
   c_u                  decimal(12,3) default NULL comment 'c相电压（交流输出）',
   a_i                  decimal(12,3) default NULL comment '电网a相电流(ia)',
   b_i                  decimal(12,3) default NULL comment '电网b相电流(ib)',
   c_i                  decimal(12,3) default NULL comment '电网c相电流(ic)',
   active_power         decimal(12,3) default NULL comment '有功功率',
   reactive_power       decimal(12,3) default NULL comment '无功功率',
   power_factor         decimal(12,3) default NULL comment '功率因数',
   elec_freq            decimal(12,3) default NULL comment '电网频率',
   primary key (collect_time, dev_id, station_code)
);

-- ----------------------------
-- Table structure for `ids_normalized_model_t`
-- ----------------------------
DROP TABLE IF EXISTS `ids_normalized_model_t`;
CREATE TABLE `ids_normalized_model_t` (
  `id` bigint(19) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `dev_type` bigint(19) DEFAULT NULL COMMENT '设备类型id',
  `signal_name` varchar(64) DEFAULT '' COMMENT '归一化信号点名称',
  `display_name` varchar(64) DEFAULT '' COMMENT '展示名称,中文',
  `column_name` varchar(32) DEFAULT '' COMMENT '信号点字段名称，持久化表到字段名',
  `is_persistent` tinyint(1) DEFAULT NULL COMMENT '是否持久化:0不持久化；1持久化',
  `order_num` int(20) DEFAULT NULL COMMENT '序号',
  `create_date` datetime DEFAULT NULL ON UPDATE current_timestamp(),
  `modified_date` datetime DEFAULT NULL ON UPDATE current_timestamp(),
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=247 DEFAULT CHARSET=utf8 COMMENT='归一化信号点配置表';

-- ----------------------------
-- Table structure for `ids_normalized_info_t`
-- ----------------------------
DROP TABLE IF EXISTS `ids_normalized_info_t`;
CREATE TABLE `ids_normalized_info_t` (
  `id` bigint(19) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `siganl_model_id` int(19) DEFAULT NULL COMMENT '针对104里面到模型id',
  `signal_name` varchar(64) DEFAULT NULL COMMENT '信号点名称',
  `signal_version` varchar(32) DEFAULT '' COMMENT '点表版本',
  `signal_address` int(32) DEFAULT NULL COMMENT '信号点寄存器地址',
  `normalized_signal_id` bigint(20) DEFAULT NULL COMMENT '归一化信号模型id',
  `create_date` datetime DEFAULT NULL COMMENT '创建日期',
  `modified_date` datetime DEFAULT NULL COMMENT '修改日期',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='信号点模型适配表';

/*==============================================================*/
/* Table: ids_workflow_defect_t                                 */
/*==============================================================*/
create table ids_workflow_defect_t
(
   defect_id            bigint(16) not null auto_increment,
   defect_code          varchar(64),
   defect_name          varchar(128),
   dev_id            	varchar(32),
   defect_grade         varchar(64),
   description          varchar(1000) comment '缺陷的描述',
   proc_id              varchar(32),
   proc_state           varchar(64),
   dev_type          varchar(32),
   dev_vender        varchar(255) comment '设备的供应商',
   dev_version       varchar(255),
   fault_type           varchar(32),
   alarm_ids           	varchar(128) comment '关联告警的id使用逗号隔开',
   alarm_num            bigint(16) comment '关联告警个数',
   alarm_type           varchar(32),
   station_code         varchar(32),
   station_name         varchar(128),
   station_addr			varchar(128),
   enterprise_id        bigint(16) default NULL comment '企业编号',
   file_id              varchar(255),
   create_user_id       bigint(16) comment '创建人的id',
   create_time          bigint(16) default NULL comment '创建时间',
   update_time          bigint(16) default NULL comment '修改时间',
   start_time           bigint(16) default NULL comment '实际开始时间',
   station_start_time   bigint(16) default NULL comment '开始时间(开始时间根据电站时区做偏移)',
   end_time             bigint(16) default NULL comment '实际结束时间',
   time_zone            int(32) default NULL comment '电站时区',
   primary key (defect_id)
);

/*==============================================================*/
/* Table: ids_workflow_process_t                                */
/*==============================================================*/
create table ids_workflow_process_t
(
   id                   bigint(16) not null auto_increment,
   proc_id              varchar(32),
   proc_state           varchar(32),
   busi_state           varchar(32),
   start_time           bigint(16) default NULL comment '流程开始时间',
   end_time             bigint(16) default NULL comment '流程结束时间',
   update_time          bigint(16) default NULL comment '流程更新时间',
   proc_ins_id          varchar(64),
   proc_name            varchar(255),
   proc_desc            varchar(1000),
   current_task_id      varchar(32),
   current_task_assignee     varchar(255),
   create_user          varchar(255),
   station_code         varchar(32),
   enterprise_id        bigint(16) default NULL comment '企业编号',
   primary key (id)
);

/*==============================================================*/
/* Table: ids_workflow_task_t                                   */
/*==============================================================*/
create table ids_workflow_task_t
(
   id                   bigint(16) not null auto_increment,
   task_id              varchar(32),
   pre_task_id          varchar(32),
   proc_id              varchar(32),
   task_start_time      bigint(16) default NULL comment '任务的开始时间',
   task_end_time        bigint(16) default NULL comment '任务的结束时间',
   task_state           varchar(32),
   task_content         varchar(1000) comment '任务的内容',
   transferor_name      varchar(255) comment '接收者的名字',
   assignee_name        varchar(255),
   file_id              varchar(255),
   op_state            varchar(100) comment '操作状态0代表未操作 1 代表已操作',
   op_desc       varchar(1000) comment '操作的描述',
   station_code         varchar(32),
   enterprise_id        bigint(16) default NULL comment '企业编号',
   primary key (id)
);

/*==============================================================*/
/* Table: ids_workflow_task_user_t                              */
/*==============================================================*/
create table ids_workflow_task_user_t
(
   id                   bigint(16) not null auto_increment,
   proc_id             varchar(32),
   task_id             varchar(32),
   user_id              bigint(16) default NULL,
   station_code         varchar(32),
   enterprise_id        bigint(16) default NULL,
   primary key (id)
);

DROP TABLE IF EXISTS ids_kpi_calc_task_t;
CREATE TABLE ids_kpi_calc_task_t (
	id BIGINT (16) NOT NULL auto_increment COMMENT '主键',
  task_name VARCHAR (64) NOT NULL COMMENT '任务内容',
	station_code VARCHAR (32) NOT NULL COMMENT '电站编号',
	start_time BIGINT (16) NOT NULL COMMENT '计算区间起始时间',
	end_time BIGINT (16) NOT NULL COMMENT '计算区间结束时间',
	task_status tinyint (1) NOT NULL COMMENT '任务状态', 
	PRIMARY KEY (id)
) ENGINE = INNODB DEFAULT charset = utf8 COMMENT = 'Kpi手动计算任务表';

drop table if exists ids_kpi_revise_t;
create table ids_kpi_revise_t
(
	id bigint(16) NOT NULL auto_increment  comment '主键id',
	station_code varchar(64)  default null comment '电站编号',
	station_name varchar(255) default null comment '电站名称',
	kpi_key	varchar(64) default null comment '指标名称',
	revise_type varchar(64) default null comment '修正方式',
	replace_value decimal(19,4) default null comment '替换值',
	time_dim	varchar(10) default null  comment '维度',
	revise_date bigint default null  comment '修正时间',
	offset_value decimal(19,4) default null comment '偏移量',
	ratio_value decimal(5,2) default null comment '修正系数',
	old_value decimal(19,4) default null comment '被替换的值',
	enterprise_id bigint(16) not null comment '企业编号',
	revise_status tinyint(2) default 0 comment '校正状态：0(未校正) 1(正在校正) 2(已校正)',
	PRIMARY KEY (id),
  UNIQUE INDEX (station_code, kpi_key, time_dim,revise_type,revise_date)
) engine=innodb default charset=utf8;

drop table if exists ids_operator_position_t;
create table ids_operator_position_t(
  user_id bigint(16) not null comment '用户ID, 主键',
  user_name varchar(255) default null comment '用户姓名',
  latitude decimal(12,6) default null comment '纬度',
  longitude decimal(12,6) default null comment '经度',
  enterprise_id bigint(16) not null comment '企业ID',
  update_time bigint(19) default null comment '更新时间',
  primary key(user_id)
)engine=innodb default charset=utf8;

/* ========分析统计类表======== */
/* 组串诊断日分析结果 */
drop table if exists ids_analysis_pv_day_t;
create table ids_analysis_pv_day_t(
  station_code varchar(64) not null comment '电站编号',
  dev_id bigint(16) not null comment '设备id',
  pv_code varchar(5) not null comment '组串编号',
  analysis_time bigint(20) not null comment '分析时间',
  dev_alias varchar(64) default '' comment '设备别名',
  pv_capacity decimal(10,5) default null comment '组串容量',
  matrix_id bigint(16) default null comment '子阵id',
  matrix_name varchar(64) default null comment '子阵名',
  analysis_state tinyint(2) default 0 comment '分析结果状态: 0:正常  1:故障 2:低效 3:遮挡',
  lost_power decimal(12, 3) default 0 comment '损失电量',
  last_start_time bigint(20) default null comment '问题持续开始时间',
  last_end_time bigint(20) default null comment '问题持续结束时间',
  primary key(station_code, dev_id, pv_code, analysis_time, analysis_state),
  key idx_ids_analysis_pv_day (matrix_id)
)engine=innodb default charset=utf8 COMMENT = '智能分析故障、低效、遮挡等天的损失电量';

/* 组串诊断月统计分析表*/
drop table if exists ids_analysis_pv_month_t;
create table ids_analysis_pv_month_t(
  station_code varchar(64) not null comment '电站编号',
  dev_id bigint(16) not null comment '设备id',
  pv_code varchar(5) not null comment '组串编号',
  analysis_time bigint(20) not null comment '分析时间',
  dev_alias varchar(64) default '' comment '设备别名',
  pv_capacity decimal(10,5) default null comment '组串容量',
  matrix_id bigint(16) default null comment '子阵id',
  matrix_name varchar(64) default null comment '子阵名',
  trouble_last_time int(5) default null comment '故障持续时长',
  trouble_lost_power decimal(12, 3) default 0 comment '故障损失电量',
  ineff_last_time int(5) default null comment '低效持续时长',
  ineff_lost_power decimal(12, 3) default 0 comment '低效损失电量',
  hid_last_time int(5) default null comment '遮挡持续时长',
  hid_lost_power decimal(12, 3) default 0 comment '遮挡损失电量',
  primary key(station_code, dev_id, pv_code, analysis_time),
  key idx_ids_analysis_pv_month (matrix_id)
)engine=innodb default charset=utf8;

/* 子阵日统计分析表*/
drop table if exists ids_analysis_matrix_day_t;
create table ids_analysis_matrix_day_t(
  station_code varchar(64) not null comment '电站编号',
  matrix_id bigint(16) not null comment '子阵id',
  analysis_time bigint(20) not null comment '分析时间',
  matrix_name varchar(64) default null comment '子阵名',
  installed_capacity decimal(10,5) default null comment '子阵装机容量',
  pv_num tinyint(3) default null comment '接入组串数量',
  product_power decimal(12,3) default null comment '当日发电量',
  trouble_pv_num tinyint(3) default 0 comment '故障组串数',
  trouble_lost_power decimal(12, 3) default 0 comment '故障损失电量',
  ineff_pv_num tinyint(3) default 0 comment '低效组串数',
  ineff_lost_power decimal(12, 3) default 0 comment '低效损失电量',
  hid_pv_num tinyint(3) default 0 comment '遮挡组串数',
  hid_lost_power decimal(12, 3) default 0 comment '遮挡损失电量',
  primary key(station_code, matrix_id, analysis_time)
)engine=innodb default charset=utf8;

/* 子阵月统计分析表*/
drop table if exists ids_analysis_matrix_month_t;
create table ids_analysis_matrix_month_t(
  station_code varchar(64) not null comment '电站编号',
  matrix_id bigint(16) not null comment '子阵id',
  analysis_time bigint(20) not null comment '分析时间',
  matrix_name varchar(64) default null comment '子阵名',
  installed_capacity decimal(10,5) default null comment '子阵装机容量',
  pv_num tinyint(3) default null comment '接入组串数量',
  product_power decimal(12,3) default null comment '当月发电量',
  trouble_pv_num tinyint(3) default 0 comment '故障组串数',
  trouble_lost_power decimal(12, 3) default 0 comment '故障损失电量',
  ineff_pv_num tinyint(3) default 0 comment '低效组串数',
  ineff_lost_power decimal(12, 3) default 0 comment '低效损失电量',
  hid_pv_num tinyint(3) default 0 comment '遮挡组串数',
  hid_lost_power decimal(12, 3) default 0 comment '遮挡损失电量',
  primary key(station_code, matrix_id, analysis_time)
)engine=innodb default charset=utf8;

/* 子阵年统计分析表*/
drop table if exists ids_analysis_matrix_year_t;
create table ids_analysis_matrix_year_t(
  station_code varchar(64) not null comment '电站编号',
  matrix_id bigint(16) not null comment '子阵id',
  analysis_time bigint(20) not null comment '分析时间',
  matrix_name varchar(64) default null comment '子阵名',
  installed_capacity decimal(10,5) default null comment '子阵装机容量',
  pv_num tinyint(3) default null comment '接入组串数量',
  product_power decimal(12,3) default null comment '当年发电量',
  trouble_pv_num tinyint(3) default 0 comment '故障组串数',
  trouble_lost_power decimal(12, 3) default 0 comment '故障损失电量',
  ineff_pv_num tinyint(3) default 0 comment '低效组串数',
  ineff_lost_power decimal(12, 3) default 0 comment '低效损失电量',
  hid_pv_num tinyint(3) default 0 comment '遮挡组串数',
  hid_lost_power decimal(12, 3) default 0 comment '遮挡损失电量',
  primary key(station_code, matrix_id, analysis_time)
)engine=innodb default charset=utf8;


-- ----------------------------
-- Table structure for `ids_subarray_info_t`
-- ----------------------------
drop table if exists ids_subarray_info_t;
CREATE TABLE ids_subarray_info_t
(
	id                   BIGINT(16) NOT NULL auto_increment COMMENT '主键id',
	subarray_name           VARCHAR(255) NOT NULL COMMENT '子阵名称',
	phalanx_id           BIGINT(16) NOT NULL COMMENT '方阵编号',
	station_code         VARCHAR(32) NOT NULL COMMENT '方阵编号',
	create_time          DATETIME DEFAULT NULL COMMENT '创建时间',
	create_user_id       BIGINT(16) DEFAULT NULL COMMENT '创建人id',
	update_time          DATETIME DEFAULT NULL COMMENT '创建时间',
	update_user_id       BIGINT(16) DEFAULT NULL COMMENT '创建人id',
	primary key (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='子阵信息表';

-- ----------------------------
-- Table structure for `ids_phalanx_info_t`
-- ----------------------------
drop table if exists ids_phalanx_info_t;
CREATE TABLE ids_phalanx_info_t
(
	id                   BIGINT(16) NOT NULL auto_increment COMMENT '主键id',
	phalanx_name            VARCHAR(255) NOT NULL COMMENT '方阵名称',
	station_code         VARCHAR(32) NOT NULL COMMENT '方阵编号',
	create_time          DATETIME DEFAULT NULL COMMENT '创建时间',
	create_user_id       BIGINT(16) DEFAULT NULL COMMENT '创建人id',
	update_time          DATETIME DEFAULT NULL COMMENT '创建时间',
	update_user_id       BIGINT(16) DEFAULT NULL COMMENT '创建人id',
	primary key (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='方阵信息表';

-- mqqt用户信息存储表信息
DROP TABLE IF EXISTS mqtt_user;
CREATE TABLE mqtt_user
(
   `id`                  INT(32) NOT null AUTO_INCREMENT comment '主键',
   `username`        	 VARCHAR(100) default NULL comment 'mqtt设备对应的parentSn号',
   `password`            VARCHAR(100) default NULL comment '密码信息',
   `salt`        		 VARCHAR(35) default NULL comment '密码盐值',
   `is_superuser`        TINYINT(1) null default 0 comment '是否是超级用户0: 否 1：是，默认否',
   `created`             datetime null default NULL comment '创建时间',
   `clientid`            varchar(100) default null comment '客户端id',
    PRIMARY KEY (`id`),
	UNIQUE INDEX `mqtt_username`(`username`) -- 数采parentSn唯一
)ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COMMENT='mqtt数采通讯的密码信息';
-- mqtt的mqtt_acl表
DROP TABLE IF EXISTS mqtt_acl;
CREATE TABLE mqtt_acl (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `allow` int(1) DEFAULT NULL COMMENT '0: deny, 1: allow',
  `ipaddr` varchar(60) DEFAULT NULL COMMENT 'IpAddress',
  `username` varchar(100) DEFAULT NULL COMMENT 'Username',
  `clientid` varchar(100) DEFAULT NULL COMMENT 'ClientId',
  `access` int(2) NOT NULL COMMENT '1: subscribe, 2: publish, 3: pubsub',
  `topic` varchar(100) NOT NULL DEFAULT '' COMMENT 'Topic Filter',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8;

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
	`exe_status` int(2) DEFAULT 0 COMMENT '执行的结果，0:未补采,创建默认, 1：补采中, 2：补采成功,  -1：补采失败',
	`collect_time` bigint(16) DEFAULT NULL COMMENT '当前补采进行到的时间节点',
	`kpi_re_comp_status` int(2) DEFAULT 0 COMMENT 'KPI重计算的状态  -1：失败，0：未开始，默认，1:成功',
	`create_time` bigint(16) DEFAULT NULL COMMENT '创建任务的时间',
	PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='数据补采记录创建了的补采任务表，记录设备的补采状态和确定是否执行kpi重计算的依据';

/*=====================================================*/
/*动态创建表：性能数据按天进行分表， kpi按每10天一张表*/
/*=====================================================*/

DROP FUNCTION IF EXISTS get_domain_parent_list;
DELIMITER $$
CREATE FUNCTION get_domain_parent_list (currentId VARCHAR(100),endParentId VARCHAR(100)) RETURNS VARCHAR (1000)
	BEGIN
	DECLARE pid VARCHAR (100) DEFAULT '';
	DECLARE parent_list VARCHAR (1000) DEFAULT currentId;
		WHILE currentId IS NOT NULL 
		DO
			SET pid = (SELECT parent_id FROM ids_sm_domain_info_t WHERE id = currentId);
			IF pid IS NOT NULL THEN
				SET parent_list = concat(parent_list, ',', pid);
				SET currentId = pid;
			ELSE
				SET currentId = pid;
			END IF;
			IF pid = endParentId THEN
				SET currentId = NULL;
			END IF;
		END WHILE;
	RETURN parent_list;
END $$
DELIMITER ;

DROP PROCEDURE IF EXISTS `procedure_sharding_table`;
DELIMITER $$
CREATE PROCEDURE `procedure_sharding_table`(`tableName` VARCHAR(255), `num` int, `tableIndex` int)
BEGIN
	declare i int;
	declare ntb varchar(255);
    set i = 1; 	 
    WHILE i<=num DO
    	set ntb = CONCAT(tableName, '_', CAST(tableIndex as char));
		SET @sql1 = CONCAT('drop table if exists ',ntb);
		PREPARE stmt FROM @sql1;  
		EXECUTE stmt;
		
		SET @sql2 = CONCAT('CREATE TABLE ', ntb, ' ( LIKE ', tableName, ');');
		PREPARE stmt FROM @sql2;
		EXECUTE stmt;
		
		DEALLOCATE PREPARE stmt;
        set tableIndex = tableIndex + 1; 
        SET i = i+1;
	END WHILE;
END $$
DELIMITER ;

-- 创建366 张表

call procedure_sharding_table('ids_inverter_string_data_t', 366, 1);
-- call procedure_sharding_table('ids_inverter_hoursehold_t', 366, 1);
call procedure_sharding_table('ids_inverter_conc_data_t', 366, 1);
call procedure_sharding_table('ids_meter_data_t', 366, 1);
-- call procedure_sharding_table('ids_transformer_data_t', 366, 1);
call procedure_sharding_table('ids_combiner_dc_data_t', 366, 1);
-- call procedure_sharding_table('ids_optimizer_t', 366, 1);

-- 创建37 张表
call procedure_sharding_table('ids_kpi_hour_inverter_t', 37, 1);
call procedure_sharding_table('ids_kpi_hour_meter_t', 37, 1);
call procedure_sharding_table('ids_kpi_hour_station_t', 37, 1);