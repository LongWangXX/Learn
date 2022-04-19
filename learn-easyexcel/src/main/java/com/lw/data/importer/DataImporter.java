package com.lw.data.importer;

import com.alibaba.fastjson.JSONObject;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class DataImporter<T> {
    private static final String READER_PROPERTIES = "readerProperties";
    private static final String STORAGE_PROPERTIES = "storageProperties";
    private static final String STREAM = "stream";
    private DataReader<T> dataReader;
    private DataStorage<T> dataStorage;

    public ImportInfo importExcelDatatoMysql(InputStream intputStream, String writerTable, Class<T> tClass, Predicate<? super T> filterFunction, Function<? super T, ? extends T> mapper) {
        //init 配置
        Map<String, Object> readerConf = new JSONObject();
        readerConf.put(STREAM, intputStream);
        readerConf.put("batchCnt", 10000);
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
        while (dataReader.hasNext()) {
            long l = System.currentTimeMillis();
            List<T> data = dataReader.getData(100000);
            long l2 = System.currentTimeMillis();
            System.out.println("readTime:" + (l2 - l));
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
            System.out.println("acceptCnt=" + acceptCnt);
            long e = System.currentTimeMillis();
            System.out.println("总体耗时" + (e - l));
        }
        //读取结束
        dataReader.close();
        dataStorage.close();
        return new ImportInfo(cnt, acceptCnt);
    }

}

class ImportInfo {
    long sumCnt;
    long acceptCnt;

    public ImportInfo(long sumCnt, long acceptCnt) {
        this.sumCnt = sumCnt;
        this.acceptCnt = acceptCnt;
    }

    public ImportInfo() {
    }

    public long getSumCnt() {
        return sumCnt;
    }

    public void setSumCnt(long sumCnt) {
        this.sumCnt = sumCnt;
    }

    public long getAcceptCnt() {
        return acceptCnt;
    }

    public void setAcceptCnt(long acceptCnt) {
        this.acceptCnt = acceptCnt;
    }
}
