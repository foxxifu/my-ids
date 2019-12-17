package com.interest.ids.dev.network.modbus.message;


import java.nio.ByteOrder;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.interest.ids.common.project.buffer.UnSafeHeapBuffer;
import com.interest.ids.dev.api.utils.ByteUtils;
import com.interest.ids.dev.network.modbus.command.ModbusCommand;
import com.interest.ids.dev.network.modbus.command.ParamType;
import com.interest.ids.dev.network.modbus.utils.ModbusConstant;





/**
 * 
 * @author lhq
 *
 *
 */
public class ModbusResponse   {
    
	private static final Logger log = LoggerFactory.getLogger(ModbusResponse.class);
    
    private Map<String, String> pdudata = new HashMap<String,String>();
    
    private Map<String,Object>  signalData = new HashMap<String,Object>();
    

    private ModbusCommand cmd;

    public Map<String, String> getPdudata() {
        return pdudata;
    }

    public void setPdudata(Map<String, String> pdudata) {
        this.pdudata = pdudata;
    }

    public ModbusResponse(ModbusCommand cmd) {
        this.cmd = cmd;
    }
    
    public ModbusResponse(ModbusMessage msg){
        this.cmd = msg.getCommand();
    }

    /**
     * 设置返回报文的body，其主要是为了自动解析参数，然后装入pdbData
     */
    
    public void setBody(UnSafeHeapBuffer buffer) {
        if (buffer == null || 0 == buffer.readableBytes()) {
            return;
        }

        List<ParamType> cmdParameters = cmd.getReciveParms();
       
        short deviceId = buffer.readUnsignedByte();
        pdudata.put(ModbusConstant.DEVICE_ADDRESS, deviceId+"");
        for (Iterator<ParamType> iter = cmdParameters.iterator(); iter.hasNext();){
            if(buffer == null || buffer.readableBytes() == 0){
                break;
            }
            ParamType param = iter.next();
            if(param.getOrderType().equals(ByteOrder.BIG_ENDIAN)){
                
                String tmp = ByteUtils.decodeParameteBigEndian(buffer,
                    param.getParaType(), param.getParaLen());
                pdudata.put(param.getParaName(), tmp);
            }
            else{
                String tmp = ByteUtils.decodeParameteLittleEndian(buffer,
                    param.getParaType(), param.getParaLen());
                pdudata.put(param.getParaName(), tmp);
            }
        }
        log.debug("the resolve frame is: "+pdudata);
    }
    
    
    public void resloveBody(UnSafeHeapBuffer buffer) {
        if (buffer == null || 0 == buffer.readableBytes()) {
            return;
        }
        List<ParamType> cmdParameters = cmd.getReciveParms();
        buffer.readByte();
        for (Iterator<ParamType> iter = cmdParameters.iterator(); iter.hasNext();){
            if(buffer == null || buffer.readableBytes() == 0){
                break;
            }
            ParamType param = iter.next();
            Object tmp = ByteUtils.decodeBigEndianObject(buffer, param.getParaType(), param.getParaLen());
            if(param.getBitLocation()!=null){
            	int location = param.getBitLocation().intValue();
            	long bitValue = 0;

            	bitValue = (1<<location)&Long.parseLong((String.valueOf(tmp)));	
            	if(bitValue==0){
            		tmp = (short)0;
            	}else{
            		tmp = (short)1;
            	}
            }
            if(!ModbusConstant.MODBUS_CMDCODE.equals(param.getParaName()) && !ModbusConstant.DATALEN.equals(param.getParaName())){
            	signalData.put(param.getParaName(), tmp);
            }
        }
        log.debug("the resolve frame is: {}",signalData);
        //cmd.release();
    }
    
  
    public Map<String, Object> getSignalData() {
        return signalData;
    }

    public void setSignalData(Map<String, Object> signalData) {
        this.signalData = signalData;
    }

}
