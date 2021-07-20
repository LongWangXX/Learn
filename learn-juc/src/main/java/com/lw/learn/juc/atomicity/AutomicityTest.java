package com.lw.learn.juc.atomicity;

import java.util.concurrent.atomic.AtomicInteger;

public class AutomicityTest {
    public static void main(String[] args) {
        AutomicT automicT = new AutomicT();
        for (int i = 0; i < 10; i++) {
            new Thread(automicT).start();
        }
    }
}

class AutomicT implements Runnable {

    java.util.concurrent.atomic.AtomicInteger integer = new AtomicInteger(0);

    @Override
    public void run() {
        try {
            Thread.sleep(200L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println(integer.getAndIncrement());
    }
}
