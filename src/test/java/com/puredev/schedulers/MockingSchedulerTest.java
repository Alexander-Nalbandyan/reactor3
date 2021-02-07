package com.puredev.schedulers;

import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.util.concurrent.TimeoutException;

import static java.time.Duration.ofMillis;
import static java.time.Duration.ofSeconds;

public class MockingSchedulerTest {
  @Test
  public void mockScheduler() {

    StepVerifier
        // Creates virtual time scheduler which is used as scheduler for all operators and so virtual time is used.
        //So we can do verification with expectNoEvent and thenAwait without actually waiting.
        .withVirtualTime(() ->
                             Flux
                                 .never()
                                 .timeout(ofMillis(20000))
        )
        .expectSubscription()
        .expectNoEvent(ofMillis(19999))
        .thenAwait(ofMillis(1))
        .expectError(TimeoutException.class)
        .verify(ofSeconds(1));
  }
}
