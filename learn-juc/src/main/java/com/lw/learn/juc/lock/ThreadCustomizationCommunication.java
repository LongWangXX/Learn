package com.lw.learn.juc.lock;

import sun.plugin2.os.windows.FLASHWINFO;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/** 三个线程循环顺序打印 */
public class ThreadCustomizationCommunication {
    public static void main(String[] args) {
        CustomCom customCom = new CustomCom();
        new Thread(
                        () -> {
                            for (int i = 0; i < 10; i++) {
                                customCom.print5(i);
                            }
                        },
                        "A")
                .start();

        new Thread(
                        () -> {
                            for (int i = 0; i < 10; i++) {
                                customCom.print10(i);
                            }
                        },
                        "B")
                .start();

        new Thread(
                        () -> {
                            for (int i = 0; i < 10; i++) {
                                customCom.print15(i);
                            }
                        },
                        "C")
                .start();
    }
}

class CustomCom {
    private Lock lock = new ReentrantLock();
    private Condition c1 = lock.newCondition();
    private Condition c2 = lock.newCondition();
    private Condition c3 = lock.newCondition();
    private int flag = 1;

    public void print5(int loop) {
        lock.lock();
        try {
            while (flag != 1) {
                c1.await();
            }
            for (int i = 0; i < 5; i++) {
                System.out.println("A" + loop);
            }
            flag = 2;
            // 唤醒c2线程
            c2.signalAll();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
    }

    public void print10(int loop) {
        lock.lock();
        try {
            while (flag != 2) {
                c2.await();
            }
            for (int i = 0; i < 10; i++) {
                System.out.println("B" + loop);
            }
            flag = 3;
            // 唤醒c2线程
            c3.signalAll();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
    }

    public void print15(int loop) {
        lock.lock();
        try {
            while (flag != 3) {
                c3.await();
            }
            for (int i = 0; i < 15; i++) {
                System.out.println("C"+loop);
            }
            flag = 1;
            // 唤醒c2线程
            c1.signalAll();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
    }
}
