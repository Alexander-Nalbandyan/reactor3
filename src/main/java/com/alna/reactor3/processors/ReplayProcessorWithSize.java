package com.alna.reactor3.processors;


import com.alna.reactor3.DemoSubscriber;
import com.alna.reactor3.utility.ThreadHelper;
import com.alna.reactor3.utility.datasets.FibonacciSequence;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Flux;
import reactor.core.publisher.ReplayProcessor;
import reactor.core.scheduler.Schedulers;

import java.util.concurrent.TimeUnit;

public class ReplayProcessorWithSize {

    private final static Logger log = LoggerFactory.getLogger(ReplayProcessorWithSize.class);

    public static void main(String[] args) {

        // Create our ReplayProcessor.  We create it with a limit of 20
        // which makes it a rotating window of 20 items.
        ReplayProcessor<Object> replayProcessor = ReplayProcessor.create(25);

        // Create a Fibonacci Sequence that is longer than our
        // ReplayProcessor's capacity
        Flux<Long> fibonacciSequence = FibonacciSequence
                .create(30)
                .subscribeOn(Schedulers.parallel());

        // Subscribe to the number sequence and emit
        // onNext messages into our replay subject.
        fibonacciSequence.subscribe(replayProcessor::onNext);

        // Pause to allow the sequence to run for a moment
        ThreadHelper.sleep(1, TimeUnit.SECONDS);

        // Attach to the ReplayProcessor.  We should get at least
        // 20 numbers.
        replayProcessor
                .subscribeOn(Schedulers.parallel())
                .subscribe(new DemoSubscriber<>("sub1"));

        // Give it a second...
        ThreadHelper.sleep(1, TimeUnit.SECONDS);

        log.info("----------------------------------------------------");

        // Attach a second observer and see that we get 20
        // numbers.
        replayProcessor
                .subscribeOn(Schedulers.parallel())
                .subscribe(new DemoSubscriber<>("sub2"));

        ThreadHelper.sleep(2, TimeUnit.SECONDS);

        System.exit(0);
    }
}
