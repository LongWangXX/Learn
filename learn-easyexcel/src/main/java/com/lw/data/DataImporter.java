package com.lw.data;

import com.alibaba.fastjson.JSONObject;
import com.lw.data.demo.TIsocAsset;
import com.lw.data.reader.DataReader;
import com.lw.data.reader.ExcelDataReader;
import com.lw.data.reader.MysqlReader;
import com.lw.data.storage.DataStorage;
import com.lw.data.storage.ExcelStorage;
import com.lw.data.storage.MysqlStorage;

import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class DataImporter {
    private static final String STREAM = "stream";
    private static final String TYPE = "type";


    /**
     * 导入Excel数据到Mysql
     * 阻塞队列
     * 原理 ：  reader线程=》 ===================================》 writer线程 getData 》 入库
     *
     * @param intputStream
     * @param writerTable
     * @param tClass
     * @param filterFunction
     * @param mapper
     * @return
     */
    public <T> Info importExcelDatatoMysql(InputStream intputStream, String writerTable, Class<T> tClass, Predicate<? super T> filterFunction, Function<? super T, ? extends T> mapper) {
        DataReader<T> dataReader;
        DataStorage<T> dataStorage;
        JSONObject readerJsonObjectConf = new JSONObject();
        JSONObject storageJsonObjectConf = new JSONObject();

        readerJsonObjectConf.put(STREAM, intputStream);
        storageJsonObjectConf.put("tableName", writerTable);
        storageJsonObjectConf.put("url", "jdbc:mysql://192.168.8.27:3306/isoc?useUnicode=yes&characterEncoding=UTF-8&useSSL=false");
        storageJsonObjectConf.put("user", "isoc");
        storageJsonObjectConf.put("password", "isoc@123");
        dataReader = new ExcelDataReader<>();
        dataReader.init(readerJsonObjectConf);

        dataStorage = new MysqlStorage<>();
        dataStorage.init(storageJsonObjectConf);

        dataReader.startReader(tClass);
        dataStorage.startWriter(tClass);
        //开始读取数据
        int cnt = 0;
        int acceptCnt = 0;
        long startTime = System.currentTimeMillis();

        while (dataReader.hasNext()) {
            List<T> data = dataReader.getData(10000);
            cnt += data.size();
            Stream<T> stream = data.stream();
            if (filterFunction != null) {
                stream = stream.filter(filterFunction);
            }
            if (mapper != null) {
                stream = stream.map(mapper);
            }
            List<T> collect = stream.collect(Collectors.toList());
            acceptCnt += collect.size();

            if (!collect.isEmpty())
                dataStorage.writer(collect);
        }
        long endTime = System.currentTimeMillis();

        //读取结束
        dataReader.close();
        dataStorage.close();
        return new Info(cnt, acceptCnt, startTime - endTime);
    }

    /**
     * 导入Mysql->excel
     * 阻塞队列
     * 原理 ：  reader线程读取mysql数据=》 ===================================》 writer线程 getData 》 写excel
     *
     * @param outputStream
     * @param outType        xls ,csv ,xlsx 默认xlsx
     * @param readerSql
     * @param tClass
     * @param filterFunction
     * @param mapper
     * @return
     */
    public <T> Info importMysqltoExcelData(OutputStream outputStream, String outType, String readerSql, Class<T> tClass, Predicate<? super T> filterFunction, Function<? super T, ? extends T> mapper) {
        DataReader<T> dataReader;
        DataStorage<T> dataStorage;

        //init 配置
        JSONObject writerJsonObjectConf = new JSONObject();
        JSONObject readerJsonObjectConf = new JSONObject();

        writerJsonObjectConf.put(STREAM, outputStream);
        writerJsonObjectConf.put(TYPE, outType);
        readerJsonObjectConf.put("sql", readerSql);
        readerJsonObjectConf.put("url", "jdbc:mysql://192.168.8.27:3306/isoc?useUnicode=yes&characterEncoding=UTF-8&useSSL=false");
        readerJsonObjectConf.put("user", "isoc");
        readerJsonObjectConf.put("password", "isoc@123");
        dataReader = new MysqlReader<>();
        dataReader.init(readerJsonObjectConf);

        dataStorage = new ExcelStorage<>();
        dataStorage.init(writerJsonObjectConf);

        dataReader.startReader(tClass);
        dataStorage.startWriter(tClass);
        //开始读取数据
        int cnt = 0;
        int acceptCnt = 0;
        long startTime = System.currentTimeMillis();

        while (dataReader.hasNext()) {
            List<T> data = dataReader.getData(10000);
            cnt += data.size();
            Stream<T> stream = data.stream();
            if (filterFunction != null) {
                stream = stream.filter(filterFunction);
            }
            if (mapper != null) {
                stream = stream.map(mapper);
            }
            List<T> collect = stream.collect(Collectors.toList());
            acceptCnt += collect.size();

            if (!collect.isEmpty())
                dataStorage.writer(collect);
        }
        long endTime = System.currentTimeMillis();

        //读取结束
        dataReader.close();
        dataStorage.close();
        return new Info(cnt, acceptCnt, endTime - startTime);
    }


}


