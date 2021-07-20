package com.lw.learn.juc.threadhelper;

import java.util.Random;
import java.util.concurrent.Semaphore;

/** 有6辆车 三个停车位，去停车场停车
 * Semaphore：信号灯
 * acquire方法: 抢占信号
 * release方法： 释放信号
 * */
public class SemaphoreTest {
    public static void main(String[] args) {
        Semaphore semaphore = new Semaphore(3);
        for (int i = 0; i < 6; i++) {
            new Thread(
                            () -> {
                                try {
                                    // 抢占停车位
                                    semaphore.acquire();
                                    System.out.println(Thread.currentThread().getName() + "抢到了停车位");
                                    Thread.sleep(new Random().nextInt(5));
                                    System.out.println(Thread.currentThread().getName() + "离开");

                                    //释放停车位
                                    semaphore.release();
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                            },
                            i + "")
                    .start();
        }
    }
}
