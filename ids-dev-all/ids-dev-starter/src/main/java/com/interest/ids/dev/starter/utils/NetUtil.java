package com.interest.ids.dev.starter.utils;

import java.io.IOException;
import java.net.Socket;

/**
 * @Author: sunbjx
 * @Description:
 * @Date: Created in 下午2:19 18-1-22
 * @Modified By:
 */
public class NetUtil {

    public static boolean isPortUsing(String host, int port)  {
        boolean flag = true;
        //InetAddress theAddress = InetAddress.getByName(host);
        Socket socket = null;
        try {
            socket = new Socket(host, port);
        } catch (IOException e) {
            flag = false;
        } finally {
            if (null != socket) {
                try {
                    socket.close();
                } catch (IOException e) {
                    socket = null;
                }
            }
        }
        return flag;
    }
}
