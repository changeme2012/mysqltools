package com.mysqltool.code;

import com.mysqltool.bean.TableBean;
import com.mysqltool.utils.DruidJDBCUtils;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanListHandler;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.*;

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
        String partitionword = properties.getProperty("partitionword");
        String delimited = properties.getProperty("delimited");
        String nullformat = properties.getProperty("nullformat");
        String location = properties.getProperty("location");
        String tablefilter = properties.getProperty("tablefilter");

        Connection connection;



        //去重表名
        HashSet<String> tableNameSet = new HashSet<>();


        //获取过滤表信息 创建集合
        ArrayList<String> tableFliterList = new ArrayList<>();
        if (tablefilter != null) {
            String[] filter = tablefilter.split(",");
            for (String table : filter) {
                tableFliterList.add(table);
            }
        }

        //创建columnname,columntype,comment拼接的MAP
        HashMap<String, String> tableMid = new HashMap<>();


        //TODO 2 建立连接并把查询结果放收TableBean的LIST里
        try {

            connection = DruidJDBCUtils.getConnection();
            //创建DBUtils对象
            QueryRunner queryRunner = new QueryRunner();

            //获取gmall库下所有表信息
            String sql = "select * from information_schema.COLUMNS where TABLE_SCHEMA = ? ";

            //DButils封装自动封装到LIST
            List<TableBean> gmall = queryRunner.query(connection, sql,new BeanListHandler<>(TableBean.class), "gmall");

            //TODO 目标把库里所有表拼接成多段建表语句，并保存到export路径下
            /*DROP TABLE IF EXISTS ods_activity_info_full;
            CREATE EXTERNAL TABLE ods_activity_info_full
            (
                `id`              STRING COMMENT '活动id',
                `activity_name` STRING COMMENT '活动名称',
                `activity_type` STRING COMMENT '活动类型',
                `activity_desc` STRING COMMENT '活动描述',
                `start_time`     STRING COMMENT '开始时间',
                `end_time`        STRING COMMENT '结束时间',
                `create_time`    STRING COMMENT '创建时间',
                `operate_time`   STRING COMMENT '修改时间'
            ) COMMENT '活动信息表'
                PARTITIONED BY (`dt` STRING)
                ROW FORMAT DELIMITED FIELDS TERMINATED BY '\t'
                NULL DEFINED AS ''
                LOCATION '/warehouse/gmall/ods/ods_activity_info_full/';

            DROP TABLE IF EXISTS ods_activity_rule_full;
            CREATE EXTERNAL TABLE ods_activity_rule_full
            (
                `id`                  STRING COMMENT '编号',
                `activity_id`       STRING COMMENT '类型',
                `activity_type`     STRING COMMENT '活动类型',
                `condition_amount` DECIMAL(16, 2) COMMENT '满减金额',
                `condition_num`     BIGINT COMMENT '满减件数',
                `benefit_amount`    DECIMAL(16, 2) COMMENT '优惠金额',
                `benefit_discount` DECIMAL(16, 2) COMMENT '优惠折扣',
                `benefit_level`     STRING COMMENT '优惠级别',
                `create_time`       STRING COMMENT '创建时间',
                `operate_time`      STRING COMMENT '修改时间'
            ) COMMENT '活动规则表'
                PARTITIONED BY (`dt` STRING)
                ROW FORMAT DELIMITED FIELDS TERMINATED BY '\t'

            */

            //TODO 4 拿到信息，然后拼接成中间字段放入(tablemid)
            Iterator<TableBean> iterator = gmall.iterator();

            while (iterator.hasNext()){
                TableBean tableBean = iterator.next();

                String columnname = tableBean.getColumnname();
                String columntype = tableBean.getColumntype();
                String comment = tableBean.getComment();
                String tablename = tableBean.getTablename();

                //放入set去重表名
                tableNameSet.add(tablename);

                //按过滤名单拼接表
                if (tablename !=null && tableFliterList.contains(tablename)) {

                    //TODO  表名拼接 这里出问题了，一个表名对应对个表信息，没办法唯一
                /*DROP TABLE IF EXISTS ods_activity_info_full
                CREATE EXTERNAL TABLE ods_activity_info_full*/
                    String table = columnname +"\t"+ columntype + "\t" + comment;
                    //按表名存放拼接好的字段
                    tableMid.put(tablename,table);

                }
                //如果tablefilter为null则不过滤，拼接全部表
                else {
                    String table = columnname +"\t"+ columntype + "\t" + comment;
                }

            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        // TODO 3 开始拼接
            //拼接表头 activity_info 变  ods_activity_info_full
        HashMap<String, String> tableHeader = new HashMap<>();
        for (String name : tableNameSet) {
            String tableStart = "DROP TABLE IF EXISTS"+ prefixes+name+suffixes+";\n"+
                    "CREATE EXTERNAL TABLE "+prefixes+name+suffixes+"\n(";
            tableHeader.put(name,tableStart);
        }


        //拼接表尾
        String tableEnd = ") \n" +
                "PARTITIONED BY ("+partitionword+ "STRING)\n" +
                "ROW FORMAT DELIMITED FIELDS TERMINATED BY "+delimited +"\n"+
                "NULL DEFINED AS"+ nullformat +"\n" +
                "LOCATION"+location;

        //全表拼接 把tableHeader，tableMid，tableEnd拼接起来


        //TODO 文件输出流到export目录下
        FileOutputStream fileOutputStream = new FileOutputStream("export");



        //TODO 4 关闭连接

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
