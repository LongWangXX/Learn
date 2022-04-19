package com.lw.data.importer;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.read.listener.ReadListener;
import com.alibaba.fastjson.JSONObject;

import java.io.InputStream;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

public class ExcelDataReader<T> extends BaseDataReader<T> {
    InputStream stream;

    @Override
    public void startReader(Class<T> clazz) {
        readerCom = readerDataInternal(stream, clazz);
    }

    @Override
    public void init(JSONObject properties) {
        stream = (InputStream) properties.get("stream");
    }

    @Override
    public void close() {

    }

    int count = 0;

    protected CompletableFuture<Void> readerDataInternal(InputStream stream, Class<T> clazz) {
        return CompletableFuture.runAsync(() -> EasyExcel.read(stream, clazz, new ReadListener<T>() {
            @Override
            public void invoke(T data, AnalysisContext context) {
                try {
                    count++;
                    getQueue().put(data);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void doAfterAllAnalysed(AnalysisContext context) {
                readerCom = null;
                System.out.println(count);
            }

        }).sheet().doRead());
    }
}
