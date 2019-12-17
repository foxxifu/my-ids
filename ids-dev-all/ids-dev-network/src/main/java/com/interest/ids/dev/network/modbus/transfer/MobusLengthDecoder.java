/*
 * Copyright (C) TD Tech<br>
 * All Rights Reserved.<br>
 * 
 */
package com.interest.ids.dev.network.modbus.transfer;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;

import java.nio.ByteOrder;

import com.interest.ids.common.project.buffer.UnSafeHeapBuffer;

/**
 * 
 * @author lhq
 *
 *
 */
public class MobusLengthDecoder extends LengthFieldBasedFrameDecoder {

	public MobusLengthDecoder(ByteOrder byteOrder, int maxFrameLength,
			int lengthFieldOffset, int lengthFieldLength, int lengthAdjustment,
			int initialBytesToStrip, boolean failFast) {
		super(byteOrder, maxFrameLength, lengthFieldOffset, lengthFieldLength,
				lengthAdjustment, initialBytesToStrip, failFast);
	}

	@Override
	protected Object decode(ChannelHandlerContext ctx,
			io.netty.buffer.ByteBuf in) throws Exception {
		ByteBuf buf = (ByteBuf) super.decode(ctx, in);
		if (buf != null) {
			in.markReaderIndex();
			int len = buf.readableBytes();
			if (len > 0) {
				byte[] data = new byte[len];
				buf.readBytes(data);
				UnSafeHeapBuffer buffer = new UnSafeHeapBuffer(data);
				return buffer;
			}
		}
		return null;
	}

}
