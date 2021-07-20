package com.lw.learn.juc.threadpool;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class ThradPoolTest {
    public static void main(String[] args) {
        ThreadPoolExecutor executorService = new ThreadPoolExecutor(10, 20, 10, TimeUnit.SECONDS,
                new ArrayBlockingQueue<>(40), Executors.defaultThreadFactory());
        executorService.submit(() -> {
            System.out.println("task1");
            try {
                Thread.sleep(1100L);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        });
        executorService.submit(() -> {
            try {
                System.out.println("task2");
                Thread.sleep(2100L);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        executorService.submit(() -> {
            try {
                System.out.println("task3");
                Thread.sleep(3100L);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        for (int i = 0; i < 10; i++) {
            //获取当前线程执行任务的数量
            System.out.println(executorService.getActiveCount());
            try {
                Thread.sleep(1000L);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        System.out.println(executorService.getTaskCount());

        executorService.shutdown();
    }
}