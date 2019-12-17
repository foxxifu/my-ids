package com.interest.ids.common.project.constant;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 
 * @author lhq
 *
 *
 */
public class DevTypeConstant {

	// 逆变器设备类型
	public static final Integer INVERTER_DEV_TYPE = 1;
	// 数采设备类型
	public static final Integer DC_DEV_TYPE = 2;
	//数据采集棒
	public static final Integer DC_ROD_TYPE = 3;
	//上能数采带网闸
	public static final Integer SN_DC_GATEKEEPER_TYPE = 4;
	//上能数采无网闸
	public static final Integer SN_DC_TYPE = 5;

	// 箱变
	public static final Integer BOX_DEV_TYPE = 8;

	/**
	 * 箱变电表
	 */
	public static final Integer BOXTRANFORM_DEV_TYPE_ID = 9;
	/**
	 * 环境监测仪
	 */
	public static final Integer EMI_DEV_TYPE_ID = 10;

	// 交流汇流箱
	public static final Integer ACJS_DEV_TYPE = 11;

	// 通管机
	public static final Integer MULTIPURPOSE_DEV_TYPE = 13;

	// 集中式逆变器
	public static final Integer CENTER_INVERT_DEV_TYPE = 14;

	// 直流汇流箱
	public static final Integer DCJS_DEV_TYPE = 15;

	/**
	 * 关口电表的devtypeid
	 */
	public static final Integer GATEWAYMETER_DEV_TYPE_ID = 17;
	/**
	 * 汇集站线路电表的devtypeid
	 */
	public static final Integer COLSTAMETER_DEV_TYPE_ID = 18;
	/**
	 * 厂用电生产区电表的devtypeid
	 */
	public static final Integer PRODUCTIONMETER_DEV_TYPE_ID = 19;
	/**
	 * 厂用电非生产区电表的devtypeid
	 */
	public static final Integer NONPRODUCTIONMETER_DEV_TYPE_ID = 21;

	// pid设备
	public static final Integer PID_DEV_TYPE = 22;

	
	/**
	 * 电能质量装置的devtypeid
	 */
	public static final Integer POWERQUALITY_DEV_TYPE_ID = 24;
	/**
	 * 升压变的devtypeid
	 */
	public static final Integer BOOSTTRANSFORMER_DEV_TYPE_ID = 25;
	/**
	 * 光伏并网柜的devtypeid
	 */
	public static final Integer PHOCABINET_DEV_TYPE_ID = 26;
	/**
	 * 光伏并网屏的devtypeid
	 */
	public static final Integer PHOSCREEN_DEV_TYPE_ID = 27;
	// 铁牛数采
	public static final Integer TN_DAU = 37;
	//采集棒
	public static final Integer COLLCUD=50;

	// 户用逆变器
	public static final Integer HOUSEHOLD_INVERTER_DEV_TYPE = 38;
	// 户用储能
	public static final Integer HOUSEHOLD_CN = 39;
	// 户用电表
	public static final Integer HOUSEHOLD_METER = 47;
	/** MQTT协议类型 */
	public static final String MQTT="MQTT";
	/** MODBUS协议类型 */
	public static final String MODBUS="MODBUS";
	

	/**
	 * Map<String, String>-><设备类型id, epms设备类型id>
	 */
	public static final Map<String, String> IEMS_DEV_TYPE_MAPPING_EMPS_DEV_TYPE = new HashMap<String, String>() {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		{
			put(INVERTER_DEV_TYPE.toString(), "1"); // 组串式逆变器
			put(BOX_DEV_TYPE.toString(), "8"); // 箱变
			put(EMI_DEV_TYPE_ID.toString(), "10"); // 环境监测仪
			put(CENTER_INVERT_DEV_TYPE.toString(), "14"); // 集中式逆变器
			put(DCJS_DEV_TYPE.toString(), "15"); // 直流汇流箱
			put(GATEWAYMETER_DEV_TYPE_ID.toString(), "17"); // 关口电表
		}
	};

	/**
	 * i18n 设备类型
	 */
	public static final Map<String, Integer> DEV_TYPE_I18N = new HashMap<String, Integer>() {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		{
			put("数采", DC_DEV_TYPE);
			put("通管机", MULTIPURPOSE_DEV_TYPE);
			put("交流汇流箱", ACJS_DEV_TYPE);
			put("直流汇流箱", DCJS_DEV_TYPE);
			put("关口电表", GATEWAYMETER_DEV_TYPE_ID);
			put("环境监测仪", EMI_DEV_TYPE_ID);
			put("汇集站线路电表", COLSTAMETER_DEV_TYPE_ID);
			put("厂用电生产区电表", PRODUCTIONMETER_DEV_TYPE_ID);
			put("厂用电非生产区电表", NONPRODUCTIONMETER_DEV_TYPE_ID);
			put("箱变电表", BOXTRANFORM_DEV_TYPE_ID);
			put("箱变", BOX_DEV_TYPE);
			put("集中式逆变器", CENTER_INVERT_DEV_TYPE);
			put("组串式逆变器", INVERTER_DEV_TYPE);
			put("大机", CENTER_INVERT_DEV_TYPE);
			put("pid设备", PID_DEV_TYPE);
			put("电能质量装置", POWERQUALITY_DEV_TYPE_ID);
			put("升压变", BOOSTTRANSFORMER_DEV_TYPE_ID);
			put("光伏并网柜", PHOCABINET_DEV_TYPE_ID);
			put("光伏并网屏", PHOSCREEN_DEV_TYPE_ID);
			put("铁牛数采", TN_DAU);
			put("户用逆变器", HOUSEHOLD_INVERTER_DEV_TYPE);
			put("户用储能", HOUSEHOLD_CN);
			put("户用电表", HOUSEHOLD_METER);
		}
	};
	// 做归一化的数据的集合
	public static final List<Integer> UNIFICATION_DEV_TYPE = Arrays.asList(INVERTER_DEV_TYPE,
			CENTER_INVERT_DEV_TYPE, EMI_DEV_TYPE_ID, DCJS_DEV_TYPE, GATEWAYMETER_DEV_TYPE_ID,
			BOX_DEV_TYPE, DC_DEV_TYPE, MULTIPURPOSE_DEV_TYPE, TN_DAU
			);
//	public static final Map<String, Integer> UNIFICATION_DEV_TYPE = new HashMap<String, Integer>() {
//		/**
//		 * 
//		 */
//		private static final long serialVersionUID = 1L;
//
//		{
//			put("组串式逆变器", INVERTER_DEV_TYPE);
//			put("集中式逆变器", CENTER_INVERT_DEV_TYPE);
//			put("环境监测仪", EMI_DEV_TYPE_ID);
//			put("直流汇流箱", DCJS_DEV_TYPE);
//			put("电表", GATEWAYMETER_DEV_TYPE_ID);
//			put("箱变", BOX_DEV_TYPE);
//			put("数采", DC_DEV_TYPE);
//			put("通管机", MULTIPURPOSE_DEV_TYPE);
//			put("铁牛数采", TN_DAU);
//		}
//	};

	/**
	 * i18n 设备类型
	 */
	public static final Map<Integer, String> DEV_TYPE_I18N_ID = new HashMap<Integer, String>() {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		{
			put(DC_DEV_TYPE, "数采");
			put(MULTIPURPOSE_DEV_TYPE, "通管机");
			put(ACJS_DEV_TYPE, "交流汇流箱");
			put(DCJS_DEV_TYPE, "直流汇流箱");
			put(GATEWAYMETER_DEV_TYPE_ID, "关口电表");
			put(EMI_DEV_TYPE_ID, "环境监测仪");
			put(COLSTAMETER_DEV_TYPE_ID, "汇集站线路电表");
			put(PRODUCTIONMETER_DEV_TYPE_ID, "厂用电生产区电表");
			put(NONPRODUCTIONMETER_DEV_TYPE_ID, "厂用电非生产区电表");
			put(BOXTRANFORM_DEV_TYPE_ID, "箱变电表");
			put(BOX_DEV_TYPE, "箱变");
			put(CENTER_INVERT_DEV_TYPE, "集中式逆变器");
			put(INVERTER_DEV_TYPE, "组串式逆变器");
			put(CENTER_INVERT_DEV_TYPE, "大机");
			put(PID_DEV_TYPE, "pid设备");
			put(POWERQUALITY_DEV_TYPE_ID, "电能质量装置");
			put(BOOSTTRANSFORMER_DEV_TYPE_ID, "升压变");
			put(PHOCABINET_DEV_TYPE_ID, "光伏并网柜");
			put(PHOSCREEN_DEV_TYPE_ID, "光伏并网屏");
			put(TN_DAU, "铁牛数采");
			put(HOUSEHOLD_INVERTER_DEV_TYPE, "户用逆变器");
			put(HOUSEHOLD_CN, "户用储能");
			put(HOUSEHOLD_METER, "户用电表");
		}
	};
}
