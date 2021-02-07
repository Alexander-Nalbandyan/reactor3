package com.alna.reactor3.module4;

import com.alna.reactor3.DemoSubscriber;
import com.alna.reactor3.utility.GateBasedSynchronization;
import com.alna.reactor3.utility.datasets.GreekAlphabet;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public class PositionialOperatorsTest {
  public static void main(String[] args) {
    GateBasedSynchronization gate = new GateBasedSynchronization();
    //next() returns mono from first item emitted from source or empty  mono if the flux was empty.
//    GreekAlphabet.greekAlphabetInGreekFlux().next().subscribe(new DemoSubscriber<>());

    //Switches to the other mono if current mono does not emit any item.
//    GreekAlphabet.greekAlphabetInGreekFlux().next().switchIfEmpty(Mono.just("alfa")).subscribe(new DemoSubscriber<>());

    //Competes 2 monos and returns the first item emitted from one of the sources.
//    Flux.interval(Duration.ofSeconds(10)).next().map(i -> "from first mono -> " + i)
//        .or(Flux.interval(Duration.ofSeconds(5)).next().map(i -> "from second mono -> " + i)).subscribe(new DemoSubscriber<>(gate));

    //Emmits an error if current Mono is empty.
//    Mono.empty().switchIfEmpty(Mono.error(() -> new Exception("Boom Boom"))).subscribe(new DemoSubscriber<>());

// Emits the last item emitted from the source or throws NoSuchElementException if the Source is empty.
//    GreekAlphabet.greekAlphabetInGreekFlux().last().subscribe(new DemoSubscriber<>());
//    Flux.empty().last().subscribe(new DemoSubscriber<>(gate));

    //Emmits last N elements(if available from given source) or emits no elements if source is empty.
//    Flux.empty().takeLast(45).subscribe(new DemoSubscriber<>(gate));

    //It emits either the last element if available or default value.
//    Flux.empty().last("Abrakadabra :)))").subscribe(new DemoSubscriber<>(gate));

    //If no element is available then throw given error.
//    Flux.empty().takeLast(1).switchIfEmpty(Mono.error(() -> new IllegalStateException("Error"))).subscribe(new DemoSubscriber<>(gate));

    // Emits only the element in the given index were the index starts from 0.
//    GreekAlphabet.greekAlphabetInGreekFlux().elementAt(1).subscribe(new DemoSubscriber<>(gate));
    //Emits only the element in the given index or the default value if the source is shorter.
    GreekAlphabet.greekAlphabetInGreekFlux().elementAt(56, "pahooooo").subscribe(new DemoSubscriber<>(gate));

    gate.waitForAny("onComplete", "onError");
  }
}
