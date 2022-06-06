package com.lw.data.reader;

import com.lw.data.ImporterException;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.CompletableFuture;

public abstract class BaseDataReader<T> implements DataReader<T> {
    private ArrayBlockingQueue<Object> queue = new ArrayBlockingQueue<>(100000);
    volatile CompletableFuture<Void> readerCom;

    public ArrayBlockingQueue<Object> getQueue() {
        return queue;
    }

    @Override
    public List<T> getData(int batchCount) {
        ArrayList<T> datas = new ArrayList<>();
        try {
            if (readerCom == null && queue.isEmpty()) {
                throw new ImporterException("请先调用StartReader方法");
            }
            int cnt = 0;
            int sleepCnt = 0;
            while ((readerCom != null || !queue.isEmpty()) && sleepCnt < 30) {
                Object poll = queue.poll();
                if (poll != null) {
                    if (poll instanceof RuntimeException) {
                        throw (RuntimeException) poll;
                    }
                    datas.add((T) poll);
                    if (cnt++ >= batchCount) {
                        return datas;
                    }
                } else {
                    Thread.sleep(200);
                    sleepCnt++;
                }
            }

        } catch (Exception e) {
            Thread.currentThread().interrupt();
            throw (RuntimeException) e;
        }
        return datas;
    }


    @Override
    public boolean hasNext() {
        return !queue.isEmpty() || readerCom != null;
    }
}
