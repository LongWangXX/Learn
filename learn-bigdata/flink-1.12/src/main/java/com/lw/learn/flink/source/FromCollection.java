package com.lw.learn.flink.source;

import com.lw.bean.WaterSensor;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

import java.util.Arrays;
import java.util.List;

/**
 * 从集合中读取数据
 */
public class FromCollection {
    public static void main(String[] args) throws Exception {

        List<WaterSensor> waterSensors = Arrays.asList(
                new WaterSensor("ws_001", 1577844001L, 45),
                new WaterSensor("ws_002", 1577844015L, 43),
                new WaterSensor("ws_003", 1577844020L, 42));
        //创建FLink env
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        //从集合中创建source
        DataStreamSource<WaterSensor> source = env.fromCollection(waterSensors);
        //打印
        source.print();

        env.execute();

    }
}
