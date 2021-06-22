package com.lw.learn.akka;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.japi.pf.ReceiveBuilder;
import com.lw.learn.akka.message.Registered;

public class TaskExecutor extends AbstractActor {
    private static ActorSystem system;

    public static void main(String[] args) {
        System.out.println("TaskExecutor start");
        system = ActorSystem.create("TaskExecutor");
        ActorRef taskExecutor = system.actorOf(TaskExecutor.props(), "TaskExecutor");
        taskExecutor.tell(new Registered("TaskExecutor1", new TaskExecutorInfo("TaskExecutor2"))
                , null
        );
    }

    private static Props props() {
        return Props.create(TaskExecutor.class, TaskExecutor::new);
    }

    @Override
    public Receive createReceive() {
        return ReceiveBuilder.create().matchAny(this::handleMessage).build();
    }

    private void handleMessage(Object o) {
        if (o instanceof Registered) {
            system.actorFor("akka://JobMaster/user/jobMaster").tell(o, ActorRef.noSender());
        }
    }
}
