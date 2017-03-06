package com.max.algs.actor;


import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.dispatch.Future;
import scala.concurrent.duration.Duration;

import java.util.concurrent.TimeUnit;

import static akka.pattern.Patterns.ask;

public final class ActorMain {


    private ActorMain() throws Exception {

        ActorSystem actorSystem = ActorSystem.create();

        ActorRef actorRef = actorSystem.actorOf(Props.create(EchoActor.class));
        actorRef.tell("hello", ActorRef.noSender());

//        scala.concurrent.Future f = ask(actorRef, "hello", 1000);
//        System.out.println(f.value().get());

        TimeUnit.SECONDS.sleep(2);
        actorSystem.shutdown();

        System.out.printf("ActorMain: java-%s %n", System.getProperty("java.version"));
    }

    public static void main(String[] args) {
        try {
            new ActorMain();
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
