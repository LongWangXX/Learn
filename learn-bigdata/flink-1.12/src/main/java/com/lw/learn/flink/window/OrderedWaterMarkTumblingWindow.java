package com.lw.learn.flink.window;

import com.lw.bean.WaterSensor;
import org.apache.flink.api.common.eventtime.SerializableTimestampAssigner;
import org.apache.flink.api.common.eventtime.WatermarkStrategy;
import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.api.java.functions.KeySelector;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.sink.SinkFunction;
import org.apache.flink.streaming.api.functions.source.SourceFunction;
import org.apache.flink.streaming.api.functions.windowing.ProcessWindowFunction;
import org.apache.flink.streaming.api.windowing.assigners.TumblingEventTimeWindows;
import org.apache.flink.streaming.api.windowing.time.Time;
import org.apache.flink.streaming.api.windowing.windows.TimeWindow;
import org.apache.flink.util.Collector;
import org.apache.flink.util.OutputTag;

import javax.swing.*;
import java.time.Duration;
import java.util.Date;
import java.util.Random;
import java.util.UUID;

public class OrderedWaterMarkTumblingWindow {
    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment().setParallelism(2);
        Date date = new Date(1625323525 * 1000L);
        System.out.println(date);
        SingleOutputStreamOperator<WaterSensor> stream = env
                .socketTextStream("192.168.8.22", 9999)
//                .addSource(new SourceFunction<String>() {
//                    private boolean isRunning = true;
//                    @Override
//                    public void run(SourceContext<String> ctx) throws Exception {
//                        while (isRunning){
//                            int id =  Math.abs(UUID.randomUUID().toString().hashCode())% 10;
//                            Date date = new Date();
//                            String behavies = "1";
//                            String me = id+","+date.getTime()/1000+","+behavies;
//                            ctx.collect(me);
//                            Thread.sleep(Math.abs(UUID.randomUUID().toString().hashCode())%2*1000);
//                        }
//                    }
//
//                    @Override
//                    public void cancel() {
//                        isRunning = false;
//
//                    }
//                })  // ???socket???????????????????????????????????????
                .map(new MapFunction<String, WaterSensor>() {
                    @Override
                    public WaterSensor map(String value) throws Exception {
                        String[] datas = value.split(",");
                        return new WaterSensor(datas[0], Long.valueOf(datas[1]), Integer.valueOf(datas[2]));
                    }
                }).returns(WaterSensor.class);

        // ????????????????????????
        WatermarkStrategy<WaterSensor> objectWatermarkStrategy = WatermarkStrategy.<WaterSensor>forMonotonousTimestamps().withTimestampAssigner(
                new SerializableTimestampAssigner<WaterSensor>() {
                    @Override
                    public long extractTimestamp(WaterSensor element, long recordTimestamp) {
                        return element.getTs() * 1000;
                    }
                }
        );


        WatermarkStrategy<WaterSensor> wms = WatermarkStrategy
                .<WaterSensor>forBoundedOutOfOrderness(Duration.ofSeconds(3)) // // ???????????????????????????
                .withTimestampAssigner(new SerializableTimestampAssigner<WaterSensor>() { // ???????????????
                    @Override
                    public long extractTimestamp(WaterSensor element, long recordTimestamp) {
                        return element.getTs() * 1000;
                    }
                });

        SingleOutputStreamOperator<String> testStream = stream.assignTimestampsAndWatermarks(wms)
                // ????????????????????????
                .keyBy(new KeySelector<WaterSensor, String>() {
                    @Override
                    public String getKey(WaterSensor value) throws Exception {
                        return value.getId();
                    }
                })
                .window(TumblingEventTimeWindows.of(Time.seconds(5)))
                .allowedLateness(Time.seconds(3))//	???????????????????????????event time???????????????????????????????????????????????????????????????????????? ????????????watermark + 3????????????????????????
                .sideOutputLateData(new OutputTag<WaterSensor>("TEST") {
                                    }
                )
                .process(new ProcessWindowFunction<WaterSensor, String, String, TimeWindow>() {
                    @Override
                    public void process(String key, Context context, Iterable<WaterSensor> elements, Collector<String> out) throws Exception {
                        String msg = "??????key: " + key
                                + "??????: [" + context.window().getStart() / 1000 + "," + context.window().getEnd() / 1000 + ") ????????? "
                                + elements.spliterator().estimateSize() + "????????? ";
                        out.collect(msg);
                    }
                });

        testStream.getSideOutput(new OutputTag<WaterSensor>("TEST")).print();
        testStream
                .addSink(new SinkFunction<String>() {
                    @Override
                    public void invoke(String value, Context context) throws Exception {
                        System.out.println(Thread.currentThread().getName());
                        System.out.println(value);
                    }
                });
        env.execute();
    }

}
