package com.lw.learn.juc.forkjoin;

import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinTask;
import java.util.concurrent.RecursiveTask;

class TaskExample extends RecursiveTask<Long> {
    private int start;
    private int end;
    private long sum;

    /**
     * 构造函数
     *
     * @param start
     * @param end
     */
    public TaskExample(int start, int end) {
        this.start = start;
        this.end = end;
    }

    @Override
    protected Long compute() {
        System.out.println("任务" + start + "=========" + end + "累加开始");
//大于 100 个数相加切分,小于直接加
        if (end - start <= 100) {
            for (int i = start; i <= end; i++) {
//累加
                sum += i;
            }
        } else {
//切分为 2 块
            int middle = start + 100;
//递归调用,切分为 2 个小任务
            TaskExample taskExample1 = new TaskExample(start, middle);
            TaskExample taskExample2 = new TaskExample(middle + 1, end);
//执行:异步执行
            taskExample1.fork();
            taskExample2.fork();
//同步阻塞等待执行结果
            sum = taskExample1.join() + taskExample2.join();
        }
//加完返回
        return sum;
    }
}

public class ForkJoinTest {
    public static void main(String[] args) {
//定义任务
        TaskExample taskExample = new TaskExample(1, 1000);
        //定义执行对象
        ForkJoinPool forkJoinPool = new ForkJoinPool();
        //加入任务执行
        ForkJoinTask<Long> result = forkJoinPool.submit(taskExample);
        //输出结果
        try {
            System.out.println(result.get());
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            forkJoinPool.shutdown();
        }

    }
}
