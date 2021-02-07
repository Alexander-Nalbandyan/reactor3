package com.alna.reactor3.module4;


import com.alna.reactor3.utility.datasets.GreekAlphabet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Flux;

public class FilterExample2 {
    private static final Logger log = LoggerFactory.getLogger(FilterExample2.class);

    public static void main(String[] args) {

        // Create an Observable to filter.
        Flux<String> greekAlphabet = GreekAlphabet.greekAlphabetInEnglishFlux()

                // Filter out "delta"
                .filter( nextLetter -> !nextLetter.equals("delta"));

        greekAlphabet.subscribe(
                nextLetter ->  log.info( "onNext - {}" , nextLetter),
                throwable -> log.error(throwable.getMessage(),throwable),
                () -> log.info( "onComplete")
        );

        System.exit(0);
    }
}
