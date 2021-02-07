package com.alna.reactor3.module3;

import com.alna.reactor3.DemoSubscriber;
import com.alna.reactor3.utility.GateBasedSynchronization;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public class CreationJustExample1 {

    public static void main(String[] args) {

        // "just" allows for the creation of Observables from single
        // values.
        Mono<Integer> just = Mono.just(42);
      final Flux<Integer> just1 = Flux.just(1, 2, 3, 4, 5, 6, 7, 8, 9, 0, 1);

      // Output the single value.
        GateBasedSynchronization gate = new GateBasedSynchronization();
      just1.subscribe(new DemoSubscriber<Integer>(gate));

        gate.waitForAny("onComplete", "onError");
        System.exit(0);
    }
}
