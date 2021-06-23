package com.lw.learn.juc.completablefuture;

import java.util.Random;
import java.util.concurrent.CompletableFuture;

public class MakeACall {
    public static void main(String[] args) {
        SmallTool.printTimeAndThread("开始打电话");
        CompletableFuture<String> callFuture = CompletableFuture.supplyAsync(() -> {
            Long sleepCnt = System.currentTimeMillis() % 120 * 1000L;
            SmallTool.printTimeAndThread(sleepCnt / 1000 + "");
            SmallTool.sleepMillis(sleepCnt);
            return "接电话";
        }).applyToEither(CompletableFuture.supplyAsync(() -> {
            SmallTool.sleepMillis(60000);
            return "60秒未接听电话，电话被挂断";
        }), (result) -> {
            return result;
        });
        SmallTool.printTimeAndThread(callFuture.join());

    }
}
