package com.alna.reactor3.module4;


import com.alna.reactor3.utility.MutableReference;
import com.alna.reactor3.utility.datasets.FibonacciSequence;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.function.Function;
import java.util.stream.Collectors;

public class CollectExample2 {

    private final static Logger log = LoggerFactory.getLogger(CollectExample2.class);

    public static void main(String[] args) {

        // collect is also useful for accumulating values or state information.  Here
        // we will total up the first 12 numbers of the Fibonacci sequence.
        long sum = FibonacciSequence.create(12)
//                .collect(
//                        // What is the initial state?  In this case we makeObservable a container
//                        // for an integer.
//                        () -> new MutableReference<>(0L),
//
//                        // The collection function.  Sum the next number into the MutableReference
//                        (mutableReference , nextValue) ->
//                                mutableReference.setValue( mutableReference.getValue(0L) + nextValue)
//
//                )
            .collect(Collectors.summingLong(x -> x))
                // We block and get the value out of the Single that was returned
                // by the collect operation.
                .blockOptional().orElse(0L);


        // Emit the sum
        log.info(Long.toString(sum));

        System.exit(0);
    }
}
