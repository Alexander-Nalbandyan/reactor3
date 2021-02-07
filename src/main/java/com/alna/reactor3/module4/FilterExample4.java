package com.alna.reactor3.module4;


import com.alna.reactor3.utility.datasets.GreekAlphabet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Flux;

public class FilterExample4 {
    private static final Logger log = LoggerFactory.getLogger(FilterExample4.class);

    public static void main(String[] args) {

        // Create an Observable to filter.
        // Filter out "delta"
        Flux<String> greekAlphabet = GreekAlphabet.greekAlphabetInEnglishFlux()
                .filter( nextLetter -> !nextLetter.toLowerCase().equals("delta"));

        //As we don't have error handler it throws reactor.core.Exceptions$ErrorCallbackNotImplemented wrapping our exception.
        greekAlphabet.subscribe(nextLetter -> {
            log.info( "onNext - {}" , nextLetter);

            if (nextLetter.equals("iota")) {
                throw new IllegalStateException("BOOM!");

            }
        });

        System.exit(0);
    }
}
