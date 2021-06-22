package com.lw.learn.juc.completablefuture;

import java.util.concurrent.CompletableFuture;

/**
 * 小白进入餐厅，点了一份番茄炒蛋+一碗米饭
 * 另外一个线程 厨师开始做菜
 * 小白点完菜在打王者
 * 厨师做好菜
 * 小白开始吃饭
 *
 * 开启supplyAsync任务
 */

public class SupplyAsync {
    public static void main(String[] args) {
        SmallTool.printTimeAndThread("小白进入餐厅");
        SmallTool.printTimeAndThread("小白点了 番茄炒蛋 + 一碗米饭");
        //supplyAsync开启一个任务，另外一个线程会执行这个任务
        CompletableFuture<String> cf1 = CompletableFuture.supplyAsync(() -> {
            SmallTool.printTimeAndThread("厨师炒菜");
            SmallTool.sleepMillis(200);
            SmallTool.printTimeAndThread("厨师打饭");
            SmallTool.sleepMillis(100);
            return "番茄炒蛋 + 米饭 做好了";
        });

        SmallTool.printTimeAndThread("小白在打王者");
        //cf1.join 表示等待CompletableFuture.supplyAsync开启的异步任务执行完毕
        SmallTool.printTimeAndThread(String.format("%s ,小白开吃", cf1.join()));
    }
}
