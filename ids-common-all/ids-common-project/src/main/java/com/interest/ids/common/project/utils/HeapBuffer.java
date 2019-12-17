package com.interest.ids.common.project.utils;



/**
 * 
 * @author lhq
 *
 */
public class HeapBuffer {
	
	//读取游标
	private  int readerIndex;
	//写游标
	private  int writerIndex;
	
    
    
    public void checkReadableBytes0(int minimumReadableBytes) {
        //ensureAccessible();
        if (readerIndex > writerIndex - minimumReadableBytes) {
            throw new IndexOutOfBoundsException(String.format(
                    "readerIndex(%d) + length(%d) exceeds writerIndex(%d): %s",
                    readerIndex, minimumReadableBytes, writerIndex, this));
        }
    }
    
    

}
