package com.lw.learn.akka.message;

import com.lw.learn.akka.TaskExecutorInfo;

public class Registered {

    private String taskExecutoId;
    private TaskExecutorInfo taskExecutorInfo;
    public Registered(String taskExecutoId, TaskExecutorInfo taskExecutorInfo){
        this.taskExecutoId = taskExecutoId;
        this.taskExecutorInfo  = taskExecutorInfo;
    }

    public String getTaskExecutoId() {
        return taskExecutoId;
    }

    public void setTaskExecutoId(String taskExecutoId) {
        this.taskExecutoId = taskExecutoId;
    }

    public TaskExecutorInfo getTaskExecutorInfo() {
        return taskExecutorInfo;
    }

    public void setTaskExecutorInfo(TaskExecutorInfo taskExecutorInfo) {
        this.taskExecutorInfo = taskExecutorInfo;
    }
}
