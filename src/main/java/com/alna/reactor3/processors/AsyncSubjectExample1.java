package com.alna.reactor3.processors;

import com.alna.reactor3.DemoSubscriber;
import com.alna.reactor3.utility.GateBasedSynchronization;
import com.alna.reactor3.utility.ThreadHelper;
import org.reactivestreams.Processor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.DirectProcessor;
import reactor.core.publisher.FluxProcessor;
import reactor.core.publisher.ReplayProcessor;
import reactor.core.publisher.UnicastProcessor;
import reactor.core.scheduler.Schedulers;

import java.util.concurrent.TimeUnit;

public class AsyncSubjectExample1 {

    private final static Logger log = LoggerFactory.getLogger(AsyncSubjectExample1.class);

    public static void main(String[] args) {

        GateBasedSynchronization gate = new GateBasedSynchronization();

        // Create an AsyncSubject that will contain the last event streamed
        // to it before it is closed.
        FluxProcessor<String, String> replayProcessor = DirectProcessor.create();

        // Perform a long running operation that will emit
        // an event into our AsyncSubject
        Runnable longRunningAction = () -> {

            // Sleep for 2 seconds...
            ThreadHelper.sleep(2, TimeUnit.SECONDS);

            // Emit some data
            replayProcessor.onNext("Hello World 1");
            replayProcessor.onNext("Hello World 2");
            replayProcessor.onNext("Hello World 3");


            // Sleep some more
            ThreadHelper.sleep(1, TimeUnit.SECONDS);

            replayProcessor.onError(new RuntimeException("test"));

            // Complete the stream
            //replayProcessor.onComplete();

            // Open the synchonization gate called "onComplete"
            gate.openGate("onComplete");
        };

        // Create a subscriber to the AsyncSubject
        replayProcessor
                .subscribeOn(Schedulers.parallel())
                .last() //No direct alternative for AsyncSubject but we can use last() operator instead which will emit only the last value before complete or the error.
                .subscribe(new DemoSubscriber<>());

        // Execute the long running action on the IO scheduler
        Schedulers.elastic().schedule(longRunningAction);

        gate.waitForAny("onComplete");

        System.exit(0);

    }
}