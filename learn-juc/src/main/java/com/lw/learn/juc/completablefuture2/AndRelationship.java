package com.lw.learn.juc.completablefuture2;

import com.lw.learn.juc.completablefuture.SmallTool;

import java.util.concurrent.CompletableFuture;

public class AndRelationship {
    public static void main(String[] args) {

        //thenCombine 并且 表示前面两个CompletionStage执行完，再执行BiFunction函数
        CompletableFuture.runAsync(() -> {
            SmallTool.printTimeAndThread("无参数 输入");
        }).thenCombine(
                CompletableFuture.supplyAsync(() -> "有参数"),
                (f1, f2) -> {
                    SmallTool.printTimeAndThread("thenCombine 有两个输入，有一个返回");
                    System.out.println(f1);
                    SmallTool.printTimeAndThread(f2);

                    return "";
                }
        ).join();

        CompletableFuture.supplyAsync(() -> {
            return "value1";
        }).thenAcceptBoth(CompletableFuture.supplyAsync(() -> "value2"),
                (v1, v2) -> {
                    SmallTool.printTimeAndThread(v1);
                    SmallTool.printTimeAndThread(v2);
                    SmallTool.printTimeAndThread("thenAcceptBoth  and 有两个输入，无返回");
                }).join();


        CompletableFuture.supplyAsync(() -> {
            return "value1";
        }).runAfterBoth(CompletableFuture.supplyAsync(() -> "value2"),
                () -> {
                    SmallTool.printTimeAndThread("runAfterBoth  and 无输入，无返回");
                }).join();

    }
}
