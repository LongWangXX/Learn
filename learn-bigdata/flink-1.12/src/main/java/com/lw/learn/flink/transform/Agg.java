package com.lw.learn.flink.transform;

import com.lw.bean.WaterSensor;
import org.apache.flink.api.java.functions.KeySelector;
import org.apache.flink.streaming.api.datastream.KeyedStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

import java.util.ArrayList;
import java.util.Arrays;

public class Agg {
    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
//        env.fromCollection(Arrays.asList(1,2,3,4,5,6)).keyBy(new KeySelector<Integer, String>() {
//            @Override
//            public String getKey(Integer value) throws Exception {
//                if(value%2==0) return "偶数";
//                else return "奇数";
//            }
//        }).sum(0).print("value");

//        ArrayList<WaterSensor> waterSensors = new ArrayList<>();
//        waterSensors.add(new WaterSensor("sensor_1", 1607527992000L, 20));
//        waterSensors.add(new WaterSensor("sensor_1", 1607527994000L, 50));
//        waterSensors.add(new WaterSensor("sensor_1", 1607527996000L, 30));
//        waterSensors.add(new WaterSensor("sensor_2", 1607527993000L, 10));
//        waterSensors.add(new WaterSensor("sensor_2", 1607527995000L, 30));
//
//        KeyedStream<WaterSensor, String> kbStream = env
//                .fromCollection(waterSensors)
//                .keyBy(WaterSensor::getId);
//
//        kbStream
//                .sum("vc")
//                .print("maxBy...");
        ArrayList<WaterSensor> waterSensors = new ArrayList<>();
        waterSensors.add(new WaterSensor("sensor_1", 1607527992000L, 20));
        waterSensors.add(new WaterSensor("sensor_1", 1607527994000L, 50));
        waterSensors.add(new WaterSensor("sensor_1", 1607527996000L, 50));
        waterSensors.add(new WaterSensor("sensor_2", 1607527993000L, 10));
        waterSensors.add(new WaterSensor("sensor_2", 1607527995000L, 30));

        //maxBy和minBy可以指定当出现相同值的时候,其他字段是否取第一个. true表示取第一个, false表示取最后一个.
        KeyedStream<WaterSensor, String> kbStream = env
                .fromCollection(waterSensors)
                .keyBy(WaterSensor::getId);

        kbStream
                .maxBy("vc", true)
                .print("max...");


        env.execute();
    }
}
