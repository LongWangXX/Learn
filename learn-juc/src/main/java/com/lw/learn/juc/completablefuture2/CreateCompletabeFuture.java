package com.lw.learn.juc.completablefuture2;

import com.lw.learn.juc.completablefuture.SmallTool;

import java.util.concurrent.CompletableFuture;

public class CreateCompletabeFuture {
    public static void main(String[] args) {
        //创建completableFuture
        CompletableFuture<String> completableFuture = CompletableFuture.supplyAsync(() -> {
            SmallTool.printTimeAndThread("hello");
            return "hello";
        });
        CompletableFuture<Void> completableFuture2 = CompletableFuture.runAsync(() -> {
            SmallTool.printTimeAndThread("runAsync 没有返回值");
        });

        System.out.println(completableFuture.join());
    }
}
