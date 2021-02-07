package com.alna.reactor3.module4;

import com.alna.reactor3.DemoSubscriber;
import com.alna.reactor3.utility.GateBasedSynchronization;
import com.google.common.util.concurrent.Uninterruptibles;
import org.reactivestreams.Publisher;
import reactor.core.publisher.Flux;
import reactor.core.scheduler.Schedulers;

import java.time.Duration;

public class TimeBasedOperators {
  public static void main(String[] args) {
    //Signals onError with TimeoutException if no item is emitted within given timeout.
    //The version with fallback publisher switches to given publisher in case of timeout(If publisher is null it signals TimeoutException error).
    //The version with publisher for firstTimeout signals TimeoutException if no item is published from original source before first item is emitted from given source.
    // The version with publisher for firstTimeout and nextTimeoutFactory signals timeout error either when no item  is emitted before first timeout
    //  or when no item is emmitted for each next item before corresponding source emits an item.
    //  It has also fallback version to fallback to given source in case of timout error.
//    GateBasedSynchronization gate = new GateBasedSynchronization();
//    final Publisher<?> x = Flux.interval(Duration.ofSeconds(2)).take(3);
//    Flux.create(emitter -> {
//      Uninterruptibles.sleepUninterruptibly(Duration.ofSeconds(1));
//      for (int i = 1; i < 5; i++) {
//        emitter.next("item: "+ i);
//      }
//      Uninterruptibles.sleepUninterruptibly(Duration.ofSeconds(3));
//      emitter.next("item: " + 10);
//
//      Uninterruptibles.sleepUninterruptibly(Duration.ofSeconds(1));
//      emitter.complete();
//    })
//        .timeout(Duration.ofSeconds(1), x)
//        .timeout(x)
//        .timeout(x,k -> Flux.interval(Duration.ofSeconds(2)))
//    .subscribe(new DemoSubscriber<>(gate));


    //Emits items last emitted from source before given polling window, if there are multiple items emitted in given period only the last one is emitted.
    //If there are no items emitted in given period then it doesn't emit any item.
    GateBasedSynchronization gate = new GateBasedSynchronization();
//    final Publisher<?> x = Flux.interval(Duration.ofSeconds(2)).take(3);
    Flux.create(emitter -> {
      Uninterruptibles.sleepUninterruptibly(Duration.ofSeconds(1));
      for (int i = 1; i < 5; i++) {
        emitter.next("item: " + i);
      }
      Uninterruptibles.sleepUninterruptibly(Duration.ofSeconds(3));
      emitter.next("item: " + 10);

      Uninterruptibles.sleepUninterruptibly(Duration.ofSeconds(4));

      emitter.next("item: " + 11);
      emitter.complete();
    })
        .subscribeOn(Schedulers.elastic())
      .sample(Duration.ofSeconds(2))
      .subscribe(new DemoSubscriber<>(gate));

    gate.waitForAny("onError", "onComplete");
  }
}
