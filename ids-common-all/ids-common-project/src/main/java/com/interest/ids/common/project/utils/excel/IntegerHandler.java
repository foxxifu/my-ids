package com.interest.ids.common.project.utils.excel;

import org.apache.poi.ss.usermodel.Cell;

import java.lang.reflect.Field;

/**
 * @Author: sunbjx
 * @Description:
 * @Date Created in 15:46 2017/12/4
 * @Modified By:
 */
public class IntegerHandler implements TypeHandler {

    @Override
    public Object handle(Cell cell, Field field) {
        int type = cell.getCellType();

        if (type == Cell.CELL_TYPE_NUMERIC) {
            return (int) cell.getNumericCellValue();
        } else if (type == Cell.CELL_TYPE_STRING) {
            try {
                return Integer.valueOf(cell.getStringCellValue());
            } catch (NumberFormatException e) {
                return null;
            }
        } else {
            return null;
        }
    }
}
