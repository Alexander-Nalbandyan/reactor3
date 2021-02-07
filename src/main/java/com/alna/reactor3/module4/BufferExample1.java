package com.alna.reactor3.module4;

import com.alna.reactor3.DemoSubscriber;
import com.alna.reactor3.utility.datasets.FibonacciSequence;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Flux;

import java.util.List;

public class BufferExample1 {

    private final static Logger log = LoggerFactory.getLogger(BufferExample1.class);

    public static void main(String[] args) {

        // Create a Fibonacci observable to 20 values.
        // Because we are going to buffer the results, we will expect
        // a Flux<List<Long>> instead of Observable<Long>.
        Flux<List<Long>> bufferedFibonacciSequence = FibonacciSequence.create(20)
                // Emit items 3 at a time.
                .buffer(3);

        // Subscribe using the demo subscriber.
        bufferedFibonacciSequence.subscribe(new DemoSubscriber<>());

        System.exit(0);
    }
}
