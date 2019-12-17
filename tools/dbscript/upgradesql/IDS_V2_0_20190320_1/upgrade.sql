insert into ids_sm_resource_info_t(resource_key,auth_id,resource_name,description,auth_url,request_type) values (REPLACE(UUID(), '-', ''),1012,"查询逆变器详细报表的数据","查询逆变器详细报表的数据",'/biz/produceMng/listInverterDetailRpt','POST');	   
insert into ids_sm_resource_info_t(resource_key,auth_id,resource_name,description,auth_url,request_type) values (REPLACE(UUID(), '-', ''),1012,"导出逆变器详细报表的数据","导出逆变器详细报表的数据",'/biz/produceMng/exportInverterDetailRpt','GET');	   
insert into ids_sm_resource_info_t(resource_key,auth_id,resource_name,description,auth_url,request_type) values (REPLACE(UUID(), '-', ''),1012,"电站详细逆变器数据","电站详细逆变器数据",'/biz/produceMng/listInverterDevices','POST');
insert into ids_sm_resource_info_t(resource_key,auth_id,resource_name,description,auth_url,request_type) values (REPLACE(UUID(), '-', ''),1024,"获取MQQT的版本信息","获取MQQT的版本信息",'/biz/devAccess/getMqqtVersionList','POST');
insert into ids_sm_resource_info_t(resource_key,auth_id,resource_name,description,auth_url,request_type) values (REPLACE(UUID(), '-', ''),1024,"新增mqqt设备","新增mqqt设备",'/biz/devAccess/insertMqqtDevInfo','POST');
insert into ids_sm_resource_info_t(resource_key,auth_id,resource_name,description,auth_url,request_type) values (REPLACE(UUID(), '-', ''),1024,"获取mqqt的数采SN号列表","获取mqqt的数采SN号列表",'/biz/devAccess/getMqqtParentSnList','GET');


-- 修改表结构 ids_inverter_string_data_t_1...366
DROP PROCEDURE IF EXISTS alter_table_enegine;
DELIMITER $$
CREATE PROCEDURE alter_table_enegine()
BEGIN
DECLARE `@i` int(11);
DECLARE `@sqlstr` VARCHAR(2048);
SET `@i` = 1 ;
WHILE `@i` < 367 DO
SET @sqlstr = CONCAT(
"ALTER TABLE ids_inverter_string_data_t_",
`@i`,
" ADD COLUMN `dev_sn` varchar(64) NULL COMMENT '设备sn号' AFTER `mppt_4_cap`"
);
PREPARE stmt FROM @sqlstr;
EXECUTE stmt;
SET `@i` = `@i` + 1;
END WHILE;
END $$
DELIMITER ;

CALL alter_table_enegine();
DROP PROCEDURE alter_table_enegine;
ALTER TABLE ids_inverter_string_data_t ADD COLUMN `dev_sn` varchar(64) NULL COMMENT '设备sn号' AFTER `mppt_4_cap`;

-- 添加企业的logo字段
ALTER TABLE `ids_sm_enterprise_info_t`
ADD COLUMN `logo`  varchar(255) NULL DEFAULT NULL COMMENT '企业LOGO' AFTER `radius`;

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

-- mqtt的初始化语句
INSERT INTO `mqtt_acl` VALUES ('1', '1', null, '$all', null, '2', '#');
INSERT INTO `mqtt_acl` VALUES ('2', '0', null, '$all', null, '1', '$SYS/#');
INSERT INTO `mqtt_acl` VALUES ('3', '0', null, '$all', null, '1', 'eq #');
INSERT INTO `mqtt_acl` VALUES ('5', '1', '127.0.0.1', null, null, '2', '$SYS/#');
INSERT INTO `mqtt_acl` VALUES ('6', '1', '127.0.0.1', null, null, '2', '#');
INSERT INTO `mqtt_acl` VALUES ('7', '1', null, 'dashboard', null, '1', '$SYS/#');

	   