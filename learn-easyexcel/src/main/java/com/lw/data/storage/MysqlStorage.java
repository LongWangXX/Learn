package com.lw.data.storage;

import com.alibaba.fastjson.JSONObject;
import com.lw.data.ImporterException;
import com.lw.data.util.Util;

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
            for (int j = 0; j < list.size(); j++) {
                int i = 1;
                for (Field declaredField : declaredFields) {
                    Object o = declaredField.get(list.get(j));
                    preparedStatement.setObject(i++, o);
                }
                preparedStatement.addBatch();
            }
            preparedStatement.executeBatch();
            connection.commit();
        } catch (Exception e) {
            try {
                connection.rollback();
                connection.setAutoCommit(true);
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }

            throw new ImporterException(e);
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
