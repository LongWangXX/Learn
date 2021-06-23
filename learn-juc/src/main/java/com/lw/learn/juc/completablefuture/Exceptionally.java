package com.lw.learn.juc.completablefuture;

import java.util.concurrent.CompletableFuture;

/**
 * 小白坐上了 700路车，但是坐着作者700路车出现异常了，小白合理处理异常。自己打车回家
 * <p>
 * exceptionally：处理前面任务抛出的异常
 */

public class Exceptionally {
    public static void main(String[] args) {
        SmallTool.printTimeAndThread("张三走出餐厅，来到公交站");
        SmallTool.printTimeAndThread("等待 700路 或者 800路 公交到来");

        CompletableFuture<String> bus = CompletableFuture.supplyAsync(() -> {
            SmallTool.printTimeAndThread("700路公交正在赶来");
            SmallTool.sleepMillis(100);
            if (true) {
                throw new RuntimeException("700撞树了……");
            }
            return "700路到了";
        }).exceptionally(e -> {
            SmallTool.sleepMillis(100);
            return "700路异常";
        }).applyToEitherAsync(CompletableFuture.supplyAsync(() -> {
            SmallTool.printTimeAndThread("800路公交正在赶来");
            SmallTool.sleepMillis(200);
            return "800路到了";
        }), firstComeBus -> {
            SmallTool.printTimeAndThread(firstComeBus + "s");
            if (firstComeBus.startsWith("700")) {
                throw new RuntimeException("撞树了……");
            }
            return firstComeBus;
        }).exceptionally(e -> {
            SmallTool.printTimeAndThread(e.getMessage());
            SmallTool.printTimeAndThread("小白叫出租车");
            return "出租车 叫到了";
        });

        SmallTool.printTimeAndThread(String.format("%s,小白坐车回家", bus.join()));
    }
}
