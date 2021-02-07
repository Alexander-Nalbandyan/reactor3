package com.alna.reactor3;

import com.alna.reactor3.utility.GateBasedSynchronization;
import com.alna.reactor3.utility.datasets.GreekAlphabet;
import com.alna.reactor3.utility.datasets.GreekLetterPair;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.Disposable;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Observable;
import java.util.concurrent.atomic.AtomicInteger;

public class DemoModule1ErrorHandlingExample {

    private static Logger log = LoggerFactory.getLogger(DemoModule1ErrorHandlingExample.class);

    public static void main(String[] args) {

        // My synchronization magic.  Let's keep this thread from exiting
        // until all of our test code has executed.
        GateBasedSynchronization gate = new GateBasedSynchronization();

        AtomicInteger counter =  new AtomicInteger();

        // Create an Observable and store it to a local variable.
        // This will "zip" together two streams of the same length into a single
        // stream of a composite object (GreekLetterPair).
        Flux<GreekLetterPair> zipTogether = Flux.zip(
            GreekAlphabet.greekAlphabetInGreekFlux(),
            GreekAlphabet.greekAlphabetInEnglishFlux(),
            ( greekLetter, englishLetter) -> {

//                  try {
                    // Cause an exception on the 5th event.
                    if( counter.incrementAndGet() == 5 ) {
                        throw new IllegalStateException("BOOM!");
                    }
//                  } catch (IllegalStateException e) {
//                    //If we need the original observable to continue then we need to catch error here.
//                    log.error("Error");
//                  }

                  return new GreekLetterPair(greekLetter, englishLetter);
                }
        ).doOnCancel(() -> {log.info("On dispose is called");});

//        zipTogether
                // The placement of onError operators matter.  It must be downstream of the exceptions
                // they are meant to guard against
//                .onErrorResume((e) -> Mono.just(new GreekLetterPair("κεραία", "BOOM"))) // In case of an error switches to observable returned from given function based on passed error.
//                .onErrorResume(IllegalArgumentException.class, (e) -> Flux.range(0, 20) // In case of provided error type switches to observable returned from given function based on passed error.
//                    .map(i -> new GreekLetterPair("error->" + GreekAlphabet.greekLetters[i], GreekAlphabet.greekLettersInEnglish[i])))
//                .onErrorResume((e) -> !(e instanceof IllegalStateException) && !(e instanceof IllegalArgumentException), // In case if provided predicate returns true for given error switches to observable returned from given function based on passed error.
//                               (e) -> Flux.range(0, 20)
//                                        .map(i -> new GreekLetterPair("error->" + GreekAlphabet.greekLetters[i], GreekAlphabet.greekLettersInEnglish[i])))

//                .onErrorReturn(new GreekLetterPair("κεραία", "BOOM"))  // In case of an error emits given item.
//                .onErrorReturn(IllegalStateException.class, new GreekLetterPair("κεραία", "BOOM"))  // In case of an error emits given item if the exception is of given type.
//                .onErrorReturn((e) -> e instanceof IllegalStateException, new GreekLetterPair("κεραία", "BOOM"))  // In case of an error emits given item if predicate returns true for given exception

                //Continue in case of error by skipping  emitting the value for which the error happened and calling provided consumer
                //providing error and the value for which the error did happen. In case if consumer throws an error the error is propagated further by stopping emmissions and
                // adding the original exception as suppressed exception. Also it ignores all upcoming  onErrorContinue() calls.

                //Also this operator supports predicate and error type variants.
                //Note: Not all operators support this error handling mode e.g. zip doesn't support it. To check if given operators supports
                // or not please check Error Mode support.
                  Flux.range(1, 10).map(i -> {if (i == 3) throw new IllegalArgumentException("Illegal error");
                  return new GreekLetterPair("asdasd", "werwer");})
//                      .onErrorStop() // If on error continue is used downstream then it switches to on error STOP strategy
//                      .onErrorContinue((e, value) ->  {
//                        log.info("Error: {} occurred for value: {}", e, value);
//                        throw new IllegalStateException("test");
//                      }).onErrorContinue((e, value) ->  {
//                        log.info("Error: {} occurred for value: {}", e, value);
//                        throw new IllegalStateException("test");
//                      })

            //Maps thrown error to another error using given mapper function.
            //Also it supports mapping by error type and predicate for error.
//                .onErrorMap((e) -> new RuntimeException("Error Thrown", e))

//                .onBackpressureError() //Signals onError() with Fail.withOverflowError() if demand sent from subscriber is less then emitted values.

                      //Add side effect behavior like logging in case if flux terminates with an error.
                      //Also it supports mapping by error type and predicate for error.
                      .doOnError((e) -> log.warn("Side effect in case of error: {}", e.getMessage()))
                .subscribe(new Subscriber<>() {

                  @Override
                  public void onSubscribe(Subscription s) {
                    log.info("onSubscribe");
                    s.request(Long.MAX_VALUE);
                  }

                  @Override
                  public void onNext(GreekLetterPair nextPair) {
                    log.info("onNext - ({},{})", nextPair.getGreekLetter(), nextPair.getEnglishLetter());
                  }

                  @Override
                  public void onError(Throwable e) {
                    //If no onError* operator is specified on observable then in case of error onError(e) method is called on observer.
                    log.info("onError - {}", e.getMessage());
                    log.error(e.getMessage(), e);
                    gate.openGate("onError");
                  }

                  @Override
                  public void onComplete() {
                    log.info("onComplete");
                    gate.openGate("onComplete");
                  }
                });

        // Wait for either "onComplete" or "onError" to be called.
        gate.waitForAny("onComplete", "onError");

        System.exit(0);
    }
}
