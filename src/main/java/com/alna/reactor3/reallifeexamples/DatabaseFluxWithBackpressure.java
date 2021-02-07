package com.alna.reactor3.reallifeexamples;

import com.alna.reactor3.DemoSubscriber;
import com.alna.reactor3.nitrite.NitriteTestDatabase;
import com.alna.reactor3.nitrite.dataaccess.FibonacciNumberDataAccess;
import com.alna.reactor3.nitrite.datasets.NitriteFibonacciSequenceSchema;
import com.alna.reactor3.utility.GateBasedSynchronization;
import com.alna.reactor3.utility.ThreadHelper;
import com.google.common.util.concurrent.Uninterruptibles;
import lombok.extern.slf4j.Slf4j;
import org.reactivestreams.Subscription;
import reactor.core.publisher.BaseSubscriber;
import reactor.core.publisher.Flux;
import reactor.core.scheduler.Schedulers;

import java.io.IOException;
import java.time.Duration;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
public class DatabaseFluxWithBackpressure {
  public static void main(String[] args) {
    GateBasedSynchronization gate = new GateBasedSynchronization();

    final NitriteFibonacciSequenceSchema schema = new NitriteFibonacciSequenceSchema();

    try (NitriteTestDatabase testDatabase = new NitriteTestDatabase(Optional.of(schema))) {
      final Flux<Long> fibonnachiFlux = FibonacciNumberDataAccess.selectAsFlux(testDatabase.getNitriteDatabase())
          //This allows to control replanishing optimization
          //The default prefetch size is 32
          .publishOn(Schedulers.parallel(), 3)
          .subscribeOn(Schedulers.elastic());

      fibonnachiFlux.subscribe(new BaseSubscriber<Long>() {

        private Subscription subscription;

        private AtomicInteger counter = new AtomicInteger();

        @Override
        protected void hookOnSubscribe(Subscription subscription) {
          subscription.request(3);
          this.subscription = subscription;
        }

        @Override
        protected void hookOnNext(Long value) {
          ThreadHelper.sleep(10, TimeUnit.MILLISECONDS);

          //On Every 3rd item request new 3 items
          if (counter.incrementAndGet() % 3 == 0) {
            subscription.request(3);
          }

          log.info("On next received -> {}", value);
        }

        @Override
        protected void hookOnComplete() {
          log.info("OnComplete");
          gate.openGate("onComplete");
        }

        @Override
        protected void hookOnError(Throwable throwable) {
          log.info("OnError");
          gate.openGate("OnError");
        }
      });

      gate.waitForAny("onError", "onComplete");

    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
