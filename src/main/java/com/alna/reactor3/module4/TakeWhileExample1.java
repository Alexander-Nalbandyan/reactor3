package com.alna.reactor3.module4;



import com.alna.reactor3.DemoSubscriber;
import com.alna.reactor3.utility.datasets.GreekAlphabet;
import reactor.core.publisher.Flux;

import java.util.concurrent.atomic.AtomicInteger;

public class TakeWhileExample1 {

    public static void main(String[] args) {

        // Get the usual Greek alphabet and repeat it 3 times.
        Flux<String> greekAlphabet = GreekAlphabet.greekAlphabetInEnglishFlux()
                .repeat(3);

        // Create a counter so we can count the number of times we have
        // seen the "alpha" letter.
        AtomicInteger numberOfAlphas = new AtomicInteger();

        // We want to take letters until we have seen "alpha" 2 times.
        // On the second time, we stop caring about the stream.
        greekAlphabet.takeWhile( nextLetter -> {

            // It's an alpha!  Count it!
            if( nextLetter.equals("alpha") ) {

                // If this is the second alpha, then return false - meaning that
                // we are no longer skipping.
                if (numberOfAlphas.incrementAndGet() == 2) {
                    return false;
                }
            }

            // Return true - yes, we are still taking.
            return true;

        }).subscribe(new DemoSubscriber<>());


        System.exit(0);
    }
}
