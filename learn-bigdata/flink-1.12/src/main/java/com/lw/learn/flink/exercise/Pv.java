package com.lw.learn.flink.exercise;

import com.lw.bean.UserBehavior;
import org.apache.flink.api.common.functions.ReduceFunction;
import org.apache.flink.api.java.functions.KeySelector;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.KeyedProcessFunction;
import org.apache.flink.util.Collector;

public class Pv {
    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();

        DataStreamSource<String> source = env.readTextFile("G:\\data\\flink/UserBehavior.csv");

        SingleOutputStreamOperator<UserBehavior> userBehavior = source.map(str -> {
            UserBehavior userBehavior2 = new UserBehavior();
            String[] splits = str.split(",");
            userBehavior2.setUserId(new Long(splits[0]));
            userBehavior2.setItemId(new Long(splits[1]));
            userBehavior2.setCategoryId(new Integer(splits[2]));
            userBehavior2.setBehavior(splits[3]);
            userBehavior2.setTimestamp(new Long(splits[4]));
            return userBehavior2;
        }).returns(UserBehavior.class);
        userBehavior.filter(userBehaviorbean->"pv".equals(userBehaviorbean.getBehavior()))
                .keyBy(new KeySelector<UserBehavior, String>() {
                    @Override
                    public String getKey(UserBehavior value) throws Exception {
                        return value.getBehavior();
                    }
                }).process(new KeyedProcessFunction<String, UserBehavior, Long>() {

                    Long pv = 0L;
            @Override
            public void processElement(UserBehavior userBehavior, Context context, Collector<Long> collector) throws Exception {
                pv++;
                collector.collect(pv);
            }
        }).print("pv");

        env.execute();


    }
}
