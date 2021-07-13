package com.lw.learn.juc.sync;

/**
 * 有一个共享变量为 a默认为0 A线程在a = 0的时候 +1 B线程 a=1的时候 -1
 *
 * <p>通信步骤 1.创建资源类 2.封装方法 2.1判断 2.2 干活 3.3通知
 *
 * //2 判断的时候一定要放到while循环，不然会存在虚假唤醒问题。
 */
public class Sync2ThreadCommunication {
    public static void main(String[] args) {
        SubAdd subAdd = new SubAdd();
        new Thread(
                        () -> {
                            for (int i = 0; i < 10; i++) {
                                try {
                                    subAdd.add();
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                            }
                        },
                        "A")
                .start();

        new Thread(
                        () -> {
                            for (int i = 0; i < 10; i++) {
                                try {
                                    subAdd.sub();
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                            }
                        },
                        "B")
                .start();

        new Thread(
                () -> {
                    for (int i = 0; i < 10; i++) {
                        try {
                            subAdd.add();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                },
                "C")
                .start();

        new Thread(
                () -> {
                    for (int i = 0; i < 10; i++) {
                        try {
                            subAdd.sub();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                },
                "D")
                .start();
    }
}

class SubAdd {
    int a = 0;

    public synchronized void add() throws InterruptedException {
        // 如果 a 不等于0 那么就等待，等待他变为0
        while (a != 0) {//这里如果用if可能存在线程虚假唤醒的问题。如果有四个线程来做操作的话
            wait();
        }
        a++;
        System.out.println(Thread.currentThread().getName() + "+ 1 a=" + a);
        // 加1之后唤醒另外的线程 -1
        notifyAll();
    }

    public synchronized void sub() throws InterruptedException {
        // 如果 a 不等于0 那么就等待，等待他变为0
        while (a != 1) {//这里如果用if可能存在线程虚假唤醒的问题。如果有四个线程来做操作的话
            wait();
        }
        a--;
        System.out.println(Thread.currentThread().getName() + "+ 1 a=" + a);
        // 加1之后唤醒另外的线程 +1
        notifyAll();
    }
}
