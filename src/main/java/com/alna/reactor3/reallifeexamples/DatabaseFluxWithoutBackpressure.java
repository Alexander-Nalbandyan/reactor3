package com.alna.reactor3.reallifeexamples;

import com.alna.reactor3.DemoSubscriber;
import com.alna.reactor3.nitrite.NitriteTestDatabase;
import com.alna.reactor3.nitrite.dataaccess.FibonacciNumberDataAccess;
import com.alna.reactor3.nitrite.datasets.NitriteFibonacciSequenceSchema;
import com.alna.reactor3.utility.GateBasedSynchronization;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.scheduler.Schedulers;

import java.io.IOException;
import java.time.Duration;
import java.util.Optional;

@Slf4j
public class DatabaseFluxWithoutBackpressure {
  public static void main(String[] args) {
    GateBasedSynchronization gate = new GateBasedSynchronization();

    final NitriteFibonacciSequenceSchema schema = new NitriteFibonacciSequenceSchema();

    try (NitriteTestDatabase testDatabase = new NitriteTestDatabase(Optional.of(schema))) {
      final Flux<Long> fibonnachiFlux = FibonacciNumberDataAccess.selectAsFlux(testDatabase.getNitriteDatabase())
          .publishOn(Schedulers.parallel())
          .subscribeOn(Schedulers.elastic());

      fibonnachiFlux.subscribe(new DemoSubscriber<>(gate, Duration.ofMillis(10)));

      gate.waitForAny("onError", "onComplete");

    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
