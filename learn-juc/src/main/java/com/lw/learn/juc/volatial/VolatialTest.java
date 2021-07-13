package com.lw.learn.juc.volatial;

public class VolatialTest {
    public static void main(String[] args) {
        ThreadDemo1 runnable = new ThreadDemo1();
        new Thread(runnable).start();

        while (!runnable.isFlag()) {
            if (runnable.isFlag()) {
                System.out.println("false");
                break;
            }
        }
    }
}

class ThreadDemo1 implements Runnable {
    public boolean isFlag() {
        return flag;
    }

    public void setFlag(boolean flag) {
        this.flag = flag;
    }

    private volatile boolean flag = false;

    @Override
    public void run() {
        try {
            Thread.sleep(1000);
            flag = true;
            System.out.println("flag = true");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

