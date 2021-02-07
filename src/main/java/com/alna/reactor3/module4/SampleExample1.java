package com.alna.reactor3.module4;

import com.alna.reactor3.DemoSubscriber;
import com.google.common.util.concurrent.Uninterruptibles;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Flux;
import reactor.core.scheduler.Schedulers;

import java.time.Duration;

public class SampleExample1 {

    private final static Logger log = LoggerFactory.getLogger(SampleExample1.class);

    public static void main(String[] args) {

        // Create a repeating greek alphabet
        Flux<Long> incrementingObservable = Flux.interval(Duration.ofMillis(50))

                // Like timeout, sample must use a different thread pool
                // so that it can send a message event though events
                // may be being generated on the main thread.
            //Both timeout and sample must run on separate scheduler.
                .subscribeOn(Schedulers.elastic())

                // Sample the stream every 2 seconds.
                .sample(Duration.ofMillis(100));

        // Subscribe and watch the emit happen every 2 seconds.
        incrementingObservable.subscribe(new DemoSubscriber<>());

        // Wait for 10 seconds
        Uninterruptibles.sleepUninterruptibly(Duration.ofSeconds(10));

        System.exit(0);
    }
}
