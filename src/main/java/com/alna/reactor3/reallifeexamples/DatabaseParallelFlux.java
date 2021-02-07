package com.alna.reactor3.reallifeexamples;

import com.alna.reactor3.DemoSubscriber;
import com.alna.reactor3.nitrite.NitriteTestDatabase;
import com.alna.reactor3.nitrite.dataaccess.FibonacciNumberDataAccess;
import com.alna.reactor3.nitrite.datasets.NitriteFibonacciSequenceSchema;
import com.alna.reactor3.utility.GateBasedSynchronization;
import com.alna.reactor3.utility.ThreadHelper;
import lombok.extern.slf4j.Slf4j;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;
import reactor.core.publisher.BaseSubscriber;
import reactor.core.publisher.Flux;
import reactor.core.publisher.ParallelFlux;
import reactor.core.scheduler.Schedulers;

import java.io.IOException;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.IntStream;

@Slf4j
public class DatabaseParallelFlux {

  public static final int RAIL_COUNT = 3;

  public static void main(String[] args) {
    GateBasedSynchronization[] gates = new GateBasedSynchronization[RAIL_COUNT];
    IntStream.range(0, RAIL_COUNT).forEach(val -> gates[val] = new GateBasedSynchronization());

    final NitriteFibonacciSequenceSchema schema = new NitriteFibonacciSequenceSchema();

    try (NitriteTestDatabase testDatabase = new NitriteTestDatabase(Optional.of(schema))) {

      final ParallelFlux<Long> parallelFlux = ParallelFlux.from(
          //Elements will be
          // requested from upstream RAIL_COUNT x 3
          FibonacciNumberDataAccess.selectAsFlux(testDatabase.getNitriteDatabase()).publishOn(Schedulers.parallel(), 3),
          RAIL_COUNT)
          //Controls the number of items to request on each rail.
          .runOn(Schedulers.elastic(), 1);


      parallelFlux.groups()
          .subscribe((i) -> {
        i.subscribe(new BaseSubscriber<Long>() {

          AtomicInteger counter = new AtomicInteger();

          private Subscription subscription;


          @Override
          protected void hookOnSubscribe(Subscription subscription) {
            this.subscription = subscription;

            // Request the first batch of events.  Let's ask
            // for the same number of events as we have parallel
            // rails.
            this.subscription.request(RAIL_COUNT);

          }

          @Override
          protected void hookOnNext(Long value) {
            // Slow things down on purpose.
            ThreadHelper.sleep(50, TimeUnit.MILLISECONDS);

            // Log the next number
            log.info("Next Fibonacci Number: {} - {}", value, i.key());

            if (counter.incrementAndGet() % RAIL_COUNT == 0) {
              this.subscription.request(RAIL_COUNT);
            }
          }

          @Override
          protected void hookOnComplete() {
            log.info("onComplete - {}", i.key());
            gates[i.key()].openGate("onComplete");
          }

          @Override
          protected void hookOnError(Throwable throwable) {
            log.info("onError - {}", i.key(), throwable);
            gates[i.key()].openGate("onError");
          }
        });
      });

    } catch (IOException e) {
      e.printStackTrace();
    }

    GateBasedSynchronization.waitMultiple(new String[] { "onError", "onComplete"}, gates);
  }
}
