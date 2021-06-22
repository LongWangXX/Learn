package com.lw.learn.juc.completablefuture;

import java.util.concurrent.CompletableFuture;

/**
 * 任务依赖 ，前面一个任务。
 * 小白进入餐厅，点了番茄炒蛋，
 * 开启一个线程厨师做菜，厨师做好菜
 * 将菜交给服务员，开启一个线程服务员开始打饭
 * 服务员依赖于厨师先做好饭。
 */
public class ThenCompose {
    public static void main(String[] args) {
        SmallTool.printTimeAndThread("小白进入餐厅");
        SmallTool.printTimeAndThread("小白点了 番茄炒蛋 + 一碗米饭");
        //結果由thenCompose的任务返回
        CompletableFuture<String> cf1 = CompletableFuture.supplyAsync(() -> {
            SmallTool.printTimeAndThread("厨师炒菜");
            SmallTool.sleepMillis(200);
            return "番茄炒蛋";
        }).
                //等待supplyAsync开启的任务执行完毕，将supplyAsync的返回值当作入参传给function
                //要求返回一个CompletionStage
                //CompletionStage是继承CompletionStage所以可以执行使用CompletableFuture.supplyAsync
                //来开启任务返回CompletableFuture
                thenCompose(dish -> CompletableFuture.supplyAsync(() -> {
            SmallTool.printTimeAndThread("服务员打饭");
            SmallTool.sleepMillis(100);
            return dish + " + 米饭";
        }));

        SmallTool.printTimeAndThread("小白在打王者");
        SmallTool.printTimeAndThread(String.format("%s 好了,小白开吃", cf1.join()));
    }

    /**
     * 用 applyAsync 也能实现
     */
    private static void applyAsync() {
        SmallTool.printTimeAndThread("小白进入餐厅");
        SmallTool.printTimeAndThread("小白点了 番茄炒蛋 + 一碗米饭");

        CompletableFuture<String> cf1 = CompletableFuture.supplyAsync(() -> {
            SmallTool.printTimeAndThread("厨师炒菜");
            SmallTool.sleepMillis(200);
            CompletableFuture<String> race = CompletableFuture.supplyAsync(() -> {
                SmallTool.printTimeAndThread("服务员打饭");
                SmallTool.sleepMillis(100);
                return " + 米饭";
            });
            return "番茄炒蛋" + race.join();
        });

        SmallTool.printTimeAndThread("小白在打王者");
        SmallTool.printTimeAndThread(String.format("%s 好了,小白开吃", cf1.join()));
    }
}
