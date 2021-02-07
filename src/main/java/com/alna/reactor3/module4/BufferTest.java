package com.alna.reactor3.module4;

import com.alna.reactor3.utility.GateBasedSynchronization;
import reactor.core.publisher.Flux;
import reactor.core.scheduler.Schedulers;

import java.time.Duration;
import java.util.ArrayList;
import java.util.HashSet;

public class BufferTest {
  public static void main(String[] args) {
    //Buffers elements from source publisher into a lists of provided maximum size and emits them when they reach the limit or source completes.
//    Flux.range(1, 50).buffer(15).log().subscribe();

    //This version allows to provide the buffer container factory which is called for each buffering start.
//    Flux.just(1, 2, 4, 5, 1, 3, 4, 6, 5).buffer(5, HashSet::new).log().subscribe();

    //The second argument provides when the buffer should be created
    //If maxSize > skip: then buffers will overlap e.g. in following case it will create buffer after each 2 item and will collect there 5 elements.
    //So in result it will give [1, 2, 4, 5, 1], [4, 5, 1, 3, 4], [1, 3, 4, 6, 5], [4, 6, 5]
//    Flux.just(1, 2, 4, 5, 1, 3, 4, 6, 5).buffer(5, 2).log().subscribe();

    //If maxSize < skip: then buffers will drop elements after reaching max size and before skip
    // e.g. in following case it will create buffer after each 5 item and will and will drop remaining 3 elements each time.
//    Flux.just(1, 2, 4, 5, 1, 3, 4, 6, 5).buffer(2, 5).log().subscribe();

    //If maxSize = skip: then it works like with single argument.
//    Flux.just(1, 2, 4, 5, 1, 3, 4, 6, 5).buffer(2, 2).log().subscribe();

    //This version allows to provide container factory.
//    Flux.just(1, 2, 4, 5, 1, 3, 4, 6, 5).buffer(5, 5, HashSet::new).log().subscribe();

    //Buffers elements into a list on each onNext() signal from given publisher.
//    Flux.interval(Duration.ofMillis(100)).take(20).buffer(Flux.interval(Duration.ofSeconds(1))).log().subscribe();

    //This version allows to provide container factory.
//    Flux.interval(Duration.ofMillis(100)).take(20).buffer(Flux.interval(Duration.ofSeconds(1)), ArrayList::new).log().subscribe();

    //Is equivalent to  .buffer(Flux.interval(Duration.ofSeconds(1))) and collects elements into multiple lists which are emitted after each given period.
//    Flux.interval(Duration.ofMillis(100)).take(20).buffer(Duration.ofSeconds(1)).log().subscribe();

    //This version allows to provide Scheduler on which the given period will be measured.
//    Flux.interval(Duration.ofMillis(100)).take(20).buffer(Duration.ofSeconds(1), Schedulers.parallel()).log().subscribe();

    //Collects elems into multiple lists on every given second duration. The buffer will be emitted on every first argument duration.
    //if (bufferSpan < openBuffEvery) then dropping buffers.
//    Flux.interval(Duration.ofMillis(100)).take(20).buffer(Duration.ofSeconds(1), Duration.ofSeconds(2)).log().subscribe();

    //if (bufferSpan > openBuffEvery) then overlapping buffers. it collects elements into  multiple buffers at the same time.
//    Flux.interval(Duration.ofMillis(100)).take(30).buffer(Duration.ofSeconds(2), Duration.ofSeconds(1)).log().subscribe();

    //This version allows to provide Scheduler on which the given period will be measured.
    Flux.interval(Duration.ofMillis(100)).take(30).buffer(Duration.ofSeconds(2), Duration.ofSeconds(1), Schedulers.parallel()).log().subscribe();

    new GateBasedSynchronization().waitForAny("onComplete");
  }
}
