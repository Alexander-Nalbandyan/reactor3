package com.alna.reactor3.module4;



import com.alna.reactor3.DemoSubscriber;
import com.alna.reactor3.utility.GateBasedSynchronization;
import com.alna.reactor3.utility.datasets.GreekAlphabet;
import reactor.core.publisher.Flux;

import java.time.Duration;
import java.util.Observable;
import java.util.concurrent.TimeUnit;

public class TakeUntilExample1 {

    public static void main(String[] args) {

      // Get the usual Greek alphabet and repeat it FOREVER!
      Flux<String> greekAlphabet = GreekAlphabet.greekAlphabetInEnglishFlux().repeat();

      // We want to take for 2 seconds.
      GateBasedSynchronization gate = new GateBasedSynchronization();
      //Emitts until the other source signals any event.
      greekAlphabet.takeUntilOther(Flux.interval(Duration.ofSeconds(2), Duration.ofSeconds(10))).subscribe(
          new DemoSubscriber<>(gate));

      gate.waitForAny("onComplete");
      System.exit(0);
    }
}
