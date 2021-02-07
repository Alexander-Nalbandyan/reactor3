package com.alna.reactor3.module4;



import com.alna.reactor3.DemoSubscriber;
import com.alna.reactor3.utility.datasets.GreekAlphabet;
import reactor.core.publisher.Flux;

import java.util.concurrent.atomic.AtomicInteger;

public class SkipWhileExample1 {

    public static void main(String[] args) {

        // Get the usual Greek alphabet and repeat it 3 times.
        Flux<String> greekAlphabet = GreekAlphabet.greekAlphabetInEnglishFlux()
            //Source will be emitted N + 1 times
                .repeat(3);

        // Create a counter so we can count the number of times we have
        // seen the "alpha" letter.
        AtomicInteger numberOfAlphas = new AtomicInteger();

        // We want to skip until we have seen "alpha" 3 times.
        // On the third time, we begin emitting letters.
        greekAlphabet.skipWhile( nextLetter -> {

            // It's an alpha!  Count it!
            if( nextLetter.equals("alpha") ) {

                // If this is the third alpha, then return false - meaning that
                // we are no longer skipping.
                if (numberOfAlphas.incrementAndGet() == 5) {
                    return false;
                }
            }

            // Return true - yes, we are still skipping.
            return true;

        }).subscribe(new DemoSubscriber<>());


        System.exit(0);
    }
}
