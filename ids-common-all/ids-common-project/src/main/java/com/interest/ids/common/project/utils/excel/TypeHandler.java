package com.interest.ids.common.project.utils.excel;

import org.apache.poi.ss.usermodel.Cell;

import java.lang.reflect.Field;

/**
 * @Author: sunbjx
 * @Description:
 * @Date Created in 15:27 2017/12/4
 * @Modified By:
 */
public interface TypeHandler {

    Object handle(Cell cell, Field field);
}
