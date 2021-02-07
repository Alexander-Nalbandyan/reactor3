package com.alna.reactor3.processors;

import com.alna.reactor3.DemoSubscriber;
import com.alna.reactor3.utility.GateBasedSynchronization;
import com.google.common.util.concurrent.Uninterruptibles;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.ReplayProcessor;
import reactor.core.scheduler.Schedulers;

import java.time.Duration;

@Slf4j
public class ReplayProcessorTest {
  public static void main(String[] args) {
    final GateBasedSynchronization gate = new GateBasedSynchronization();

    //Replays last n emissions to late subscribers by caching those n elements all earlier emissions are dropped.
//    final ReplayProcessor<String> replayProcessor = ReplayProcessor.create(2);
    //If unbounded parameter is true then the first argument is just ignored and all emitted elements are replayed with each subscriber.
//    final ReplayProcessor<String> replayProcessor = ReplayProcessor.create(2, true);
    //Replays all elements emitted
//    final ReplayProcessor<String> replayProcessor = ReplayProcessor.create();

    //Replays only the last element emitted.
//    final ReplayProcessor<String> replayProcessor = ReplayProcessor.cacheLast();

    //Again replays only last element emitted and if subscriber comes before any emission then it gets the default value.
//    final ReplayProcessor<String> replayProcessor = ReplayProcessor.cacheLastOrDefault("Hello");
    //Replays only elements whoes age is less then given time window.
//    final ReplayProcessor<String> replayProcessor = ReplayProcessor.createTimeout(Duration.ofSeconds(5));
    //This  version allows to provide scheduler to use for age calculation.
//    final ReplayProcessor<String> replayProcessor = ReplayProcessor.createTimeout(Duration.ofSeconds(2), Schedulers.elastic());
    //This version keeps elements based on age and maximum given number of items.
    final ReplayProcessor<String> replayProcessor = ReplayProcessor.createSizeAndTimeout(2, Duration.ofSeconds(2));

    log.info("Emitting first item");
    replayProcessor.onNext("test1");
//    replayProcessor.onNext("test2");
//    replayProcessor.onNext("test3");

//    Uninterruptibles.sleepUninterruptibly(Duration.ofSeconds(2));
//    log.info("Emitting second item");
//    replayProcessor.onNext("test2");
    Uninterruptibles.sleepUninterruptibly(Duration.ofSeconds(10));
    log.info("Emitting complete");
    replayProcessor.onComplete();

    Uninterruptibles.sleepUninterruptibly(Duration.ofSeconds(1));
//    replayProcessor.onNext("test3");
//    Uninterruptibles.sleepUninterruptibly(Duration.ofSeconds(1));

//    replayProcessor.subscribe(new DemoSubscriber<>("sub1"));
    replayProcessor.subscribe((str) -> System.out.println(str));

//    replayProcessor.onNext("test4");
    //Once the processor is terminated all items will be replayed from buffer regardless of their age.
//    replayProcessor.subscribe(new DemoSubscriber<>("sub2"));
//    replayProcessor.subscribe(new DemoSubscriber<>("sub3"));

    //After subscription elements emitted after are delivered to subscribers without delay. The buffer will only affect new subscribers.
//        replayProcessor.onNext("test4");
//        replayProcessor.onNext("test5");
//        replayProcessor.onNext("test6");
//

    gate.waitForAny("onComplete");
  }
}
