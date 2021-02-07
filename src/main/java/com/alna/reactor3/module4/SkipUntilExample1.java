package com.alna.reactor3.module4;


import com.alna.reactor3.DemoSubscriber;
import com.alna.reactor3.utility.ThreadHelper;
import com.alna.reactor3.utility.datasets.GreekAlphabet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Flux;
import reactor.core.scheduler.Schedulers;

import java.time.Duration;
import java.util.Observable;
import java.util.concurrent.TimeUnit;

public class SkipUntilExample1 {

    private final static Logger log = LoggerFactory.getLogger(SkipUntilExample1.class);

    public static void main(String[] args) {

        // Get the usual Greek alphabet and repeat it FOREVER!
        Flux<String> greekAlphabet = GreekAlphabet.greekAlphabetInEnglishFlux()
                .repeat();

        // We want to skip until the "interval" Observable emits in 2 seconds.
        greekAlphabet.skipUntilOther(Flux.interval(Duration.ofSeconds(2), Duration.ofSeconds(10)).take(1))
                .subscribeOn(Schedulers.elastic())
                .subscribe(new DemoSubscriber<>());

        // Wait for 3 seconds before terminating the process.
        ThreadHelper.sleep(3, TimeUnit.SECONDS);

        System.exit(0);
    }
}
