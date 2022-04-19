package com.lw.data.importer;

import com.alibaba.fastjson.JSONObject;

import java.util.List;

public interface DataReader<T> {
    /**
     * 数据读取
     */
    public List<T> getData(int batchCount);

    /**
     * 开始读取数据
     *
     * @return
     */
    public void startReader(Class<T> clazz);

    /**
     * 初始化
     *
     * @param properties
     */
    public void init(JSONObject properties);

    /**
     * 是否还有数据
     *
     * @return
     */
    public boolean hasNext();

    /**
     * 关闭资源
     */
    public void close();
}
