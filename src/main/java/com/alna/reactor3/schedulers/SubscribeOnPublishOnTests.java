package com.alna.reactor3.schedulers;

import com.alna.reactor3.DemoSubscriber;
import com.alna.reactor3.utility.GateBasedSynchronization;
import lombok.extern.slf4j.Slf4j;
import org.reactivestreams.Subscription;
import reactor.core.publisher.BaseSubscriber;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Scheduler;
import reactor.core.scheduler.Schedulers;

@Slf4j
public class SubscribeOnPublishOnTests {
  public static void main(String[] args) throws InterruptedException {
    GateBasedSynchronization gate = new GateBasedSynchronization();


    //By default all operators are executing on the same thread where the previous operator was executed.
    //The top most operator(or the source) is executed in the same thread where the subscribe call was made.
    // Thus in the following example the mono creation was done in main thread but as subscribe call is made in new Thread-0
    // the map, doOnSubscribe, onNext all operators are executed in that new thread-0.
    final Mono<String> mono = Mono.just("hello ");

//    Thread t = new Thread(() -> mono
//        .log()
//        .subscribeOn(Schedulers.single())
//        .doOnSubscribe(sub -> log.info("DoOnSubscribe"))
////        .map(msg -> msg + "thread ")
//        .block());
////        .subscribe(new DemoSubscriber<>(gate)));
//    t.start();
//    t.join();

    //Reactor provides 2 ways of changing execution context(or the thread where the operations must be executed.)
    //subscribeOn() and publishOn() both supports argument for Scheduler.
    //Remember nothing happens until you subscribe to the publisher and send request() signal upto the original publisher this is when the data starts to flow in.
    //When subscribing to the publisher chain intermediate Subscriber objects gets created for each operator which is hidden from developer
    // but it is were the actual code gets done.
//    Flux.just("1231").publishOn();




    //The publishOn() effects execution of all operators put after it.
    //So in following case the first map operator will use the create manual thread -0 but the second map() will run on parralel sheduler thread
    // because it stands after publishOn() also subscription itself will be done in thread 0 but onNext() calls will be executed on parralel scheduler
    // as according to spec onNext() calls happen in sequence.
//    Scheduler s = Schedulers.newParallel("parallel-scheduler", 4);
//    final Flux<String> flux = Flux
//        .range(1, 2)
//        .log()
//        .map(i -> {
//          log.info("Map 1");
//          return 10 + i;
//        })
//        .publishOn(s)
//        .map(i -> {
//          log.info("Map 2");
//          return "value " + i;
//        });
//
//    new Thread(() -> flux.subscribe(log::info)).start();


    //subscribeOn() affects the backward subscription chain creation process so no matter were you put subscribeOn() it will effect
    // the source emissions(including onError and onComplete signals) context and publishOn() can override the context for subsequent operators.
    // Note: Only first call to subscribeOn() is taken into account all other calls are just ignored.
    //So in the next example only first subscribeOn() with single scheduler is taking into account and source emissions of 1 and 2 are
    //done in that single scheduler after that publishOn() overrides the scheduler and all subsequent operators are executed in elastic scheduler.
    Scheduler s = Schedulers.newParallel("parallel-scheduler", 4);
    final Flux<String> flux = Flux
        .range(1, 2)
        .log()
        .publishOn(Schedulers.elastic())
        .subscribeOn(Schedulers.single())
        .map(i -> 10 + i)
        .subscribeOn(s)
        .map(i -> {
          log.info("Map called: {}", i);
          return "value " + i;
        });

    new Thread(() -> flux.subscribe(log::info)).start();


    gate.waitForAny("onComplete");

  }
}
