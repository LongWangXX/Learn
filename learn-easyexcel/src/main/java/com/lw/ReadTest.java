package com.lw;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.alibaba.fastjson.JSONObject;
import com.lw.data.importer.DataImporter;
import com.lw.data.importer.ExcelDataReader;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Date;
import java.util.List;

public class ReadTest {
    public static void main(String[] args) throws InterruptedException, FileNotFoundException {
        // 读取的excel文件路径
        String filename = "C:\\Users\\Administrator\\Desktop\\test.xlsx";
        DataImporter<DemoData> demoDataDataImporter = new DataImporter<>();
        demoDataDataImporter.importExcelDatatoMysql(new FileInputStream(filename),"test",DemoData.class,null,null);
        // 读取excel
//        ExcelDataReader<DemoData> demoDataExcelDataReader = new ExcelDataReader<>();
//        JSONObject properties = new JSONObject();
//        properties.put("stream", new FileInputStream(filename));
//        demoDataExcelDataReader.init(properties);
//        demoDataExcelDataReader.startReader(DemoData.class);
//        long start = System.currentTimeMillis();
//        System.out.println(start);
//        int count = 0;
//        System.out.println(new Date(1650354708423L));
//        while (demoDataExcelDataReader.hasNext()) {
//            List<DemoData> data = demoDataExcelDataReader.getData(10000);
//            count += data.size();
//
//            System.out.println(count);
////            if(!data.isEmpty())
////            System.out.println(data);
//        }
//        long end = System.currentTimeMillis();
//        System.out.println(end);
//        System.out.println(start);
//        System.out.println((end - start) / 1000);
//        System.out.println(count);
    }
}

