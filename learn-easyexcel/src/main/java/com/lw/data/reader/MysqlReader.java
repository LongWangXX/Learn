package com.lw.data.reader;

import com.alibaba.fastjson.JSONObject;
import com.lw.data.ImporterException;
import com.lw.data.util.DateUtil;
import com.lw.data.util.Util;
import com.mysql.cj.jdbc.util.TimeUtil;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.sql.*;
import java.util.concurrent.CompletableFuture;

public class MysqlReader<T> extends BaseDataReader<T> {
    private String sql;
    private Connection connection;

    @Override
    public void startReader(Class<T> clazz) {
        readerCom = CompletableFuture.runAsync(() ->
                queryMysqlForDim(sql, clazz, connection));
    }

    public void queryMysqlForDim(String sql, Class<T> tClass, Connection connection) {
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql);
             ResultSet resultSet = preparedStatement.executeQuery()) {
            ResultSetMetaData metaData = resultSet.getMetaData();
            while (resultSet.next()) {
                int columnCount = metaData.getColumnCount();
                T instance = tClass.newInstance();
                for (int i = 1; i <= columnCount; i++) {
                    String columnLabel = metaData.getColumnLabel(i);
                    Object object = resultSet.getObject(columnLabel);
                    String columnName = Util.toLineString(columnLabel);
                    Field declaredField = tClass.getDeclaredField(columnName);
                    setAttrByField(instance, declaredField, object);
                }
                getQueue().put(instance);
            }
            readerCom = null;
        } catch (Exception e) {
            try {
                getQueue().put(e);
            } catch (Exception interruptedException) {
                Thread.currentThread().interrupt();
            }
        }
    }


    public void setAttrByField(T t, Field field, Object value) {
        try {
            Class<?> clazz = t.getClass();
            PropertyDescriptor pd = new PropertyDescriptor(field.getName(), clazz);
            Method method = pd.getWriteMethod();
            String type = field.getGenericType()
                    .toString();
            switch (type) {
                case "class java.lang.String":
                    method.invoke(t, value);
                    break;
                case "class java.lang.Integer":
                    method.invoke(t, Double.valueOf(value.toString())
                            .intValue());
                    break;
                case "class java.lang.Long":
                    method.invoke(t, Double.valueOf(value.toString())
                            .longValue());
                    break;
                case "class java.lang.Double":
                    method.invoke(t, Double.valueOf(value.toString()));
                    break;
                case "class java.lang.Float":
                    method.invoke(t, Double.valueOf(value.toString())
                            .floatValue());
                    break;
                case "class java.lang.Character":
                    method.invoke(t, value.toString().toCharArray()[0]);
                    break;
                case "class java.math.BigDecimal":
                    method.invoke(t, new BigDecimal(value.toString()));
                    break;
                case "class java.util.Date":
                    method.invoke(t, DateUtil.strToDate(value.toString()));
                    break;
                default:
                    method.invoke(t, (Object) null);
                    break;
            }
        } catch (Exception e) {
            throw new ImporterException(e);
        }
    }

    @Override
    public void init(JSONObject properties) {
        try {
            sql = properties.getString("sql");
            Class.forName("com.mysql.jdbc.Driver");
            connection = DriverManager.getConnection(properties.getString("url"), properties.getString("user"), properties.getString("password"));
        } catch (Exception e) {
            throw new ImporterException(e);
        }
    }

    @Override
    public void close() {
        try {
            connection.close();
            connection = null;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }
}
