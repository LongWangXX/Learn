package com.lw.learn.flink.timeservice;

import com.lw.bean.WaterSensor;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.KeyedProcessFunction;
import org.apache.flink.util.Collector;

import java.util.Date;

public class TimeServiceProcessTime {
    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        SingleOutputStreamOperator<WaterSensor> stream = env
                .socketTextStream("hadoop102", 9999)  // 在socket终端只输入毫秒级别的时间戳
                .map(value -> {
                    String[] datas = value.split(",");
                    return new WaterSensor(datas[0], Long.valueOf(datas[1]), Integer.valueOf(datas[2]));

                });

        stream
                .keyBy(WaterSensor::getId)
                .process(new KeyedProcessFunction<String, WaterSensor, String>() {
                    @Override
                    public void processElement(WaterSensor value, Context ctx, Collector<String> out) throws Exception {
                        // 处理时间过后5s后触发定时器
                        System.out.println(ctx.timestamp());
                        System.out.println(ctx.timerService().currentProcessingTime());
                        System.out.println(new Date(ctx.timerService().currentProcessingTime()));
                        ctx.timerService().registerProcessingTimeTimer(ctx.timerService().currentProcessingTime() + 5000);
                        out.collect(value.toString());
                    }


                    // 定时器被触发之后, 回调这个方法
                    // 参数1: 触发器被触发的时间
                    @Override
                    public void onTimer(long timestamp, OnTimerContext ctx, Collector<String> out) throws Exception {
                        System.out.println(timestamp);
                        System.out.println(new Date(timestamp));

                        out.collect("我被触发了....");
                    }
                })
                .print();

        env.execute();

    }
}
