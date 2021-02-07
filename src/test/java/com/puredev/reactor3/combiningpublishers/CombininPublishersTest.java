package com.puredev.reactor3.combiningpublishers;

import com.alna.reactor3.utility.GateBasedSynchronization;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.time.Duration;

public class CombininPublishersTest {
  Flux<Integer> evenNumbers = Flux
      .range(1, 5)
      .filter(x -> x % 2 == 0); // i.e. 2, 4

  Flux<Integer> oddNumbers = Flux
      .range(1, 5)
      .filter(x -> x % 2 > 0);  // ie. 1, 3, 5

  @Test
  public void givenFluxes_whenConcat_thenConcat() {
    //Concat subscribes and consumes events sequentially.
    final Flux<Integer> concat = Flux.concat(evenNumbers, oddNumbers);

    StepVerifier.create(concat)
        .expectNext(2)
        .expectNext(4)
        .expectNext(1)
        .expectNext(3)
        .expectNext(5)
        .expectComplete()
    .verify();

    //Any error will stop and go downstream.
    final RuntimeException error = new RuntimeException("Interrupted ----->");
    final Flux<Integer> concat2 = Flux.concat(evenNumbers, Mono.error(error), oddNumbers);
    StepVerifier.create(concat2)
        .expectNext(2)
        .expectNext(4)
        .expectError(RuntimeException.class)
        .verify();

  }

  @Test
  public void givenFluxes_whenConcatWith_thenConcat() {
    //Instead of static method we can also use non static to concat current publisher with the given one sequentially no interleave.
    final Flux<Integer> concat = evenNumbers.concatWith(oddNumbers);

    StepVerifier.create(concat)
        .expectNext(2)
        .expectNext(4)
        .expectNext(1)
        .expectNext(3)
        .expectNext(5)
        .expectComplete()
        .verify();

    //Any error will stop and go downstream.
    final RuntimeException error = new RuntimeException("Interrupted ----->");
    final Flux<Integer> concat2 = evenNumbers.concatWith(Mono.error(error));
    StepVerifier.create(concat2)
        .expectNext(2)
        .expectNext(4)
        .expectError(RuntimeException.class)
        .verify();

  }

  @Test
  public void givenFluxes_whenCombineLatest_thenCombineLatest() {
    //Concat subscribes and consumes events sequentially.
    final Flux<Long> per10 = Flux.interval(Duration.ofMillis(10)).take(4);
    final Flux<Long> per20 = Flux.interval(Duration.ofMillis(20)).take(3);

    final Flux<String> combineLatest = Flux.combineLatest(per10, per20, (even, odd)  -> even + " - " + odd).log();


//    StepVerifier.withVirtualTime(() -> combineLatest)
//        .expectSubscription()
////        .expectNoEvent()ext("1 - 0")
//        .expectNext("2 - 0")
//        .expectNext(1)
//        .expectNext(3)
//        .expectNext(5)
//        .expectComplete()
//        .verify();
//
//    //Any error will stop and go downstream.
//    final RuntimeException error = new RuntimeException("Interrupted ----->");
//    final Flux<Integer> concat2 = Flux.concat(evenNumbers, Mono.error(error), oddNumbers);
//    StepVerifier.create(concat2)
//        .expectNext(2)
//        .expectNext(4)
//        .expectError(RuntimeException.class)
//        .verify();

  }


}
