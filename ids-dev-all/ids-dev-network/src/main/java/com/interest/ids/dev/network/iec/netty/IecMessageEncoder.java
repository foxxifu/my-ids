package com.interest.ids.dev.network.iec.netty;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.interest.ids.common.project.utils.MessageFormatUtils;
import com.interest.ids.dev.api.utils.ByteUtils;
import com.interest.ids.dev.network.iec.bean.Iec104Message;

/**
 * netty编码器
 * 
 * @author claude
 *
 */
public class IecMessageEncoder extends MessageToByteEncoder<Iec104Message> {

	private static final Logger log = LoggerFactory.getLogger(IecMessageEncoder.class);

	@Override
	protected void encode(ChannelHandlerContext ctx, Iec104Message message,
			ByteBuf out) throws Exception {
		if (message == null) {
			return;
		}
		byte[] msg = message.array();
		if (msg == null || msg.length <= 0) {
			return;
		}
		String logMsg = MessageFormatUtils.getMsg("send 104 msg:", 
				ctx.channel().remoteAddress(), ByteUtils.formatHexString(msg));
		log.info(logMsg);
		out.writeBytes(msg);
	}
}
