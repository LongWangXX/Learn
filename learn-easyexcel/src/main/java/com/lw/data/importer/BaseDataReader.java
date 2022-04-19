package com.lw.data.importer;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.CompletableFuture;

public abstract class BaseDataReader<T> implements DataReader<T> {
    private ArrayBlockingQueue<T> queue = new ArrayBlockingQueue(100000);
    volatile CompletableFuture<Void> readerCom;

    public ArrayBlockingQueue<T> getQueue() {
        return queue;
    }

    @Override
    public List<T> getData(int batchCount) {
        ArrayList<T> datas = new ArrayList<>();
        try {
            if (readerCom == null && queue.isEmpty()) {
                throw new RuntimeException("请先调用StartReader方法");
            }
            int cnt = 0;
            int sleepCnt = 0;
            while ((readerCom != null || !queue.isEmpty()) && sleepCnt < 30000000) {
                T poll = queue.poll();
                if (poll != null) {
                    datas.add(poll);
                    if (cnt++ >= batchCount) {
                        return datas;
                    }
                } else {
                    Thread.sleep(200);
                    sleepCnt++;
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return datas;
    }


    @Override
    public boolean hasNext() {
        return !queue.isEmpty() || readerCom != null;
    }
}
