package com.interest.ids.dev.network.iec.netty;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;

import com.interest.ids.dev.network.iec.bean.Iec104Message;
import com.interest.ids.dev.network.iec.utils.IecConstant;
import com.interest.ids.dev.network.iec.utils.IecConstant.Iec104FrameType;
import com.interest.ids.dev.network.iec.utils.IecUtils;

/**
 * netty解码器
 * 
 * @author claude
 *
 */
public class IecMessageDecoder extends ByteToMessageDecoder {

	@Override
	protected void decode(ChannelHandlerContext ctx, ByteBuf buffer, List<Object> out) throws Exception {
		if (buffer != null) {
			Iec104Message msg = decode104(buffer);
			if (msg != null) {
				out.add(msg);
			}
		}
	}

	/**
	 * 解码104消息
	 * 
	 * @param buffer
	 *            消息内容
	 * @return Iec104Message
	 * @throws Exception
	 */
	private Iec104Message decode104(ByteBuf buffer) throws Exception {

		buffer.markReaderIndex();

		int readableLength = buffer.readableBytes();

		for (int i = 0; i < readableLength; i++) {

			if (buffer.readByte() != IecConstant.IEC104_FRAME_START) {
				continue;
			}
			if (!buffer.isReadable()) {

				buffer.resetReaderIndex();
				return null;
			}

			short length = buffer.readUnsignedByte();

			if (buffer.readableBytes() < length) {
				buffer.resetReaderIndex();
				return null;
			}

			byte ctrl1 = buffer.readByte();
			byte ctrl2 = buffer.readByte();
			byte ctrl3 = buffer.readByte();
			byte ctrl4 = buffer.readByte();
			byte[] ctrlArray = new byte[] { ctrl1, ctrl2, ctrl3, ctrl4 };
			Iec104FrameType frameType = IecUtils.getFrameType(ctrl1);
			// 这种说明是I帧，需要解析
			if (length > IecConstant.CTRL_FIELD_LEN) {
				byte[] asdu = new byte[length - IecConstant.CTRL_FIELD_LEN];
				buffer.readBytes(asdu);
				Iec104Message msg = new Iec104Message(ctrlArray, asdu);
				msg.setFrameType(frameType);
				return msg;
			}

			Iec104Message msg = new Iec104Message(ctrlArray);
			msg.setRecvTime(System.currentTimeMillis());
			msg.setFrameType(frameType);
			return msg;
		}
		buffer.resetReaderIndex();
		return null;
	}

}
