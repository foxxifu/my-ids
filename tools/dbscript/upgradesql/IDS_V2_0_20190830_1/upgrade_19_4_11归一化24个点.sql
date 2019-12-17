-- 组串式逆变器的24串
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
" ADD COLUMN `pv24_u` decimal(8,3) DEFAULT NULL AFTER `pv14_u`,
	ADD COLUMN `pv23_u` decimal(8,3) DEFAULT NULL AFTER `pv14_u`,
	ADD COLUMN `pv22_u` decimal(8,3) DEFAULT NULL AFTER `pv14_u`,
	ADD COLUMN `pv21_u` decimal(8,3) DEFAULT NULL AFTER `pv14_u`,
	ADD COLUMN `pv20_u` decimal(8,3) DEFAULT NULL AFTER `pv14_u`,
	ADD COLUMN `pv19_u` decimal(8,3) DEFAULT NULL AFTER `pv14_u`,
	ADD COLUMN `pv18_u` decimal(8,3) DEFAULT NULL AFTER `pv14_u`,
	ADD COLUMN `pv17_u` decimal(8,3) DEFAULT NULL AFTER `pv14_u`,
	ADD COLUMN `pv16_u` decimal(8,3) DEFAULT NULL AFTER `pv14_u`,
	ADD COLUMN `pv15_u` decimal(8,3) DEFAULT NULL AFTER `pv14_u`,
	ADD COLUMN `pv24_i` decimal(8,3) DEFAULT NULL AFTER `pv14_i`,
	ADD COLUMN `pv23_i` decimal(8,3) DEFAULT NULL AFTER `pv14_i`,
	ADD COLUMN `pv22_i` decimal(8,3) DEFAULT NULL AFTER `pv14_i`,
	ADD COLUMN `pv21_i` decimal(8,3) DEFAULT NULL AFTER `pv14_i`,
	ADD COLUMN `pv20_i` decimal(8,3) DEFAULT NULL AFTER `pv14_i`,
	ADD COLUMN `pv19_i` decimal(8,3) DEFAULT NULL AFTER `pv14_i`,
	ADD COLUMN `pv18_i` decimal(8,3) DEFAULT NULL AFTER `pv14_i`,
	ADD COLUMN `pv17_i` decimal(8,3) DEFAULT NULL AFTER `pv14_i`,
	ADD COLUMN `pv16_i` decimal(8,3) DEFAULT NULL AFTER `pv14_i`,
	ADD COLUMN `pv15_i` decimal(8,3) DEFAULT NULL AFTER `pv14_i`"
);
PREPARE stmt FROM @sqlstr;
EXECUTE stmt;
SET `@i` = `@i` + 1;
END WHILE;
END $$
DELIMITER ;

CALL alter_table_enegine();
DROP PROCEDURE alter_table_enegine;


ALTER TABLE ids_inverter_string_data_t 
ADD COLUMN `pv24_u` decimal(8,3) DEFAULT NULL AFTER `pv14_u`,
ADD COLUMN `pv23_u` decimal(8,3) DEFAULT NULL AFTER `pv14_u`,
ADD COLUMN `pv22_u` decimal(8,3) DEFAULT NULL AFTER `pv14_u`,
ADD COLUMN `pv21_u` decimal(8,3) DEFAULT NULL AFTER `pv14_u`,
ADD COLUMN `pv20_u` decimal(8,3) DEFAULT NULL AFTER `pv14_u`,
ADD COLUMN `pv19_u` decimal(8,3) DEFAULT NULL AFTER `pv14_u`,
ADD COLUMN `pv18_u` decimal(8,3) DEFAULT NULL AFTER `pv14_u`,
ADD COLUMN `pv17_u` decimal(8,3) DEFAULT NULL AFTER `pv14_u`,
ADD COLUMN `pv16_u` decimal(8,3) DEFAULT NULL AFTER `pv14_u`,
ADD COLUMN `pv15_u` decimal(8,3) DEFAULT NULL AFTER `pv14_u`,
ADD COLUMN `pv24_i` decimal(8,3) DEFAULT NULL AFTER `pv14_i`,
ADD COLUMN `pv23_i` decimal(8,3) DEFAULT NULL AFTER `pv14_i`,
ADD COLUMN `pv22_i` decimal(8,3) DEFAULT NULL AFTER `pv14_i`,
ADD COLUMN `pv21_i` decimal(8,3) DEFAULT NULL AFTER `pv14_i`,
ADD COLUMN `pv20_i` decimal(8,3) DEFAULT NULL AFTER `pv14_i`,
ADD COLUMN `pv19_i` decimal(8,3) DEFAULT NULL AFTER `pv14_i`,
ADD COLUMN `pv18_i` decimal(8,3) DEFAULT NULL AFTER `pv14_i`,
ADD COLUMN `pv17_i` decimal(8,3) DEFAULT NULL AFTER `pv14_i`,
ADD COLUMN `pv16_i` decimal(8,3) DEFAULT NULL AFTER `pv14_i`,
ADD COLUMN `pv15_i` decimal(8,3) DEFAULT NULL AFTER `pv14_i`;
-- 配置组串式的归一化配置
INSERT INTO `ids_normalized_model_t` (`id`, `dev_type`, `signal_name`, `display_name`, `column_name`, `is_persistent`, `order_num`, `create_date`, `modified_date`) VALUES ('270', '1', 'PV15输入电压', 'PV15输入电压', 'pv15_u', '1', '38', '2019-02-27 15:07:53', '2019-02-27 15:07:53');
INSERT INTO `ids_normalized_model_t` (`id`, `dev_type`, `signal_name`, `display_name`, `column_name`, `is_persistent`, `order_num`, `create_date`, `modified_date`) VALUES ('271', '1', 'PV16输入电压', 'PV15输入电压', 'pv16_u', '1', '38', '2019-02-27 15:07:53', '2019-02-27 15:07:53');
INSERT INTO `ids_normalized_model_t` (`id`, `dev_type`, `signal_name`, `display_name`, `column_name`, `is_persistent`, `order_num`, `create_date`, `modified_date`) VALUES ('272', '1', 'PV17输入电压', 'PV15输入电压', 'pv17_u', '1', '38', '2019-02-27 15:07:53', '2019-02-27 15:07:53');
INSERT INTO `ids_normalized_model_t` (`id`, `dev_type`, `signal_name`, `display_name`, `column_name`, `is_persistent`, `order_num`, `create_date`, `modified_date`) VALUES ('273', '1', 'PV18输入电压', 'PV15输入电压', 'pv18_u', '1', '38', '2019-02-27 15:07:53', '2019-02-27 15:07:53');
INSERT INTO `ids_normalized_model_t` (`id`, `dev_type`, `signal_name`, `display_name`, `column_name`, `is_persistent`, `order_num`, `create_date`, `modified_date`) VALUES ('274', '1', 'PV19输入电压', 'PV15输入电压', 'pv19_u', '1', '38', '2019-02-27 15:07:53', '2019-02-27 15:07:53');
INSERT INTO `ids_normalized_model_t` (`id`, `dev_type`, `signal_name`, `display_name`, `column_name`, `is_persistent`, `order_num`, `create_date`, `modified_date`) VALUES ('275', '1', 'PV20输入电压', 'PV15输入电压', 'pv20_u', '1', '38', '2019-02-27 15:07:53', '2019-02-27 15:07:53');
INSERT INTO `ids_normalized_model_t` (`id`, `dev_type`, `signal_name`, `display_name`, `column_name`, `is_persistent`, `order_num`, `create_date`, `modified_date`) VALUES ('276', '1', 'PV21输入电压', 'PV15输入电压', 'pv21_u', '1', '38', '2019-02-27 15:07:53', '2019-02-27 15:07:53');
INSERT INTO `ids_normalized_model_t` (`id`, `dev_type`, `signal_name`, `display_name`, `column_name`, `is_persistent`, `order_num`, `create_date`, `modified_date`) VALUES ('277', '1', 'PV22输入电压', 'PV15输入电压', 'pv22_u', '1', '38', '2019-02-27 15:07:53', '2019-02-27 15:07:53');
INSERT INTO `ids_normalized_model_t` (`id`, `dev_type`, `signal_name`, `display_name`, `column_name`, `is_persistent`, `order_num`, `create_date`, `modified_date`) VALUES ('278', '1', 'PV23输入电压', 'PV15输入电压', 'pv23_u', '1', '38', '2019-02-27 15:07:53', '2019-02-27 15:07:53');
INSERT INTO `ids_normalized_model_t` (`id`, `dev_type`, `signal_name`, `display_name`, `column_name`, `is_persistent`, `order_num`, `create_date`, `modified_date`) VALUES ('279', '1', 'PV24输入电压', 'PV15输入电压', 'pv24_u', '1', '38', '2019-02-27 15:07:53', '2019-02-27 15:07:53');

INSERT INTO `ids_normalized_model_t` (`id`, `dev_type`, `signal_name`, `display_name`, `column_name`, `is_persistent`, `order_num`, `create_date`, `modified_date`) VALUES ('280', '1', 'PV15输入电流', 'PV15输入电流', 'pv15_i', '1', '52', '2019-02-27 15:08:32', '2019-02-27 15:08:32');
INSERT INTO `ids_normalized_model_t` (`id`, `dev_type`, `signal_name`, `display_name`, `column_name`, `is_persistent`, `order_num`, `create_date`, `modified_date`) VALUES ('281', '1', 'PV16输入电流', 'PV16输入电流', 'pv16_i', '1', '52', '2019-02-27 15:08:32', '2019-02-27 15:08:32');
INSERT INTO `ids_normalized_model_t` (`id`, `dev_type`, `signal_name`, `display_name`, `column_name`, `is_persistent`, `order_num`, `create_date`, `modified_date`) VALUES ('282', '1', 'PV17输入电流', 'PV17输入电流', 'pv17_i', '1', '52', '2019-02-27 15:08:32', '2019-02-27 15:08:32');
INSERT INTO `ids_normalized_model_t` (`id`, `dev_type`, `signal_name`, `display_name`, `column_name`, `is_persistent`, `order_num`, `create_date`, `modified_date`) VALUES ('283', '1', 'PV18输入电流', 'PV18输入电流', 'pv18_i', '1', '52', '2019-02-27 15:08:32', '2019-02-27 15:08:32');
INSERT INTO `ids_normalized_model_t` (`id`, `dev_type`, `signal_name`, `display_name`, `column_name`, `is_persistent`, `order_num`, `create_date`, `modified_date`) VALUES ('284', '1', 'PV19输入电流', 'PV19输入电流', 'pv19_i', '1', '52', '2019-02-27 15:08:32', '2019-02-27 15:08:32');
INSERT INTO `ids_normalized_model_t` (`id`, `dev_type`, `signal_name`, `display_name`, `column_name`, `is_persistent`, `order_num`, `create_date`, `modified_date`) VALUES ('285', '1', 'PV20输入电流', 'PV20输入电流', 'pv20_i', '1', '52', '2019-02-27 15:08:32', '2019-02-27 15:08:32');
INSERT INTO `ids_normalized_model_t` (`id`, `dev_type`, `signal_name`, `display_name`, `column_name`, `is_persistent`, `order_num`, `create_date`, `modified_date`) VALUES ('286', '1', 'PV21输入电流', 'PV21输入电流', 'pv21_i', '1', '52', '2019-02-27 15:08:32', '2019-02-27 15:08:32');
INSERT INTO `ids_normalized_model_t` (`id`, `dev_type`, `signal_name`, `display_name`, `column_name`, `is_persistent`, `order_num`, `create_date`, `modified_date`) VALUES ('287', '1', 'PV22输入电流', 'PV22输入电流', 'pv22_i', '1', '52', '2019-02-27 15:08:32', '2019-02-27 15:08:32');
INSERT INTO `ids_normalized_model_t` (`id`, `dev_type`, `signal_name`, `display_name`, `column_name`, `is_persistent`, `order_num`, `create_date`, `modified_date`) VALUES ('288', '1', 'PV23输入电流', 'PV23输入电流', 'pv23_i', '1', '52', '2019-02-27 15:08:32', '2019-02-27 15:08:32');
INSERT INTO `ids_normalized_model_t` (`id`, `dev_type`, `signal_name`, `display_name`, `column_name`, `is_persistent`, `order_num`, `create_date`, `modified_date`) VALUES ('289', '1', 'PV24输入电流', 'PV24输入电流', 'pv24_i', '1', '52', '2019-02-27 15:08:32', '2019-02-27 15:08:32');




-- 集中式逆变器

DROP PROCEDURE IF EXISTS alter_table_conc_enegine;
DELIMITER $$
CREATE PROCEDURE alter_table_conc_enegine()
BEGIN
DECLARE `@i` int(11);
DECLARE `@sqlstr` VARCHAR(2048);
SET `@i` = 1 ;
WHILE `@i` < 367 DO
SET @sqlstr = CONCAT(
"ALTER TABLE ids_inverter_conc_data_t_",
`@i`,
" ADD COLUMN `center_i_24` decimal(8,3) DEFAULT NULL AFTER `center_i_10`,
	ADD COLUMN `center_i_23` decimal(8,3) DEFAULT NULL AFTER `center_i_10`,
	ADD COLUMN `center_i_22` decimal(8,3) DEFAULT NULL AFTER `center_i_10`,
	ADD COLUMN `center_i_21` decimal(8,3) DEFAULT NULL AFTER `center_i_10`,
	ADD COLUMN `center_i_20` decimal(8,3) DEFAULT NULL AFTER `center_i_10`,
	ADD COLUMN `center_i_19` decimal(8,3) DEFAULT NULL AFTER `center_i_10`,
	ADD COLUMN `center_i_18` decimal(8,3) DEFAULT NULL AFTER `center_i_10`,
	ADD COLUMN `center_i_17` decimal(8,3) DEFAULT NULL AFTER `center_i_10`,
	ADD COLUMN `center_i_16` decimal(8,3) DEFAULT NULL AFTER `center_i_10`,
	ADD COLUMN `center_i_15` decimal(8,3) DEFAULT NULL AFTER `center_i_10`,
	ADD COLUMN `center_i_14` decimal(8,3) DEFAULT NULL AFTER `center_i_10`,
	ADD COLUMN `center_i_13` decimal(8,3) DEFAULT NULL AFTER `center_i_10`,
	ADD COLUMN `center_i_12` decimal(8,3) DEFAULT NULL AFTER `center_i_10`,
	ADD COLUMN `center_i_11` decimal(8,3) DEFAULT NULL AFTER `center_i_10`"
);
PREPARE stmt FROM @sqlstr;
EXECUTE stmt;
SET `@i` = `@i` + 1;
END WHILE;
END $$
DELIMITER ;

CALL alter_table_conc_enegine();
DROP PROCEDURE alter_table_conc_enegine;



ALTER TABLE ids_inverter_conc_data_t 
ADD COLUMN `center_i_24` decimal(8,3) DEFAULT NULL AFTER `center_i_10`,
ADD COLUMN `center_i_23` decimal(8,3) DEFAULT NULL AFTER `center_i_10`,
ADD COLUMN `center_i_22` decimal(8,3) DEFAULT NULL AFTER `center_i_10`,
ADD COLUMN `center_i_21` decimal(8,3) DEFAULT NULL AFTER `center_i_10`,
ADD COLUMN `center_i_20` decimal(8,3) DEFAULT NULL AFTER `center_i_10`,
ADD COLUMN `center_i_19` decimal(8,3) DEFAULT NULL AFTER `center_i_10`,
ADD COLUMN `center_i_18` decimal(8,3) DEFAULT NULL AFTER `center_i_10`,
ADD COLUMN `center_i_17` decimal(8,3) DEFAULT NULL AFTER `center_i_10`,
ADD COLUMN `center_i_16` decimal(8,3) DEFAULT NULL AFTER `center_i_10`,
ADD COLUMN `center_i_15` decimal(8,3) DEFAULT NULL AFTER `center_i_10`,
ADD COLUMN `center_i_14` decimal(8,3) DEFAULT NULL AFTER `center_i_10`,
ADD COLUMN `center_i_13` decimal(8,3) DEFAULT NULL AFTER `center_i_10`,
ADD COLUMN `center_i_12` decimal(8,3) DEFAULT NULL AFTER `center_i_10`,
ADD COLUMN `center_i_11` decimal(8,3) DEFAULT NULL AFTER `center_i_10`;

-- 集中式逆变器的归一化
INSERT INTO `ids_normalized_model_t` (`id`, `dev_type`, `signal_name`, `display_name`, `column_name`, `is_persistent`, `order_num`, `create_date`, `modified_date`) VALUES ('290', '14', '第11路电流值', '第11路电流值', 'center_i_11', '1', '22', '2019-02-27 15:22:43', '2019-02-27 15:22:43');
INSERT INTO `ids_normalized_model_t` (`id`, `dev_type`, `signal_name`, `display_name`, `column_name`, `is_persistent`, `order_num`, `create_date`, `modified_date`) VALUES ('291', '14', '第12路电流值', '第12路电流值', 'center_i_12', '1', '22', '2019-02-27 15:22:43', '2019-02-27 15:22:43');
INSERT INTO `ids_normalized_model_t` (`id`, `dev_type`, `signal_name`, `display_name`, `column_name`, `is_persistent`, `order_num`, `create_date`, `modified_date`) VALUES ('292', '14', '第13路电流值', '第13路电流值', 'center_i_13', '1', '22', '2019-02-27 15:22:43', '2019-02-27 15:22:43');
INSERT INTO `ids_normalized_model_t` (`id`, `dev_type`, `signal_name`, `display_name`, `column_name`, `is_persistent`, `order_num`, `create_date`, `modified_date`) VALUES ('293', '14', '第14路电流值', '第14路电流值', 'center_i_14', '1', '22', '2019-02-27 15:22:43', '2019-02-27 15:22:43');
INSERT INTO `ids_normalized_model_t` (`id`, `dev_type`, `signal_name`, `display_name`, `column_name`, `is_persistent`, `order_num`, `create_date`, `modified_date`) VALUES ('294', '14', '第15路电流值', '第15路电流值', 'center_i_15', '1', '22', '2019-02-27 15:22:43', '2019-02-27 15:22:43');
INSERT INTO `ids_normalized_model_t` (`id`, `dev_type`, `signal_name`, `display_name`, `column_name`, `is_persistent`, `order_num`, `create_date`, `modified_date`) VALUES ('295', '14', '第16路电流值', '第16路电流值', 'center_i_16', '1', '22', '2019-02-27 15:22:43', '2019-02-27 15:22:43');
INSERT INTO `ids_normalized_model_t` (`id`, `dev_type`, `signal_name`, `display_name`, `column_name`, `is_persistent`, `order_num`, `create_date`, `modified_date`) VALUES ('296', '14', '第17路电流值', '第17路电流值', 'center_i_17', '1', '22', '2019-02-27 15:22:43', '2019-02-27 15:22:43');
INSERT INTO `ids_normalized_model_t` (`id`, `dev_type`, `signal_name`, `display_name`, `column_name`, `is_persistent`, `order_num`, `create_date`, `modified_date`) VALUES ('297', '14', '第18路电流值', '第18路电流值', 'center_i_18', '1', '22', '2019-02-27 15:22:43', '2019-02-27 15:22:43');
INSERT INTO `ids_normalized_model_t` (`id`, `dev_type`, `signal_name`, `display_name`, `column_name`, `is_persistent`, `order_num`, `create_date`, `modified_date`) VALUES ('298', '14', '第19路电流值', '第19路电流值', 'center_i_19', '1', '22', '2019-02-27 15:22:43', '2019-02-27 15:22:43');
INSERT INTO `ids_normalized_model_t` (`id`, `dev_type`, `signal_name`, `display_name`, `column_name`, `is_persistent`, `order_num`, `create_date`, `modified_date`) VALUES ('299', '14', '第20路电流值', '第20路电流值', 'center_i_20', '1', '22', '2019-02-27 15:22:43', '2019-02-27 15:22:43');
INSERT INTO `ids_normalized_model_t` (`id`, `dev_type`, `signal_name`, `display_name`, `column_name`, `is_persistent`, `order_num`, `create_date`, `modified_date`) VALUES ('300', '14', '第21路电流值', '第21路电流值', 'center_i_21', '1', '22', '2019-02-27 15:22:43', '2019-02-27 15:22:43');
INSERT INTO `ids_normalized_model_t` (`id`, `dev_type`, `signal_name`, `display_name`, `column_name`, `is_persistent`, `order_num`, `create_date`, `modified_date`) VALUES ('301', '14', '第22路电流值', '第22路电流值', 'center_i_22', '1', '22', '2019-02-27 15:22:43', '2019-02-27 15:22:43');
INSERT INTO `ids_normalized_model_t` (`id`, `dev_type`, `signal_name`, `display_name`, `column_name`, `is_persistent`, `order_num`, `create_date`, `modified_date`) VALUES ('302', '14', '第23路电流值', '第23路电流值', 'center_i_23', '1', '22', '2019-02-27 15:22:43', '2019-02-27 15:22:43');
INSERT INTO `ids_normalized_model_t` (`id`, `dev_type`, `signal_name`, `display_name`, `column_name`, `is_persistent`, `order_num`, `create_date`, `modified_date`) VALUES ('303', '14', '第24路电流值', '第24路电流值', 'center_i_24', '1', '22', '2019-02-27 15:22:43', '2019-02-27 15:22:43');



-- 直流汇流箱

DROP PROCEDURE IF EXISTS alter_table_dc_enegine;
DELIMITER $$
CREATE PROCEDURE alter_table_dc_enegine()
BEGIN
DECLARE `@i` int(11);
DECLARE `@sqlstr` VARCHAR(2048);
SET `@i` = 1 ;
WHILE `@i` < 367 DO
SET @sqlstr = CONCAT(
"ALTER TABLE ids_combiner_dc_data_t_",
`@i`,
" ADD COLUMN `dc_i24` decimal(10,3) DEFAULT NULL AFTER `dc_i20`,
	ADD COLUMN `dc_i23` decimal(10,3) DEFAULT NULL AFTER `dc_i20`,
	ADD COLUMN `dc_i22` decimal(10,3) DEFAULT NULL AFTER `dc_i20`,
	ADD COLUMN `dc_i21` decimal(10,3) DEFAULT NULL AFTER `dc_i20`"
);
PREPARE stmt FROM @sqlstr;
EXECUTE stmt;
SET `@i` = `@i` + 1;
END WHILE;
END $$
DELIMITER ;

CALL alter_table_dc_enegine();
DROP PROCEDURE alter_table_dc_enegine;



ALTER TABLE ids_combiner_dc_data_t 
ADD COLUMN `dc_i24` decimal(10,3) DEFAULT NULL AFTER `dc_i20`,
ADD COLUMN `dc_i23` decimal(10,3) DEFAULT NULL AFTER `dc_i20`,
ADD COLUMN `dc_i22` decimal(10,3) DEFAULT NULL AFTER `dc_i20`,
ADD COLUMN `dc_i21` decimal(10,3) DEFAULT NULL AFTER `dc_i20`;


-- 直流汇流箱的容量
INSERT INTO `ids_normalized_model_t` (`id`, `dev_type`, `signal_name`, `display_name`, `column_name`, `is_persistent`, `order_num`, `create_date`, `modified_date`) VALUES ('304', '15', '第21路电流值', '第21路电流值', 'dc_i21', '1', '26', '2019-02-27 15:28:04', '2019-02-27 15:28:04');
INSERT INTO `ids_normalized_model_t` (`id`, `dev_type`, `signal_name`, `display_name`, `column_name`, `is_persistent`, `order_num`, `create_date`, `modified_date`) VALUES ('305', '15', '第22路电流值', '第22路电流值', 'dc_i22', '1', '26', '2019-02-27 15:28:04', '2019-02-27 15:28:04');
INSERT INTO `ids_normalized_model_t` (`id`, `dev_type`, `signal_name`, `display_name`, `column_name`, `is_persistent`, `order_num`, `create_date`, `modified_date`) VALUES ('306', '15', '第23路电流值', '第23路电流值', 'dc_i23', '1', '26', '2019-02-27 15:28:04', '2019-02-27 15:28:04');
INSERT INTO `ids_normalized_model_t` (`id`, `dev_type`, `signal_name`, `display_name`, `column_name`, `is_persistent`, `order_num`, `create_date`, `modified_date`) VALUES ('307', '15', '第24路电流值', '第24路电流值', 'dc_i24', '1', '26', '2019-02-27 15:28:04', '2019-02-27 15:28:04');