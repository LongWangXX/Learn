package com.lw.learn.dynamicproxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

public class AkkaInvocationHandler implements InvocationHandler {
    private TaskExecutorGateway gateway;

    public AkkaInvocationHandler(TaskExecutorGateway gateway) {
        this.gateway = gateway;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
//        System.out.println(method);
//        System.out.println(args);
        System.out.println(method.getDeclaringClass().equals(TaskExecutorGateway.class));
        return method.invoke(gateway, args);
    }
}
