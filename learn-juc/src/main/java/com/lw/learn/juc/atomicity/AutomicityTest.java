package com.lw.learn.juc.atomicity;

import java.util.concurrent.atomic.AtomicInteger;

public class AutomicityTest {
    public static void main(String[] args) throws InterruptedException {
        AutomicT automicT = new AutomicT();
        for (int i = 0; i < 10; i++) {
            new Thread(automicT).start();
        }
        Thread.sleep(1000L);
        System.out.println(automicT.integer);
    }
}

class AutomicT implements Runnable {
    //automic 将自增操作合并成一
    java.util.concurrent.atomic.AtomicInteger integer = new AtomicInteger(0);
//    public int integer = 0;

    @Override
    public void run() {
//        try {
//            Thread.sleep(200L);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//        System.out.println(integer.getAndIncrement());
        for (int i = 0; i < 1000; i++) integer.getAndIncrement();
    }
}
