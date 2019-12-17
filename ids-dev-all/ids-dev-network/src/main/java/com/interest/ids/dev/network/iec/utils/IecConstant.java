package com.interest.ids.dev.network.iec.utils;

public class IecConstant {

	// 启动符
	public static final byte IEC104_FRAME_START = 0x68;
	// 控制域长度
	public static final int CTRL_FIELD_LEN = 4;

	public static final int INT_ZERO = 0;

	public static final byte WHOLE_CALL_STOP_SIGN = 0x14;

	public static final int CONTINUE_SQ = 0x80;

	public static final byte QCC = 0x45;

	public static final byte POWER_REQUEST = 0x05;

	// 总召唤遥信、变位遥信
	public static final byte CMD_DI = 0x01;
	// SOE事项
	public static final byte CMD_SOE = 0x02;
	// 双点遥信
	public static final byte CMD_DOUBLE_YX = 0x03;
	// 遥测
	public static final byte CMD_AI_EX = 0x0D;
	// 遥脉电度量
	public static final byte CMD_POWER = 0x0F;
	// 短时标的遥脉
	public static final byte CMD_SHORT_TIME_POWER = 0x10;
	// 长时标的遥脉
	public static final byte CMD_LONG_TIME_POWER = 0x25;
	// 总召唤遥测量
	public static final byte CMD_AI = 0x15;
	// 遥调
	public static final byte CMD_AO = 0x30;
	// 遥调 标度化值
	public static final byte CMD_BO = 0x31;
	// 遥调 短浮点数
	public static final byte CMD_CO = 0x32;
	// 总召唤 100
	public static final byte CMD_WHOLE_CALL = 0x64;
	// 召唤电度量
	public static final byte CMD_CALL_POWER = 0x65;
	// 校时
	public static final byte CMD_CHECK_TIME = 0x67;
	// 单点遥信
	public static final byte CMD_SINGLE_SOE = 0x1e;
	// 双点遥信
	public static final byte CMD_DOUBLE_SOE = 0x1f;
	// 短浮点设点
	public static final byte CMD_SET_POINT = 0x32;
	// 遥测
	public static final byte CMD_REMOTE_MEASURE = 0x0D;
	// 品质遥测
	public static final byte CMD_QUALITY_YC = 0x09;

	/**
	 * 传输原因
	 */
	// 周期
	public static final short COT_CYCLE = 0x01;
	// 背景扫描
	public static final short COT_BACKGROUND_SCAN = 0x02;
	// 突变信息
	// 初始化
	public static final short COT_CHANGE_INFO = 0x03;
	public static final short COT_INIT = 0x04;
	// 请求或被请求
	public static final short COT_REQUEST = 0x05;
	// 激活
	public static final short COT_ACTIVE = 0x06;
	// 激活确认
	public static final short COT_ACTIVE_AFFIRM = 0x07;
	// 停止激活
	public static final short COT_STOP_ACTIVE = 0x08;
	// 停止激活确认
	public static final short COT_STOP_ACTIVE_AFFIRM = 0x09;
	// 激活结束
	public static final short COT_ACTIVE_END = 0x0a;
	// 响应总召唤
	public static final short COT_RESPONSE_ALL_SUMMON = 0x14;
	// 循环
	public static final byte IEC103_CIRCLE = 0x02;
	// 总召
	public static final byte IEC103_WHOLE_CALL = 0x09;
	/**
	 * 特殊原因
	 */
	// 未知类型标示
	public static final short COT_UNKNOW_CMD = 0x2c;
	// 未知传送原因
	public static final short COT_UNKNOW_COT = 0x2d;
	// 未知公共地址
	public static final short COT_UNKNOW_RTUADDRESS = 0x2e;
	// 未知信息体地址
	public static final short COT_UNKNOW_INFO_ADDRESS = 0x2f;

	public static final int SUCCESS = 1;
	public static final int FAILED = 0;

	// 命令类型1个字节
	public static final int CMD_TYPE_LEN = 1;
	// 可变结构限定词1个字节
	public static final int SQ_LEN = 1;
	// 传输原因
	public static final int COT_LEN = 2;
	// 传输原因101
	public static final int COT_LEN_101 = 1;
	// 公共地址，即子站地址2个字节
	public static final int PUB_ADDR_LEN = 2;
	// 公共地址，即子站地址1个字节101
	public static final int PUB_ADDR_LEN_101 = 1;
	// 信息体地址3个字节
	public static final int INFO_ADDR_LEN = 3;
	// 信息体地址2个字节
	public static final int INFO_ADDR_LEN_101 = 2;

	public static final Integer TYPE_YC = 1;

	public static final Integer TYPE_YX = 2;
	public static final Integer TYPE_YM = 3;
	public static final Integer TYPE_YK = 4;

	public static final Integer THREE_SECOND = 3 * 1000;

	public static final Integer FIFTEEN_MINUTE = 15 * 60 * 1000;

	public static final byte CMD_CHECK_TIME103 = 0x06;

	public static final byte COT_CHECK_TIME = 0x08;

	public static final String IEC104 = "104";

	public enum Iec104CmdType {

		// 单点遥信
		SINGLE_YX("single_yx", (byte) 0x01),
		// 双点遥信
		DOUBLE_YX("double_yx", (byte) 0x03),
		// 品质遥测
		QUALITY_YC("quality_yc", (byte) 0x09),
		// 浮点遥测
		FLOATING_YC("floating_yc", (byte) 0x0d),

		// 1e---带7个字节时标的单点遥信--soe
		SEVEN_SINGLE_YX("seven_single_yx", (byte) 0x1e),
		// 1f---带7个字节时标的双点遥信--soe
		SEVEN_DOUBLE_YX("seven_double_yx", (byte) 0x1f),
		// 0f---不带时标的电度量，每个电度量占5个字节--遥脉
		NORMAL_POWER("normal_power", (byte) 0x0f),
		// 10---带3个字节短时标的电度量，每个电度量占8个字节--遥脉
		SHORT_TIME_POWER("short_time_power", (byte) 0x10),
		// 25---带7个字节长时标的电度量，每个电度量占12个字节--遥脉
		LONG_TIME_POWER("long_time_power", (byte) 0x25),

		// 单点遥控
		SINGLE_POINT_TELECONTROL("single_point_teleControl", (byte) 0x2d),
		// 双点遥控
		DOUBLE_POINT_TELECONTROL("double_point_teleControl", (byte) 0x2e),

		// 不带时标的单点信息
		CMD_SINGLE_NO_TIME("cmd_single_no_time", (byte) 0x01),

		// 带时标的单点信息
		CMD_SINGLE_TIME("cmd_single_time", (byte) 0x02),

		// 不带时标的单点信息
		CMD_DOUBLE_NO_TIME("cmd_double_no_time", (byte) 0x03),

		// 带时标的单点信息
		CMD_DOUBLE_TIME("cmd_double_time", (byte) 0x04),

		// 带品质描述的归一化值
		CMD_NORMALIZED_QUALITY("cmd_normalized_quality", (byte) 0x09),

		// 短浮点数测量值
		CMD_FLOAT_MEASURED("cmd_float_measured", (byte) 0x0D),

		// 电能脉冲计数量
		CMD_PULSE_METER_COUNT("cmd_pulse_meter_count", (byte) 0x0F),

		// 不带品质描述的测量值
		CMD_MEASURED_NO_QUALITY("cmd_measured_no_quality", (byte) 0x15);

		Iec104CmdType(String name, byte code) {
			this.name = name;
			this.code = code;
		}

		private String name;

		private byte code;

		public String getName() {
			return name;
		}

		public byte getCode() {
			return code;
		}
	}

	public enum Iec104FrameType {
		// I帧
		I_FRAME,

		// S帧
		S_FRAME,

		// U帧
		U_FRAME,
	}

	// U报文类型
	public enum UTypeEnum {
		U_START_EFFECT, // 启动命令，生效
		U_START_CONFIRM, // 启动命令，确认
		U_STOP_EFFECT, // 停止命令，生效
		U_STOP_CONFIRM, // 停止命令，确认
		U_TEST_EFFECT, // 测试命令，生效
		U_TEST_CONFIRM, // 测试命令，确认
	}

	public enum IFrameTypeEnum {
		IFrame_WHOLECALL, // 总招
		IFRAM_POWER_WHOLECALL, // 电度总召唤
		IFRAM_POWER_REQUEST, // 电度请求
		CMD_CHECK_TIME, // 对时
		IFRAM_POWER_STOP, // 传输数据结束
		IFRAME_REMOTEINFO, // 遥信
		IFRAME_TELECONTROL, // 遥控
		IFRAME_REMOTE_REGULATE, // 遥调
		IFRAME_UNKNOW
	}
}
