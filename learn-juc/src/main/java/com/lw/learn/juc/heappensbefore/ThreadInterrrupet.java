package com.lw.learn.juc.heappensbefore;

public class ThreadInterrrupet {
    private static int a = 1;

    public static void main(String[] args) throws InterruptedException {
        Thread thread = new Thread(
                () -> {
                    while (true) {
//                        try {
//                            Thread.sleep(1000000);
//                        } catch (InterruptedException e) {
//                            e.printStackTrace();
//                        }
                        if (Thread.currentThread().isInterrupted()) {
                            System.out.println(Thread.currentThread().isInterrupted() + "Thre" + Thread.currentThread().getName());
                            System.out.println("break");
                            System.out.println(a);
                            break;
                        }
//                        System.out.println("hello world");
//
//
//                        System.out.println(Thread.currentThread().interrupted() + "thread1");
//                        System.out.println(Thread.currentThread().interrupted() + "thread1");
//                        System.out.println(Thread.currentThread().interrupted() + "thread1");
//                        System.out.println(Thread.currentThread().interrupted() + "thread1");
//                        System.out.println(Thread.currentThread().interrupted() + "thread1");
//                        System.out.println("hello word2");
                    }
                }
        );


        thread.start();
        System.out.println(thread.getState());
        a = 13;
        thread.interrupt();
        System.out.println(thread.isInterrupted());

        for (int i = 0; i < 10; i++) {
            Thread.sleep(1000L);
            System.out.println(thread.isInterrupted());
        }
        System.out.println(thread.getState());
        thread.start();
        System.out.println(thread.getState());
    }
}
