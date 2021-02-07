package com.alna.reactor3.module4;

import com.alna.reactor3.DemoSubscriber;
import com.alna.reactor3.utility.GateBasedSynchronization;
import com.alna.reactor3.utility.ThreadHelper;
import com.alna.reactor3.utility.datasets.FibonacciSequence;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.ConnectableFlux;
import reactor.core.scheduler.Schedulers;

import java.util.concurrent.TimeUnit;

public class ConnectableFluxExample1 {

    private final static Logger log = LoggerFactory.getLogger(ConnectableFluxExample1.class);

    public static void main(String[] args) {

        GateBasedSynchronization gate1 = new GateBasedSynchronization();
        GateBasedSynchronization gate2 = new GateBasedSynchronization();

        // Create the Fibonacci sequence of numbers 20 long...
        ConnectableFlux<Long> fibonacciSequence = FibonacciSequence.create(20)
                // ...scheduled on the computation thread pool
                .subscribeOn(Schedulers.parallel())
                // Call publish to turn it into a ConnectableFlux.
                .publish();

        // Create the 2 subscribers
        DemoSubscriber<Long> subscriber1 = new DemoSubscriber<>(gate1);
        DemoSubscriber<Long> subscriber2 = new DemoSubscriber<>(gate2);

        // Create 2 DemoSubscribers that are subscribed to the same sequence.
        fibonacciSequence.subscribe(subscriber1);
        fibonacciSequence.subscribe(subscriber2);

        ThreadHelper.sleep(2, TimeUnit.SECONDS);

        // Call connect to start the flow of events in the Fibonacci sequence.
        fibonacciSequence.connect();

        // Wait for both DemoSubscribers to complete.
        GateBasedSynchronization.waitMultiple( new String[] {"onComplete", "onError"}, gate1, gate2);

        System.exit(0);
    }
}