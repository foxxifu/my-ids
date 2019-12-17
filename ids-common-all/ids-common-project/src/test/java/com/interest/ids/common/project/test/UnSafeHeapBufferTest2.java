package com.interest.ids.common.project.test;

import java.util.Arrays;

import com.interest.ids.common.project.buffer.UnSafeHeapBuffer;

/**
 * 
 * @author lhq
 *
 */
public class UnSafeHeapBufferTest2 {
	
	public static void main(String[] args) {
		UnSafeHeapBuffer buffer = new UnSafeHeapBuffer(100);
		buffer.writeShortLittleEndian(10);
		buffer.writeIntLittleEndian(20);
		buffer.writeLongLittleEndian(30);
		buffer.writeFloatLittleEndian(40.0f);
		buffer.writeDoubleLittleEndian(5.123);
		
		System.out.println(Arrays.toString(buffer.array()));
		System.out.println(buffer.readShortLittleEndian());
		System.out.println(buffer.readIntLittleEndian());
		System.out.println(buffer.readLongLittleEndian());
		System.out.println(buffer.readFloatLittleEndian());
		System.out.println(buffer.readDoubleLittleEndian());
		
		
		
	}

}
