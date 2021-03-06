package com.lw.learn.flink.source;

import org.apache.flink.api.common.serialization.SimpleStringSchema;
import org.apache.flink.streaming.api.datastream.ConnectedStreams;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer;

import java.util.Arrays;
import java.util.Properties;

public class FromKafka {
    public static void main(String[] args) throws Exception {

        // 0.Kafka相关配置
        Properties properties = new Properties();
        properties.setProperty("bootstrap.servers", "hadoop102:9092,hadoop103:9092,hadoop104:9092");
        properties.setProperty("group.id", "Flink01_Source_Kafka");
        properties.setProperty("auto.offset.reset", "latest");

        // 1. 创建执行环境
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();

        DataStreamSource<Integer> dataStream = env.fromCollection(Arrays.asList(1, 2, 3, 4));

        DataStreamSource<String> kafkStream = env.addSource(new FlinkKafkaConsumer<>("sensor", new SimpleStringSchema(), properties));

        ConnectedStreams<Integer, String> connect = dataStream.connect(kafkStream);
        connect.getFirstInput().print("first");
        connect.getSecondInput().print("sesond");

        env.execute();
    }

}
