package com.alna.reactor3.utility.datasets;




import reactor.core.publisher.Flux;

import java.util.ArrayList;
import java.util.Observable;

public class FibonacciSequence {

    public static Flux<Long> create(final long totalNumbers) {

        return Flux.create(emitter -> {

            long count = 0;
            long previousValue1 = 1;
            long previousValue2 = 1;

            while( count < totalNumbers ) {

                if( emitter.isCancelled() ) {
                    break;
                }

                ++count;

                if( count == 1 ) {
                    emitter.next(0L);
                    continue;
                }

                if( count == 2 ) {
                    emitter.next(1L);
                    continue;
                }

                long newValue = previousValue1 + previousValue2;
                emitter.next(newValue);

                previousValue1 = previousValue2;
                previousValue2 = newValue;
            }

            if( !emitter.isCancelled() ) {
                emitter.complete();
            }
        });

    }

    public static Long[] toArray( int totalNumbers ) {

        return create(totalNumbers)
                .collect(() -> new ArrayList<Long>(totalNumbers), ArrayList::add)
                .block()
                .toArray(new Long[totalNumbers]);

    }

    public static ArrayList<Long> toArrayList( int totalNumbers ) {
        return create(totalNumbers)
                .collect(() -> new ArrayList<Long>(totalNumbers), ArrayList::add)
                .block();
    }

}
