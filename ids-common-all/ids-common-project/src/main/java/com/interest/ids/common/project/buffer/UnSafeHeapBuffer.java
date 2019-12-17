package com.interest.ids.common.project.buffer;




/**
 * 
 * @author lhq
 *
 */
public class UnSafeHeapBuffer {
	
	 private int readIndex;
	 
	 private int writeIndex;
	 
	 private byte[] data;
	 
	 private int capcity;
	 
	 public UnSafeHeapBuffer(byte[] data){
		 this.data = data;
		 capcity = data.length;
		 writeIndex = data.length;
	 }
	 
	 public UnSafeHeapBuffer(int length){
		 data = new byte[length];
		 capcity = data.length;
	 }
	 
	 
	 //BIG_ENDIAN的情况
	 public byte readByte(){
		 checkReadable(1);
		 byte v = UnsafeByteBufUtil.getByte(data, readIndex);
		 readIndex += 1;
		 return v;
	 }
	 
	 public short readUnsignedByte(){
		 
		 return (short) (readByte() & 0xFF);
	 }
	 
	 public short readShort(){
		 checkReadable(2);
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
		 checkReadable(4);
		 int v = UnsafeByteBufUtil.getInt(data, readIndex);
		 readIndex += 4;
		 return v;
	 }
	 
	 public long readLong(){
		 checkReadable(8);
		 long v = UnsafeByteBufUtil.getLong(data, readIndex);
		 readIndex += 8;
		 return v;
	 }
	 
	 public float readFloat(){
		 return Float.intBitsToFloat(readInt());
	 }
	 
	 public double readDouble(){
		 return Double.longBitsToDouble(readLong());
	 }
	 
	 public UnSafeHeapBuffer readBytes(byte[] dst){
		 checkReadable(dst.length);
		 readBytes(dst,0,dst.length);
		 //readIndex += dst.length;
		 return this;
	 }
	 
	 public UnSafeHeapBuffer readBytes(byte[] dst, int dstIndex, int length) {
		 checkReadable(length);
	     getBytes(readIndex, dst, dstIndex, length);
	     readIndex += length;
	     return this;
	 }
	 
	 public UnSafeHeapBuffer getBytes(int index, byte[] dst, int dstIndex, int length) {
	      System.arraycopy(data, index, dst, dstIndex, length);
	      return this;
	 }
	 
	 public byte[] readBytes(int length){
		 
		 byte[] data = new byte[length];
		 readBytes(data);
		 return data;
	 }
	 
	 public UnSafeHeapBuffer skipBytes(int length){
		 checkReadable(length);
	     readIndex += length;
	     return this;
	 }
	 
	 public int readableBytes() {
	        return writeIndex - readIndex;
	 }
	 
	 /**
	  * 小端模式开始
	  * 
	  */
	 public short readShortLittleEndian(){
		return Short.reverseBytes(readShort());
	 }
	 
	 public int readUnsignedShortLittleEndian(){
			return Short.reverseBytes(readShort())& 0xFFFF;
	 }
	 
	 public long readUnsignedIntLittleEndian(){
			return Integer.reverseBytes(readInt())& 0xFFFFFFFFL;
	 }
	 
	 public int readIntLittleEndian(){
		 return Integer.reverseBytes(readInt());
	 }
	 
	 public long readLongLittleEndian(){
		 
		 return Long.reverseBytes(readLong());
	 }
	 
	 public float readFloatLittleEndian(){
		 
		 return Float.intBitsToFloat(readIntLittleEndian());
	 }
	 
	 public double readDoubleLittleEndian(){
		 
		 return Double.longBitsToDouble(readLongLittleEndian());
	 }
	 
	 
	 public byte getByte(int index){
		 //checkReadable(1);
		 return UnsafeByteBufUtil.getByte(data, index);
	 }
	 
	 
	 
	 //write
	 public UnSafeHeapBuffer writeByte(int value){
		 ensureWritable(1);
		 UnsafeByteBufUtil.setByte(data, writeIndex++, (byte) value);
		 return this;
	 }
	 
	 public UnSafeHeapBuffer writeShort(int value){
		 ensureWritable(2);
		 UnsafeByteBufUtil.setShort(data, writeIndex, (short) value);
		 writeIndex += 2;
		 return this;
	 }
	 
	 public UnSafeHeapBuffer writeInt(int value){
		 ensureWritable(4);
		 UnsafeByteBufUtil.setInt(data, writeIndex, value);
		 writeIndex += 4;
		 return this;
	 }
	 
	 public UnSafeHeapBuffer writeLong(long value){
		 ensureWritable(8);
		 UnsafeByteBufUtil.setLong(data, writeIndex, value);
		 writeIndex += 8;
		 return this;
	 }
	 
	 public UnSafeHeapBuffer writeFloat(float value){
		 writeInt(Float.floatToRawIntBits(value));
		 return this;
	 }
	 
	 public UnSafeHeapBuffer writeDouble(double value){
		 writeLong(Double.doubleToRawLongBits(value));
		 return this;
	 } 
	 
	 public UnSafeHeapBuffer writeShortLittleEndian(int value){
		 ensureWritable(2);
		 short v = Short.reverseBytes((short) value);
		
		 UnsafeByteBufUtil.setShort(data, writeIndex,  v);
		 writeIndex += 2;
		 return this;
	 }
	 
	 public UnSafeHeapBuffer writeIntLittleEndian(int value){
		 ensureWritable(4);
		 int v = Integer.reverseBytes(value);
		 UnsafeByteBufUtil.setInt(data, writeIndex, v);
		 writeIndex += 4;
		 return this;
	 }
	 
	 public UnSafeHeapBuffer writeLongLittleEndian(long value){
		 ensureWritable(8);
		 long v = Long.reverseBytes(value);
		 UnsafeByteBufUtil.setLong(data, writeIndex,  v);
		 writeIndex += 8;
		 return this;
	 }
	 
	 public UnSafeHeapBuffer writeFloatLittleEndian(float value){
		 
		 writeIntLittleEndian(Float.floatToRawIntBits(value));

		 return this;
	 }
	 
	 public UnSafeHeapBuffer writeDoubleLittleEndian(double value){
		 writeLongLittleEndian(Double.doubleToRawLongBits(value));
		 return this;
	 }
	 
	
	 
	 public UnSafeHeapBuffer writeBytes(byte[] src){
		 
		 writeBytes(src,0,src.length);
		 return this;
	 }
	 
	 public UnSafeHeapBuffer writeBytes(byte[] src, int srcIndex, int length) {
	       ensureWritable(length);
	       setBytes(writeIndex, src, srcIndex, length);
	       writeIndex += length;
	       return this;
	 }
	 
	 private UnSafeHeapBuffer setBytes(int index, byte[] src, int srcIndex, int length) {
	      System.arraycopy(src, srcIndex, data, index, length);
	      return this;
	 }
	 
	 private void checkReadable(int readableBytes){
		 if (readIndex > writeIndex - readableBytes) {
	            throw new IndexOutOfBoundsException(String.format(
	                    "readerIndex(%d) + length(%d) exceeds writerIndex(%d): %s",
	                    readIndex, readableBytes, writeIndex, this));
	        }
	 }
	 
	 public int writableBytes(){
		 return capcity - writeIndex;
	 }
	 
	 private void ensureWritable(int writableBytes) {
	        if (writableBytes <= writableBytes()) {
	            return;
	        }

	        if (writableBytes > capcity - writeIndex) {
	            throw new IndexOutOfBoundsException(String.format(
	                    "writerIndex(%d) + minWritableBytes(%d) exceeds maxCapacity(%d): %s",
	                    writeIndex, writableBytes, capcity, this));
	        }

	        
	        //int newCapacity = calculateNewCapacity(writeIndex + writableBytes);

	        
	        //capacity(newCapacity);
	    }

	 public byte[] array(){
		 return data;
	 }
	 
	 public String toString(){
		 return readIndex+"  "+writeIndex;
	 }
}
