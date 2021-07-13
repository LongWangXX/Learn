package com.lw.learn.juc.collection;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.CopyOnWriteArraySet;

public class CollectionConcurrentProblem {
    public static void main(String[] args) {
        //arraylist存在并发修改问题
        //解决方案使用CopyO
       // List<String> list = new ArrayList<>();
        List<String> list = new CopyOnWriteArrayList<>();
        for(int i = 0;i<30;i++){
            new Thread(()->{
                list.add(UUID.randomUUID().toString());
                System.out.println(list);
            }).start();
        }

//        Set<String> set = new HashSet<>();
        //hashset存在多线程读写并发问题，使用CopyOnWriteArraySet解决
        Set<String> set = new CopyOnWriteArraySet<>();

        for(int i = 0;i<30;i++){
            new Thread(()->{
                set.add(UUID.randomUUID().toString());
                System.out.println(set);
            }).start();
        }

        //Map<String,String> map = new HashMap();
        //hashMap存在多线程并发问题，使用currentHashMap来解决
        Map<String,String> map =new ConcurrentHashMap<>();

        for(int i = 0;i<30;i++){
            new Thread(()->{
                map.put(UUID.randomUUID().toString(),UUID.randomUUID().toString());
                System.out.println(map);
            }).start();
        }
    }
}
