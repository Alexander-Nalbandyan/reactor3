package com.alna.reactor3.module4;


import com.alna.reactor3.utility.datasets.GreekAlphabet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Flux;

public class FilterExample3 {
    private static final Logger log = LoggerFactory.getLogger(FilterExample3.class);

    public static void main(String[] args) {

        // Create an Observable to filter.
        Flux<String> greekAlphabet = GreekAlphabet.greekAlphabetInEnglishFlux()

                // Filter out "delta"
                .filter( nextLetter -> !nextLetter.toLowerCase().equals("delta"));

        //It will call given consumer for all events emitted from source with unlimited demand Long.MAX_VALUE
        greekAlphabet.subscribe(nextLetter -> log.info( "onNext - {}" , nextLetter));

        System.exit(0);
    }
}
