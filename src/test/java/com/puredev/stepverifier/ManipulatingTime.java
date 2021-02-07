package com.puredev.stepverifier;

import org.junit.jupiter.api.Test;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.time.Duration;

public class ManipulatingTime {
  @Test
  void testMonoWithLongDelay() {
    //Allows replace scheduler with VirtualTimeScheduler to be able to verify long running emissions.
    // Note: to make sure that scheduler is injected correctly Flux or Mono must be created directly in lambda with supplier.
    //Verify method returns the duration of the whole test.
    final Duration duration = StepVerifier.withVirtualTime(() -> Mono.delay(Duration.ofDays(5)))
        //Pauses the evaluation for given period in case of classic scheduler it pauses the thread in case of virtual it advances the time for given period.
        //It ignores all the events happening during this period of time.
//        .thenAwait(Duration.ofDays(5))
        .expectSubscription()
        .then(() -> System.out.println("Running ---------"))
        //Expects no event for given period.
        //Before calling this method one must first call expectSubscription because it will fail for OnSubscribe event
        .expectNoEvent(Duration.ofDays(5))
//        .thenAwait(Duration.ofDays(1))
        .expectNext(0L).expectComplete().verify();

    System.out.println(duration.toMillis());


    //Note: Virtual time is not a silver bullet the same scheduler is used for all fluxes and it can cause the running thread to hog for infinite publishers.
  }
}
