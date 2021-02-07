package com.alna.reactor3.module4;


import com.alna.reactor3.DemoSubscriber;
import com.alna.reactor3.utility.datasets.GreekAlphabet;
import org.apache.commons.lang3.tuple.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Flux;

import static org.apache.commons.lang3.tuple.Pair.*;

public class MapExample1 {

    private final static Logger log = LoggerFactory.getLogger(MapExample1.class);

    public static void main(String[] args) {

        // Take each english representation and return a string that
        // contains the string length of each one...
        Flux<Pair> lengthStream = GreekAlphabet.greekAlphabetInEnglishFlux()
                                        .map(s -> of(s, s.length()));

        lengthStream.subscribe(new DemoSubscriber<>());

        System.exit(0);
    }
}
