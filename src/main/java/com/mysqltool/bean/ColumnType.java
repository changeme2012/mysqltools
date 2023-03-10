package com.mysqltool.bean;

/**
 * ClassName:ColumnType
 * Package:com.mysqltool.bean
 * Description:
 *
 * @Author: 龙成
 * @Create: 2023/3/10-23:33
 */
public class ColumnType {

   public static final String BIGINT="BIGINT";
    public static final String STRING="STRING";
    public static final String DOUBLE="DOUBLE";

    public static String getColumeType(String columntype){
        //columntype类型转换
        switch (columntype){
            case "decimal":
            case  "double":
            case   "float":
                columntype=DOUBLE;
                break;
            case "bigint":
            case "int":
            case "smallint":
            case "tinyint":
                columntype=BIGINT;
                break;
            default:
                columntype=STRING;
                break;
        }

        return columntype;
    }





}
