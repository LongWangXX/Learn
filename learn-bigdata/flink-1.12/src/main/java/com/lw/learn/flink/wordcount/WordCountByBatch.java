package com.lw.learn.flink.wordcount;

import org.apache.flink.api.common.functions.FlatMapFunction;
import org.apache.flink.api.common.typeinfo.Types;
import org.apache.flink.api.java.ExecutionEnvironment;
import org.apache.flink.api.java.operators.AggregateOperator;
import org.apache.flink.api.java.operators.DataSource;
import org.apache.flink.api.java.operators.FlatMapOperator;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.util.Collector;

public class WordCountByBatch {
    public static void main(String[] args) throws Exception {
        //1.创建环境
        ExecutionEnvironment env = ExecutionEnvironment.getExecutionEnvironment();

        DataSource<String> dataSource = env.readTextFile("G:\\data\\flink\\test.txt");

        FlatMapOperator<String, Tuple2<String, Long>> flatMapOperator = dataSource.flatMap(new FlatMapFunction<String, Tuple2<String, Long>>() {
            public void flatMap(String line, Collector<Tuple2<String, Long>> out) throws Exception {
                String[] split = line.split(" ");
                for (String word : split) {
                    out.collect(Tuple2.of(word, 1L));
                }

            }
        });

        AggregateOperator<Tuple2<String, Long>> sum = flatMapOperator.groupBy(0).sum(1);
        sum.print();


    }
}
