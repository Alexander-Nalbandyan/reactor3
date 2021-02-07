package com.alna.reactor3.module4;

import com.alna.reactor3.DemoSubscriber;
import com.alna.reactor3.utility.datasets.FibonacciSequence;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Flux;

public class ScanExample1 {

    private final static Logger log = LoggerFactory.getLogger(ScanExample1.class);

    public static void main(String[] args) {

        // collect is also useful for accumulating values or state information.  Here
        // we will total up the first 12 digits of the Fibonacci sequence.
        Flux<Long> sumSequence = FibonacciSequence.create(12)
                .scan(
                        // Set the initial value
//                        0L,

                        // The collection function.  Sum the next number into the MutableReference
                        (currentValue , nextValue) -> {
                            log.info("{} + {} = {}", currentValue, nextValue, currentValue + nextValue);
                            return currentValue + nextValue;
                        }
                );

        sumSequence.subscribe(new DemoSubscriber<>());

        System.exit(0);
    }
}
