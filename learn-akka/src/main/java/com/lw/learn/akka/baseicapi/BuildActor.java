package com.lw.learn.akka.baseicapi;

import akka.actor.*;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import akka.japi.Creator;

/**
 * 在一个应用中，所有的actor共同构成了atcor系统，即ActorSystem，他是一个层级结构
 */
public class BuildActor {
    public static void main(String[] args) {
        //1.不能直接new actorDemo
        //ActorDemo actorDemo = new ActorDemo();
        //actorSystem是一个非常重量级的对象，一般来说一个jvm 应用只需要创建一个system
        //在创建ActorSystem和actor时候，最好都给出名字（不能重名）
        ActorSystem system = ActorSystem.create("actorSys");
        //actorof返回的不是actor对象，而是ActorRef，既actor的引用。
        system.actorOf(Props.create(ActorDemo.class), "actorDemo");
        system.shutdown();
    }
}

class ActorDemo extends UntypedActor {
    private LoggingAdapter log = Logging.getLogger(this.getContext().system(), this);
    //可以通过getContext来创建当前actor的子actor
    ActorRef actor = getContext().actorOf(Props.create(ActorDemo.class), "ChildActor1");

    @Override
    public void onReceive(Object message) throws Exception, Exception {
        if (message instanceof String) {
            log.info(message.toString());
        } else {
            //匹配到无法响应的消息类型时，推荐使用unhandled来处理。
            unhandled(message);
        }
    }
}

class ChildActor extends UntypedActor {
    private LoggingAdapter log = Logging.getLogger(this.getContext().system(), this);

    @Override
    public void onReceive(Object message) throws Exception, Exception {
        if (message instanceof String) {
            log.info(message.toString());
        } else {
            //匹配到无法响应的消息类型时，推荐使用unhandled来处理。
            unhandled(message);
        }
    }
}

/**
 * 可通过actor工厂来创建actor
 */
class PropsDemoActor extends UntypedActor {

    @Override
    public void onReceive(Object message) throws Exception, Exception {

    }

    public static Props createProps() {
        return Props.create(new Creator<Actor>() {
            @Override
            public Actor create() throws Exception, Exception {
                return new PropsDemoActor();
            }
        });
    }
}
