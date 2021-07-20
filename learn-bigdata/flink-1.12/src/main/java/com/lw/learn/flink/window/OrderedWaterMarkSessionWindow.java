package com.lw.learn.flink.window;

import com.lw.bean.WaterSensor;
import org.apache.flink.api.common.eventtime.SerializableTimestampAssigner;
import org.apache.flink.api.common.eventtime.WatermarkStrategy;
import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.api.java.functions.KeySelector;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.sink.SinkFunction;
import org.apache.flink.streaming.api.functions.windowing.ProcessWindowFunction;
import org.apache.flink.streaming.api.windowing.assigners.EventTimeSessionWindows;
import org.apache.flink.streaming.api.windowing.assigners.SessionWindowTimeGapExtractor;
import org.apache.flink.streaming.api.windowing.assigners.SlidingEventTimeWindows;
import org.apache.flink.streaming.api.windowing.time.Time;
import org.apache.flink.streaming.api.windowing.windows.TimeWindow;
import org.apache.flink.util.Collector;

import java.time.Duration;
import java.util.Date;

/**
 * 事件时间的sessionwindow
 * 是 当前watermark的的时间与前面最后的时间 差 >= 传入的时间差 则划分窗口
 */

public class OrderedWaterMarkSessionWindow {
    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment().setParallelism(1);
        Date date = new Date(1625323525 * 1000L);
        System.out.println(date);
        SingleOutputStreamOperator<WaterSensor> stream = env
                .socketTextStream("hadoop102", 9999)
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
//                })  // 在socket终端只输入毫秒级别的时间戳
                .map(new MapFunction<String, WaterSensor>() {
                    @Override
                    public WaterSensor map(String value) throws Exception {
                        String[] datas = value.split(",");
                        return new WaterSensor(datas[0], Long.valueOf(datas[1]), Integer.valueOf(datas[2]));
                    }
                });

        // 创建水印生产策略
        WatermarkStrategy<WaterSensor> objectWatermarkStrategy = WatermarkStrategy.<WaterSensor>forMonotonousTimestamps().withTimestampAssigner(
                new SerializableTimestampAssigner<WaterSensor>() {
                    @Override
                    public long extractTimestamp(WaterSensor element, long recordTimestamp) {
                        return element.getTs() * 1000;
                    }
                }
        );


        WatermarkStrategy<WaterSensor> wms = WatermarkStrategy
                .<WaterSensor>forBoundedOutOfOrderness(Duration.ofSeconds(3)) // // 最大容忍的延迟时间
                .withTimestampAssigner(new SerializableTimestampAssigner<WaterSensor>() { // 指定时间戳
                    @Override
                    public long extractTimestamp(WaterSensor element, long recordTimestamp) {
                        return element.getTs() * 1000;
                    }
                });

        stream.assignTimestampsAndWatermarks(wms)
                // 指定水印和时间戳
                .keyBy(new KeySelector<WaterSensor, String>() {
                    @Override
                    public String getKey(WaterSensor value) throws Exception {
                        return value.getId();
                    }
                })
                .window(EventTimeSessionWindows.withGap(Time.seconds(5)))
                .process(new ProcessWindowFunction<WaterSensor, String, String, TimeWindow>() {
                    @Override
                    public void process(String key, Context context, Iterable<WaterSensor> elements, Collector<String> out) throws Exception {
                        String msg = "当前key: " + key
                                + "窗口: [" + context.window().getStart() / 1000 + "," + context.window().getEnd() / 1000 + ") 一共有 "
                                + elements.spliterator().estimateSize() + "条数据 ";
                        out.collect(msg);
                    }
                })
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
