package com.lw.learn.juc.completablefuture;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.util.concurrent.CompletableFuture;

public class Test {
    public static void main(String[] args) {
//        JSONObject jsonObject = new JSONObject();
//        JSONObject jsonObject1 = new JSONObject();
//        JSONArray objects = new JSONArray();
//        jsonObject1.put("name", "abc");
//        objects.add(jsonObject1);
//        objects.add(jsonObject1);
//        objects.add(jsonObject1);
//        jsonObject.put("res", jsonObject1);
//        jsonObject.put("res", objects);
//        System.out.println(jsonObject.get("res2"));
//        System.out.println(jsonObject);
        CompletableFuture<String> completableFuture = new CompletableFuture<>();
        new Thread(() -> {
            try {
                Thread.sleep(5000L);
                System.out.println(Thread.currentThread());
                System.out.println("complete");
                completableFuture.complete("abc");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();

        CompletableFuture<Object> objectCompletableFuture = completableFuture.thenCompose(money -> {
            System.out.println(money);
            System.out.println(Thread.currentThread());
            return null;
        });
        System.out.println("完成");
    }
}
