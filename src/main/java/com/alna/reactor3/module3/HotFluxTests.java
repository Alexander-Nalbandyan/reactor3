package com.alna.reactor3.module3;

import com.alna.reactor3.DemoSubscriber;
import com.alna.reactor3.utility.GateBasedSynchronization;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.DirectProcessor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
public class HotFluxTests {
  public static void main(String[] args) {
    //just() call captures data on initialization and then uses that data for each subscriber.
    final Mono<String> just = Mono.just(getData());

    //Creates mono provider which will provide target mono for each subscriber and getData() will be called each time.
    final Mono<String> defer = Mono.defer(() -> Mono.just(getData()));
    //Mono.deferWithContext(); is same as defer() but it also receives the current context.

    GateBasedSynchronization gate = new GateBasedSynchronization();
//    defer.subscribe(new DemoSubscriber<>(gate));
//    defer.subscribe(new DemoSubscriber<>(gate));
//    defer.subscribe(new DemoSubscriber<>(gate));
//    defer.subscribe(new DemoSubscriber<>(gate));

    final DirectProcessor<String> hotSource = DirectProcessor.create();
    final Flux<String> hotFlux = hotSource.map(String::toUpperCase).doOnNext((s) -> log.info("Emitting -> {}", s));
    hotSource.onNext("white");
    hotFlux.subscribe(new DemoSubscriber<>("sub1"));
    hotSource.onNext("green");
    hotSource.onNext("yellow");

    hotFlux.subscribe(new DemoSubscriber<>("sub2"));
    hotSource.onNext("blue");
    hotSource.onNext("red");
  }

  private static String getData() {
    log.info("GetData called");
    return "Hello there";
  }
}
