package com.alna.reactor3.module3;


import com.alna.reactor3.DemoSubscriber;
import com.alna.reactor3.utility.GateBasedSynchronization;
import com.alna.reactor3.utility.ThreadHelper;
import com.alna.reactor3.utility.datasets.GreekAlphabet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Flux;

import java.util.concurrent.TimeUnit;

public class DemoModule1ColdVsHot_HotExample {

    private static Logger log = LoggerFactory.getLogger(DemoModule1ColdVsHot_HotExample.class);

    public static void main(String[] args) {

        // Synchronization magic.
        GateBasedSynchronization gate = new GateBasedSynchronization();

        // Create a "hot" observable that emits greek letters at a furious pace.
        // We only take the first 49 events to keep things understandable.
        Flux<String> hotGreekAlphabet =
                GreekAlphabet.greekAlphabetInEnglishHotFlux(true)
                        .take(49);

        // Sleep for 2 seconds to give the hot observable a chance to run.
        ThreadHelper.sleep(2, TimeUnit.SECONDS);

        // Setup a subscriber
        DemoSubscriber<String> subscriber = new DemoSubscriber<>(gate);

        // Subscribe to the hot stream of greek letters.
        log.info("Subscribing now...");
        hotGreekAlphabet.subscribe(subscriber);

        // Wait for 2 seconds, or until one of the gates is opened.
        log.info("Wait for subscriber to signal that it is finished.");
        gate.waitForAny("onComplete", "onError");

        System.exit(0);
    }
}