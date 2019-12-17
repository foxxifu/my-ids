package com.interest.ids.dev.network.modbus.transfer;


import com.interest.ids.dev.network.modbus.utils.RC4Utils;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

import com.interest.ids.common.project.buffer.UnSafeHeapBuffer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * @author lhq
 */
public class ModbusMessageEncoder extends MessageToByteEncoder<UnSafeHeapBuffer> {
    private static final Logger log = LoggerFactory.getLogger(ModbusMessageEncoder.class);

    @Override
    protected void encode(ChannelHandlerContext ctx, UnSafeHeapBuffer message, io.netty.buffer.ByteBuf out){
        if (message == null) {
            return;
        }
        if (message == null || message.readableBytes() <= 0) {
            return;
        }
        message = RC4Utils.rc4Deal(message);
        out.writeBytes(message.array());
    }


}
