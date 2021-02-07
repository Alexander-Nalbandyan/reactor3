package com.alna.reactor3.module4;

import com.alna.reactor3.utility.GateBasedSynchronization;
import com.alna.reactor3.utility.datasets.GreekAlphabet;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;
import reactor.core.publisher.Flux;

import java.time.Duration;
import java.util.ArrayList;

public class GroupByTest {
  public static void main(String[] args) {

    //groupBy() divides incoming sequence into separate fluxes for each unique key extracted by given function.
    //Please note that it is recommended to have low cardinality keys because otherwise it will produce too many fluxes and in case of
    //low concurrency for processing them flatmap can hang.
    // key() function on GroupedFlux returns the key value extracted for every group that is how it differs from regular flux.
    GreekAlphabet.greekAlphabetInEnglishFlux()
        .groupBy(String::length)
        .flatMap(groupedFlux -> groupedFlux.collectList()
            .map(list -> StringUtils.join(list, ","))
            .map(s -> groupedFlux.key() + " -> " + s))
        .log().subscribe();

    //This version provides an argument for prefetch size from initial source.
//    GreekAlphabet.greekAlphabetInEnglishFlux()
//        .log()
//        .groupBy(String::length, 5)
//
//        .flatMap(flux -> flux.collectList().map(list -> StringUtils.join(list, ",")))
//      .subscribe();

    //This version also provides way to specify value mapper for each item emitted from source.
//    GreekAlphabet.greekAlphabetInEnglishFlux()
//        .groupBy(String::length, letter -> GreekAlphabet.greekLetters[GreekAlphabet.findGreekLetterOffset(letter, GreekAlphabet.greekLettersInEnglish)])
//        .flatMap(flux -> flux.collectList().map(list -> StringUtils.join(list, ",")))
//        .log().subscribe();

    //This version in conjunction with value mapper also provides a way to specify initial source prefetch size.
//    GreekAlphabet.greekAlphabetInEnglishFlux()
//        .log()
//        .groupBy(String::length, letter -> GreekAlphabet.greekLetters[GreekAlphabet.findGreekLetterOffset(letter, GreekAlphabet.greekLettersInEnglish)], 10)
//        .flatMap(flux -> flux.collectList().map(list -> StringUtils.join(list, ",")))
//        .subscribe();


    //Combines elements from 2 publishers into pairs which overlap time window.
    // The time window for first publisher is determined by first signal (onNext or onComplete) from left function.
    // The time window for other publisher is determined by first signal (onNext or onComplete) from right function.
    // The result selector defines function to apply on pair of values.
//    Flux.interval(Duration.ofMillis(300), Duration.ofMillis(100)).take(10)
//        .join(Flux.interval(Duration.ofMillis(300)).take(10),
//              l ->  Flux.interval(Duration.ofMillis((l + 1) * 100)),
//              r -> Flux.interval(Duration.ofMillis((r + 1) * 300)),
//              Pair::of)
//        .log().subscribe();


    //In contrast with join() this version calls result selector with empty flux if there is no value to combine from second publisher.
    //Also the items from second publisher are provided as Flux instead of calling one by one.
//        Flux.interval(Duration.ofMillis(100)).take(10)
//            .groupJoin(Flux.interval(Duration.ofMillis(300)).take(10),
//                  l ->  Flux.interval(Duration.ofMillis((l + 1) * 100)),
//                  r -> Flux.interval(Duration.ofMillis((r + 1) * 300)),
//                       (l, rflux) -> Pair.of(l, rflux))
//            .flatMap(pair -> pair.getRight().collect(StringBuilder::new,
//                                                     (sb, l) -> {
//                                                       if (sb.length() > 0) sb.append(",");
//                                                       sb.append(l);
//                                                     }).map(res -> pair.getLeft() + " -> " + res))
//            .log().subscribe();

    new GateBasedSynchronization().waitForAny("onComplete");
  }
}
