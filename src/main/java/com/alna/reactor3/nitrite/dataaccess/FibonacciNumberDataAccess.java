package com.alna.reactor3.nitrite.dataaccess;

import com.alna.reactor3.nitrite.entity.FibonacciNumber;

import org.dizitart.no2.Nitrite;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Flux;

import java.util.Observable;

public class FibonacciNumberDataAccess {

    private final static Logger log = LoggerFactory.getLogger(com.alna.reactor3.nitrite.dataaccess.FibonacciNumberDataAccess.class);

    public static Flux<Long> selectAsFlux(Nitrite db) {

        // Use the generate operator to makeObservable an Observable that returns
        // documents from the FibonacciNumber collection.
        return Flux.generate(

                // Function to makeFlux the initial state...in this case, an
                // iterator over the collection.
                () -> db.getRepository(FibonacciNumber.class).find().iterator(),

                // The emitter method.  This call should return a single event.
                (fibonacciNumberIterator, longEmitter) -> {

                    try {
                        // See if there are more documents to return
                        // from the database.
                        if (fibonacciNumberIterator.hasNext()) {

                            // Get the next document...a FibonacciNumber
                            FibonacciNumber nextNumber = fibonacciNumberIterator.next();

                            // Log
                            log.info("onNext - {}", nextNumber.getNumberValue());

                            // Emit the next number to the Observable<Long>
                            longEmitter.next(nextNumber.getNumberValue());
                        } else {

                            // Log that there are no more documents...
                            log.info("onComplete");

                            // Tell the subscribers that we are done.
                            longEmitter.complete();
                        }
                    }
                    catch(Throwable t) {

                        // Any exception causes an onError.
                        longEmitter.error(t);
                    }
                    return fibonacciNumberIterator;
                }
        );

    }

}
