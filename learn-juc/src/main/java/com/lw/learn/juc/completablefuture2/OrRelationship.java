package com.lw.learn.juc.completablefuture2;

import com.lw.learn.juc.completablefuture.SmallTool;

import java.util.concurrent.CompletableFuture;

public class OrRelationship {
    public static void main(String[] args) {
        CompletableFuture<String> completableFuture = CompletableFuture.supplyAsync(() -> {
            return "value1";
        }).applyToEither(CompletableFuture.supplyAsync(() -> "value2")
                , (value) -> {
                    return value;
                });
        SmallTool.printTimeAndThread(completableFuture.join());
    }
}
