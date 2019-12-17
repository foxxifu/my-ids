package com.interest.ids.dev.network.modbus.message;



import io.netty.channel.Channel;

import java.nio.ByteOrder;
import java.util.Iterator;
import java.util.List;

import com.interest.ids.common.project.buffer.UnSafeHeapBuffer;
import com.interest.ids.common.project.utils.CallBack;
import com.interest.ids.dev.api.handler.BizEventHandler.DataMsgType;
import com.interest.ids.dev.api.utils.ByteUtils;
import com.interest.ids.dev.network.modbus.command.ModbusCommand;
import com.interest.ids.dev.network.modbus.command.ParamType;
import com.interest.ids.dev.network.modbus.utils.SerialNumGenerateUtil;


/**
 * 
 * @author lhq
 *
 *
 */
public class ModbusMessage {
    /**
     * 帧序列号
     */
    private int serialNum = -1;
    /**
     * 设备编号
     */
    private int deviceNO;
    /**
     * 消息所属数采
     */
    private String channelName;
    /**
     * 帧头部
     */
    private MbapHeader head;

    /**
     * 整个报文帧
     */
    private UnSafeHeapBuffer request;
    
    private UnSafeHeapBuffer body;

    /**
     * 响应帧
     */
    private UnSafeHeapBuffer response;

    /**
     * 帧超时时间,默认为5秒
     */
    private long timeOut = 30000*10;

    /**
     * 是否是同步请求
     */
    private boolean isSync = true;

    /**
     * 重发次数
     */
    private Integer reSendCount = 1;

    /**
     * 重发时间间隔
     */
    private Integer periodTime = 5;

    /**
     * 帧发送时间
     */
    private long sendTime;

    /**
     * 消息处理器
     */
    private DefaultFuture executor;
    /**
     * 命令
     */
    private ModbusCommand command;
    
    private Channel channel;
    
    private CallBack callBack;
    
    private DataMsgType msgType;
    
    private String sn;

    public String getSn() {
		return sn;
	}

	public void setSn(String sn) {
		this.sn = sn;
	}

	public DataMsgType getMsgType() {
		return msgType;
	}

	public void setMsgType(DataMsgType msgType) {
		this.msgType = msgType;
	}

	public ModbusMessage(boolean isSync, int deviceNo, ModbusCommand command) {
        this.isSync = isSync;
        this.deviceNO = deviceNo;
        this.command = command;
    }

    public ModbusMessage() {

    }

    public long getTimeOut() {
        return timeOut;
    }

    public void setTimeOut(long timeOut) {
        this.timeOut = timeOut;
    }
    public ModbusCommand getCommand() {
        return command;
    }

    public void setCommand(ModbusCommand command) {
        this.command = command;
    }

    public DefaultFuture getExecutor() {
        return executor;
    }

    public void setExecutor(DefaultFuture executor) {
        this.executor = executor;
    }

    public int getSerialNum() {
       
    	if(serialNum == -1){
    		serialNum = SerialNumGenerateUtil.getSerialNum();
    	}
        return this.serialNum;
    }

    public void setSerialNum(int serialNum) {
        this.serialNum = serialNum;
    }

    public int getDeviceNO() {
        return deviceNO;
    }

    public void setDeviceNO(int deviceNO) {
        this.deviceNO = deviceNO;
    }

    public String getChannelName() {
        return channelName;
    }

    public void setChannelName(String channelName) {
        this.channelName = channelName;
    }

    public MbapHeader getHead() {

        // 获得命令中的帧长度
        int datalen = getCommand().getFrameLen();
        // 如果头部为null则创建一个新的头部
        if(head == null){
            // 帧序列号（会话号）、长度、设备编号
            head = new MbapHeader(getSerialNum(), datalen, (short)getDeviceNO());
        }

        return head;
    }

    public void setHead(MbapHeader head) {
        this.head = head;
    }

    public UnSafeHeapBuffer getRequest() {
    	if(request == null){
            getFrame();
        }
		return request;
	}

	public void setRequest(UnSafeHeapBuffer request) {
		this.request = request;
	}

	public UnSafeHeapBuffer getResponse() {
		return response;
	}

	public void setResponse(UnSafeHeapBuffer response) {
		this.response = response;
	}

	public boolean isSync() {
        return isSync;
    }

    public void setSync(boolean isSync) {
        this.isSync = isSync;
    }

    public Integer getReSendCount() {
        return reSendCount;
    }

    public void setReSendCount(Integer reSendCount) {
        this.reSendCount = reSendCount;
    }

    public Integer getPeriodTime() {
        return periodTime;
    }

    public void setPeriodTime(Integer periodTime) {
        this.periodTime = periodTime;
    }

    public long getSendTime() {
        return sendTime;
    }

    public void setSendTime(long sendTime) {
        this.sendTime = sendTime;
    }

   

    public UnSafeHeapBuffer getHeaderBuf() {
        return getHead().getHeadBuf();
    }
    
    public void setBody(UnSafeHeapBuffer body) {
		this.body = body;
	}

	public UnSafeHeapBuffer getBody() {
        
    	if(command == null && body != null){
    		return body;
    	}
    	UnSafeHeapBuffer buffer = new UnSafeHeapBuffer(command.getFrameLen());
        
        // 获得发送参数列表
        List<ParamType> cmdParameters = command.getSendParams();
        for (Iterator<ParamType> iter = cmdParameters.iterator(); iter
            .hasNext();){
            // 过滤掉地址码和命令码，这部分在头里面统一处理。
            ParamType para = iter.next();
            if(null != para.getParaValue()){
                // 根据参数的值，类型以及长度转为字符码并追加到字节码中
                if(para.getOrderType().equals(ByteOrder.BIG_ENDIAN)){
                    //ByteUtils.encodeParameteBigEndian(buffer,
                        //para.getParaValue(), para.getParaType(), para.getParaLen());
                }else{
                	//ByteUtils.encodeParameteLittleEndian(buffer,
                      //  para.getParaValue(), para.getParaType(), para.getParaLen());
                }
            }
        }
        return buffer;
    }
   //设置报文帧
   private void getFrame(){
	   
	   UnSafeHeapBuffer headerBuf = getHeaderBuf();
	   headerBuf.writeBytes(getBody().array());
       setRequest(headerBuf);
   }
   
	public Channel getChannel() {
	    return channel;
	}
	
	public void setChannel(Channel channel) {
	    this.channel = channel;
	}

	

	public CallBack getCallBack() {
		return callBack;
	}

	public void setCallBack(CallBack callBack) {
		this.callBack = callBack;
	}
}
