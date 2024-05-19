package com.lw.learn.flink.datagen;


import org.apache.flink.configuration.Configuration;
import org.apache.flink.configuration.RestOptions;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.table.api.Table;
import org.apache.flink.table.api.bridge.java.StreamTableEnvironment;
import org.apache.flink.types.Row;

/**
 * @author fanrui03
 * @date 2020/9/20 14:19
 */
public class DataGen {

    public static void main(String[] args) throws Exception {

        // set up the Java DataStream API
        final StreamExecutionEnvironment env = StreamExecutionEnvironment.createLocalEnvironmentWithWebUI(new Configuration());
        env.setParallelism(3);
        // set up the Java Table API
        final StreamTableEnvironment tableEnv = StreamTableEnvironment.create(env);

        String sourceDDl = "CREATE TABLE Orders (\n" +
                "    id INT,\n" +
                "    app        INT,\n" +
                "    channel        INT,\n" +
                "    user_id        INT,\n" +
                "    ts   TIMESTAMP(3),\n" +
                "    WATERMARK FOR ts AS ts" +
                ") WITH (\n" +
                "  'connector' = 'datagen',\n" +
                "  'rows-per-second' = '30000',\n" +
                "  'fields.id.kind' = 'random',\n" +
                "  'fields.id.min' = '1',\n" +
                "  'fields.id.max' = '100',\n" +
                "  'fields.app.min' = '1',\n" +
                "  'fields.app.max' = '10',\n" +
                "  'fields.channel.min' = '21',\n" +
                "  'fields.channel.max' = '30'\n" +
                ")";

        tableEnv.executeSql(sourceDDl);

        String querySql = "select if(id <90,1,id),count(*) from Orders group by if(id <90,1,id)";


        Table table = tableEnv.sqlQuery(querySql);
        tableEnv.toRetractStream(table, Row.class).print();
        env.execute();
    }

}
