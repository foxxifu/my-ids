package com.interest.ids.common.project.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @Author: sunbjx
 * @Description:
 * @Date Created in 16:52 2017/12/5
 * @Modified By:
 */
public class ApiLog {

    private static Logger logger = LoggerFactory.getLogger(ApiLog.class.getName() + ".ApiLog");

    public static void chargeLog(String orderNo, String created, String amount) {
        StringBuffer bf = new StringBuffer();
        bf.append(orderNo).append("|").append(created.toString()).append("|").append(amount.toString());
        logger.info(bf.toString());
    }

    public static void chargeLog1(String omsg) {
        StringBuffer bf = new StringBuffer();
        bf.append(omsg);
        logger.info(bf.toString());
    }

    public static void log(String mag) {
        logger.info(mag);
    }

}
