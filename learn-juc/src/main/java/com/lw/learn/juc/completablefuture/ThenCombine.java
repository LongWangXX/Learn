package com.lw.learn.juc.completablefuture;

import java.util.concurrent.CompletableFuture;

/**
 * thenCombine 結合两个任务的输出结果。结果又结合之后的结果返回
 *
 *
 */
public class ThenCombine {
    public static void main(String[] args) {
        SmallTool.printTimeAndThread("小白进入餐厅");
        SmallTool.printTimeAndThread("小白点了 番茄炒蛋 + 一碗米饭");
        //开启一个任务厨师做菜，做好一个番茄炒蛋
        //同时服务员开始蒸饭，蒸好米饭，返回米饭
        //最后合并两个任务的结果。
        CompletableFuture<String> cf1 = CompletableFuture.supplyAsync(() -> {
            SmallTool.printTimeAndThread("厨师炒菜");
            SmallTool.sleepMillis(200);
            return "番茄炒蛋";
        }).
                //同时开启一个线程服务员蒸饭。
                thenCombine(CompletableFuture.supplyAsync(() -> {
            SmallTool.printTimeAndThread("服务员蒸饭");
            SmallTool.sleepMillis(300);
            return "米饭";
        }), //第二个参数是一个bimfunction，会将前面两个任务的输出结果作为输入传入到bimFunction中。
                        (dish, rice) -> {
            SmallTool.printTimeAndThread("服务员打饭");
            SmallTool.sleepMillis(100);
            return String.format("%s + %s 好了", dish, rice);
        });

        SmallTool.printTimeAndThread("小白在打王者");
        SmallTool.printTimeAndThread(String.format("%s ,小白开吃", cf1.join()));

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
            return "番茄炒蛋";
        });

        CompletableFuture<String> race = CompletableFuture.supplyAsync(() -> {
            SmallTool.printTimeAndThread("服务员蒸饭");
            SmallTool.sleepMillis(300);
            return "米饭";
        });
        SmallTool.printTimeAndThread("小白在打王者");

        String result = String.format("%s + %s 好了", cf1.join(), race.join());
        SmallTool.printTimeAndThread("服务员打饭");
        SmallTool.sleepMillis(100);

        SmallTool.printTimeAndThread(String.format("%s ,小白开吃", result));
    }
}
