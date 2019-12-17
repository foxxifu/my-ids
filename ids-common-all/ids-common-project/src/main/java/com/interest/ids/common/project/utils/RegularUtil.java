package com.interest.ids.common.project.utils;

/**
 * @Author: sunbjx
 * @Description:
 * @Date Created in 17:02 2017/12/4
 * @Modified By:
 */
public class RegularUtil {

    /**
     * 是否是2003的Excel，返回TRUE 是2003
     * @param filePath
     * @return
     */
    public static boolean isExcel2003(String filePath) {
        return filePath.matches("^.+\\.(?i)(xls)$");
    }

    /**
     * 是否是2007的Excel，返回TRUE 是2007
     * @param filePath
     * @return
     */
    public static boolean isExcel2007(String filePath) {
        return filePath.matches("^.+\\.(?i)(xlsx)$");
    }


//    public static void main(String[] args) {
//        System.out.println(isExcel2007("aaa.xlsx"));
//    }
}
