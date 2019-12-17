package com.interest.ids.dev.network.modbus.command;

import io.netty.channel.Channel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.interest.ids.common.project.bean.signal.SignalInfo;
import com.interest.ids.common.project.buffer.UnSafeHeapBuffer;
import com.interest.ids.dev.api.handler.BizEventHandler.DataMsgType;
import com.interest.ids.dev.network.modbus.message.MessageBuilder;
import com.interest.ids.dev.network.modbus.message.MessageSender;
import com.interest.ids.dev.network.modbus.message.ModbusMessage;
import com.interest.ids.dev.network.modbus.message.ModbusResponse;
import com.interest.ids.dev.network.modbus.utils.ModbusConstant;
import com.interest.ids.dev.network.modbus.utils.ModbusUtils;



/**
 * 
 * @author lhq
 *
 *
 */
public class ReadAndWriteExe  {

	protected static final Logger log = LoggerFactory.getLogger(ReadAndWriteExe.class);

    /**
     * 同步读寄存器 调用此函数后用map获取参数registValue的值即可
     */
    public static Map<String, String> readRegisterSyn(String parentEsnCode,Integer secondAddr,SignalInfo register) {
        return readRegisterSyn(parentEsnCode,secondAddr,register.getSignalAddress(), register.getRegisterNum());
    }

    public static Map<String, String> writeRegisterSyn(String parentEsnCode,Integer secondAddr,SignalInfo signal,int registerValue) {
        if(signal != null){
            int registerNum = signal.getRegisterNum();
            if(registerNum == 1){
                return writeRegisterSyn2(parentEsnCode,secondAddr, signal,registerValue,true);
            }
            else{
                return writeRegistersSyn(parentEsnCode,secondAddr, signal, registerNum, registerValue,true);
            }
        }
        log.error("write the register error.the bean is null");
        return null;
        
    }
    
    public static void writeRegisterAsyn(String parentEsnCode,Integer secondAddr,SignalInfo signal,int registerValue){
        if(signal != null){
            int registerNum = signal.getRegisterNum();
            if(registerNum == 1){
                 writeRegisterSyn2(parentEsnCode,secondAddr, signal, registerValue,false);
            }
            else{
                 writeRegistersSyn(parentEsnCode,secondAddr, signal, registerNum, registerValue,false);
            }
        }else{
        	log.error("write the register error.the bean is null");
        }
    }
    /**
     * 读寄存器的优化接口
     */
    public static Map<SignalInfo, Object> readRegistersSyn(String parentEsn,Integer secondAddr, List<SignalInfo> registers) {
        Map<SignalInfo,Object> map = new HashMap<SignalInfo,Object>();
        Map<String,Object> temp = new HashMap<String,Object>();
        List<ModbusCommand> cmds = createReadCommands(registers);
        for(ModbusCommand cmd:cmds){
            try{
		        ModbusMessage request = MessageBuilder.buildMessage(parentEsn,secondAddr, cmd, true,-1L);
		        ModbusResponse response = new ModbusResponse(cmd);
		        UnSafeHeapBuffer receive = MessageSender.sendSyncMsg(request);
		        response.resloveBody(receive);
		        temp.putAll(response.getSignalData());
            }
            catch(Exception e){
                log.error("the exception is:", e);
            }finally{
                cmd.release();
            }
        }
        //遍历设值
        for(SignalInfo bean:registers){
            if(bean == null){
                continue;
            }
          String signalId =  String.valueOf(bean.getId());
          if(temp.containsKey(signalId)){
              Object value = temp.get(signalId);
              map.put(bean, value);
          }
          else{
              map.put(bean, null);
          }
        }
        registers.clear();
        return map;
    }
    
    
    /**
     * 异步读多个寄存器
     * @param esn
     * @param secondAddr
     * @param registers
     * @param callBack
     */
    public static void readRegistersAsyn(String parentEsn,String esn,Integer secondAddr,List<SignalInfo> registers,DataMsgType callBack){
    	List<ModbusCommand> cmds = createReadCommands(registers);
        for(ModbusCommand cmd:cmds){
            try{
	            ModbusMessage msg = MessageBuilder.buildMessage(parentEsn,secondAddr, cmd, false,-1L);
	            if(msg != null){
	            	msg.setMsgType(callBack);
	            	msg.setSn(esn);
	 	            MessageSender.sendAsynMsg(msg);
	            }
            }
            catch(Exception e){
                log.error("read registers asyn error:", e);
            }
        }
    }
    

    /**
     * 同步写多个寄存器
     */

    private static Map<String, String> writeRegistersSyn(String parentEsnCode,Integer secondAddr,SignalInfo signal, int registerNum, int registerValue,boolean isSyn) {
        ModbusCommand cmd = ModbusCommandUtils.getCommandByCmdName(ModbusConstant.WRITE_MANY_REGISTER);
        setWriteManyRegisterCon(cmd, signal, registerNum, registerValue);
        if(isSyn){
        	return writeRegister(parentEsnCode,secondAddr, cmd);
        }
        else{
        	writeRegisterAsyn(parentEsnCode,secondAddr, cmd);
        	return null;
        }
    }

    /**
     * hw设备字符串写值
     * @param parentEsnCode 
     * @param secondAddr 设备二级地址
     * @param signalAddr 寄存器地址
     * @param registerNum 寄存器个数
     * @param registerValue 值
     * @param isSyn 是否同步写
     * @return
     */
    public static Map<String, String> writeRegistersStr(String parentEsnCode,Integer secondAddr,int signalAddr,int registerNum, String registerValue,boolean isSyn) {
        ModbusCommand cmd = ModbusCommandUtils.getCommandByCmdName(ModbusConstant.WRITE_MANY_REGISTER);
        List<ParamType> params = cmd.getSendParams();
        for (Iterator<ParamType> iter = params.iterator(); iter.hasNext();){
            ParamType para = iter.next();
            if(para.getParaName().equals(ModbusConstant.STARTADDR)){
                para.setParaValue(signalAddr);
            }
            else if(para.getParaName().equals(ModbusConstant.REGISTER_NUM)){
                para.setParaValue(registerNum);
            }
            else if(para.getParaName().equals(ModbusConstant.DATALEN)){
                para.setParaValue(registerNum * 2);
            }
            else if(para.getParaName().equals(ModbusConstant.REGIST_VALUE)){
                para.setParaValue(registerValue);
                para.setParaName("Regist_Value");
                para.setParaLen(registerNum * 2);
                para.setParaType(3);
            }
        }
        if(isSyn){
            return writeRegister(parentEsnCode,secondAddr, cmd);
        }
        else{
            writeRegisterAsyn(parentEsnCode,secondAddr, cmd);
            return null;
        }
    }
    
    /**
     * @param ip
     * @param deviceAddress
     * @param cmd
     * @return
     */
    private static Map<String, String> writeRegister(String parentEsnCode,Integer secondAddr,ModbusCommand cmd) {
    	ModbusMessage request = MessageBuilder.buildMessage(parentEsnCode,secondAddr, cmd, true,-1L);
        ModbusResponse response = new ModbusResponse(cmd);
        UnSafeHeapBuffer receive = MessageSender.sendSyncMsg(request);
        response.setBody(receive);
        return response.getPdudata();
    }
    
    private static void writeRegisterAsyn(String parentEsnCode,Integer secondAddr,ModbusCommand cmd) {
        ModbusMessage request = MessageBuilder.buildMessage(parentEsnCode,secondAddr, cmd, false,-1L);
        MessageSender.sendAsynMsg(request);
    }
 
    /**
     * 同步写单个寄存器
     */
    private static Map<String, String> writeRegisterSyn2(String parentEsnCode,Integer secondAddr,SignalInfo signal, int registerValue,boolean isSyn) {
        ModbusCommand cmd = ModbusCommandUtils.getCommandByCmdName(ModbusConstant.WRITE_ONE_REGISTER);
        setWriteOneRegisterCon(cmd, signal, registerValue);
        if(isSyn){
        	return writeRegister(parentEsnCode,secondAddr, cmd);
        }
        else{
        	writeRegisterAsyn(parentEsnCode,secondAddr, cmd);
        	return null;
        }
    }

    private static void setWriteManyRegisterCon(ModbusCommand cmd,SignalInfo signal, int registerNum, int registerValue) {
        List<ParamType> params = cmd.getSendParams();
        for (Iterator<ParamType> iter = params.iterator(); iter.hasNext();){
            ParamType para = iter.next();
            if(para.getParaName().equals(ModbusConstant.STARTADDR)){
                para.setParaValue(signal.getSignalAddress());
            }
            else if(para.getParaName().equals(ModbusConstant.REGISTER_NUM)){
                para.setParaValue(registerNum);
            }
            else if(para.getParaName().equals(ModbusConstant.DATALEN)){
                para.setParaValue(registerNum * 2);
            }
            else if(para.getParaName().equals(ModbusConstant.REGIST_VALUE)){
                para.setParaValue(String.valueOf(registerValue));
                para.setParaName(String.valueOf(signal.getSignalAddress()));
                para.setParaLen(registerNum * 2);
                para.setParaType(ModbusUtils.getDataType(signal.getDataType().longValue()));
            }
        }

    }

    /**
     * 设置读寄存器参数
     * @param cmd
     * @param startAddress
     * @param registerNum
     */
    public static void setReadRegisterCondition(ModbusCommand cmd, int startAddress,int registerNum) {
        List<ParamType> params = cmd.getSendParams();
        for (Iterator<ParamType> iter = params.iterator(); iter.hasNext();){
            ParamType para = iter.next();
            if(para.getParaName().equals(ModbusConstant.STARTADDR)){
                para.setParaValue(startAddress);
            }
            else if(para.getParaName().equals(ModbusConstant.REGISTER_NUM)){
                para.setParaValue(registerNum);
            }
        }

        List<ParamType> receive = cmd.getReciveParms();
        for (ParamType type : receive){
            if(type.getParaName().equals(ModbusConstant.REGIST_VALUE)){
                type.setParaLen(registerNum * 2);
                if(registerNum > 2){
                    // 设置为字符串型 3,默认为0
                    type.setParaType(3);
                }
            }
        }
    }

    private static void setWriteOneRegisterCon(ModbusCommand cmd, SignalInfo signal,int registerValue) {
        List<ParamType> params = cmd.getSendParams();
        List<ParamType> receiveParam = cmd.getReciveParms();
        for (java.util.Iterator<ParamType> iter = params.iterator(); iter
            .hasNext();){
            ParamType para = iter.next();
            if(para.getParaName().equals(ModbusConstant.STARTADDR)){
                para.setParaValue(signal.getSignalAddress());
            }
            else if(para.getParaName().equals(ModbusConstant.REGIST_VALUE)){
                para.setParaType(ModbusUtils.getDataType(signal.getDataType().longValue()));
                para.setParaValue(registerValue);
            } 
        }
        
        for (Iterator<ParamType> iter = receiveParam.iterator(); iter.hasNext();){
            ParamType para = iter.next();
        if(para.getParaName().equals(ModbusConstant.REGIST_VALUE)){
            para.setParaType(ModbusUtils.getDataType(signal.getDataType().longValue()));
        }
        
    }
    }
    
    

   public static Object readRegisterSyn2(String parentEsnCode,Integer secondAddr,int startAddress, int registerNum){
	   Map<String, String> map = readRegisterSyn(parentEsnCode,secondAddr,startAddress,registerNum);
	   if(map != null){
		   return map.get(ModbusConstant.REGIST_VALUE);
	   }
	   return null;
   }

    public static Map<String, String> readRegisterSyn(String parentEsnCode,Integer secondAddr,int startAddress, int registerNum) {
        return readRegisterSyn(parentEsnCode,secondAddr,startAddress,registerNum,-1L);
    }
    
    /**
     * 异步读寄存器
     * @param parentEsnCode
     * @param secondAddr
     * @param startAddress
     * @param registerNum
     * @param type
     */
    public static void readRegisterAsyn(String parentEsn,String esn,Integer secondAddr,int startAddress, int registerNum,DataMsgType type){
    	
    	if(startAddress == 0 || parentEsn == null || "".equals(parentEsn)){
            log.error("error register esn:"+parentEsn);
            return;
        }
        ModbusCommand cmd = ModbusCommandUtils.getCommandByCmdName(ModbusConstant.READ_REGISTER);
        setReadRegisterCondition(cmd, startAddress, registerNum);
        ModbusMessage request = MessageBuilder.buildMessage(parentEsn,secondAddr, cmd, false,-1L);
        if(request != null){
        	request.setMsgType(type);
        	request.setSn(esn);
        }
        MessageSender.sendAsynMsg(request);
    }
    
    public static Map<String, String> readRegisterSyn(String parentEsnCode,Integer secondAddr,int startAddress, int registerNum,Long timeOut){
        if(startAddress == 0 || parentEsnCode == null || "".equals(parentEsnCode)){
            log.error("error register startAddress:"+startAddress);
            return Collections.emptyMap();
        }
        ModbusCommand cmd = ModbusCommandUtils.getCommandByCmdName(ModbusConstant.READ_REGISTER);
        setReadRegisterCondition(cmd, startAddress, registerNum);
        ModbusMessage request = MessageBuilder.buildMessage(parentEsnCode,secondAddr, cmd, true,timeOut);
        ModbusResponse response = new ModbusResponse(cmd);
        UnSafeHeapBuffer receive = null;
        try{
            receive = MessageSender.sendSyncMsg(request);
            if(receive != null){
                response.setBody(receive);
            }
        }
        catch(Exception e){
            log.error("", e);
        }
        return response.getPdudata();
    }
    
    /**
     * 根据通道同步读取设备的寄存器的值
     * @param channel
     * @param devNo
     * @param startAddress
     * @param registerNum
     * @param timeOut
     * @return
     */
    public static Map<String,String> readRegisterSyn(Channel channel,int devNo,int startAddress, int registerNum,Long timeOut){
        if(startAddress == 0 || channel == null){
            log.error("error register startAddress:"+startAddress);
            return null;
        }
        ModbusCommand cmd = ModbusCommandUtils.getCommandByCmdName(ModbusConstant.READ_REGISTER);
        setReadRegisterCondition(cmd, startAddress, registerNum);
        ModbusMessage request = MessageBuilder.buildMessage(channel,devNo, cmd, true,timeOut);
        ModbusResponse response=new ModbusResponse(cmd);
        UnSafeHeapBuffer receive = null;
        try{
            receive = MessageSender.sendSyncMsg(request);
            if(receive!=null){
            	response.setBody(receive);
            }
            return response.getPdudata();
         }
        catch(Exception e){
            log.error("", e);
        }
        return null;
    } 
    
    /**
     * 异步读寄存器
     */
    public static void readRegisterAsyn(String parentEsnCode,Integer secondAddr,int startAddress, int registerNum){
        if(startAddress == 0 || parentEsnCode == null || "".equals(parentEsnCode)){
            log.error("error register esn:"+parentEsnCode);
            return;
        }
        ModbusCommand cmd = ModbusCommandUtils.getCommandByCmdName(ModbusConstant.READ_REGISTER);
        setReadRegisterCondition(cmd, startAddress, registerNum);
        ModbusMessage request = MessageBuilder.buildMessage(parentEsnCode,secondAddr, cmd, false,-1L);
        MessageSender.sendAsynMsg(request);
    }
    
    public static void readRegisterAsyn(Channel channel,int deviceAddress,int startAddress, int registerNum){
        if(startAddress == 0 ){
            log.error("error register startAddress:"+startAddress);
            return;
        }
        ModbusCommand cmd = ModbusCommandUtils.getCommandByCmdName(ModbusConstant.READ_REGISTER);
        setReadRegisterCondition(cmd, startAddress, registerNum);
        ModbusMessage request = MessageBuilder.buildMessage(channel,deviceAddress, cmd, false);
        if(request != null){
        	 MessageSender.sendAsynMsg(request);
        }
    }
    
    
    
    

    public static UnSafeHeapBuffer readRegistSyn(String parentEsnCode,Integer secondAddr,int startAddress, int registerNum) {
        ModbusCommand cmd = ModbusCommandUtils.getCommandByCmdName(ModbusConstant.READ_REGISTER);
        setReadRegisterCondition(cmd, startAddress, registerNum);
        ModbusMessage request = MessageBuilder.buildMessage(parentEsnCode,secondAddr, cmd, true,-1L);
        UnSafeHeapBuffer receive = null;
        try{
            receive = MessageSender.sendSyncMsg(request);
        }
        catch(Exception e){
            log.error("", e);
        }
        return receive;
    }

    // 创建多条读命令
    private static List<ModbusCommand> createReadCommands(List<SignalInfo> registers) {
        List<ModbusCommand> cmds = new ArrayList<ModbusCommand>();
        // 把属性按照寄存器从小到大排序， 相邻寄存器做一帧发送
        registers = sortRegister(registers);
       
        List<List<SignalInfo>> frams = seekFrame(registers);
        // 总共多少帧
        int framsCount = frams.size();
        int readMax = ModbusConstant.REQUEST_LIMIT_SUN;// 读寄存器个数上限
        for (int i = 0; i < framsCount; i++){
            // 参数总个数
            int attrCount = frams.get(i).size();
            int index = 0;
            // 改帧寄存器个数
            int regCount = 0;
            while (index < attrCount){
                regCount = regCount + frams.get(i).get(index).getRegisterNum();
                index++;
            }
            // 命令条数
            int cmdCount = 0;
            // 计算要发多少帧
            if(regCount % readMax == 0){
                cmdCount = regCount / readMax;
            }
            else{
                cmdCount = regCount / readMax + 1;
            }
            // 生成命令
            for (int j = 0; j < cmdCount; j++){
                // 如果单帧寄存器个数超多帧的最大上限个数，则又需要分多帧
                ModbusCommand cmd = createReadMore(frams.get(i), regCount);
                cmds.add(cmd);
            }
        }
        return cmds;
    }

    private static  ModbusCommand createReadMore(List<SignalInfo> beans,int registerCount) {
        // 将list转换为map以便根据寄存器获取属性值
        Map<String, SignalInfo> attsMap = new HashMap<String, SignalInfo>();
        for (int i = 0; i < beans.size(); i++){
            attsMap.put(String.valueOf(beans.get(i).getSignalAddress()),beans.get(i));
        }
        ModbusCommand cmdStruct = null;
        // 根据命令字0x03创建命令
        cmdStruct = ModbusCommandUtils.getCommandByCmdName(ModbusConstant.READ_REGISTER);
        List<ParamType> sendParameters = cmdStruct.getSendParams();
        int sendParasSize = sendParameters.size();
        // 替换发送参数值
        for (int i = 0; i < sendParasSize; i++){
            ParamType paraType = sendParameters.get(i);
            if(paraType.getParaName().equals(ModbusConstant.STARTADDR)){
                // 寄存器起始地址
                paraType.setParaValue(String.valueOf(beans.get(0).getSignalAddress()));
            }
            if(paraType.getParaName().equals(ModbusConstant.REGISTER_NUM)){
                // 寄存器个数
                paraType.setParaValue(String.valueOf(registerCount));
            }
        }
        List<ParamType> receiveParam = cmdStruct.getReciveParms();
        int returnParasSize = receiveParam.size();
        // 替换返回参数值
        for (int i = 0; i < returnParasSize; i++){
            ParamType paraType = receiveParam.get(i);
            if(paraType.getParaName().equals(ModbusConstant.DATALEN)){
                paraType.setParaValue(String.valueOf(registerCount * 2));
                paraType.setParaLen(1);
            }
            if(paraType.getParaName().equals(ModbusConstant.REGIST_VALUE)){

                receiveParam.remove(paraType);
                //List<ParamType> params = ObjectPoolFactory.getInstance().getParamType(beans.size());
                for (int m = 0; m < beans.size(); m++){
                    ParamType param = new ParamType();
                    SignalInfo bean = beans.get(m);
                    param.setParaLen(2 * bean.getRegisterNum());
                    param.setParaName(String.valueOf(bean.getSignalAddress()));
                    param.setParaType(ModbusUtils.getDataType(bean.getDataType().longValue()));
                    if(bean.getBit()!=null){
                    	param.setBitLocation(bean.getBit());
                    }
                    receiveParam.add(param);
                }
            }
        }
        return cmdStruct;
    }

    // 排序寄存器
    public static  List<SignalInfo> sortRegister(List<SignalInfo> registers) {
    	int len = 0;
        //首先缓存所有的register
        Map<String, List<SignalInfo>> map = new HashMap<String, List<SignalInfo>>();
        Integer arrays[] = new Integer[registers.size()];
        for (SignalInfo bean : registers){
            if(bean == null){
                continue;
            }
            int register = bean.getSignalAddress();
            String key = register + "_" ;
            if(map.keySet().contains(key)){
            	map.get(key).add(bean);
            }else{
            	List<SignalInfo> temp = new ArrayList<SignalInfo>();
            	temp.add(bean);
            	map.put(key, temp);
            	arrays[len] = register;
            	len++;
            }
        }
        List<SignalInfo> arrayResult = new ArrayList<SignalInfo>();
       
        Integer temp = 0;
        //使用冒泡将寄存器地址排序
        for (int i = 0; i < len - 1; i++){
            for (int j = i; j < len; j++){
                if(arrays[i] >= arrays[j]){
                    temp = arrays[i];
                    arrays[i] = arrays[j];
                    arrays[j] = temp;
                }
            }
        }
        
        //最后按照顺序获取所有registerBean
        for (int i = 0; i < len; i++){
            Object[] obj = map.keySet().toArray();
            for (int j = 0; j < obj.length; j++) {
            	String value = String.valueOf(obj[j]);
            	String key = String.valueOf(arrays[i]) + "_";
            	if(value.startsWith(key)){
            		List<SignalInfo> beanList = map.remove(value);
            		if(beanList!=null&&beanList.size()>0){
            			for (int k = 0; k < beanList.size(); k++) {
            				arrayResult.add(beanList.get(k));
            			}
            		}
            	}
			}
        }
        return arrayResult;
    }
    
    //连续的合为一帧
    public static List<List<SignalInfo>> seekFrame(List<SignalInfo> beans) {
        int readMax = ModbusConstant.REQUEST_LIMIT_SUN;
        List<List<SignalInfo>> cmds = new ArrayList<List<SignalInfo>>();
        int beanSize = beans.size();
        boolean isSave = false;
        int maxRegistNum = 0;
        List<SignalInfo> cmd = new ArrayList<SignalInfo>();
        if(beanSize <= 1){
            cmds.add(beans);
            return cmds;
        }
        for (int i = 0; i < beanSize - 1; i++){
            maxRegistNum = maxRegistNum + beans.get(i).getRegisterNum();
            if(maxRegistNum >= readMax){
                cmds.add(cmd);
                isSave = true;
                cmd = new ArrayList<SignalInfo>();
                maxRegistNum = 0;
                isSave = false;
            }
            cmd.add(beans.get(i));
            if(beans.get(i + 1).getSignalAddress() - (beans.get(i).getSignalAddress() + beans.get(i).getRegisterNum() - 1) != 1){
                cmds.add(cmd);
                isSave = true;
                cmd = new ArrayList<SignalInfo>();
                maxRegistNum = 0;
                isSave = false;
            }
            if(i + 2 == beanSize){
                cmd.add(beans.get(i + 1));
            }
        }
        if(!isSave){
            if(cmd.size() > 0){
                cmds.add(cmd);
            }
        }
        return cmds;
    }

   
    /**
     * 同步写单个寄存器
     * 根据设备esn 寄存器地址和信号点 DataType
     */
    public static Map<String, String> writeRegisterSyn2Address(String parentEsnCode,Integer secondAddr,Long dataType,Integer signalAddress,int registerValue,boolean isSyn) {
        ModbusCommand cmd = ModbusCommandUtils.getCommandByCmdName(ModbusConstant.WRITE_ONE_REGISTER);
        setWriteOneRegisterConAddress(cmd, dataType,signalAddress,registerValue);
        if(isSyn){
        	return writeRegister(parentEsnCode,secondAddr, cmd);
        }
        else{
        	writeRegisterAsyn(parentEsnCode,secondAddr, cmd);
        	return null;
        }
    }

    private static void setWriteOneRegisterConAddress(ModbusCommand cmd,Long dataType,Integer signalAddress,int registerValue) {
        List<ParamType> params = cmd.getSendParams();
        List<ParamType> receiveParam = cmd.getReciveParms();
        for (java.util.Iterator<ParamType> iter = params.iterator(); iter
            .hasNext();){
            ParamType para = iter.next();
            if(para.getParaName().equals(ModbusConstant.STARTADDR)){
                para.setParaValue(signalAddress);
            }
            else if(para.getParaName().equals(ModbusConstant.REGIST_VALUE)){
                para.setParaType(ModbusUtils.getDataType(dataType));
                para.setParaValue(registerValue);
            } 
        }
        
        for (Iterator<ParamType> iter = receiveParam.iterator(); iter.hasNext();){
            ParamType para = iter.next();
	        if(para.getParaName().equals(ModbusConstant.REGIST_VALUE)){
	            para.setParaType(ModbusUtils.getDataType(dataType));
	        }
        
        }
    }

}
