package com.lw.learn.juc.readwritelock;

import java.time.temporal.ValueRange;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/** 一个map用于缓存 */
public class ReadWriteLock {
    public static void main(String[] args) throws InterruptedException {
        MyCache cache = new MyCache();
        for(int i = 0;i<6; i++){
            final int j = i;
            new Thread(()->{
                try {
                    cache.put(j+"",j+"");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            },i+"").start();
        }

        Thread.sleep(1000L);

        for(int i = 0;i<6; i++){
            final int j = i;
            new Thread(
                            () -> {
                                try {
                                    System.out.println(cache.get(j + ""));
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                            },
                            i + "")
                    .start();
        }

    }

}

class MyCache {
    Map<String, String> map = new HashMap<>();
    java.util.concurrent.locks.ReadWriteLock lock =  new ReentrantReadWriteLock();
    public void put(String key, String value) throws InterruptedException {
        lock.writeLock().lock();
        try{
            System.out.println(Thread.currentThread().getName()+"写入数据");
            map.put(key,value);
            Thread.sleep(1000L);
            System.out.println(Thread.currentThread().getName()+"writer success ");
        }finally{
            lock.writeLock().unlock();
        }

    }
    public String get(String key) throws InterruptedException {
        String result = null;
       lock.readLock().lock();
            try{
                System.out.println(Thread.currentThread().getName()+"获取数据");

                 result = map.get(key);
                Thread.sleep(1000L);
                System.out.println(Thread.currentThread().getName()+"read success ");
            }catch(Exception e){
                e.printStackTrace();
            }finally{
                lock.readLock().unlock();
            }
        return result;
    }
}

