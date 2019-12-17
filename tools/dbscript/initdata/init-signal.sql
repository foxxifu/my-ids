
-- ----------------------------
-- Records of ids_analysis_alarm_model_t
-- ----------------------------
INSERT INTO `ids_analysis_alarm_model_t` VALUES ('1', '电站停机', '1.检查电站是否拉闸关停；', '2', '（1）并网状态点断开；（2）分析周期内电站功率为0且所有箱变线电压为0；（3）分析周期内环境监测仪可用且最小辐照强度大于100W/m2 && 分析周期内电站功率为0；（4）分析周期内电站功率为0且所有逆变器直流电压大于400V，直流电流为0A');
INSERT INTO `ids_analysis_alarm_model_t` VALUES ('2', '箱变停机', '1.检查箱变是否严重告警；', '2', '分析周期内箱变AB、BC、CA线电压全为0 ');
INSERT INTO `ids_analysis_alarm_model_t` VALUES ('3', '集中式逆变器输出功率为0', '1、检查逆变器是否存在设备级别告警；\n2、检查逆变器是否停机', '2', '分析周期内集中式逆变器发电量小于等于0且最后一个采集时刻点逆变器输出功率小于等于0；');
INSERT INTO `ids_analysis_alarm_model_t` VALUES ('4', '集中式逆变器低效', '1.检查设备物理采集模块是否损坏、通信线缆是否损坏或干扰；\n2.检查逆变器是否存在漏电流现象，逆变器MPPT模块是否异常，逆变器转换效率是否偏低，逆变器自耗电是否过大；', '3', '逆变器非故障且偏离度小于低效门限；逆变器偏离度=1-|判定逆变器等效利用小时数-电站等效利用小时数| / 电站等效利用小时数');
INSERT INTO `ids_analysis_alarm_model_t` VALUES ('5', '集中式逆变异常', '1.检查设备物理采集模块是否损坏、通信线缆是否损坏或干扰；\n2.检查逆变器是否存在漏电流现象，逆变器MPPT模块是否异常，逆变器转换效率是否偏低，逆变器自耗电是否过大；\n3.检查监控装机容量配置是否错误或者接入的错误设备；', '3', null);
INSERT INTO `ids_analysis_alarm_model_t` VALUES ('6', '组串式逆变器输出功率为0', '1、检查逆变器是否存在设备级别告警；\n2、检查逆变器是否停机', '2', ' 当逆变器装机容量非0且不为空时：\r\n分析周期内逆变器发电量小于等于0且最后一个采集时刻点逆变器输出功率小于等于0；');
INSERT INTO `ids_analysis_alarm_model_t` VALUES ('7', '组串式逆变器所有PV支路电流为0', '1.检查设备物理采集模块是否损坏、通信线缆是否损坏或干扰；\n2.检查逆变器是否存在漏电流现象，逆变器MPPT模块是否异常；', '3', '当逆变器装机容量非0且不为空时：\r\n分析周期内逆变器下组串最大输出电流小于等于0.3；');
INSERT INTO `ids_analysis_alarm_model_t` VALUES ('8', '组串式逆变器所有PV支路电压为0', '1.检查设备物理采集模块是否损坏、通信线缆是否损坏或干扰；\n2.检查逆变器是否存在漏电流现象，逆变器MPPT模块是否异常；', '3', '当逆变器装机容量非0且不为空时：\r\n分析周期内逆变器下各组串最大输出电压之和小于等于0；');
INSERT INTO `ids_analysis_alarm_model_t` VALUES ('9', '组串式逆变器低效', '1.检查设备物理采集模块是否损坏、通信线缆是否损坏或干扰；\n2.检查逆变器是否存在漏电流现象，逆变器MPPT模块是否异常，逆变器转换效率是否偏低，逆变器自耗电是否过大；', '3', '逆变器非故障且偏离度小于低效门限；逆变器偏离度=1-|判定逆变器等效利用小时数-子阵等效利用小时数| / 子阵等效利用小时数');
INSERT INTO `ids_analysis_alarm_model_t` VALUES ('10', '组串式逆变器异常', '1.检查设备物理采集模块是否损坏、通信线缆是否损坏或干扰；\n2.检查逆变器是否存在漏电流现象，逆变器MPPT模块是否异常，逆变器转换效率是否偏低，逆变器自耗电是否过大；\n3.检查监控装机容量配置是否错误或者接入的错误设备；', '3', null);
INSERT INTO `ids_analysis_alarm_model_t` VALUES ('11', '组串式逆变器装机容量为0或空', '1.检查设备是否应该正常接入；\n2.检查设备是否已配置装机容量；', '3', '组串式逆变器装机容量为0或空');
INSERT INTO `ids_analysis_alarm_model_t` VALUES ('12', '直流汇流箱所有PV支路电流为0', '1.检查设备物理采集模块是否损坏、通信线缆是否损坏或干扰；\n2.检查汇流箱开路保险是否损坏；', '3', '当直流汇流箱装机容量非0且不为空时：\r\n分析周期内直流汇流箱组串最大输出电流小于等于0.3；');
INSERT INTO `ids_analysis_alarm_model_t` VALUES ('13', '直流汇流箱电压为0', '1.检查设备物理采集模块是否损坏、通信线缆是否损坏或干扰；\n2.检查汇流箱开路保险是否损坏；', '3', '当直流汇流箱装机容量非0且不为空时：\r\n分析周期内直流汇流箱最大光伏电压小于等于0');
INSERT INTO `ids_analysis_alarm_model_t` VALUES ('14', '直流汇流箱异常', '1.检查设备物理采集模块是否损坏、通信线缆是否损坏或干扰；\n2.检查汇流箱开路保险是否损坏；\n3.检查监控装机容量配置是否错误或者接入的错误设备；', '3', null);
INSERT INTO `ids_analysis_alarm_model_t` VALUES ('15', '直流汇流箱装机容量为0或空', '1.检查设备是否应该正常接入；\n2.检查设备是否已配置装机容量；', '3', '直流汇流箱装机容量为0或空');
INSERT INTO `ids_analysis_alarm_model_t` VALUES ('16', '组串式逆变器PVX支路断开', '1.检测组串间MC4插头是否虚接或损坏；\n2.检测光伏组串线路或组件本身是否损坏；', '2', '分析周期内逆变器最大直流电压>0 且 分析周期内集中式逆变器发电量 <= 0 且 最后一个采集时刻点逆变器输出功率 <= 0');
INSERT INTO `ids_analysis_alarm_model_t` VALUES ('17', '直流汇流箱PVX支路断开', '1.检查汇流箱开路保险是否损坏；\n2.检测组串间MC4插头是否虚接或损坏；\n3.检测光伏组串线路或组件本身是否损坏；', '2', '分析周期内逆变器最大PV支路电压>0 且 分析周期内组串式逆变器发电量 <= 0 且 最后一个采集时刻点逆变器输出功率 <= 0');
INSERT INTO `ids_analysis_alarm_model_t` VALUES ('18', '组串式逆变器PVX功率偏低', '检查光伏组串是否有局部组件热斑、隐裂；', '3', '汇流箱无死值 且 分析周期内汇流箱下所有组串最大电流 <= 0.3A');
INSERT INTO `ids_analysis_alarm_model_t` VALUES ('19', '直流汇流箱PVX功率偏低', '1.检查光伏组串是否存在遮挡；\n2.检查光伏组串是否灰尘严重；\n3.检查汇流箱开路电压是否偏低；', '3', '分析周期内PVX支路最大电流 <= 0.3A 且 离散率 > 10%');
INSERT INTO `ids_analysis_alarm_model_t` VALUES ('20', '集中式逆变器停机', '1、检查逆变器是否存在设备级别告警；\n2、检查逆变器是否停机', '2', '分析周期内PVX支路最大电流 <=0.3A 且 离散率 >10%');
INSERT INTO `ids_analysis_alarm_model_t` VALUES ('21', '组串式逆变器停机', '1、检查逆变器是否存在设备级别告警；\n2、检查逆变器是否停机', '2', '组串式逆变器PVX功率偏低（离散率>10% && |P-Avg(P)|/ Avg(P) >组串支路功率偏低阈值门限（默认20%））；功率取电流乘以电压');
INSERT INTO `ids_analysis_alarm_model_t` VALUES ('22', '直流汇流箱故障', '1.检查设备物理采集模块是否损坏、通信线缆是否损坏或干扰；\n2.检查汇流箱开路保险是否损坏；\n3.检查监控装机容量配置是否错误或者接入的错误设备；', '3', '直流汇流箱PVX功率偏低（离散率>10% && |P-Avg(P)|/ Avg(P) >组串支路功率偏低阈值门限（默认20%））；功率取电流乘以电压');
INSERT INTO `ids_analysis_alarm_model_t` VALUES ('23', '监控电站断连', '1.检查监控测服务是否开启；\n2.检查监控测是否修改了配置；\n3.检查监控与集维之间网络是否连通；', '1', '未检测到监控连接');


INSERT INTO `ids_alarm_model_t` VALUES ('1', null, null, '0', '65535', null, '1', null, '3', null, 'Equipment disconnection', 'Equipment disconnection', null, null, null, 'Please check whether there is a problem with the communication configuration between the equipment and the monitoring system or whether there is a communication failure problem with the equipment.', '1', '2', '0', '0', '0', '1', null, '2018-06-04 15:04:30');
-- INSERT INTO `ids_alarm_model_t` VALUES ('1', null, null, '0', '65535', null, '1', null, '3', null, '设备断链', '设备断链', null, null, null, '请检查设备与监控系统的通信配置是否有问题或者设备是否出现通信故障问题。', '1', '2', '0', '0', '0', '1', null, '2018-06-04 15:04:30');  安装中文版本时，采用手工的方式将该告警插入数据库，将原来英文的删掉。
