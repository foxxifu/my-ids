package com.interest.ids.common.project.test;

import com.interest.ids.common.project.buffer.UnCheckedHeapBuffer;
import com.interest.ids.common.project.buffer.UnSafeHeapBuffer;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

public class BufferTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		ByteBuf buf = Unpooled.buffer(1000);
		buf.writeBytes(new byte[1000]);
		
		
		long t1 = System.nanoTime();
		for(int i=0;i<1000;i++){
			buf.readByte();
		}
		long t2 = System.nanoTime();
		
		long t = t2 - t1;
		System.out.println(t);
		
		UnCheckedHeapBuffer buf2 = new UnCheckedHeapBuffer(new byte[1000]);
		
		
		 t1 = System.nanoTime();
		for(int i=0;i<1000;i++){
			buf2.readByte();
		}
		 t2 = System.nanoTime();
		
		 t = t2 - t1;
		System.out.println(t);
		
		UnSafeHeapBuffer buf3 = new UnSafeHeapBuffer(new byte[1000]);
		buf3.writeBytes(new byte[1000]);
		 t1 = System.nanoTime();
		for(int i=0;i<1000;i++){
			buf3.readByte();
		}
		 t2 = System.nanoTime();
		
		 t = t2 - t1;
		System.out.println(t);
	}

}
