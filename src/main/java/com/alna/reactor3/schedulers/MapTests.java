package com.alna.reactor3.schedulers;

import com.alna.reactor3.DemoSubscriber;
import com.alna.reactor3.utility.GateBasedSynchronization;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;

import java.util.Collections;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static java.util.function.Function.identity;

@Slf4j
public class MapTests {

  public static void main(String[] args) {
    GateBasedSynchronization gate = new GateBasedSynchronization();
    //Applies given function to each value emitted from original source
    //Note: It doesn't run map function in parallel but one at a time even if we provide specific scheduler to run on.
//    Flux.range(1, 20).map(i -> i + 20).log().subscribe();

    //Instead flat map runs each publisher in parallel and on merge there is no guarantee on ordering of publishers or emitted elements.
//    Flux.range(1, 20).flatMap(i -> Flux.range(i, 10)).log().subscribe();


    //This version controls how many publishers can be subscribed and flattened in parallel, it also should the subscriber request()
    // initial value from this operator to upstream so in following example it will show request() value for Flux.range().
//    Flux.range(1, 20).log().flatMap(i -> Flux.range(i, 10), 5).subscribe();

    //This version allows to both control concurrency as the above version and also configure the prefetch size.
//    Flux.range(1, 20).flatMap(i -> Flux.range(i, 10).log(), 5, 10).subscribe();

    //This version allows to define mapper for onError() and onComplete() signals so they are mapped to corresponding publishers which are
    // merged into resulting flattened publisher.
//    Flux.error(new Throwable("eee")).flatMap((i) -> Flux.just(i).log(), (e) -> Flux.just(9999), () -> Flux.just(1111)).log().subscribe();


    //To maintain publishers and elements ordering we can use flatMapSequential() but note that it will queue up all fast publisher
    // until elements are emitted in the correct order.
//    Flux.range(1, 20).flatMapSequential(i -> Flux.range(i, 10)).log().subscribe();

    //The concurrency level controls how many merged publishers can happen in parallel even though the resulting values will be queued to keep the ordering.
//    Flux.range(1, 20).flatMapSequential(i -> Flux.range(i, 10).log(), 5).subscribe();

    //There is also version with prefetch size which control request() size for each publisher
//    Flux.range(1, 20).flatMapSequential(i -> Flux.range(i, 10).log(), 5, 3).subscribe();

    // This version delays error until the whole sequence is processed.
    Flux.range(1, 20).flatMapSequentialDelayError(i -> {
      if (i == 5) {
        return Flux.error(new Throwable("asdad"));
      }
      return Flux.range(i, 10);
    }, 5, 3).log().subscribe();

    //This version will delay error until all publishers are flattened.
//    Flux.range(1, 20).flatMapDelayError(i -> {
//      if (i == 5) {
//        return Flux.error(new Throwable("asdad"));
//      }
//      return Flux.range(i, 10);
//    }, 5, 3).log().subscribe();

    //Maps element emitted by initial flux into iterables then flatten all iterable elements in same order into single Flux.
//    Flux.range(1, 20).flatMapIterable(i -> IntStream.range(1, i).boxed().collect(Collectors.toList())).log().subscribe();

    //According to the document the prefetch argument controls the number of elements to prefetch from initial source to convert into Iterable.
//    Flux.range(1, 20).doOnRequest(s -> log.info("Request: {}", s)).log()
//        .flatMapIterable(i -> IntStream.range(1, i).boxed().collect(Collectors.toList()), 5).subscribe();



    gate.waitForAny("onError", "onComplete");
  }
}
