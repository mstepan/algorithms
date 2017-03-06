package com.max.algs.actor;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.Status;
import akka.japi.pf.ReceiveBuilder;
import scala.PartialFunction;
import scala.runtime.BoxedUnit;

/**
 * Simple echo actor
 */
public class EchoActor extends AbstractActor {

    @Override
    public PartialFunction<Object, BoxedUnit> receive() {
        return ReceiveBuilder.
                matchEquals("hello", s -> sender().tell("hello too, your message is " + s, ActorRef.noSender())).
                matchAny(x -> sender().tell(new Status.Failure(new IllegalArgumentException("unknown message")), self())).
                build();
    }
}
