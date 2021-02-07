package com.alna.reactor3.module4;


import com.alna.reactor3.DemoSubscriber;
import com.alna.reactor3.utility.GateBasedSynchronization;
import com.alna.reactor3.utility.datasets.GreekAlphabet;
import com.alna.reactor3.utility.datasets.GreekLetterPair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Flux;
import reactor.core.scheduler.Scheduler;
import reactor.core.scheduler.Schedulers;

import java.util.Observable;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

public class FlatMapExample1 {

    private final static Logger log = LoggerFactory.getLogger(FlatMapExample1.class);

    public static void main(String[] args) {

        GateBasedSynchronization gate = new GateBasedSynchronization();

        // Create the first thread pool
        AtomicInteger threadPool1Counter = new AtomicInteger();
        Executor threadPool1 = Executors.newFixedThreadPool(20, runnable -> {
            Thread returnThread = new Thread(runnable, "Pool 1 Thread " + threadPool1Counter.getAndIncrement());
            return returnThread;
        });
        Scheduler scheduler1 = Schedulers.fromExecutor(threadPool1);

        // Create a second thread pool
        AtomicInteger threadPool2Counter = new AtomicInteger();
        Executor threadPool2 = Executors.newFixedThreadPool(20, runnable -> {
            Thread returnThread = new Thread(runnable, "Pool 2 Thread " + threadPool2Counter.getAndIncrement());
            return returnThread;
        });
        Scheduler scheduler2 = Schedulers.fromExecutor(threadPool2);


        // flatMap is used to process a single event into an Observable of zero or
        // many events.  In this case, we will take a single Greek letter,
        // find it's English counterpart and pair them together.  But first we emit
        // strings that represent the Greek and English strings.
        Flux<Object> greekLetterPairs = GreekAlphabet.greekAlphabetInGreekFlux()
                .flatMap((String greekLetter) -> {

                    // Find the offset into the array of this greek character.
                    //All this inner flux emissions are done in scheduler2
                    int offset = GreekAlphabet.findGreekLetterOffset(greekLetter, GreekAlphabet.greekLetters);

                    return Flux.just(
                            greekLetter,
                            GreekAlphabet.greekLettersInEnglish[offset],
                            new GreekLetterPair(greekLetter, GreekAlphabet.greekLettersInEnglish[offset])

                    )
                    .log()
                    .subscribeOn(scheduler2);

                }, 3)
                .publishOn(scheduler1);

        //All the flattened publisher operations are done in scheduler1 except initial subscription which is done in the main thread
      // because the subscribe method was called from the main thread.
        greekLetterPairs
                .log()
                .subscribe();

        gate.waitForAny("onComplete", "onError");

        System.exit(0);
    }
}
