package com.lw.learn.akka;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.japi.pf.ReceiveBuilder;
import com.lw.learn.akka.message.RegisterSuccessful;
import com.lw.learn.akka.message.Registered;
import javafx.concurrent.Task;

import java.util.HashMap;
import java.util.HashSet;

public class JobMaster extends AbstractActor {
    private static ActorSystem system;
    HashMap<String, TaskExecutorInfo> taskExecutors = new HashMap();

    static Props props() {
        return Props.create(JobMaster.class, JobMaster::new);
    }

    @Override
    public Receive createReceive() {
        return ReceiveBuilder.create()
                .match(Registered.class, this::handleMessage)
                .build();
    }

    public void handleMessage(Object message) {
        System.out.println(message);
        if (message instanceof Registered) {
            Registered registered = (Registered) message;
            System.out.println(String.format("收到%s注册", registered.getTaskExecutorInfo().toString()));
            taskExecutors.put(registered.getTaskExecutoId(), registered.getTaskExecutorInfo());
            sender().tell(new RegisterSuccessful(), null);
        }
    }

    public static void main(String[] args) {

        System.out.println("JobMaster start");
//        system = ActorSystem.create("JobMaster",);

        ActorRef jobMaster = system.actorOf(JobMaster.props(), "jobMaster");
        System.out.println(jobMaster.path());

    }
}
