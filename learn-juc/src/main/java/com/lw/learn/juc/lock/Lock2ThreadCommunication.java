package com.lw.learn.juc.lock;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/** 使用lock方式实现2线程的通信 */
public class Lock2ThreadCommunication {
    public static void main(String[] args) {
        SubAdd subAdd = new SubAdd();
        new Thread(
                        () -> {
                            for (int i = 0; i < 10; i++) subAdd.add();
                        },
                        "A")
                .start();
        new Thread(
                        () -> {
                            for (int i = 0; i < 10; i++) subAdd.sub();
                        },
                        "B")
                .start();

        new Thread(
                () -> {
                    for (int i = 0; i < 10; i++) subAdd.add();
                },
                "C")
                .start();
        new Thread(
                () -> {
                    for (int i = 0; i < 10; i++) subAdd.sub();
                },
                "D")
                .start();
    }
}

class SubAdd {
    private Lock lock = new ReentrantLock();
    private Condition condition = lock.newCondition();
    private int cnt = 0;

    public void add() {
        lock.lock();
        try {
            // 1.判断
            while (cnt != 0) {
                condition.await();
            }
            // 2.干活
            cnt++;
            System.out.println(Thread.currentThread().getName() + "+ 1 cnt=" + cnt);
            // 3.唤醒
            condition.signalAll();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
    }

    public void sub() {
        lock.lock();
        try {
            // 1.判断
            while (cnt != 1) {
                condition.await();
            }
            // 2.干活
            cnt--;
            System.out.println(Thread.currentThread().getName() + "- 1 cnt=" + cnt);
            // 3.唤醒
            condition.signalAll();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
    }
}
