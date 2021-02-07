package com.alna.reactor3.reallifeexamples;


import com.alna.reactor3.DemoSubscriber;
import com.alna.reactor3.utility.GateBasedSynchronization;
import com.alna.reactor3.utility.network.HttpResponseMonoFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.publisher.ParallelFlux;
import reactor.core.scheduler.Schedulers;

import java.net.URI;
import java.time.Duration;

public class NetworkingExample2 {

    private static final Logger log = LoggerFactory.getLogger(NetworkingExample2.class);

    public static void main(String[] args) {

        try {
            // Synchronization gates
            GateBasedSynchronization gate = new GateBasedSynchronization();

            // Create two requests to our addition service.  The first will have a long delay
            URI request1 = new URI("http://localhost:22221/addition?a=5&b=9&delay=6000");

            // The second request will no have a delay.
            URI request2 = new URI("http://localhost:22221/addition?a=21&b=21&delay=0");

            // Use our HttpResponseObserverFactory to makeObservable an Observable that returns
            // the result of the call to the addition service for the first request.
            // Note that we are placing this request on the IO thread pool since it
            // will be waiting on IO predominantly.
            Mono<Integer> networkRequest1 =
                    HttpResponseMonoFactory.additionRequestResponseObservable(request1)
                    .subscribeOn(Schedulers.elastic());

            // ...and another for the second request.
            Mono<Integer> networkRequest2 =
                  HttpResponseMonoFactory.additionRequestResponseObservable(request2)
                      .subscribeOn(Schedulers.elastic());

            // We use the merge operator with maxConcurrency of 2 in order
            // to cause both networkRequest1 and networkRequest2 to be executed
            // simultaneously.  We want all of this on the IO threads.
            // We also set a timeout of 5 seconds for the requests.
            ParallelFlux<Integer> responseStream = Flux.merge(networkRequest1, networkRequest2)
                    .timeout(Duration.ofSeconds(5), Mono.just(-1))
                    .parallel(2)
                    .runOn(Schedulers.elastic());

            // Now that we have our Observable chain, we can use our standard
            // DemoSubscriber to cause it to execute.
            responseStream.subscribe(new DemoSubscriber<>(gate));

            // We wait for success or failure.
            gate.waitForAny("onError", "onComplete");

        } catch (Throwable e) {
            log.error(e.getMessage(),e);
        }

    }
}
