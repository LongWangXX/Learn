package com.lw.learn.juc.threadpool;

import java.util.concurrent.*;

public class ThreadPoolCreate {
    public static void main(String[] args) {
        // 创建一个可缓存线程池，如果线程池长度超过处理需要，可灵活回收空
        //闲线程，若无可回收，则新建线程
        ExecutorService executorService = Executors.newCachedThreadPool();
        for (int i = 0; i < 20; i++) {
            executorService.submit(() -> {
                System.out.println(Thread.currentThread().getName());
            });
        }
        executorService.shutdown();
        //创建一个使用单个 worker 线程的 Executor，
        ExecutorService executorService2 = Executors.newSingleThreadExecutor();
        for (int i = 0; i < 20; i++) {
            executorService2.submit(() -> {
                System.out.println(Thread.currentThread().getName());
            });
        }
        executorService2.shutdown();
        //创建一个定长线程池
        ExecutorService executorService3 = Executors.newFixedThreadPool(10);
        for (int i = 0; i < 20; i++) {
            executorService3.submit(() -> {
                System.out.println(Thread.currentThread().getName());
            });
        }
        executorService3.shutdown();


        //自己创建线程池



    }
}
