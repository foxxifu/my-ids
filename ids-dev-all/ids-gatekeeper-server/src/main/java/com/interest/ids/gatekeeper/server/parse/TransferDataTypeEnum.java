package com.interest.ids.gatekeeper.server.parse;


/**
 * 
 * @author lhq
 *
 *
 */
public enum TransferDataTypeEnum {
	
	ALARM((byte)1,"com.interest.ids.gatekeeper.server.parse.");
	
	private byte type;  
    private String clazz;  
  
    private TransferDataTypeEnum(byte type,String clazz){  
        this.type=type;  
        this.clazz=clazz;  
    }

	public byte getType() {
		return type;
	}

	public void setType(byte type) {
		this.type = type;
	}

	public String getClazz() {
		return clazz;
	}

	public void setClazz(String clazz) {
		this.clazz = clazz;
	}  
    
}
