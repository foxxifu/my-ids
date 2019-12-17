package com.interest.ids.biz.web.utils;

import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFClientAnchor;
import org.apache.poi.hssf.usermodel.HSSFComment;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFPatriarch;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.VerticalAlignment;

import com.interest.ids.common.project.utils.MathUtil;

/**
 * 导出工具类
 * 
 * @author claude
 *
 */
public class ExportUtils {

	/**
	 * 创建点表excel导出
	 * 
	 * @param sheetNames
	 * 			点表每个sheet页名称
	 * @param headerNames
	 * 			点表每个sheet页表头
	 * @param datas
	 * 			点表每个sheet页数据
	 * @return HSSFWorkbook
	 */
	@SuppressWarnings("unchecked")
	public static HSSFWorkbook createSignalExcel(String[] sheetNames,
			String[][] headerNames, Object[] datas) {

		// 创建excel对象
		HSSFWorkbook excel = new HSSFWorkbook();
		for (int i = 0; i < sheetNames.length; i++) {
			if (i == 0) {
				// 创建worksheet对象
				HSSFSheet sheet = excel.createSheet();
				excel.setSheetName(0, sheetNames[0]);

				// 创建字体
				HSSFFont cellFont = excel.createFont();
				cellFont.setFontHeightInPoints((short) 12);
				cellFont.setBold(true);
				cellFont.setFontName("宋体");

				// 创建单元格样式
				HSSFCellStyle cellStyle = excel.createCellStyle();
				cellStyle.setFont(cellFont);
				cellStyle.setAlignment(HorizontalAlignment.CENTER);
				cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
				cellStyle.setBorderBottom(BorderStyle.THIN);
				cellStyle.setBorderLeft(BorderStyle.THIN);
				cellStyle.setBorderRight(BorderStyle.THIN);
				cellStyle.setBorderTop(BorderStyle.THIN);
				cellStyle.setWrapText(true);

				// 创建表头
				HSSFRow headerRow = sheet.createRow(0);
				headerRow.setHeight((short) (15.625 * 44));// 设置行高
				int columnNum = headerNames[0].length;
				for (int j = 0; j < columnNum; j++) {
					sheet.setColumnWidth(i,
							headerNames[0][j].split("\r\n")[0].getBytes().length * 2 * 256);
					HSSFCell headerCell = headerRow.createCell(j);
					headerCell.setCellValue(headerNames[0][j]);
					headerCell.setCellStyle(cellStyle);
				}

				// 数据行字体
				cellFont = excel.createFont();
				cellFont.setFontHeightInPoints((short) 12);
				cellFont.setFontName("宋体");

				cellStyle = excel.createCellStyle();
				cellStyle.setAlignment(HorizontalAlignment.CENTER);
				cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
				cellStyle.setBorderBottom(BorderStyle.THIN);
				cellStyle.setBorderLeft(BorderStyle.THIN);
				cellStyle.setBorderRight(BorderStyle.THIN);
				cellStyle.setBorderTop(BorderStyle.THIN);
				cellStyle.setFont(cellFont);
				List<Object[]> data = (List<Object[]>) datas[0];
				if (datas != null && data.size() > 0) {
					int rownum = 1;
					for (Object[] rowData : data) {
						HSSFRow dataRow = sheet.createRow(rownum);
						dataRow.setHeight((short) (15.625 * 26));// 设置行高
						for (int j = 0; j < columnNum; j++) {
							sheet.setColumnWidth(j,
								headerNames[1][j].split("\r\n")[0].getBytes().length * 2 * 256);
							HSSFCell dataCell = dataRow.createCell(j);
							dataCell.setCellValue(MathUtil.formatString(rowData[j], null));
							dataCell.setCellStyle(cellStyle);
						}
						rownum++;
					}
				}
			}

			if (i == 1) {
				// 创建worksheet对象
				HSSFSheet sheet = excel.createSheet();
				excel.setSheetName(1, sheetNames[1]);

				// 创建字体
				HSSFFont cellFont = excel.createFont();
				cellFont.setFontHeightInPoints((short) 12);
				cellFont.setBold(true);
				cellFont.setFontName("宋体");

				// 创建单元格样式
				HSSFCellStyle cellStyle = excel.createCellStyle();
				cellStyle.setFont(cellFont);
				cellStyle.setAlignment(HorizontalAlignment.CENTER);
				cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
				cellStyle.setBorderBottom(BorderStyle.THIN);
				cellStyle.setBorderLeft(BorderStyle.THIN);
				cellStyle.setBorderRight(BorderStyle.THIN);
				cellStyle.setBorderTop(BorderStyle.THIN);
				cellStyle.setWrapText(true);

				// 创建表头
				HSSFRow headerRow = sheet.createRow(0);
				headerRow.setHeight((short) (15.625 * 44));// 设置行高
				int columnNum = headerNames[1].length;
				for (int j = 0; j < columnNum; j++) {
					sheet.setColumnWidth(j,
							headerNames[1][j].split("\r\n")[0].getBytes().length * 2 * 256);
					HSSFCell headerCell = headerRow.createCell(j);
					headerCell.setCellValue(headerNames[1][j]);
					headerCell.setCellStyle(cellStyle);
					if(j == 1){
						// 创建绘图对象
						HSSFPatriarch p = sheet.createDrawingPatriarch();
						HSSFComment comment = p
								.createComment(new HSSFClientAnchor(0, 0, 0, 0,
										(short) 3, 2, (short) 5, 6));
						// 输入批注信息
						comment.setString(new HSSFRichTextString("作者:XY\r\n1：遥测\r\n2：遥信\r\n3：遥脉\r\n4：遥控"));
						// 将批注添加到单元格对象中
						headerCell.setCellComment(comment);
					}
				}

				// 数据行字体
				cellFont = excel.createFont();
				cellFont.setFontHeightInPoints((short) 12);
				cellFont.setFontName("宋体");

				cellStyle = excel.createCellStyle();
				cellStyle.setAlignment(HorizontalAlignment.CENTER);
				cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
				cellStyle.setBorderBottom(BorderStyle.THIN);
				cellStyle.setBorderLeft(BorderStyle.THIN);
				cellStyle.setBorderRight(BorderStyle.THIN);
				cellStyle.setBorderTop(BorderStyle.THIN);
				cellStyle.setFont(cellFont);

				List<Object[]> data = (List<Object[]>) datas[1];
				if (datas != null && data.size() > 0) {
					int rownum = 1;
					for (Object[] rowData : data) {
						HSSFRow dataRow = sheet.createRow(rownum);
						dataRow.setHeight((short) (15.625 * 26));// 设置行高
						for (int j = 0; j < columnNum; j++) {
							HSSFCell dataCell = dataRow.createCell(j);
							dataCell.setCellValue(MathUtil.formatString(rowData[j], null));
							dataCell.setCellStyle(cellStyle);
						}
						rownum++;
					}
				}
			}
			if (i == 2) {
				// 创建worksheet对象
				HSSFSheet sheet = excel.createSheet();
				excel.setSheetName(2, sheetNames[2]);

				// 创建字体
				HSSFFont cellFont = excel.createFont();
				cellFont.setFontHeightInPoints((short) 12);
				cellFont.setBold(true);
				cellFont.setFontName("宋体");

				// 创建单元格样式
				HSSFCellStyle cellStyle = excel.createCellStyle();
				cellStyle.setFont(cellFont);
				cellStyle.setAlignment(HorizontalAlignment.CENTER);
				cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
				cellStyle.setBorderBottom(BorderStyle.THIN);
				cellStyle.setBorderLeft(BorderStyle.THIN);
				cellStyle.setBorderRight(BorderStyle.THIN);
				cellStyle.setBorderTop(BorderStyle.THIN);
				cellStyle.setWrapText(true);

				// 创建表头
				HSSFRow headerRow = sheet.createRow(0);
				headerRow.setHeight((short) (15.625 * 44));// 设置行高
				int columnNum = headerNames[2].length;
				for (int j = 0; j < columnNum; j++) {
					sheet.setColumnWidth(j,
							headerNames[2][j].split("\r\n")[0].getBytes().length * 2 * 256);
					HSSFCell headerCell = headerRow.createCell(j);
					headerCell.setCellValue(headerNames[2][j]);
					headerCell.setCellStyle(cellStyle);
					if(j == 1){
						// 创建绘图对象
						HSSFPatriarch p = sheet.createDrawingPatriarch();
						HSSFComment comment = p
								.createComment(new HSSFClientAnchor(0, 0, 0, 0,
										(short) 3, 2, (short) 5, 6));
						// 输入批注信息
						comment.setString(new HSSFRichTextString("作者:XY\r\n1：遥测\r\n2：遥信\r\n3：遥脉\r\n4：遥控"));
						// 将批注添加到单元格对象中
						headerCell.setCellComment(comment);
					}
				}

				// 数据行字体
				cellFont = excel.createFont();
				cellFont.setFontHeightInPoints((short) 12);
				cellFont.setFontName("宋体");

				cellStyle = excel.createCellStyle();
				cellStyle.setAlignment(HorizontalAlignment.CENTER);
				cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
				cellStyle.setBorderBottom(BorderStyle.THIN);
				cellStyle.setBorderLeft(BorderStyle.THIN);
				cellStyle.setBorderRight(BorderStyle.THIN);
				cellStyle.setBorderTop(BorderStyle.THIN);
				cellStyle.setFont(cellFont);

				List<Object[]> data = (List<Object[]>) datas[2];
				if (datas != null && data.size() > 0) {
					int rownum = 1;
					for (Object[] rowData : data) {
						HSSFRow dataRow = sheet.createRow(rownum);
						dataRow.setHeight((short) (15.625 * 26));// 设置行高
						for (int j = 0; j < columnNum; j++) {
							HSSFCell dataCell = dataRow.createCell(j);
							dataCell.setCellValue(MathUtil.formatString(rowData[j], null));
							dataCell.setCellStyle(cellStyle);
						}
						rownum++;
					}
				}
			}

		}

		return excel;
	}

	@SuppressWarnings("static-access")
	public static void main(String[] args) {
		String[][] headerNames = new String[][] {
				{ "设备类型", "设备型号", "协议类型","设备版本", "厂商名称", "设备名称", "电站名称" },
				{ "设备信号名称", "功能类型", "单位","增益", "信号地址", "偏移量", "设备名称", "信号名称" }};
		String[] sheetNames = new String[]{"A","B"};
		Object[] datas = new Object[2];
		List<Object[]> data1 = new ArrayList<Object[]>();
		for (int i = 0; i < 25; i++) {
			Object[] o = new Object[7];
			o[0] = "逆变器" + (1 + i);
			o[1] = "2018-2-12";
			o[2] = String.format("%.2f", (Math.random() * 3480));
			o[3] = String.format("%.2f", (Math.random() * 8));
			o[4] = String.format("%.2f", (Math.random() * 100));
			o[5] = String.format("%.2f", (Math.random() * 2640));
			o[6] = new SimpleDateFormat("yyyy-mm-dd HH:MM:ss")
					.format(new Date());
			data1.add(o);
		}
		datas[0] = data1;
		List<Object[]> data2 = new ArrayList<Object[]>();
		for (int i = 0; i < 25; i++) {
			Object[] o = new Object[8];
			o[0] = "逆变器" + (1 + i);
			o[1] = "2018-2-12";
			o[2] = String.format("%.2f", (Math.random() * 3480));
			o[3] = String.format("%.2f", (Math.random() * 8));
			o[4] = String.format("%.2f", (Math.random() * 100));
			o[5] = String.format("%.2f", (Math.random() * 2640));
			o[6] = new SimpleDateFormat("yyyy-mm-dd HH:MM:ss")
					.format(new Date());
			o[7] = new SimpleDateFormat("yyyy-mm-dd HH:MM:ss")
			.format(new Date());
			data2.add(o);
		}
		datas[1] = data2;
		ExportUtils eu = new ExportUtils();
		HSSFWorkbook excel = eu.createSignalExcel(sheetNames, headerNames, datas);
		 try 
         {
             FileOutputStream fos = new FileOutputStream("d:\\a.xls");
             excel.write(fos);
             System.out.println("恭喜您！写入成功！！！！！！");
             fos.close();
         } catch (IOException e) 
         {
        	 System.out.println("写入文件出错啦！");
             e.printStackTrace();
         }

	}
}
