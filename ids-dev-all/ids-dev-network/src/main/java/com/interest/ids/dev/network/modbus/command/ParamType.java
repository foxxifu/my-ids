package com.interest.ids.dev.network.modbus.command;

import java.nio.ByteOrder;

/**
 * 
 * @author lhq
 *
 *
 */
public class ParamType  {
	
    private String paraName;
    /**
     *  参数值
     */
    private Object paraValue;
    /**
     *  长度
     */
    private int paraLen;
    /**
     *  类型
     */
    private int paraType;
    
    /**
     *  所占bit位的位置
     */
    private Integer bitLocation;
    /**
     * 字节序号
     */
    private ByteOrder orderType = ByteOrder.BIG_ENDIAN;
    
    private boolean isBorrowed;

    
    public ParamType(String paraName, String paraValue, int paraLen,int paraType) {
        this.paraName = paraName;
        this.paraValue = paraValue;
        this.paraLen = paraLen;
        this.paraType = paraType;
    }

    public ParamType(String paraName, int paraLen, int paraType) {
        this.paraName = paraName;
        this.paraLen = paraLen;
        this.paraType = paraType;
    }

    public ParamType() {

    }

    public String getParaName() {
        return paraName;
    }

    public void setParaName(String paraName) {
        this.paraName = paraName;
    }

    public Object getParaValue() {
        return paraValue;
    }

    public void setParaValue(Object paraValue) {
        this.paraValue = paraValue;
    }

    public int getParaLen() {
        return paraLen;
    }

    public void setParaLen(int paraLen) {
        this.paraLen = paraLen;
    }

    public int getParaType() {
        return paraType;
    }

    public void setParaType(int paraType) {
        this.paraType = paraType;
    }

    public ByteOrder getOrderType() {
        return orderType;
    }

    public void setOrderType(ByteOrder orderType) {
        this.orderType = orderType;
    }
    
    public boolean isBorrowed() {
        return isBorrowed;
    }

    public void setBorrowed(boolean isBorrowed) {
        this.isBorrowed = isBorrowed;
    }

	public Integer getBitLocation() {
		return bitLocation;
	}

	public void setBitLocation(Integer bitLocation) {
		this.bitLocation = bitLocation;
	}

	public void clear(){
        //do nothing
    }

	@Override
	public String toString() {
		return "ParamType [paraName=" + paraName + ", paraValue=" + paraValue
				+ ", paraLen=" + paraLen + ", paraType=" + paraType + "]";
	}
	
}
