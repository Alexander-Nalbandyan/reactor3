package com.alna.reactor3;

import com.alna.reactor3.utility.GateBasedSynchronization;
import lombok.extern.slf4j.Slf4j;
import org.reactivestreams.Subscription;
import reactor.core.publisher.BaseSubscriber;
import reactor.core.publisher.Flux;
import reactor.core.scheduler.Schedulers;

import java.util.List;
import java.util.function.Consumer;

@Slf4j
public class ReactorBaseSubscriber {
  public static void main(String[] args) {
    GateBasedSynchronization gate = new GateBasedSynchronization();

    Flux.range(1, 50)
        .doOnRequest(r -> System.out.println("request of " + r))

        //Buffers emmitted items into list of 5 elements so that when request(2) arrives it is interpretted as request for 2 buffers each with five elements so 2x5 10 elements
//        .buffer(5)

        //It splits the downstream request to smaller batches when propagating to upstream (here it will send 10 request of 10 batches instead of request(100)
        //Also it is using replanishing so that if 75%(or 8 items are consumed) it request another 75% from upstream.
        //This is also called replanishing optimization and most of the operators implement it.
//        .limitRate(10)
        //This second variant allows to control replanishing amount so instead of default 75% you can put number of elements to replanish after.
        //If it is set to 0 then replanishing is not done at all.
//        .limitRate(10, 5)
        // It limits the rate of emitting items from upsteam(source) and when 75% of rate is emitted it prefetches again.
//        .limitRequest(20) // It limits total number of items emitted from source(is usefull to make sure that no more elements are emitted in case of race condition with cancel)

        //Some operators also support prefetch parameter which allows to control the initial request size from upstream  and how many elements will be requested again on replanishing when 75% percent gets fulfilled.
        .publishOn(Schedulers.elastic(), 4)
        .subscribe(new BaseSubscriber<>() {

          @Override
          public void hookOnSubscribe(Subscription subscription) {
            request(100);
          }

          @Override
          public void hookOnNext(Integer integer) {
            System.out.println("Next: " + integer);
//            cancel();
          }

          @Override
          protected void hookOnComplete() {
            log.info("OnComplete ---");
            gate.openGate("complete");
          }

          @Override
          protected void hookOnCancel() {
            log.info("OnCancel ---");
            gate.openGate("cancel");
          }
        });

    gate.waitForAny("cancel", "complete");

  }
}
