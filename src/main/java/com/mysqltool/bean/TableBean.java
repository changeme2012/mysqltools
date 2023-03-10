package com.mysqltool.bean;

/**
 * ClassName:TableBean
 * Package:PACKAGE_NAME
 * Description:
 *
 * @Author: 龙成
 * @Create: 2023/3/10-0:10
 */
public class TableBean {
    private String databases;
    private String table_name;
    private String column_name;
    private String column_type;
    private String column_comment;


    public TableBean() {
    }

    public String getDatabases() {
        return databases;
    }

    public void setDatabases(String databases) {
        this.databases = databases;
    }

    public String getTable_name() {
        return table_name;
    }

    public void setTable_name(String table_name) {
        this.table_name = table_name;
    }

    public String getColumn_name() {
        return column_name;
    }

    public void setColumn_name(String column_name) {
        this.column_name = column_name;
    }

    public String getColumn_type() {
        return column_type;
    }

    public void setColumn_type(String column_type) {
        this.column_type = column_type;
    }

    public String getColumn_comment() {
        return column_comment;
    }

    public void setColumn_comment(String column_comment) {
        this.column_comment = column_comment;
    }

    @Override
    public String toString() {
        return "TableBean{" +
                "databases='" + databases + '\'' +
                ", table_name='" + table_name + '\'' +
                ", column_name='" + column_name + '\'' +
                ", column_type='" + column_type + '\'' +
                ", column_comment='" + column_comment + '\'' +
                '}';
    }
}
