package com.mysqltool.utils;

import com.alibaba.druid.pool.DruidDataSourceFactory;

import javax.sql.DataSource;
import java.io.FileInputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;

/**
 * ClassName:DruidJDBCUtils
 * Package:PACKAGE_NAME
 * Description:
 *
 * @Author: 龙成
 * @Create: 2023/3/9-23:46
 */
public class DruidJDBCUtils {
    private static DataSource ds;

        static {
            try {
                Properties properties = new Properties();
                properties.load(new FileInputStream("conf\\druid.properties"));
                ds=DruidDataSourceFactory.createDataSource(properties);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }

        }

        public static Connection getConnection() throws SQLException {
           return ds.getConnection();

    }

    public static void close(ResultSet resultSet, PreparedStatement preparedStatement,Connection connection) {


        try {
            if (resultSet != null) {
                resultSet.close();
            }
            if (preparedStatement != null) {
                preparedStatement.close();
            }
            if (connection != null) {
                connection.close();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

}
