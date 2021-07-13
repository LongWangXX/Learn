package com.lw.learn.juc.readwritelock;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class ReadWriteLockDowngrade2 {
    public static void main(String[] args) {
        java.util.concurrent.locks.ReadWriteLock lock = new ReentrantReadWriteLock();
        Lock readLock = lock.readLock();
        Lock writeLock = lock.writeLock();
        readLock.lock();
        System.out.println("读数据");
        writeLock.lock();
        System.out.println("写数据");
        writeLock.unlock();
        readLock.unlock();
    }
}
