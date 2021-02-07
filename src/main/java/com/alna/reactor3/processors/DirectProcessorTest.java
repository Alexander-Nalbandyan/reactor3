package com.alna.reactor3.processors;

import com.alna.reactor3.DemoSubscriber;
import com.alna.reactor3.utility.GateBasedSynchronization;
import reactor.core.publisher.DirectProcessor;

public class DirectProcessorTest {
  public static void main(String[] args) {
    GateBasedSynchronization gate = new GateBasedSynchronization();

    //Direct processor allows to subscribe 0...n subscribers.
    //It doesn't replay onNext() signals to new subscribers.
    //It only replays termination signals onError() and onComplete()
    //If onComplete() or onError() is called then all further onNext() calls are ignored.
    //
    final DirectProcessor<String> directProcessor = DirectProcessor.create();

    directProcessor.onNext("test1");
//    directProcessor.onComplete();
//    directProcessor.onError(new RuntimeException("test message"));
    directProcessor.subscribe(new DemoSubscriber<>("sub1", 10L));
    //Signals onError() to the subscriber with IllegalStateException which requests less elements.
    directProcessor.subscribe(new DemoSubscriber<>("sub2", 3L));

    directProcessor.onNext("test2");
    directProcessor.onNext("test3");
    directProcessor.onNext("test4");
    directProcessor.onNext("test5");
    directProcessor.onNext("test6");
    directProcessor.onNext("test7");
    directProcessor.onNext("test8");
    directProcessor.onNext("test9");
    directProcessor.onComplete();

    gate.waitForAny("onComplete");
  }
}
