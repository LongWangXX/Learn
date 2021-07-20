package com.lw.learn.juc.heappensbefore;

public class VolatileExample {

    int x = 0;
    volatile boolean v = false;

    public void writer() {
        x = 42;
        v = true;
    }

    public void reader() {
        if (v == true) {
            System.out.println(x);
        }
    }

    public static void main(String[] args) {
        VolatileExample volatileExample = new VolatileExample();
        for (int i = 0; i < 10; i++) {
            new Thread(
                    () -> {
                        volatileExample.reader();
                    }
            ).start();
        }
        volatileExample.writer();


    }
}
