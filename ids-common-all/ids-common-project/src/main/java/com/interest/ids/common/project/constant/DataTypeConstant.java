package com.interest.ids.common.project.constant;

import java.util.HashMap;
import java.util.Map;


public class DataTypeConstant {

	// 系统字典区分
	/**
	 * 无符号整形
	 */
	public static final Integer UINT16 = 1;

	/**
	 * 字符串'
	 */
	public static final Integer STRING = 2;

	/**
	 * 无符号长整形
	 */
	public static final Integer UINT32 = 3;

	/**
	 * 整形
	 */
	public static final Integer INT16 = 4;

	/**
	 * 长整形
	 */
	public static final Integer INT32 = 5;

	/**
	 * 空值
	 */
	public static final Integer NULLDATA = 6;

	/**
	 * 日期
	 */
	public static final Integer EPOCHTIME = 7;

	/**
	 * bit位
	 */
	public static final Integer BIT = 8;

	/**
	 * 浮点数
	 */
	public static final Integer FLOAT = 9;

	// byte解析时使用的常量
	/**
	 * 无符号
	 */
	public static final int ParaTypeUnsigned = 0;

	/**
	 * 有符号类型
	 */
	public static final int ParaTypeSigned = 1;

	/**
	 * 浮点数
	 */
	public static final int ParaTypeFloat = 2;

	/**
	 * 字符串类型
	 */
	public static final int ParaTypeString = 3;

	/**
	 * 纪元秒的最大值，是非法的的
	 */
	public static final long MAX_EPOCHTIME = 4294967295l;

	/**
	 * 纪元秒的最小值，非法的
	 */
	public static final long MIN_EPOCHTIME = 0l;

	/**
	 * 字符串最大长度
	 */
	public static final int VARCHAR_LENGTH_MAX = 64;

	/**
	 * 浮点型能入库的有效值，非法的
	 */
	public static final Double DECM_MAX = 999999999999.9999D;
	
	/**
	 * 浮点型能入库的有效值，非法的
	 */
	public static final Double DECM_MIN = -99999999999.9999D;

	/**
	 * 增益类型为乘法
	 */
	public static final Integer DATA_SIGN_TYPE_X = 1;
	
	/**
	 * 增益类型：除法
	 */
	public final static Integer DATA_SIGN_TYPE_C = 2;

	@SuppressWarnings("serial")
	public static final Map<String, Integer> DATA_TYPE = new HashMap<String, Integer>(){{
		put("UINT16",  UINT16);
		put("STRING",  STRING);
		put("UINT32",  UINT32);
		put("INT16",  INT16);
		put("INT32",  INT32);
		put("NULLDATA",  NULLDATA);
		put("EPOCHTIME",  EPOCHTIME);
		put("BIT",  BIT);
		put("FLOAT",  FLOAT);
	}};

}
