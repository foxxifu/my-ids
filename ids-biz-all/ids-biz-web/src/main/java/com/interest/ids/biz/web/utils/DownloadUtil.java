package com.interest.ids.biz.web.utils;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @Author: sunbjx
 * @Description:
 * @Date Created in 00:50 2018/1/22
 * @Modified By:
 */
public class DownloadUtil {

    private static final Logger logger = LoggerFactory.getLogger(DownloadUtil.class);

    public static boolean downLoadFileByPath(String filePath, HttpServletResponse response) throws Exception {
        // 根据文件路径获得File文件
        File file = new File(filePath);
        // 取得文件名。
        String filename = file.getName();
        // 取得文件的后缀名。
        String ext = filename.substring(filename.lastIndexOf(".") + 1).toUpperCase();
        // 设置文件类型(这样设置就不止是下Excel文件了，一举多得)
        if ("pdf".equals(ext)) {
            response.setContentType("application/pdf;charset=GBK");
        } else if ("xls".equals(ext)) {
            response.setContentType("application/msexcel;charset=GBK");
        } else if ("doc".equals(ext)) {
            response.setContentType("application/msword;charset=GBK");
        }
        // 文件名
        response.setHeader("Content-Disposition", "attachment;filename=\""
                + new String(filename.getBytes(), "ISO8859-1") + "\"");
        response.setContentLength((int) file.length());
        byte[] buffer = new byte[4096];// 缓冲区
        BufferedOutputStream output = null;
        BufferedInputStream input = null;
        try {
            output = new BufferedOutputStream(response.getOutputStream());
            input = new BufferedInputStream(new FileInputStream(file));
            int n = -1;
            // 遍历，开始下载
            while ((n = input.read(buffer, 0, 4096)) > -1) {
                output.write(buffer, 0, n);
            }
            output.flush(); // 不可少
            response.flushBuffer();// 不可少
        } catch (Exception e) {
            logger.info("Download error: ", e);
        } finally {
            // 关闭流，不可少
            if (input != null)
                input.close();
            if (output != null)
                output.close();
        }
        return false;
    }

    public static boolean downLoadFile(File file, HttpServletResponse response) throws Exception {
        // 取得文件名。
        String filename = file.getName();
        // 取得文件的后缀名。
        String ext = filename.substring(filename.lastIndexOf(".") + 1).toUpperCase();
        // 设置文件类型(这样设置就不止是下Excel文件了，一举多得)
        if ("pdf".equals(ext)) {
            response.setContentType("application/pdf;charset=GBK");
        } else if ("xls".equals(ext)) {
            response.setContentType("application/msexcel;charset=GBK");
        } else if ("doc".equals(ext)) {
            response.setContentType("application/msword;charset=GBK");
        }
        // 文件名
        response.setHeader("Content-Disposition", "attachment;filename=\""
                + new String(filename.getBytes(), "ISO8859-1") + "\"");
        response.setContentLength((int) file.length());
        byte[] buffer = new byte[4096];// 缓冲区
        BufferedOutputStream output = null;
        BufferedInputStream input = null;
        try {
            output = new BufferedOutputStream(response.getOutputStream());
            input = new BufferedInputStream(new FileInputStream(file));
            int n = -1;
            // 遍历，开始下载
            while ((n = input.read(buffer, 0, 4096)) > -1) {
                output.write(buffer, 0, n);
            }
            output.flush(); // 不可少
            response.flushBuffer();// 不可少
        } catch (Exception e) {
            logger.info("Download error: ", e);
        } finally {
            // 关闭流，不可少
            if (input != null)
                input.close();
            if (output != null)
                output.close();
        }
        return false;
    }

    public static void downLoadExcel(HSSFWorkbook excel, String fileName, HttpServletResponse response)
            throws Exception {

        if (excel == null || fileName == null || response == null) {
            return;
        }

        OutputStream ops = null;
        try {
            
            response.reset();

            // 设置文件类型
            response.setContentType("application/msexcel");
            fileName = getUrlSpace(fileName);
            // 文件名
            response.setHeader("Content-Disposition", "attachment;filename="+ fileName);
            ops = response.getOutputStream();
            excel.write(ops);

            ops.flush();
            response.flushBuffer();

        } catch (Exception e) {
            logger.error("Download Excel get error!", e);
        } finally {
            // 关闭流
            if (ops != null)
                ops.close();
            
            if (excel != null) 
                excel.close();
            
        }
    }

    /**
     * 将url中的空格转换为可以使用的
     * @param fileName
     * @return
     * @throws UnsupportedEncodingException
     */
	public static String getUrlSpace(String fileName) throws UnsupportedEncodingException {
		fileName = URLEncoder.encode(fileName, "UTF-8");
		fileName = StringUtils.replace(fileName, "+", "%20"); // +是空格转换过来的，小酒馆为空格的符号 
		return fileName;
	}
}
