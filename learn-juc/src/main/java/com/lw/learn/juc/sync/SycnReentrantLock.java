package com.lw.learn.juc.sync;

public class SycnReentrantLock {
    public static void main(String[] args) {
        Object o = new Object();
        synchronized (o) {
            System.out.println("外层");
            synchronized (o) {
                System.out.println("中层");
                synchronized (o) {
                    System.out.println("内层");
                }
            }
        }
    }
}
