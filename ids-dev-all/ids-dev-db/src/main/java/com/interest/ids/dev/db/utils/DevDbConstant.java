package com.interest.ids.dev.db.utils;

/**
 * 
 * @author lhq
 *
 *
 */
public final class DevDbConstant {
	
	//数据解析的MEMORY，默认为50M
	public static final long DEFAULT_PARSE_MEMORY = 50 * 1024 *1024;
	
	/**
	 * 补采数据
	 */
	public static final Long DATA_SUPPLEMENT_ACQUIRE = 15L;
	
	/**
	 * 正常采集数据
	 */
	public static final Long DATA_NORMAL_ACQUIRE = 16L;
	
	
	//0x10_1071_255.bin
	public static final String BIN_LONG_START_PROFIX = "0x10_";
	
	public static final String BIN_LONG_END_PROFIX = "_255.bin";
	//组串式逆变器的数据表
	public static final String INVERTER_STRING_TABLE = "ids_inverter_string_data_t";
	
	public static final String EMI_TYPE_STRINF = "environment";
	
	public static final String INVERTER_TYPE_STRING = "inverter";
	
	public static final String COMBINER_TYPE_STRING = "combiner";
	
	public static final String TRAN_TYPE_STRING = "transformer";
	

}
