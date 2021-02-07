package com.alna.reactor3.module4;

import com.alna.reactor3.utility.datasets.GreekAlphabet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;

public class CollectExample1 {

    private final static Logger log = LoggerFactory.getLogger(CollectExample1.class);

    public static void main(String[] args) {

        // collect is useful to combine a stream of events into a single event
        // or object.  In this case, we will make an ArrayList<String> that contains
        // all of the greek letters.
        ArrayList<String> greekLetterArray = GreekAlphabet.greekAlphabetInGreekFlux()
                .collect(
                        // What is the initial state?  In this case a blank ArrayList
                        ArrayList<String>::new,

                        // The collection function.  Put the greekLetter into the arraylist.
                        ArrayList::add)

                // We block and get the value out of the Single that was returned
                // by the collect operation.
                .block();

        // Emit each letter
        greekLetterArray.forEach(log::info);

        System.exit(0);
    }
}
