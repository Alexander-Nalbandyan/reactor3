package com.alna.reactor3.module4;

import com.alna.reactor3.DemoSubscriber;
import com.alna.reactor3.utility.ThreadHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.ConnectableFlux;
import reactor.core.publisher.Flux;
import reactor.core.scheduler.Schedulers;

import java.time.Duration;
import java.util.Observable;
import java.util.concurrent.TimeUnit;

public class ConnectableFluxExample2 {

    private final static Logger log = LoggerFactory.getLogger(ConnectableFluxExample2.class);

    public static void main(String[] args) {

        // Create an Observable that emits every 100 milliseconds.
        Flux<Long> intervalSequence = Flux.interval(Duration.ofMillis(100))

                // ...process it on the computation scheduler
                .subscribeOn(Schedulers.parallel())

                // Log each time the interval emits
                .doOnNext( nextLong -> log.info("doOnNext - {}", nextLong))

                // Publish to make it a ConnectableObservable.
                .publish()

                // Call refCount to make it track the number of Subscribers and
                // stop emitting when no one is listening.
                .refCount();

        // Create the two DemoSubscribers we will use.
        DemoSubscriber<Long> demoSubscriber1 = new DemoSubscriber<>();
        DemoSubscriber<Long> demoSubscriber2 = new DemoSubscriber<>();

        // Have both DemoSubscribers subscribe to the interval sequence.
        // This will start the flow of events.
        intervalSequence.subscribe(demoSubscriber1);
        intervalSequence.subscribe(demoSubscriber2);

        // Allow things to happen for 2 seconds.
        ThreadHelper.sleep(2, TimeUnit.SECONDS);

        // Dispose of the first subscriber.  Notice that the
        // events continue to flow.
        demoSubscriber1.getSubscription().cancel();

        // Wait another 2 seconds
        ThreadHelper.sleep(2 , TimeUnit.SECONDS);

        // Dispose of the second subscriber.  Notice that the
        // events stop flowing
        demoSubscriber2.getSubscription().cancel();

        // Wait for another 2 seconds and emit a message
        // so we see that no events are flowing.
        log.info( "Pausing for 2 seconds...");
        ThreadHelper.sleep(2, TimeUnit.SECONDS);
        log.info( "...pause complete");

        ThreadHelper.sleep(2, TimeUnit.SECONDS);

        System.exit(0);
    }
}
