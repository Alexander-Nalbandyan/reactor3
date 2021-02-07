package com.alna.reactor3;

import com.alna.reactor3.utility.GateBasedSynchronization;
import com.alna.reactor3.utility.datasets.GreekAlphabet;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Observable;


public class DemoModule1CardinalityExample {

    private final static Logger log = LoggerFactory.getLogger(DemoModule1CardinalityExample.class);

    public static void main(String[] args) {

        // My synchronization magic.  Let's keep this thread from exiting
        // until all of our test code has executed.
        GateBasedSynchronization gate = new GateBasedSynchronization();

        // Single result stream
        Mono<String> firstGreekLetterOnly = Flux.fromArray(GreekAlphabet.greekLetters).next()
            .or(Mono.just("?"));

        // Maybe result stream - First letter (alpha)
        Mono<String> maybeGreekLetterOneEvent = Flux.fromArray(GreekAlphabet.greekLetters)
                .next()
                .or(Mono.just("?"))
                .filter( nextLetter -> nextLetter.equals("\u03b1"));

        // Maybe result stream - No letters
        Mono<String> maybeGreekLetterNoEvents = Flux.fromArray(GreekAlphabet.greekLetters)
                .next()
                .or(Mono.just("?"))
                .filter( nextLetter -> !nextLetter.equals("\u03b1"));

        // Completable result stream - No output to subscriber, only success or failure.
        Mono completableMono = Flux.fromArray(GreekAlphabet.greekLetters)
                .ignoreElements();

        // --------------------------------------------------------------------------------------
        // See what happens with the events when we subscribe to each of them...
        // --------------------------------------------------------------------------------------
        log.info( "Single -----------------------------------------------------");
        firstGreekLetterOnly.subscribe(new DemoSubscriber<>(gate));

        gate.waitForAny("onError", "onComplete");

        log.info( "------------------------------------------------------------");
        log.info( "" );
        log.info( "Maybe (1 result) -------------------------------------------");

        gate.resetAll();

        maybeGreekLetterOneEvent.subscribe(new DemoSubscriber<>(gate));

        gate.waitForAny("onError", "onComplete");

        log.info( "------------------------------------------------------------");
        log.info( "" );
        log.info( "Maybe (0 results) -------------------------------------------");
        gate.resetAll();

        maybeGreekLetterNoEvents.subscribe(new DemoSubscriber<>(gate));
        gate.waitForAny("onError", "onComplete");

        log.info( "------------------------------------------------------------");
        log.info( "" );
        log.info( "Completable ------------------------------------------------");
        gate.resetAll();

        completableMono.subscribe(new DemoSubscriber(gate));

        gate.waitForAny("onError", "onComplete");
    }
}
