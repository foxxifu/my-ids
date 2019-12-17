package com.interest.ids.dev.network.modbus.command;

import java.util.ArrayList;
import java.util.List;

import com.interest.ids.dev.network.modbus.utils.ModbusConstant;

/**
 * 
 * @author lhq
 *
 *
 */
public class ModbusCommandUtils {
    

    public static ModbusCommand getCommandByCmdName(String cmdName) {
        
        switch (cmdName) {
		case ModbusConstant.SUBSCRIBE:
			return createSubscription();
			
		case ModbusConstant.WRITE_ONE_REGISTER:
			return createWriteRegister();
			
		case ModbusConstant.WRITE_MANY_REGISTER:
			return createWriteRegisters();
			
		case ModbusConstant.READ_REGISTER:
			return createReadRegisters();
			
		default:
			break;
		}
       
      
        return null;
    }

    
    
    
    

    /**
     * 信号订阅命令
     * 
     * @return
     */
    private static ModbusCommand createSubscription() {
        ModbusCommand cmd = new ModbusCommand();
        List<ParamType> sendparas = new ArrayList<ParamType>();
        List<ParamType> recvparas = new ArrayList<ParamType>();
        ParamType paramType = null;

        paramType = new ParamType(ModbusConstant.MODBUS_CMDCODE, "0x65", 1, 0);
        sendparas.add(paramType);
        //子功能码
        paramType = new ParamType(ModbusConstant.SUBCOMDCODE, "0x03", 1, 0);
        sendparas.add(paramType);
        paramType = new ParamType(ModbusConstant.DATALEN, "0", 1, 0);
        sendparas.add(paramType);
        //设置时间周期为5秒
        paramType = new ParamType(ModbusConstant.REGISTPERIOD, "5", 1, 0);
        sendparas.add(paramType);
        paramType = new ParamType(ModbusConstant.SUBTYPE, "2", 1, 0);
        sendparas.add(paramType);

        paramType = new ParamType(ModbusConstant.MODBUS_CMDCODE, 1, 0);
        recvparas.add(paramType);
        paramType = new ParamType(ModbusConstant.SUBCOMDCODE, 1, 0);
        recvparas.add(paramType);
        paramType = new ParamType(ModbusConstant.DATALEN, 1, 0);
        recvparas.add(paramType);
        //返回结果，1标示失败，0标示成功
        paramType = new ParamType(ModbusConstant.SUBSCRIBERESULT, 1, 0);
        recvparas.add(paramType);

        cmd.setSendParams(sendparas);
        cmd.setReciveParms(recvparas);
        return cmd;
    }
   
  
    
    
    /**
     * 写单个寄存器的命令
     */
    private static ModbusCommand createWriteRegister(){
        ModbusCommand cmd = new ModbusCommand();
        List<ParamType> sendparams = new ArrayList<ParamType>();
        List<ParamType> recvparams = new ArrayList<ParamType>();
        ParamType paramType = null;
        
        //设置功能码
        paramType = new ParamType(ModbusConstant.MODBUS_CMDCODE,"0x06",1,0);
        sendparams.add(paramType);      
        paramType = new ParamType(ModbusConstant.STARTADDR,2,0);
        sendparams.add(paramType);        
        paramType = new ParamType(ModbusConstant.REGIST_VALUE,2,0);
        sendparams.add(paramType);
        
      
        paramType = new ParamType(ModbusConstant.MODBUS_CMDCODE,1,0);
        recvparams.add(paramType);
        paramType = new ParamType(ModbusConstant.STARTADDR,2,0);
        recvparams.add(paramType);
        paramType = new ParamType(ModbusConstant.REGIST_VALUE,2,0);
        recvparams.add(paramType);
        
        cmd.setSendParams(sendparams);
        cmd.setReciveParms(recvparams);
        return cmd;
    }
    
    /**
     * 写多个寄存器命令
     */
    private static ModbusCommand createWriteRegisters(){
        ModbusCommand cmd = new ModbusCommand();
        List<ParamType> sendparams = new ArrayList<ParamType>();
        List<ParamType> recvparams = new ArrayList<ParamType>();
        ParamType paramType = null;
        
        paramType = new ParamType(ModbusConstant.MODBUS_CMDCODE,"0x10",1,0);
        sendparams.add(paramType);         
        paramType = new ParamType(ModbusConstant.STARTADDR,2,0);
        sendparams.add(paramType);         
        paramType = new ParamType(ModbusConstant.REGISTER_NUM,2,0);
        sendparams.add(paramType);         
        paramType = new ParamType(ModbusConstant.DATALEN,1,0);
        sendparams.add(paramType);         
        paramType = new ParamType(ModbusConstant.REGIST_VALUE,2,0);
        sendparams.add(paramType);               

        paramType = new ParamType(ModbusConstant.MODBUS_CMDCODE,1,0);
        recvparams.add(paramType);        
        paramType = new ParamType(ModbusConstant.STARTADDR,2,0);
        recvparams.add(paramType);         
        paramType = new ParamType(ModbusConstant.REGISTER_NUM,2,0);
        recvparams.add(paramType); 
        
        cmd.setSendParams(sendparams);
        cmd.setReciveParms(recvparams);
        return cmd;
    }
    
    /**
     * 读寄存器
     */
    
    private static ModbusCommand createReadRegisters(){
        ModbusCommand cmd = new ModbusCommand();
        List<ParamType> sendparas = new ArrayList<ParamType>();
        List<ParamType> recvparas = new ArrayList<ParamType>();
        ParamType paramType = null;
        
        paramType = new ParamType(ModbusConstant.MODBUS_CMDCODE,"0x03",1,0);
        sendparas.add(paramType);        
        paramType = new ParamType(ModbusConstant.STARTADDR,"0x00",2,0);
        sendparas.add(paramType);        
        paramType = new ParamType(ModbusConstant.REGISTER_NUM,"0",2,0);
        sendparas.add(paramType);        
        
        paramType = new ParamType(ModbusConstant.MODBUS_CMDCODE,1,0);
        recvparas.add(paramType);        
        paramType = new ParamType(ModbusConstant.DATALEN,1,0);
        recvparas.add(paramType);        
        paramType = new ParamType(ModbusConstant.REGIST_VALUE,2,0);
        recvparas.add(paramType);
        
        cmd.setSendParams(sendparas);
        cmd.setReciveParms(recvparas);
        return cmd;
    }
}
