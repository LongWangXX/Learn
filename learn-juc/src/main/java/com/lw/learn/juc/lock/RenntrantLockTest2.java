package com.lw.learn.juc.lock;

import java.util.concurrent.locks.ReentrantLock;

public class RenntrantLockTest2 {
    public static void main(String[] args) {
        ReentrantLock lock = new ReentrantLock();
        try{
            lock.lock();
            System.out.println("外层");
            lock.lock();
                try{
                    System.out.println("內层");

                }catch(Exception e){
                    e.printStackTrace();
                }finally{
                    //不释放锁
                   // lock.unlock();

                }
        }finally{
            lock.lock();
        }

        new Thread(()->{
            lock.lock();
                try{
                    System.out.println("start");
                }catch(Exception e){
                    e.printStackTrace();
                }finally{
                    lock.unlock();
                }
        },"A").start();

    }
}
