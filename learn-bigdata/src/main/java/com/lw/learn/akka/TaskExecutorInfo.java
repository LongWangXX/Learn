package com.lw.learn.akka;

public class TaskExecutorInfo {
    private String name;

    public TaskExecutorInfo(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "TaskExecutorInfo{" +
                "name='" + name + '\'' +
                '}';
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
