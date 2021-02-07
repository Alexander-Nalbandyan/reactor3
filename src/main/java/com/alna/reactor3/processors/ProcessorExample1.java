package com.alna.reactor3.processors;


import com.alna.reactor3.DemoSubscriber;
import com.alna.reactor3.utility.ThreadHelper;
import com.alna.reactor3.utility.datasets.FibonacciSequence;
import com.alna.reactor3.utility.datasets.GreekAlphabet;
import com.alna.reactor3.utility.processors.SelectableProcessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.DirectProcessor;
import reactor.core.scheduler.Schedulers;

import java.util.concurrent.TimeUnit;

public class ProcessorExample1 {

    private final static Logger log = LoggerFactory.getLogger(ProcessorExample1.class);

    public static void main(String[] args) {

        // Create a SelectableProcessor<String> using a PublishSubject
        SelectableProcessor subject = new SelectableProcessor(DirectProcessor.create());

        // At least one consumer needs to be present, else the producers
        // will detect that no one is listening and dispose themselves.
        subject.addEventConsumer(
                new DemoSubscriber("sub1")
        );
        subject.addEventConsumer(
                new DemoSubscriber("sub2")
        );

        // Create an Observable that emits the English form of the Greek alphabet.
        subject.addEventProducer(
            // The base observable will be the English version of the Greek
            // alphabet.
            GreekAlphabet.greekAlphabetInEnglishFlux()
//                .repeat()
                .subscribeOn(Schedulers.parallel())
        );

        subject.addEventProducer(
            // The base observable will be the Fibonacci Numbers in String form.
            FibonacciSequence.create(20)
                        .repeat()
                        .map( nextNumber -> Long.toString(nextNumber))
                        .subscribeOn(Schedulers.parallel())
        );

        ThreadHelper.sleep(10, TimeUnit.SECONDS);

        System.exit(0);
    }
}
