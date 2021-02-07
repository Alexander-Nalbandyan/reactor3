package com.alna.reactor3.module3;


import com.alna.reactor3.DemoSubscriber;
import com.alna.reactor3.utility.GateBasedSynchronization;
import com.alna.reactor3.utility.datasets.FibonacciSequence;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.Observable;
import java.util.concurrent.*;

public class CreationFromExample1 {

    private final static Logger log = LoggerFactory.getLogger(CreationFromExample1.class);

    public static void main(String[] args) {

        Long[] firstFiveFibonacciNumbers = FibonacciSequence.toArray(10);
      GateBasedSynchronization gate = new GateBasedSynchronization();

        // example of "fromArray" using an array of 5 integers.
        log.info("fromArray");

        //Creates flux which emits items in array and then completes.
        Flux<Long> targetFlux = Flux.fromArray(firstFiveFibonacciNumbers);
        targetFlux.subscribe(new DemoSubscriber<>(gate));

        log.info("");
        log.info("-----------------------------------------------------------------------------------");
        log.info("");
        log.info("fromIterable");

        // example of "fromIterable" using an array of 7 integers
        ArrayList<Long> fibonacciArray = FibonacciSequence.toArrayList(7);
        //Creates flux from given iterable without need to iterate yourself
        targetFlux = Flux.fromIterable(fibonacciArray);
        targetFlux.subscribe(new DemoSubscriber<>(gate));
//
        log.info("");
        log.info("-----------------------------------------------------------------------------------");
        log.info("");
        log.info("fromCallable");

        // example of "fromCallable" using an array of 9 integers
        Mono<Long[]> workObservable = Mono.fromCallable(() -> FibonacciSequence.toArray(9));

        // Note that the fromCallable method only returns a single value in the return Observable.
        targetFlux = Flux.fromArray(workObservable.block());
        targetFlux.subscribe(new DemoSubscriber<>(gate));


//
        log.info("");
        log.info("-----------------------------------------------------------------------------------");
        log.info("");
        log.info("fromFuture");

        // example of Observable creation via "fromFuture" using an array of 6 integers

        // Create an ExecutorService that has a single thread.
        ExecutorService executor = Executors.newFixedThreadPool(1);

        // Create a FutureTask that will return an Integer array of 6 elements
        CompletableFuture<Long[]> futureTask = CompletableFuture.supplyAsync(() -> FibonacciSequence.toArray(6), executor);


        //Allows to create mono from completable future.
        workObservable = Mono.fromFuture(futureTask);

        // Block to pull out the data from the future and pass into an Observable<Integer>
        // using fromArray
        targetFlux = Flux.fromArray(workObservable.block());
        targetFlux.subscribe(new DemoSubscriber<>(gate));


        log.info("");
        log.info("-----------------------------------------------------------------------------------");
        log.info("");
        log.info("fromStream");

        // example of "fromStream" using an array of 7 integers
        ArrayList<Long> fibonacciArray2 = FibonacciSequence.toArrayList(10);
        //Creates flux from given stream it is also possible to create flux from stream supplier.
        targetFlux = Flux.fromStream(fibonacciArray.stream());
        targetFlux.subscribe(new DemoSubscriber<>(gate));



        System.exit(0);
    }
}
