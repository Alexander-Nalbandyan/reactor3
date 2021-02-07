package com.alna.reactor3.module3;

import com.alna.reactor3.DemoSubscriber;
import com.alna.reactor3.utility.GateBasedSynchronization;
import com.alna.reactor3.utility.ThreadHelper;
import com.alna.reactor3.utility.datasets.GreekAlphabet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Flux;
import reactor.core.scheduler.Schedulers;

import java.util.concurrent.TimeUnit;

public class DemoModule1ColdVsHot_ColdExample {

    private static final Logger log = LoggerFactory.getLogger(DemoModule1ColdVsHot_ColdExample.class);

    public static void main(String[] args) {

        // My synchronization magic.  Let's keep this thread from exiting
        // until all of our test code has executed.
        GateBasedSynchronization gate = new GateBasedSynchronization();

        // Create a "cold" observable that emits the Greek alphabet using English words.
        // We want the stream to keep repeating until we unsubscribe, but not more than
        // 49 events since we don't want to overflow our output window for illustration purposes.
        Flux coldGreekAlphabet = GreekAlphabet.greekAlphabetInEnglishFlux()
               .repeat()
            //Takes first N elements from source(if available) and then calls cancel()
            //If N is 0 then it completes as soon as first element is emitted(or if the source is empty).
                .take(0)
                .doOnCancel(() -> log.info("Request to cancel"))
                .subscribeOn(Schedulers.elastic());

        // Sleep for 2 seconds to give the observable time to run if it's going to...
        // but it's not since it's a cold observable.
        ThreadHelper.sleep(2, TimeUnit.SECONDS);

        // Setup a subscriber
        log.info("Subscribing...");
        DemoSubscriber<String> subscriber = new DemoSubscriber<>(gate);

        // Subscribe to the stream of greek letters.
        coldGreekAlphabet.subscribe(subscriber);

        // Let it run for 2 seconds, or until it's taken 49 items and opens the onComplete gate.
        log.info("Wait for subscriber to signal that it has finished.");
        gate.waitForAny("onComplete", "onError");

        System.exit(0);
    }
}
