package com.interest.ids.dev.db.test;

import io.netty.util.collection.IntObjectHashMap;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Date;
import java.util.Map;

import com.interest.ids.common.project.bean.signal.DeviceInfo;
import com.interest.ids.common.project.bean.signal.SignalInfo;
import com.interest.ids.common.project.buffer.UnSafeHeapBuffer;
import com.interest.ids.common.project.spring.propholder.CustomPropertyConfigurer;
import com.interest.ids.dev.api.bean.SingleRecordBean;
import com.interest.ids.dev.api.localcache.DeviceLocalCache;
import com.interest.ids.dev.api.localcache.SignalLocalCache;
import com.interest.ids.dev.api.utils.DevServiceUtils;
import com.interest.ids.dev.db.utils.DevDbConstant;

public class BinlogParser {

	/**
	 * @param args
	 * @throws FileNotFoundException 
	 */
	public static void main(String[] args) throws Exception {
	
	    /*File file = new File("F:/srv/admin/data/0x10_1000_255.bin");
		long size = file.length();
		FileInputStream fis = null;
		
			fis = new FileInputStream(file);
			byte[] data = new byte[fis.available()];
			
			fis.read(data);
		
		
			UnSafeHeapBuffer buffer = new UnSafeHeapBuffer(data);
			
			int version = buffer.readUnsignedShortLittleEndian();
			int devNumber = buffer.readUnsignedShortLittleEndian();
			buffer.skipBytes(2);
			// 时区
			int timeZone = buffer.readUnsignedShortLittleEndian();
			
			for(int i=0;i<devNumber;i++){
								
				byte[] esnBytes = new byte[DevDbConstant.devESNCode];
				buffer.readBytes(esnBytes);
				String esnCode = new String(esnBytes).trim();
				boolean isExist = false;
				Map<Integer, String> columnAttr = null;
				
				System.out.println(esnCode);
				
				buffer.skipBytes(2);
				int record = buffer.readUnsignedShortLittleEndian();
				
				//int record = buffer.readByte();
				
				for(int j=0;j<record;j++){
					Long startTime = buffer.readUnsignedIntLittleEndian()*1000;
					System.out.println(new Date(startTime));
					buffer.skipBytes(1);
					short signalNum = buffer.readUnsignedByte();
					for (int item = 0; item < signalNum; item++) {
						
						int regAddr = buffer.readUnsignedShortLittleEndian();
						//
						int readableBytes = 2 * buffer.readUnsignedByte();
						int index = 0;
						
						if(!isExist){
							buffer.skipBytes(readableBytes);
							continue;
						}
						while(index < readableBytes){
							
								//跳过当前寄存器
								buffer.skipBytes(2);
								index += 2;
								regAddr += 1;
								continue;
							
						}
					}
				}
				
			}*/
	
		}

	

}
