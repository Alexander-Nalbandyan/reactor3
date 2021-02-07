package com.alna.reactor3.reallifeexamples;

import com.alna.reactor3.DemoSubscriber;
import com.alna.reactor3.utility.GateBasedSynchronization;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.tuple.Pair;
import reactor.core.publisher.Flux;
import reactor.core.scheduler.Schedulers;


@Slf4j
public class ParallelFluxTest {
  public static void main(String[] args) {
    GateBasedSynchronization gate = new GateBasedSynchronization();

    Flux.range(1, 10)
        //When calling parallel the stream is divided into rails and elements are distributed in round robin fasion.
        //parallel call itself doesn't run in parallel we should also call runOn() to emit events in parallel.
        .parallel(2)
        .runOn(Schedulers.parallel())
        //We can apply operation to each rail using following operator where for each flux we have field key which is the rail number 0 based.
//        .transformGroups((groupedFlux) -> groupedFlux.map(i -> "rail: " + groupedFlux.key() + " -> " + i))

        //Exposes each rail with separate GroupedFlux which can be subscribed only once.
        //Note that cancelling only one rail may result in undefined behavior.
        .groups()

        //If we want to run the rest of operators sequentially and switch back to normal flux we need to call sequential.
        //Note: when calling subscribe() with subscriber then sequential is implied implicitly.
//        .sequential()
//        .map(i -> Pair.of(i, i * i))
        //If we subscribe to it with lambda subscriber then all of them run in parallel
        .subscribe((i) -> {
          i.subscribe(new DemoSubscriber<>("Rail - " + i.key()));
        });
        //Otherwise with subscriber the rails are merged.
//        .subscribe(new DemoSubscriber<>(gate));

    gate.waitForAny("onComplete");
  }
}
