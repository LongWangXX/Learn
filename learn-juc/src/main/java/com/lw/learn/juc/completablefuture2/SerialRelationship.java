package com.lw.learn.juc.completablefuture2;

import com.lw.learn.juc.completablefuture.SmallTool;

import java.util.concurrent.CompletableFuture;

public class SerialRelationship {
    public static void main(String[] args) {
        //thenAcceptAsync 有入参没有返回值
        CompletableFuture<Void> voidCompletableFuture = CompletableFuture.supplyAsync(() -> {
            SmallTool.printTimeAndThread("start");
            return "开启 一个异步任务";
        }).thenAcceptAsync(start -> {
            SmallTool.printTimeAndThread("thenAccept 没有返回值");
        });
        voidCompletableFuture.join();

        //thenApply 串行有返回值有入参
        System.out.println(CompletableFuture.supplyAsync(() -> {
            SmallTool.printTimeAndThread("start");
            return "开启 一个异步任务";
        }).thenApply(start -> {
            SmallTool.printTimeAndThread("thenApply 有返回值，有入参");
            return "hello";
        }).join());

        //thenRunAsync有没有入参没有返回值
        CompletableFuture.supplyAsync(()->{
        return  "start";
        }).thenRunAsync(()->{
            SmallTool.printTimeAndThread("thenRun 没有入参，没有返回值");
        }).join();

        CompletableFuture<Void> thenCompose = CompletableFuture.supplyAsync(() -> "start").thenCompose(str -> {
            SmallTool.printTimeAndThread("thenCompose 有入參，有返回值，返回值 是一个 CompletionStage");
            return CompletableFuture.runAsync(() -> {
                SmallTool.printTimeAndThread("返回 CompletionStage");
            });
        });
        thenCompose.join();

    }
}
