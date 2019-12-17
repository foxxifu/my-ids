package com.interest.ids.dev.network.util;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class TestServer {
	
	public static void main(String[] args) throws Exception {
		ServerSocket s = new ServerSocket(2408);
		while(true){
			Socket so = s.accept();
			System.out.println(so.getRemoteSocketAddress());
		}
	}

}
