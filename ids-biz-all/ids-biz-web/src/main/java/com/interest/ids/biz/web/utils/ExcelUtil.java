package com.interest.ids.biz.web.utils;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.List;
import java.util.Map;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.util.CellRangeAddress;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import com.interest.ids.common.project.utils.MathUtil;

/**
 * @Author: sunbjx
 * @Description:
 * @Date Created in 00:48 2018/1/22
 * @Modified By:
 */
public class ExcelUtil {
	
	private static final Logger log = LoggerFactory.getLogger(ExcelUtil.class);

	@SuppressWarnings("resource")
	public static void specifiedLoopWrite(String filePath, int dateX,
			int dateY, String dateStart, String dateEnd, int x, int y,
			List<Map<String, Object>> list) {
		try {
			// 创建Excel的工作书册 Workbook,对应到一个excel文档
			HSSFWorkbook wb = new HSSFWorkbook(new FileInputStream(filePath));

			// 设置单元格样式
			CellStyle cs = wb.createCellStyle();
			cs.setBorderLeft(BorderStyle.THIN);
			cs.setBorderRight(BorderStyle.THIN);
			cs.setBorderTop(BorderStyle.THIN);
			cs.setBorderBottom(BorderStyle.THIN);
			cs.setAlignment(HorizontalAlignment.CENTER);
			// 获取页脚
			HSSFSheet sheet = wb.getSheetAt(0);
			// 获取行
			HSSFRow row = sheet.getRow(dateX);
			// 获取单元格
			HSSFCell cell = row.getCell((short) dateY);
			cell.setCellValue(dateStart);
			cell.setCellStyle(cs);
			cell = row.getCell((short) ++dateY);
			cell.setCellValue("至");
			cell.setCellStyle(cs);
			cell = row.getCell((short) ++dateY);
			cell.setCellValue(dateEnd);
			cell.setCellStyle(cs);
			for (Map<String, Object> map : list) {
				row = sheet.getRow(x);
				for (Object value : map.values()) {
					cell = row.getCell((short) y);
					cell.setCellValue(StringUtils.isEmpty(String.valueOf(value)) ? ""
							: String.valueOf(value));
					cell.setCellStyle(cs);
					++y;
				}
				++x;
			}

			FileOutputStream os;
			os = new FileOutputStream(filePath);
			wb.write(os);
			os.close();
		} catch (Exception e) {
			log.error("specifiedLoopWrite error. msg : " + e);
		}
	}

	/**
	 * 通用报表对象创建
	 *
	 * @param title
	 *            标题
	 * @param headerNames
	 *            列头名
	 * @param datas
	 *            数据
	 * @return
	 */
	public static HSSFWorkbook createCommonExcel(String title,
			List<String> headerNames, List<Object[]> datas) {

		// 创建excel对象
		HSSFWorkbook excel = new HSSFWorkbook();

		// 创建worksheet对象
		HSSFSheet sheet = excel.createSheet();
		excel.setSheetName(0, title);

		// 创建字体
		HSSFFont cellFont = excel.createFont();
		cellFont.setFontHeightInPoints((short) 10);
		cellFont.setBold(true);
		cellFont.setFontName("微软雅黑");

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

		// 创建标题行
		HSSFRow titleRow = sheet.createRow(0);
		titleRow.setHeight((short) (15.625 * 44));// 设置行高
		HSSFCell titleCell = titleRow.createCell(0);
		titleCell.setCellValue(title);
		titleCell.setCellStyle(cellStyle);
		// 合并标题行
		int columnNum = headerNames.size();
		CellRangeAddress rangeAddress = new CellRangeAddress(0, 0, 0,
				columnNum - 1);
		sheet.addMergedRegion(rangeAddress);

		// 创建表头
		HSSFRow headerRow = sheet.createRow(1);
		headerRow.setHeight((short) (15.625 * 44));// 设置行高
		for (int i = 0; i < columnNum; i++) {
			if (headerNames.get(i).split("\r\n")[0].getBytes().length * 256 >= 4608) {
				sheet.setColumnWidth(
						i,
						headerNames.get(i).split("\r\n")[0].getBytes().length * 256);
			} else {
				sheet.setColumnWidth(
						i,
						headerNames.get(i).split("\r\n")[0].getBytes().length * 256 + 2000);
			}
			HSSFCell headerCell = headerRow.createCell(i);
			headerCell.setCellValue(headerNames.get(i));
			headerCell.setCellStyle(cellStyle);
		}

		// 数据行字体
		cellFont = excel.createFont();
		cellFont.setFontHeightInPoints((short) 10);
		cellFont.setFontName("微软雅黑");

		cellStyle = excel.createCellStyle();
		cellStyle.setAlignment(HorizontalAlignment.CENTER);
		cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
		cellStyle.setBorderBottom(BorderStyle.THIN);
		cellStyle.setBorderLeft(BorderStyle.THIN);
		cellStyle.setBorderRight(BorderStyle.THIN);
		cellStyle.setBorderTop(BorderStyle.THIN);
		cellStyle.setFont(cellFont);

		if (datas != null && datas.size() > 0) {
			// 从第3行开始创建,因前面有标题行跟列头占据了两行
			int rownum = 2;
			for (Object[] rowData : datas) {
				HSSFRow dataRow = sheet.createRow(rownum);
				dataRow.setHeight((short) (15.625 * 26));// 设置行高
				for (int i = 0; i < columnNum; i++) {
					HSSFCell dataCell = dataRow.createCell(i);
					dataCell.setCellValue(MathUtil.formatString(rowData[i],
							null));
					dataCell.setCellStyle(cellStyle);
				}
				rownum++;
			}
		}

		return excel;
	}
}
