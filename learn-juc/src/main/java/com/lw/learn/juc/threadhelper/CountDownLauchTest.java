package com.lw.learn.juc.threadhelper;

import java.util.concurrent.CountDownLatch;

/**
 * 有6个同学回家之后班长锁门
 *
 *countDown 每调用一次countDown方法减1，wait方法等待 ，直到countDownlatch变为0。
 */
public class CountDownLauchTest {
    public static void main(String[] args) throws InterruptedException {
        // 创建countDownlatch计数
        CountDownLatch countDownLatch = new CountDownLatch(6);
        for(int i = 0 ;i<6;i++){
            new Thread(()->{
                countDownLatch.countDown();
                System.out.println("同学"+Thread.currentThread().getName()+"回家了");
            },i+"").start();
        }
        //一直等待直到countDownlatch里面的数变成0
        countDownLatch.await();
        System.out.println("班长锁门");


    }
}
