package com.lw.learn.juc.deadlock;

public class DeadLockTest {
    public static void main(String[] args) {
        Object lock1 = new Object();
        Object lock2 = new Object();

        new Thread(()->{
            synchronized (lock1){
                System.out.println("线程" + Thread.currentThread().getName()+"获取了 lock1，" +
                        "尝试获取 lock2");
                try {
                    Thread.sleep(1000L);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                synchronized (lock2){

                }
            }
        },"A").start();
        new Thread(()->{
            synchronized (lock2){
                System.out.println("线程" + Thread.currentThread().getName()+"获取了 lock2，" +
                        "尝试获取 lock1");
                synchronized (lock1){

                }
            }
        },"B").start();

    }
}
