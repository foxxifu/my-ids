package com.interest.ids.common.project.utils;

import com.interest.ids.common.project.utils.excel.DoubleHandler;
import com.interest.ids.common.project.utils.excel.IntegerHandler;
import com.interest.ids.common.project.utils.excel.StringHandler;
import com.interest.ids.common.project.utils.excel.TypeHandler;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * @Author: sunbjx
 * @Description:
 * @Date Created in 14:14 2017/12/4
 * @Modified By:
 */
public class ExcelUtil {

    private final static Map<Class<?>, TypeHandler> typeHandlerMap = new HashMap<>();

    static {
        typeHandlerMap.put(Integer.class, new IntegerHandler());
        typeHandlerMap.put(Double.class, new DoubleHandler());
        typeHandlerMap.put(String.class, new StringHandler());
    }

    public static <T> List<T> importExcel(File excelFile, Class<T> type) throws Exception {

        Workbook workbook = null;
        String fileName = excelFile.getName();

        // 验证是否是 excel 文件
        if (fileName == null || !(RegularUtil.isExcel2003(fileName)) || RegularUtil.isExcel2007(fileName)) {
            return null;
        }

        if (RegularUtil.isExcel2003(fileName)) {
            workbook = new HSSFWorkbook(new FileInputStream(excelFile));
        } else {
            workbook = new XSSFWorkbook(excelFile);
        }

        List<T> result = new ArrayList<T>();

        // 得到第一个sheet
        Sheet sheet = workbook.getSheetAt(0);

        // 遍历行数
        for (int r = sheet.getFirstRowNum() + 1; r <= sheet.getLastRowNum(); r++) {
            Row row = sheet.getRow(r);
            T object = type.newInstance();
            Field[] fields = object.getClass().getDeclaredFields();
            Field.setAccessible(fields, true);

            // 遍历列数
            for (int c = row.getFirstCellNum(); c <= row.getLastCellNum(); c++) {
                Cell cell = row.getCell(c);
                if (null != cell) {
                    Object value = getCellValue(cell, fields[c]);
                    fields[c].set(object, value);
                }
            }
            result.add(object);
            Field.setAccessible(fields, false);
        }

        return result;
    }


    private static Object getCellValue(Cell cell, Field field) {
        TypeHandler handler = typeHandlerMap.get(field.getType());
        if (handler == null) return null;
        return handler.handle(cell, field);
    }

    /**
     * 验证 Excel 文件
     * @param fileName
     * @return
     */
    public boolean validateExcel(String fileName) {
        return fileName != null && RegularUtil.isExcel2003(fileName) || RegularUtil.isExcel2007(fileName);
    }
}
