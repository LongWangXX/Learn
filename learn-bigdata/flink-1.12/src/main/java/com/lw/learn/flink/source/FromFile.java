package com.lw.learn.flink.source;

import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

public class FromFile {
    public static void main(String[] args) throws Exception {

        // 1. 创建执行环境
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env
                .readTextFile("G:\\data\\flink\\test.txt")
                .print();

        env.execute();
    }

}

