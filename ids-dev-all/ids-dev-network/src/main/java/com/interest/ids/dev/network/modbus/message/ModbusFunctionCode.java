package com.interest.ids.dev.network.modbus.message;


/**
 * 
 * @author lhq
 *
 *
 */
public enum ModbusFunctionCode {
	
	
	 	ReadCoils(0x01),
	    ReadDiscreteInputs(0x02),
	    ReadHoldingRegisters(0x03),
	    ReadInputRegisters(0x04),
	    WriteSingleCoil(0x05),
	    WriteSingleRegister(0x06),
	    WriteMultipleCoils(0x0F),
	    WriteMultipleRegisters(0x10);
	    

	    private final int code;

	    ModbusFunctionCode(int code) {
	        this.code = code;
	    }

	    public int getCode() {
	        return code;
	    }


}
