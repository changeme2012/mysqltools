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
    private String tablename;
    private String columnname;
    private String columntype;
    private String comment;

    public TableBean() {
    }

    public String getTablename() {
        return tablename;
    }

    public void setTablename(String tablename) {
        this.tablename = tablename;
    }

    public String getColumnname() {
        return columnname;
    }

    public void setColumnname(String columnname) {
        this.columnname = columnname;
    }

    public String getColumntype() {
        return columntype;
    }

    public void setColumntype(String columntype) {
        this.columntype = columntype;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    @Override
    public String toString() {
        return "TableBean{" +
                "tablename='" + tablename + '\'' +
                ", columnname='" + columnname + '\'' +
                ", columntype='" + columntype + '\'' +
                ", comment='" + comment + '\'' +
                '}';
    }
}
