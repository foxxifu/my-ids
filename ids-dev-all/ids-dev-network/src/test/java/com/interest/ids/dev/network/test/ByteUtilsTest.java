package com.interest.ids.dev.network.test;

import java.math.BigInteger;

public class ByteUtilsTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		 byte[] data = new byte[10];
		 for(int i=0;i<data.length;i++){
			 data[i] = 16;
		 }
		 BigInteger bigInteger = new BigInteger(1, data);  
	     System.out.println(bigInteger.toString(16));

	}

}
