package com.alna.reactor3;

import com.google.common.collect.ImmutableList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Flux;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Random;

public class CreatingFlux {

  private static Logger log = LoggerFactory.getLogger(CreatingFlux.class);

  public static void main(String[] args) {

    final Flux<String> just = Flux.just("1", "3", "5");
    final Flux<Long> billion = Flux.just(1_000_000_000L);

    Flux.range(1, 1_000_000_000);//.subscribe(i -> {log.info("Consumed -> {}", i);});

    Flux.fromIterable(new ArrayList<>());
    Flux.fromStream(ImmutableList.of(1, 2, 3).stream());

    Flux.interval(Duration.ofSeconds(3));

    Flux.from(just);
    just.next();

    //This is low level api where you need to send signals directly like next(v), complete() or error() and can be useful when
    // migrating from legacy api and other ways to create flux are not applicable. You have more control over what is happening with flux.

    Flux<Double> flux = Flux.create(emitter -> {
      Random rnd = new Random();
      for(int i = 0; i <= 10; i++) emitter.next(rnd.nextDouble());
      int random = rnd.nextInt(2);
      if (random < 1) emitter.complete();
      else {
        emitter.error(new RuntimeException("Bad luck, you had one chance out of 2 to complete the Flux"));
      }

    });

    //Flux.blockxxx or any other method which returns non asynch java type must be avoided because it makes the code non reactive.
  }


}
