package com.alna.reactor3.module4;

import com.alna.reactor3.DemoSubscriber;
import com.alna.reactor3.utility.datasets.GreekAlphabet;
import reactor.core.publisher.Flux;

import java.util.HashSet;

public class DistinctExample2 {

    public static void main(String[] args) {

        // Get the usual Greek alphabet and repeat it 3 times.
        Flux<String> greekAlphabet = GreekAlphabet.greekAlphabetInEnglishFlux()
                .repeat(3);

        // We want only "distinct" values.
      //It provides the key extractor which in this case is the second char of letter.
      //The store collection supplier.
      //The predicate which should also store the value for next check.
      //And the cleanup consumer which is supposed to cleanup collection store on temination
        greekAlphabet
                .distinct(nextLetter -> nextLetter.charAt(1),
                          HashSet::new,
                          (se, ch) -> {

                            final boolean doesNotContain = !se.contains(ch);
                            se.add(ch);
                            return doesNotContain;
                        },
                        HashSet::clear)
                .subscribe(new DemoSubscriber<>());

        System.exit(0);
    }
}
