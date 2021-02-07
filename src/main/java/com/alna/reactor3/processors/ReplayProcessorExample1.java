package com.alna.reactor3.processors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.Disposable;
import reactor.core.publisher.ReplayProcessor;

public class ReplayProcessorExample1 {

    private final static Logger log = LoggerFactory.getLogger(ReplayProcessorExample1.class);

    public static void main(String[] args) {

        // Create our test ReplayProcessor with an initial state of "omega"
        ReplayProcessor<String> replayProcessor = ReplayProcessor.cacheLastOrDefault("omega");

        // Subscribe and notice that we will have an initial state - the last
        // event that was emitted into the ReplayProcessor.
        Disposable subscription1 = replayProcessor.subscribe(
                nextLetter -> log.info("onNext - {}", nextLetter));
        subscription1.dispose();

        // Emit a few more letters
        replayProcessor.onNext("alpha");
        replayProcessor.onNext("beta");
        replayProcessor.onNext("gamma");

        // See what we get for the current event
        Disposable subscription2 = replayProcessor.subscribe(
                nextLetter -> log.info("onNext - {}", nextLetter));
        subscription2.dispose();

        System.exit(0);
    }
}