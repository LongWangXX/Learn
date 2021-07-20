package com.lw.learn.flink.timeservice;

import com.lw.bean.WaterSensor;
import org.apache.flink.api.common.eventtime.WatermarkStrategy;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.KeyedProcessFunction;
import org.apache.flink.util.Collector;

import java.time.Duration;

/**
 *
 * 时间进展依据的是watermark
 */
public class TimeServiceEventTime {
    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.setParallelism(2);
        SingleOutputStreamOperator<WaterSensor> stream = env
                .socketTextStream("hadoop102", 9999)  // 在socket终端只输入毫秒级别的时间戳
                .map(value -> {
                    String[] datas = value.split(",");
                    return new WaterSensor(datas[0], Long.valueOf(datas[1]), Integer.valueOf(datas[2]));

                });

        WatermarkStrategy<WaterSensor> wms = WatermarkStrategy
                .<WaterSensor>forBoundedOutOfOrderness(Duration.ofSeconds(3))
                .withTimestampAssigner((element, recordTimestamp) -> element.getTs() * 1000);
        stream
                .assignTimestampsAndWatermarks(wms)
                .keyBy(WaterSensor::getId)
                .process(new KeyedProcessFunction<String, WaterSensor, String>() {
                    @Override
                    public void processElement(WaterSensor value, Context ctx, Collector<String> out) throws Exception {
                        //事件时间
                        System.out.println(ctx.timestamp());
                        ctx.timerService().registerEventTimeTimer(ctx.timestamp() + 5000);
                        out.collect(value.toString());
                    }

                    @Override
                    public void onTimer(long timestamp, OnTimerContext ctx, Collector<String> out) throws Exception {
                        System.out.println("定时器被触发.....");
                    }
                })
                .print();

        env.execute();

    }
}
