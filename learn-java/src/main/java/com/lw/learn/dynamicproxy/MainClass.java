package com.lw.learn.dynamicproxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;

public class MainClass {
    public static void main(String[] args) {
        TaskExecutor taskExecutor = new TaskExecutor();
        InvocationHandler proxy = new AkkaInvocationHandler(taskExecutor);
        TaskExecutorGateway proxyInstance = (TaskExecutorGateway) Proxy.newProxyInstance(taskExecutor.getClass().getClassLoader(),
                taskExecutor.getClass().getInterfaces(), proxy);
        proxyInstance.requestSloat();
        proxyInstance.submitTask("slear");
    }
}
