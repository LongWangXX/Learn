package com.lw.learn.juc.callable;

import java.util.concurrent.*;

public class CallableTest {
    public static void main(String[] args) throws ExecutionException, InterruptedException, TimeoutException {
        FutureTask<Integer> futureTask = new FutureTask<>(new Callable<Integer>() {
            @Override
            public Integer call() throws Exception {
                System.out.println(Thread.currentThread().getName() + "call");
                Thread.sleep(5000L);
                return 1;
            }
        });

        new Thread(futureTask).start();
        //get方法等待线程结束

        System.out.println(futureTask.get());
        //如果超時则抛出异常
        System.out.println(futureTask.get(1L, TimeUnit.SECONDS));
        System.out.println("over");
    }
}
