package com.lw.learn.juc.lock;

import com.sun.org.apache.xpath.internal.operations.Lt;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class ReentrantLockTest {
    public static void main(String[] args) {
        LTicket lTicket = new LTicket();
        for (int i = 0; i < 3; i++) {
            new Thread(
                            () -> {
                                for (int j = 0; j < 30; j++) {
                                    lTicket.sale();
                                }
                            },
                            "Threat" + i)
                    .start();//调用start方法不一定立马创建线程，而是由操作系统创建，操作系统根据当前的系统情况来创建。
        }
    }
}

class LTicket {
    Lock lock = new ReentrantLock(true);
    private int cnt = 30;

    public void sale() {
        lock.lock();
        try {
            if (cnt <= 0) {
                System.out.println("票已售完");
                return;
            }
            System.out.println(Thread.currentThread().getName() + "买票 1,票数剩余" + (--cnt));
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
    }
}
