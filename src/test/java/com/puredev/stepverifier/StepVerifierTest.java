package com.puredev.stepverifier;

import com.google.common.collect.ImmutableList;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;
import reactor.test.StepVerifierOptions;

import java.time.Duration;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class StepVerifierTest {
  Flux<String> contactWithError(Flux<String> source) {
    return source.concatWith(Mono.error(new IllegalArgumentException("boom")));
  }

  @Test
  void checkSignals() {
    final Flux<String> source = Flux.just("s1", "s2", "s3");

    //What does StepVerifier do
    //After verify is called it subscribes to the given flux or mono and checks given expectation until the it is terminated.
    //Note: there is no timeout by default for all StepVerifiers verify() methods.
    //Alternatively we can use verify(Duration) methods.
    StepVerifier.setDefaultTimeout(Duration.ofMillis(1000));

    //To prepare StepVerifier we need to call create() which prepares a builder and then at the end call verify()
//    StepVerifier.create(contactWithError(source))
//    StepVerifier.create(source)
        //verifies that next received element is given value.
//        .expectNext("s1")
//        .expectNext("s2")
//        .expectNext("s3")
        //Verifies that given elements are signaled in order.
//        .expectNext("s1", "s2", "s3")
        //Verifies that given number of elements are emitted after previous expectation or from subscription.
//        .expectNextCount(2)
        //Verifies that on Error is signalled.
//        .expectError()

        //Verifies on Error signal with given message.
//        .expectErrorMessage("boom")

        //Verifies that onError signal with given error class is emitted.
        // Note: only one of expect Error verifiers can be used because they return StepVerifier instead of step.
//        .expectError(IllegalArgumentException.class)

        //Verifies that onError is signalled and it matches given predicate.
//        .expectErrorMatches((e) -> e instanceof IllegalArgumentException && e.getMessage().equals("boom"))

        //Verifies onError signal and checks assertions given in the consumer.
//        .expectErrorSatisfies((e) -> {
//          assertEquals("boom", e.getMessage());
//          assertTrue(e instanceof IllegalArgumentException);
//        })
//
        //Verifies onComplete signal next.
        //Note: verify can only be called either after error or complete expectation so after calling expectComplete or Error the api is changed.
//        .expectComplete()
//        .verify();


//    StepVerifier.create(Mono.error(new RuntimeException("test")))
        //Expects and consumes subscription.
//        .consumeSubscriptionWith(sub -> System.out.println("subcription = " + sub.hashCode()))

        //Expects and consumes an error.
//        .consumeErrorWith(e -> {e.printStackTrace();})
        //This allows to do more advanced assertions in the consumer and propagate assertion errors.
        //The provided consumer will receive next emitted value.
//        .consumeNextWith(s -> {
//          assertEquals("s1", s);
//        })
//        .expectNext("s1")
        //records all emitted value into provided collection.
        //Note: In order to record elements we need to proceed with next elements by expectNext() or some other proceeding calls..
//        .recordWith(ArrayList::new)
//        .expectNextCount(3)
        //Allows to do assertion for previously recorded values.
        //If no recording is done previously then it throws an error.
//        .consumeRecordedWith(list -> assertEquals(ImmutableList.of("s1", "s2", "s3"), list))
//      .expectNext("s1")
//      .expectComplete()
//      .verify();


//    StepVerifier.create(Flux.range(1, 10)) // 1 2 3 4 5 6 7 8 9 10
//        Consumes all emission until given predicate is true.
//        .thenConsumeWhile(i -> i < 5) // 1 2 3 4
//        .thenAwait()
//        .expectNext(5)
//        .expectNextCount(5) // 6 7 8 9 10
//        .verifyComplete();

    //Identifying which case caused the failure of test.
//    StepVerifier.create(Flux.range(1, 10))
//        .expectNext(5)
        //Given description is printed if previous expectation is not satisfied.
//        .as("Expected 5 signal")
        //as(String) can not be called on terminal even expectation or after verify() methods.
//        .expectComplete()
//        .verify();

    //Giving description to the whole scenario for flux or mono.
    StepVerifier.create(Flux.range(1, 10),
                        StepVerifierOptions.create().scenarioName("Test Scenario"))
//    .expectNext(1)
        //Note: The description only works for StepVerifier methods not for manual assertion and for thrown errors.
    .consumeNextWith(i -> {
      assertEquals(i, 5);
    })
        .expectNextCount(9)
    .verifyComplete();


  }
}
