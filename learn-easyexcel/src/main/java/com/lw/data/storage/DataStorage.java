package com.lw.data.storage;


import com.alibaba.fastjson.JSONObject;

import java.util.List;

public interface DataStorage<T> {
    public void writer(List<T> list);

    public void startWriter(Class<T> t);

    public void init(JSONObject properties);

    /**
     * 关闭资源
     */
    public void close();
}
