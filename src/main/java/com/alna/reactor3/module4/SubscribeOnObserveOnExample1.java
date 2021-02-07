package com.alna.reactor3.module4;


import com.alna.reactor3.DemoSubscriber;
import com.alna.reactor3.utility.GateBasedSynchronization;
import com.alna.reactor3.utility.datasets.FibonacciSequence;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Flux;
import reactor.core.scheduler.Schedulers;

public class SubscribeOnObserveOnExample1 {

    private final static Logger log = LoggerFactory.getLogger(SubscribeOnObserveOnExample1.class);

    public static void main(String[] args) {

        GateBasedSynchronization gate = new GateBasedSynchronization();

        // Our base observable for this example will be a FibonacciSequence with 10 numbers.
        Flux<Long> fibonacciFlux = FibonacciSequence.create(10)
                .doOnSubscribe( disposable -> {
                    log.info("fibonacciObservable::onSubscribe");
                });

        // -----------------------------------------------------------------------------------------

        // First, let's look at subscription with no threading modification.
        fibonacciFlux.subscribe(new DemoSubscriber<>(gate));

        // No threading, but do our synchronization pattern anyway.
        gate.waitForAny("onError", "onComplete");
        log.info("--------------------------------------------------------");

        // -----------------------------------------------------------------------------------------
        // Scan the numbers on the computation thread pool
        // -----------------------------------------------------------------------------------------

        gate.resetAll();

        // SubscribeOn example illustrating how first SubscribeOn wins.
        fibonacciFlux
                .subscribeOn(Schedulers.parallel())
                .subscribeOn(Schedulers.boundedElastic()) // This will be ignored.  subscribeOn is always first come, first served.
                .subscribe(new DemoSubscriber<>(gate));

        // No threading, but do our synchronization pattern anyway.
        gate.waitForAny("onError", "onComplete");
        log.info("--------------------------------------------------------");

        // -----------------------------------------------------------------------------------------
        // Illustrate how observeOn's position effects which scheduler is used.
        // -----------------------------------------------------------------------------------------

        gate.resetAll();

        // Illustrate how observeOn's position alters the scheduler that is
        // used for the observation portion of the code.
      //Source emission is doe from subscribeOn scheduler and all sequent operations including DemoSubscriber onNext() on Complete()
      // calls are done in publishOn() scheduler

        fibonacciFlux
            .log()
                // First publishOn...will be altered by the
                // observeOn further downstream.
                .publishOn(Schedulers.parallel())

                // The location of subscribeOn doesn't matter.
                // First subscribeOn always wins.
                .subscribeOn(Schedulers.elastic())

                // the last publishOn takes precedence.
                .publishOn(Schedulers.boundedElastic())

                .subscribe(new DemoSubscriber<>(gate));

        // No threading, but do our synchronization pattern anyway.
        gate.waitForAny("onError", "onComplete");
        log.info("--------------------------------------------------------");

        System.exit(0);
    }
}
