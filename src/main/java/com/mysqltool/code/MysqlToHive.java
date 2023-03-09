package com.mysqltool.code;

import com.mysqltool.bean.TableBean;
import com.mysqltool.utils.DruidJDBCUtils;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanListHandler;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

/**
 * ClassName:NullTransformer
 * Package:PACKAGE_NAME
 * Description:
 *
 * @Author: 龙成
 * @Create: 2023/3/8-22:23
 */
public class MysqlToHive {
    public static void main(String[] args) throws IOException {

        //TODO 1 获取配置信息

        Properties properties = new Properties();
        properties.load(new FileInputStream("conf\\mysql.properties"));

        String database = properties.getProperty("database");
        String prefixes = properties.getProperty("Prefixes");
        String suffixes = properties.getProperty("suffixes");
        String partitionFields = properties.getProperty("partition_fields");
        String delimited = properties.getProperty("delimited");
        String nullformat = properties.getProperty("nullformat");
        String location = properties.getProperty("location");


        Connection connection;
        //TODO 2 建立连接并把查询结果放收TableBean的LIST里
        try {

            connection = DruidJDBCUtils.getConnection();
            //创建DBUtils对象
            QueryRunner queryRunner = new QueryRunner();

            //获取gmall库下所有表信息
            String sql = "select * from information_schema.COLUMNS where TABLE_SCHEMA = ? ";

            //DButils封装自动封装到LIST
            List<TableBean> gmall = queryRunner.query(connection, sql,new BeanListHandler<>(TableBean.class), "gmall");

            //TODO 拿到表名，然后拼接前后缀


            Iterator<TableBean> iterator = gmall.iterator();

            while (iterator.hasNext()){
                TableBean tableBean = iterator.next();

                String columnname = tableBean.getColumnname();
                String columntype = tableBean.getColumntype();
                String comment = tableBean.getComment();
                String tablename = tableBean.getTablename();

                //表名拼接 这里出问题了，一个表名对应对个表信息，没办法唯一
                /*DROP TABLE IF EXISTS ods_activity_info_full
                CREATE EXTERNAL TABLE ods_activity_info_full*/
                String table = prefixes + tablename + suffixes;





            }


        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        //TODO 3 关闭连接
        DruidJDBCUtils.close(null,null,connection);


        //参考

        String str="DROP TABLE IF EXISTS ods_activity_info_full;\n" +
        "CREATE EXTERNAL TABLE ods_activity_info_full\n" +
        "(\n" +
        "    `id`              STRING COMMENT '活动id',\n" +
        "    `activity_name` STRING COMMENT '活动名称',\n" +
        "    `activity_type` STRING COMMENT '活动类型',\n" +
        "    `activity_desc` STRING COMMENT '活动描述',\n" +
        "    `start_time`     STRING COMMENT '开始时间',\n" +
        "    `end_time`        STRING COMMENT '结束时间',\n" +
        "    `create_time`    STRING COMMENT '创建时间',\n" +
        "    `operate_time`   STRING COMMENT '修改时间'\n" +
        ") COMMENT '活动信息表'\n" +
        "    PARTITIONED BY (`dt` STRING)\n" +
        "    ROW FORMAT DELIMITED FIELDS TERMINATED BY '\\t'\n" +
        "    NULL DEFINED AS ''\n" +
        "    LOCATION '/warehouse/gmall/ods/ods_activity_info_full/';\n";
    }


}
