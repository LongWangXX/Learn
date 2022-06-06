package com.lw.data.reader;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.EasyExcelFactory;
import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.read.listener.ReadListener;
import com.alibaba.fastjson.JSONObject;
import com.lw.data.ImporterException;

import java.io.IOException;
import java.io.InputStream;
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
        try {
            stream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    int count = 0;

    protected CompletableFuture<Void> readerDataInternal(InputStream stream, Class<T> clazz) {
        return CompletableFuture.runAsync(() -> EasyExcelFactory.read(stream, clazz, new ReadListener<T>() {
            @Override
            public void invoke(T data, AnalysisContext context) {
                try {
                    count++;
                    getQueue().put(data);
                } catch (Exception e) {
                    try {
                        getQueue().put(e);
                    } catch (InterruptedException interruptedException) {
                        Thread.currentThread().interrupt();
                    }
                }
            }

            @Override
            public void doAfterAllAnalysed(AnalysisContext context) {
                readerCom = null;
            }

        }).doReadAll()).exceptionally((fn) -> {
            throw new ImporterException(fn);
        });
    }
}
