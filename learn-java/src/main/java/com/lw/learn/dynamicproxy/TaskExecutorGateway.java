package com.lw.learn.dynamicproxy;

public interface TaskExecutorGateway {
    public void requestSloat();

    public void submitTask(String task);

    public void triggerCheckpoint();
}
