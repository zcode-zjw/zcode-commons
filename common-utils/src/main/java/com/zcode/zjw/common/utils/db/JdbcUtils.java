package com.zcode.zjw.common.utils.db;

import com.zcode.zjw.common.utils.file.PropertiesUtil;
import com.zcode.zjw.common.utils.file.YamlFileUtil;
import com.zcode.zjw.common.utils.reflect.MethodUtils;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Slf4j
public class JdbcUtils {

    /**
     * 获取连接
     *
     * @return 连接
     */
    public static Connection getConnection(String url, String user, String password) {
        Connection connection = null;
        try {
            connection = DriverManager.getConnection(url, user, password);
            connection.setAutoCommit(false);
        } catch (SQLException e) {
            log.error("获取数据库连接失败：{}", url, e);
        }
        return connection;
    }

    /**
     * 关闭连接
     *
     * @param statement
     * @param connection
     */
    public static void close(Statement statement, Connection connection) {
        if (statement != null) {
            try {
                statement.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    @SneakyThrows
    public static int executeUpdateBySecurity(Connection connection, String sql) {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            int res = preparedStatement.executeUpdate();
            connection.commit();
            return res;
        } catch (Exception e) {
            connection.rollback();
            log.error("执行更新语句失败", e);
        }
        return 0;
    }

    private static JdbcDataSourceInfo getJdbcDataSourceInfoBySpring(String springConfigFileName) throws IOException {
        // 读取Spring配置文件
        String[] split = springConfigFileName.split("\\.");
        JdbcDataSourceInfo jdbcDataSourceInfo = new JdbcDataSourceInfo();
        if (split[split.length - 1].equals("properties")) {
            jdbcDataSourceInfo.setUrl(PropertiesUtil.getSpringItem(springConfigFileName, "spring.datasource.url"));
            jdbcDataSourceInfo.setUsername(PropertiesUtil.getSpringItem(springConfigFileName, "spring.datasource.username"));
            jdbcDataSourceInfo.setPassword(PropertiesUtil.getSpringItem(springConfigFileName, "spring.datasource.password"));
        } else if (split[split.length - 1].equals("yaml") || split[split.length - 1].equals("yml")) {
            jdbcDataSourceInfo.setUrl(YamlFileUtil.getSpringItem(springConfigFileName, "spring.datasource.url"));
            jdbcDataSourceInfo.setUsername(YamlFileUtil.getSpringItem(springConfigFileName, "spring.datasource.username"));
            jdbcDataSourceInfo.setPassword(YamlFileUtil.getSpringItem(springConfigFileName, "spring.datasource.password"));
        } else {
            throw new RuntimeException("找不到指定的spring配置文件（"+ springConfigFileName +"）");
        }
        return jdbcDataSourceInfo;
    }

    public static <T> List<T> selectRecord(String springConfigFileName, String sql, Class<?> entityClazz) {
        List<T> res = new ArrayList<>();
        try {
            JdbcDataSourceInfo jdbcDataSourceInfo = getJdbcDataSourceInfoBySpring(springConfigFileName);
            Connection connection = getConnection(jdbcDataSourceInfo.getUrl(),
                    jdbcDataSourceInfo.getUsername(), jdbcDataSourceInfo.getPassword());
            // 执行查询
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);
            // 处理查询结果
            while (resultSet.next()) {
                List<Map<String, Object>> allFieldInfo = MethodUtils.findAllFieldInfo(entityClazz);
                for (Map<String, Object> fieldInfo : allFieldInfo) {
                    resultSet.getClass().getMethod(String.valueOf(fieldInfo.get("jdbcResult")))
                            .invoke(String.valueOf(fieldInfo.get("name")));
                }
            }

            // 6. 关闭连接
            resultSet.close();
            statement.close();
            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

        return res;
    }

    public static <T> List<T> selectRecord(JdbcDataSourceInfo jdbcDataSourceInfo, String sql, Class<?> entityClazz) {
        List<T> res = new ArrayList<>();
        try {
            Connection connection = getConnection(jdbcDataSourceInfo.getUrl(),
                    jdbcDataSourceInfo.getUsername(), jdbcDataSourceInfo.getPassword());
            // 执行查询
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);
            // 处理查询结果
            while (resultSet.next()) {
                List<Map<String, Object>> allFieldInfo = MethodUtils.findAllFieldInfo(entityClazz);
                for (Map<String, Object> fieldInfo : allFieldInfo) {
                    resultSet.getClass().getMethod(String.valueOf(fieldInfo.get("jdbcResult")))
                            .invoke(String.valueOf(fieldInfo.get("name")));
                }
            }

            // 6. 关闭连接
            resultSet.close();
            statement.close();
            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return res;
    }

    @SneakyThrows
    public static Integer executeUpdateAndCloseBySecurity(String springConfigFileName, String sql) {
        // 读取Spring配置文件
        JdbcDataSourceInfo jdbcDataSourceInfo = getJdbcDataSourceInfoBySpring(springConfigFileName);
        Connection connection = getConnection(jdbcDataSourceInfo.getUrl(),
                jdbcDataSourceInfo.getUsername(), jdbcDataSourceInfo.getPassword());
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            int res = preparedStatement.executeUpdate();
            connection.commit();
            return res;
        } catch (Exception e) {
            connection.rollback();
            log.error("执行更新语句失败", e);
            return null;
        } finally {
            connection.close();
        }
    }

    @SneakyThrows
    public static int executeUpdateAndCloseBySecurity(JdbcDataSourceInfo jdbcDataSourceInfo, String sql) {
        Connection connection = getConnection(jdbcDataSourceInfo.getUrl(),
                jdbcDataSourceInfo.getUsername(), jdbcDataSourceInfo.getPassword());
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            int res = preparedStatement.executeUpdate();
            connection.commit();
            return res;
        } catch (Exception e) {
            connection.rollback();
            log.error("执行更新语句失败", e);
        } finally {
            connection.close();
        }
        return 0;
    }

    @SneakyThrows
    public static int executeCallAndCloseBySecurity(JdbcDataSourceInfo jdbcDataSourceInfo, String sql) {
        Connection connection = getConnection(jdbcDataSourceInfo.getUrl(),
                jdbcDataSourceInfo.getUsername(), jdbcDataSourceInfo.getPassword());
        try {
            PreparedStatement preparedStatement = connection.prepareCall(sql);
            int res = preparedStatement.executeUpdate();
            connection.commit();
            return res;
        } catch (Exception e) {
            connection.rollback();
            log.error("执行调用语句失败", e);
        } finally {
            connection.close();
        }
        return 0;
    }


    /**
     * 关闭查询连接
     *
     * @param resultSet
     * @param statement
     * @param connection
     */
    public static void close(ResultSet resultSet, Statement statement, Connection connection) {
        if (resultSet != null) {
            try {
                resultSet.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        if (statement != null) {
            try {
                statement.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}