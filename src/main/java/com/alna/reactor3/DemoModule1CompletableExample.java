package com.alna.reactor3;


import com.alna.reactor3.utility.GateBasedSynchronization;
import com.alna.reactor3.utility.datasets.GreekAlphabet;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.Disposable;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Observable;

// ---------------------------------------------------------------------------------------------------------------
// This demonstration is intended to illustrate...
// 1.  Show have to makeObservable a Completable type of Observable.
// ---------------------------------------------------------------------------------------------------------------
public class DemoModule1CompletableExample {

    private static Logger log = LoggerFactory.getLogger(DemoModule1CompletableExample.class);

    public static void main(String[] args) {

        // My synchronization magic.  Let's keep this thread from exiting
        // until all of our test code has executed.
        GateBasedSynchronization gate = new GateBasedSynchronization();

        // Create an Observable<String> that contains the 24 greek letters.
        Mono<String> targetObservable = Flux.fromArray(GreekAlphabet.greekLetters)

                // We want to see that the list really does call onNext 24 times
                // for 24 letters.  We can use one of the "peek" functions to log
                // on each onNext call to the subscriber.
                .doOnNext( nextLetter -> {
                    log.info("doOnNext - {}", nextLetter);
                })

            // Tell the Observable that we only care that it did the work...we don't
            // care about the stream of elements.  This gives us an Observable of
            // the type "Completable".
            .ignoreElements();


        // Subscribe to the "Completable" Observable.
        // Note the slightly different method names on the MaybeObserver interface.
        targetObservable.subscribe(new Subscriber<>() {

            // If the Observable completes without error, then onComplete
            // will be called.  Once onComplete has been called, you are guaranteed
            // to not get an onError event.
            @Override
            public void onComplete() {
                log.info( "onComplete" );
                gate.openGate("onComplete");
            }

            @Override
            public void onSubscribe(Subscription s) {
              s.request(1);
              log.info("Subscribe");
            }

            @Override
            public void onNext(String o) {
              //Must not be called.
              log.info("OnNext: {}", o);
            }

          // If an exception is thrown while processing the
            // observable, then onError is called.  onComplete will not be
            // called if onError is called.
            @Override
            public void onError(Throwable e) {

                // Send the error message to the log.
                log.error("onError - {}" , e.getMessage());

                // Open the gate for "onError" so that the main
                // thread will be allowed to continue.
                gate.openGate("onError");
            }
        });

        // Wait for either "onComplete" or "onError" to be called.
        gate.waitForAny("onComplete", "onError");

        System.exit(0);
    }
}
