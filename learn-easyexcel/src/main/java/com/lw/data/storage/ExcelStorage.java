package com.lw.data.storage;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.EasyExcelFactory;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.support.ExcelTypeEnum;
import com.alibaba.excel.write.metadata.WriteSheet;
import com.alibaba.excel.write.metadata.WriteTable;
import com.alibaba.fastjson.JSONObject;
import com.lw.data.ImporterException;

import java.io.OutputStream;
import java.util.List;

public class ExcelStorage<T> implements DataStorage<T> {
    private OutputStream outputStream;
    private ExcelWriter excelWriter;
    private String sheetName;
    private WriteSheet writeSheet;
    private WriteTable writeTable;
    private ExcelTypeEnum etype;

    @Override
    public void writer(List<T> list) {
        excelWriter.write(list, writeSheet, writeTable);
    }

    @Override
    public void startWriter(Class<T> t) {
        excelWriter = EasyExcelFactory.write(outputStream, t).excelType(etype).build();
        writeSheet = EasyExcelFactory.writerSheet(sheetName).needHead(Boolean.FALSE).build();
        // 这里必须指定需要头，table 会继承sheet的配置，sheet配置了不需要，table 默认也是不需要
        writeTable = EasyExcelFactory.writerTable(0).needHead(Boolean.TRUE).build();
    }

    @Override
    public void init(JSONObject properties) {
        outputStream = (OutputStream) properties.get("stream");
        String type = properties.getString("type") == null ? "xlsx" : properties.getString("type");
        if (type.equals("xlsx")) etype = ExcelTypeEnum.XLSX;
        else if (type.equals("csv")) etype = ExcelTypeEnum.CSV;
        else if (type.equals("xls")) etype = ExcelTypeEnum.XLS;
        else {
            throw new ImporterException("类型设置应该设置为[xlsx | csv | xls]");
        }
        sheetName = properties.getString("sheetName") == null ? "Sheet1" : properties.getString("sheetName");
    }

    @Override
    public void close() {
        excelWriter.finish();
    }
}
