package com.lw.learn.flink;

import java.util.ArrayList;

public class Test {
    public static void main(String[] args) {
        String s = "\\\"([\\d+\\-\\d]+\\s+[\\d+:]+).\\d+\\\"\\s+(\\S+)\\s+(\\S+)\\s+(\\S+)\\s+(\\S+)\\s+(\\S+)\\s+(\\S+)\\s(\\S+)\\s+(\\S+)\\s+([\\S+\\s+]+)\\\"*\\s+(\\S+)";
        System.out.println(s);
        ArrayList<String> strings = new ArrayList<>();
        strings.add("");
        strings.add(null);
        System.out.println(strings);
    }
}
