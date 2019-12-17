package com.interest.ids.dev.network.modbus.command;

import java.util.Iterator;
import java.util.List;


/**
 * 
 * @author lhq
 *
 *
 */
public class ModbusCommand {

    /**
     * 发送参数
     */
    private List<ParamType> sendParams;

    /**
     * 发送参数个数
     */
    private int sendParamsStructLen;

    /**
     * 接收参数
     */
    private List<ParamType> reciveParms;

    /**
     * 接收参数个数
     */
    private int reciveParamsStructLen;

    public int getFrameLen() {
        int frameLen = 0;
        for (Iterator<ParamType> iter = sendParams.iterator(); iter.hasNext();){
            ParamType para = iter.next();
            frameLen += para.getParaLen();
        }
        return frameLen;
    }

    public List<ParamType> getSendParams() {
        return sendParams;
    }

    public void setSendParams(List<ParamType> sendParams) {
        this.sendParams = sendParams;
        this.sendParamsStructLen = sendParams.size();
    }

    public List<ParamType> getReciveParms() {
        return reciveParms;
    }

    public void setReciveParms(List<ParamType> reciveParms) {
        this.reciveParms = reciveParms;
        this.reciveParamsStructLen = reciveParms.size();
    }

    public void refresh() {
        while (this.sendParams.size() > this.sendParamsStructLen){
            this.sendParams.remove(sendParams.size() - 1);
        }

        while (this.reciveParms.size() > this.reciveParamsStructLen){
            this.reciveParms.remove(reciveParms.size() - 1);
        }
    }
    
    public void resetParamValue(String name, Object value) {
        if(sendParams == null){
            return;
        }
        for (ParamType param : sendParams){
            if(name != null && name.equals(param.getParaName())){
                param.setParaValue(value);
                break;
            }
        }
    }
    
    public void release(){
        if(sendParams != null){
            getSendParams().clear();
            
        }
        if(reciveParms != null){
            getReciveParms().clear();
        }
        
    }

	
    
     
}
