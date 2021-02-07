package com.alna.reactor3.module4;

import com.alna.reactor3.DemoSubscriber;
import com.alna.reactor3.utility.datasets.GreekAlphabet;
import reactor.core.publisher.Flux;

public class DistinctExample1 {

    //private final static Logger log = LoggerFactory.getLogger(DistinctExample1.class);

    public static void main(String[] args) {

        // Get the usual Greek alphabet and repeat it 3 times.
        Flux<String> greekAlphabet = GreekAlphabet.greekAlphabetInEnglishFlux()
                .repeat(3);

        // We want only "distinct" values.
        greekAlphabet.distinct().subscribe(new DemoSubscriber<>());


        System.exit(0);
    }
}
