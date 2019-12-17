package com.interest.ids.dev.network.modbus.message;

import com.interest.ids.common.project.buffer.UnSafeHeapBuffer;


/**
 * 
 * @author lhq
 *
 *
 */
public class ModbusResponseMessage {
	
	private int transactionId;
	//单元标识
	private short unitId;
	//功能码
	private int functionCode;
	
	private UnSafeHeapBuffer body;

	public int getTransactionId() {
		return transactionId;
	}

	public void setTransactionId(int transactionId) {
		this.transactionId = transactionId;
	}

	public short getUnitId() {
		return unitId;
	}

	public void setUnitId(short unitId) {
		this.unitId = unitId;
	}

	public int getFunctionCode() {
		return functionCode;
	}

	public void setFunctionCode(int functionCode) {
		this.functionCode = functionCode;
	}

	public UnSafeHeapBuffer getBody() {
		return body;
	}

	public void setBody(UnSafeHeapBuffer body) {
		this.body = body;
	}
}
