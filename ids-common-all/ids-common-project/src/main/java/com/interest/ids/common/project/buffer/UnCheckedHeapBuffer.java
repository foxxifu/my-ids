package com.interest.ids.common.project.buffer;




/**
 * 
 * @author lhq
 *
 */
public class UnCheckedHeapBuffer {
	
	private int readIndex;
	 
	private int writeIndex;
	 
	private byte[] data;
	 
	private int capcity;
	 
	
	public UnCheckedHeapBuffer(byte[] data){
		this.data = data;
	}
	
	public UnCheckedHeapBuffer(int length){
		data = new byte[length];
	}
	 
	 //BIG_ENDIAN的情况
	 public byte readByte(){
		 byte v = UnsafeByteBufUtil.getByte(data, readIndex);
		 readIndex += 1;
		 return v;
	 }
	 
	 public short readUnsignedByte(){
		 
		 return (short) (readByte() & 0xFF);
	 }
	 
	 public short readShort(){
		 short v = UnsafeByteBufUtil.getShort(data, readIndex);
		 readIndex += 2;
		 return v;
	 }
	 
	 public int readUnsignedShort(){
		 return readShort() & 0xFFFF;
	 }
	 
	 public long readUnsignedInt(){
		 
		 return readInt() & 0xFFFFFFFFL;
	 }
	 
	 
	 public int readInt(){
		 int v = UnsafeByteBufUtil.getInt(data, readIndex);
		 readIndex += 4;
		 return v;
	 }
	 
	 public long readLong(){
		 long v = UnsafeByteBufUtil.getLong(data, readIndex);
		 readIndex += 8;
		 return v;
	 }
	 
	 public short readShortLittleEndian(){
		 short v = UnsafeByteBufUtil.getShort(data, readIndex);
		 short v2 = Short.reverseBytes(v);
		 readIndex += 2;
		 return v2;
	 }
	 
	 public int readIntLittleEndian(){
		 int v = UnsafeByteBufUtil.getInt(data, readIndex);
		 int v2 = Integer.reverseBytes(v);
		 readIndex += 4;
		 return v2;
	 }
	 
	 public long readLongLittleEndian(){
		 long v = UnsafeByteBufUtil.getLong(data, readIndex);
		 long v2 = Long.reverseBytes(v);
		 readIndex += 8;
		 return v2;
	 }
	 
	 public int readUnsignedShortLittleEndian(){
		 return readShortLittleEndian() & 0xFFFF;
	 }
	 
	 public long readUnsignedIntLittleEndian(){
		 
		 return readIntLittleEndian() & 0xFFFFFFFFL;
	 }
	 
	 public UnCheckedHeapBuffer readBytes(byte[] dst){
		 readBytes(dst,0,dst.length);
		 readIndex += dst.length;
		 return this;
	 }
	 
	 public UnCheckedHeapBuffer readBytes(byte[] dst, int dstIndex, int length) {
	     getBytes(readIndex, dst, dstIndex, length);
	     readIndex += length;
	     return this;
	 }
	 
	 public UnCheckedHeapBuffer getBytes(int index, byte[] dst, int dstIndex, int length) {
	      System.arraycopy(data, index, dst, dstIndex, length);
	      return this;
	 }
	 
	 
	 //write
	 public UnCheckedHeapBuffer writeByte(int value){
		 UnsafeByteBufUtil.setByte(data, writeIndex++, (byte) value);
		 return this;
	 }
	 
	 public UnCheckedHeapBuffer writeShort(int value){
		 UnsafeByteBufUtil.setShort(data, writeIndex, (short) value);
		 writeIndex += 2;
		 return this;
	 }
	 
	 public UnCheckedHeapBuffer writeInt(int value){
		 UnsafeByteBufUtil.setInt(data, writeIndex, value);
		 writeIndex += 4;
		 return this;
	 }
	 
	 public UnCheckedHeapBuffer writeLong(long value){
		 UnsafeByteBufUtil.setLong(data, writeIndex, value);
		 writeIndex += 8;
		 return this;
	 }
	 
	 public UnCheckedHeapBuffer writeBytes(byte[] src){
		 
		 writeBytes(src,0,src.length);
		 return this;
	 }
	 
	 public UnCheckedHeapBuffer writeBytes(byte[] src, int srcIndex, int length) {
	       setBytes(writeIndex, src, srcIndex, length);
	       writeIndex += length;
	       return this;
	 }
	 
	 private UnCheckedHeapBuffer setBytes(int index, byte[] src, int srcIndex, int length) {
	      System.arraycopy(src, srcIndex, data, index, length);
	      return this;
	 }
	 
	 
	 public int writableBytes(){
		 return capcity - writeIndex;
	 }
	

}
