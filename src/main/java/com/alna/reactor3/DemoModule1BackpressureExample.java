package com.alna.reactor3;


import com.alna.reactor3.utility.GateBasedSynchronization;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Flux;
import reactor.core.scheduler.Schedulers;

import java.time.Duration;
import java.util.Observable;
import java.util.concurrent.TimeUnit;

public class DemoModule1BackpressureExample {

    private static Logger log = LoggerFactory.getLogger(DemoModule1BackpressureExample.class);

    public static void main(String[] args) {

        // Synchronization helper
        GateBasedSynchronization gate = new GateBasedSynchronization();

        // Create an ever-repeating number counter that counts from 1 to 1 billion.
        Flux<Integer> rangeOfNumbers = Flux.range(1 , 1_000_000_000)
                .repeat()
                .doOnNext( nextInt -> log.info("emitting int {}", nextInt))
                .subscribeOn(Schedulers.elastic())
                .publishOn(Schedulers.elastic(), 1000); //Usually by default it is 32


        // Create a DemoSubscriber with a slight delay of 10ms.
        // This should make the rangeOfNumber's emission far outpace
        // the subscriber.
        DemoSubscriber<Integer> demoSubscriber = new DemoSubscriber<Integer>(gate, Duration.ofMillis(10L));

        // Subscribe to start the numbers flowing.
        rangeOfNumbers.subscribe(demoSubscriber);

        // Wait for things to finish
        gate.waitForAny("onError", "onComplete");

        System.exit(0);
    }
}
