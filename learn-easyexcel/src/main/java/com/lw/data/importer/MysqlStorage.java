package com.lw.data.importer;

import com.alibaba.fastjson.JSONObject;
import com.lw.DemoData;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

public class MysqlStorage<T> implements DataStorage<T> {
    Connection connection;
    private String insertSql = " insert into tableName(fields) values(placeholder)";

    Field[] declaredFields;
    private String tableName;
    PreparedStatement preparedStatement;

    @Override
    public void writer(List<T> list) {
        try {
            connection.setAutoCommit(false);
            long l = System.currentTimeMillis();
            for (int j = 0; j < list.size(); j++) {
                int i = 1;
//                for (Field declaredField : declaredFields) {
//                    Object o = declaredField.get(list.get(j));//
//                    preparedStatement.setObject(i++, o);
//                }
                DemoData o = (DemoData) list.get(j);
                preparedStatement.setObject(1, o.getHireDate());
                preparedStatement.setObject(2, o.getName());
                preparedStatement.setObject(3, o.getSalary());
                preparedStatement.addBatch();
            }
            preparedStatement.executeBatch();
            connection.commit();
            long n = System.currentTimeMillis();
            System.out.println("mysqlInsertTime="+(n - l));
        } catch (Exception e) {
            try {
                connection.rollback();
                connection.setAutoCommit(true);
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }

            throw new RuntimeException(e);
        } finally {
            try {
                connection.setAutoCommit(true);
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
    }

    @Override
    public void startWriter(Class<T> t) {
        try {
            StringBuilder buildMysqlFieldsPlaceholder = new StringBuilder();
            StringBuilder buildMysqlFields = new StringBuilder();
            declaredFields = t.getDeclaredFields();
            for (Field declaredField : declaredFields) {
                declaredField.setAccessible(true);
                buildMysqlFieldsPlaceholder.append("?,");
                buildMysqlFields.append(Util.toHumpString(declaredField.getName())).append(",");
            }
            insertSql =
                    insertSql
                            .replace("tableName", tableName)
                            .replace(
                                    "fields", buildMysqlFields.deleteCharAt(buildMysqlFields.length() - 1).toString())
                            .replace(
                                    "placeholder",
                                    buildMysqlFieldsPlaceholder
                                            .deleteCharAt(buildMysqlFieldsPlaceholder.length() - 1)
                                            .toString());
            System.out.println("insert sql:" + insertSql);
            preparedStatement = connection.prepareStatement(insertSql);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

    }

    @Override
    public void init(JSONObject properties) {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            connection = DriverManager.getConnection(properties.getString("url"), properties.getString("user"), properties.getString("password"));
            tableName = properties.getString("tableName");
        } catch (Exception throwables) {
            throwables.printStackTrace();
        }
    }

    @Override
    public void close() {
        try {
            connection.close();
            preparedStatement.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }
}
