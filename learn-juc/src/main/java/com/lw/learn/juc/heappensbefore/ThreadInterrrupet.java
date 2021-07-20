package com.lw.learn.juc.heappensbefore;

public class ThreadInterrrupet {
    public static void main(String[] args) throws InterruptedException {
        Thread thread = new Thread(
                () -> {
                    System.out.println("hello world");

//                    try {
//                        Thread.sleep(1000000);
//                    } catch (InterruptedException e) {
//                        e.printStackTrace();
//                    }

                    System.out.println(Thread.currentThread().interrupted() + "thread1");
                    System.out.println(Thread.currentThread().interrupted() + "thread1");
                    System.out.println(Thread.currentThread().interrupted() + "thread1");
                    System.out.println(Thread.currentThread().interrupted() + "thread1");
                    System.out.println(Thread.currentThread().interrupted() + "thread1");
                    System.out.println("hello word2");
                }
        );


        thread.start();
        thread.interrupt();

        for (int i = 0; i < 10; i++) {
            Thread.sleep(1000L);
            System.out.println(thread.isInterrupted());
        }
    }
}
