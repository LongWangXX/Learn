package com.lw.learn.flink.datagen;


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
        final StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();

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
                "  'rows-per-second' = '10',\n" +
                "  'fields.id.kind' = 'sequence',\n" +
                "  'fields.id.start' = '0',\n" +
                "  'fields.id.end' = '100',\n" +
                "  'fields.app.min' = '1',\n" +
                "  'fields.app.max' = '10',\n" +
                "  'fields.channel.min' = '21',\n" +
                "  'fields.channel.max' = '30'\n" +
                ")";

        tableEnv.sqlUpdate(sourceDDl);
        // 优化后的 sql，解决了数据倾斜，将全量数据根据 userId 打散成 1024 个桶，
        // 分桶内去重，最后聚合
        String querySql = "select * from Orders";

        tableEnv.executeSql(querySql);

        Table table = tableEnv.sqlQuery(querySql);
        tableEnv.toAppendStream(table, Row.class).print();

//        System.out.println(env.getExecutionPlan());
        env.execute(DataGen.class.getSimpleName());
    }

}
