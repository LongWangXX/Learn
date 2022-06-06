package com.lw.data.demo;

import com.alibaba.fastjson.JSONObject;
import com.lw.data.DataImporter;
import com.lw.data.reader.ExcelDataReader;
import com.lw.data.reader.MysqlReader;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.List;

public class ReadTest {
    public static void main(String[] args) throws InterruptedException, FileNotFoundException {
        //读取excel到mysql
//        testImportExcelToMysql();
        //读取excel数据
        testExcelDataReader();

//        testMysqlDataReader();
//        testImportoMysqlToExcel();
//        testImportoMysqlToExcel2();
    }

    private static void testMysqlDataReader() {
        MysqlReader mysqlReader = new MysqlReader();
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("url", "jdbc:mysql://192.168.8.27:3306/isoc?useUnicode=yes&characterEncoding=UTF-8&useSSL=false");
        jsonObject.put("user", "isoc");
        jsonObject.put("password", "isoc@123");
        jsonObject.put("sql", "select id,ip,create_time from t_isoc_asset");
        mysqlReader.init(jsonObject);
        mysqlReader.startReader(TIsocAsset.class);
        while (mysqlReader.hasNext()) {
            List data = mysqlReader.getData(1000);
            System.out.println(data);
            System.out.println(data.size());
        }
    }

    public static void testImportExcelToMysql() {
        try {
            String filename = "C:\\Users\\Administrator\\Desktop\\test.xlsx";
            DataImporter demoDataDataImporter = new DataImporter();
            demoDataDataImporter.importExcelDatatoMysql(new FileInputStream(filename), "test", DemoData.class, null, null);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static void testImportoMysqlToExcel() {
        try {
            String filename = "C:\\Users\\Administrator\\Desktop\\test3.xlsx";
            DataImporter demoDataDataImporter = new DataImporter();
            demoDataDataImporter.importMysqltoExcelData(new FileOutputStream(filename), "csv", "select id,ip,create_time from t_isoc_asset", TIsocAsset.class, null, null);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static void testImportoMysqlToExcel2() {
        try {
            String filename = "C:\\Users\\Administrator\\Desktop\\test3.xlsx";
            DataImporter demoDataDataImporter = new DataImporter();
            System.out.println(demoDataDataImporter.importMysqltoExcelData(new FileOutputStream(filename), null,
                    "select * from t_isoc_incident_detail", TIsocIncidentDetail.class, null, null));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static void testExcelDataReader() throws FileNotFoundException {
        // 读取excel
        String filename = "C:\\Users\\Administrator\\Desktop\\test12.xls";

        ExcelDataReader<DemoData> demoDataExcelDataReader = new ExcelDataReader<>();
        JSONObject properties = new JSONObject();
        properties.put("stream", new FileInputStream(filename));
        demoDataExcelDataReader.init(properties);
        demoDataExcelDataReader.startReader(DemoData.class);
        long start = System.currentTimeMillis();
        System.out.println(start);
        int count = 0;
        while (demoDataExcelDataReader.hasNext()) {
            List<DemoData> data = demoDataExcelDataReader.getData(10000);
            count += data.size();
            System.out.println(data);
            System.out.println(count);
//            if(!data.isEmpty())
//            System.out.println(data);
        }
        long end = System.currentTimeMillis();
        System.out.println(end);
        System.out.println(start);
        System.out.println((end - start) / 1000);
        System.out.println(count);
    }
}

