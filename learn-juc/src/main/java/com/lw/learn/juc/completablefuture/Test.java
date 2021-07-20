package com.lw.learn.juc.completablefuture;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

public class Test {
    public static void main(String[] args) {
        JSONObject jsonObject = new JSONObject();
        JSONObject jsonObject1 = new JSONObject();
        JSONArray objects = new JSONArray();
        jsonObject1.put("name", "abc");
        objects.add(jsonObject1);
        objects.add(jsonObject1);
        objects.add(jsonObject1);
        jsonObject.put("res", jsonObject1);
        jsonObject.put("res", objects);
        System.out.println(jsonObject.get("res2"));
        System.out.println(jsonObject);
    }
}
