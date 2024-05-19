package com.lw.learn.juc;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class Test {
    public static void main(String[] args) {
        ReentrantLock reentrantLock = new ReentrantLock();
        ReentrantLock reentrantLock2 = new ReentrantLock();
        Condition condition = reentrantLock.newCondition();
        Thread aaa = new Thread(() -> {
            // System.out.println("aaa");//

            try {
                Thread.sleep(1000L);
                reentrantLock.lock();
                condition.signal();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                reentrantLock.unlock();
            }

        });
        Thread bbb = new Thread(() -> {
            // System.out.println("aaa");//

            try {
                Thread.sleep(1001);
                reentrantLock.lock();
                System.out.println("cc");
                condition.await();
                System.out.println("dd");
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                reentrantLock.unlock();
            }

        });
        aaa.start();
        bbb.start();
        try {
            Thread.sleep(100);
            reentrantLock.lock();
            System.out.println("aa");
            condition.await();
            System.out.println("bb");

        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            reentrantLock.unlock();
        }
    }


}
