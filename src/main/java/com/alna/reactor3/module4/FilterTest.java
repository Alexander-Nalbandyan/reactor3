package com.alna.reactor3.module4;

import com.alna.reactor3.DemoSubscriber;
import com.alna.reactor3.utility.GateBasedSynchronization;
import com.alna.reactor3.utility.datasets.GreekAlphabet;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.scheduler.Scheduler;
import reactor.core.scheduler.Schedulers;

import java.time.Duration;
import java.util.ArrayList;
import java.util.function.Function;

@Slf4j
public class FilterTest {
  public static void main(String[] args) {
    //If given predicate returns true then value is emitted otherwise value is ignored.
//    GreekAlphabet.greekAlphabetInEnglishFlux().filter(s -> !s.equals(GreekAlphabet.greekLettersInEnglish[1]))
//        .subscribe(new DemoSubscriber<>());


    //Ignores duplicate values and emits only unique ones
    //When using with big fluxes can use too much memory because it needs to queue for checking duplicates.
    //Also provides variants for providing key extractor, collection and predicate.
//    GreekAlphabet.greekAlphabetInGreekFlux().repeat().take(50)
//        .distinct(Function.identity(), ArrayList::new)
//        .subscribe(new DemoSubscriber<>());

// SKips emitting items while provided condition is true and starts emitting items when condition becomes false.
//        GreekAlphabet.greekAlphabetInEnglishFlux()
//            .skipWhile(s -> !s.equals(GreekAlphabet.greekLettersInEnglish[5]))
//            .subscribe(new DemoSubscriber<>());

    //It is the inverse of skipWhile
//    GreekAlphabet.greekAlphabetInEnglishFlux()
//        .doOnNext(s -> log.info("On next -> {}", s))
//        .takeWhile(s -> !s.equals(GreekAlphabet.greekLettersInEnglish[5]))
//        .subscribe(new DemoSubscriber<>());

    //Skip until provided condition is false and start emitting items when condition becomes true
//        GreekAlphabet.greekAlphabetInEnglishFlux()
//            .skipUntil(s -> s.equals(GreekAlphabet.greekLettersInEnglish[5]))
//            .subscribe(new DemoSubscriber<>());

    //It is the inverse of skipUntil also it includes the matching data.
//        GreekAlphabet.greekAlphabetInEnglishFlux()
//            .takeUntil(s -> s.equals(GreekAlphabet.greekLettersInEnglish[5]))
//            .subscribe(new DemoSubscriber<>());

    //Skips emitting given number of elements from start of input source.
//    GreekAlphabet.greekAlphabetInEnglishFlux()
//        .skip(2L)
//        .subscribe(new DemoSubscriber<>());

    //Skips elements emitted within given period not inclusive and start emitting item right after duration was elapsed.
//    GateBasedSynchronization gate = new GateBasedSynchronization();
//    Flux.interval(Duration.ofSeconds(1))
//        .skip(Duration.ofSeconds(3))
//        .take(10)
//        .subscribe(new DemoSubscriber<>(gate));
//    gate.waitForAny("onComplete");

   //Skips elements emitted within given period where the period is calculated by provided time-capable scheduler ????
    GateBasedSynchronization gate = new GateBasedSynchronization();
//    Flux.interval(Duration.ofSeconds(1))
//    .skip(Duration.ofSeconds(3), Schedulers.elastic())
//    .take(10)
//    .subscribe(new DemoSubscriber<>(gate));
//    gate.waitForAny("onComplete");

    //Skips emitting items from given source at the beginning until other publish either signals onNext() or onComplete()
//    Flux.interval(Duration.ofSeconds(1))
//        .skipUntilOther(Flux.interval(Duration.ofSeconds(5)).take(1))
//        .take(10)
//        .subscribe(new DemoSubscriber<>(gate));
//    gate.waitForAny("onComplete");


    //Emits first N items(if available) from source then completes.
    //if N is 0 then it completes as soon as source signals the first value.
//        GreekAlphabet.greekAlphabetInEnglishFlux()
//        .doOnNext(s -> log.info("On next -> {}", s))
//        .take(40L)
//        .subscribe(new DemoSubscriber<>());


    //Emits items from source for given duration if  duration is 0 then it completes as soon as first items gets signaled from source.
//    Flux.interval(Duration.ofSeconds(1))
//        .take(Duration.ofSeconds(0))
//        .subscribe(new DemoSubscriber<>(gate));
//    gate.waitForAny("onComplete");



  }
}
