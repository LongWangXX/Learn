package com.lw.learn.juc.threadhelper;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

/**
 * 集齐7龙珠可以召唤神龙
 *
 * 循环栅栏
 * 每await一次计数器自增1，直到7，就执行传入的runable接口中的方法
 *
 */
public class CylicBarrierTest {
    public static void main(String[] args) {
        CyclicBarrier cyclicBarrier = new CyclicBarrier(7, () -> {
            System.out.println(Thread.currentThread().getName() + "召唤神龙");
        });

        for(int i = 1;i<=7;i++){
            new Thread(()->{
                System.out.println("集齐了" +Thread.currentThread().getName()+"龙珠");
                try {
                    cyclicBarrier.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (BrokenBarrierException e) {
                    e.printStackTrace();
                }
            },i+"").start();
        }
    }
}
