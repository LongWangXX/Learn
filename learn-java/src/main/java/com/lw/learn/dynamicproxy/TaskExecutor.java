package com.lw.learn.dynamicproxy;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

public class TaskExecutor implements TaskExecutorGateway, Map<String,String> {
    @Override
    public void requestSloat() {
        System.out.println(String.format("成功申请到%s个Sloat", System.currentTimeMillis() % 10));
    }

    @Override
    public void submitTask(String task) {
        System.out.println(String.format("成功提交%s", task));

    }

    @Override
    public void triggerCheckpoint() {
        System.out.println("成功触发CheckPoint");
    }

    @Override
    public int size() {
        return 0;
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    @Override
    public boolean containsKey(Object key) {
        return false;
    }

    @Override
    public boolean containsValue(Object value) {
        return false;
    }

    @Override
    public String get(Object key) {
        return null;
    }

    @Override
    public String put(String key, String value) {
        return null;
    }

    @Override
    public String remove(Object key) {
        return null;
    }

    @Override
    public void putAll(Map<? extends String, ? extends String> m) {

    }

    @Override
    public void clear() {

    }

    @Override
    public Set<String> keySet() {
        return null;
    }

    @Override
    public Collection<String> values() {
        return null;
    }

    @Override
    public Set<Entry<String, String>> entrySet() {
        return null;
    }
}
