package com.interest.ids.common.project.utils.excel;

import org.apache.poi.ss.usermodel.Cell;

import java.lang.reflect.Field;

/**
 * @Author: sunbjx
 * @Description:
 * @Date Created in 15:47 2017/12/4
 * @Modified By:
 */
public class DoubleHandler implements TypeHandler {

    @Override
    public Object handle(Cell cell, Field field) {
        int type = cell.getCellType();
        if (type == Cell.CELL_TYPE_NUMERIC) {
            return cell.getNumericCellValue();
        } else if (type == Cell.CELL_TYPE_STRING) {
            try {
                return Double.valueOf(cell.getStringCellValue());
            } catch (NumberFormatException e) {
                return null;
            }
        } else {
            return null;
        }
    }
}
