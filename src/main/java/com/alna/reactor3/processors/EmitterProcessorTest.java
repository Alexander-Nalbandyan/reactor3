package com.alna.reactor3.processors;

import com.alna.reactor3.DemoSubscriber;
import com.alna.reactor3.utility.GateBasedSynchronization;
import com.google.common.util.concurrent.Uninterruptibles;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.EmitterProcessor;

import java.time.Duration;

@Slf4j
public class EmitterProcessorTest {
  public static void main(String[] args) {
    GateBasedSynchronization gate = new GateBasedSynchronization();
    final EmitterProcessor<String> emitterProcessor = EmitterProcessor.create(2, true);
    //Elements emitted before first subscriber will be buffered until first one subscribed and will be sent to it.
    //All other subscribers will just receive elements emitted after they are subscribed.
    //It will block current thread for onNext() calls after buffer size is reached and no new subscribers are subscribed.
    //Note if buffer size is less then 8 then 8 will be used as min buffer size.
    emitterProcessor.onNext("test1");
    emitterProcessor.onNext("test2");
    emitterProcessor.onNext("test3");
    emitterProcessor.onNext("test4");
    emitterProcessor.onNext("test5");
    emitterProcessor.onNext("test6");
    emitterProcessor.onNext("test7");
    emitterProcessor.onNext("test8");
    //If parallel thread subscriber is used then it can still elements and current thread subscribers will not receive any of them.
//    Thread thread = new Thread(){
//      public void run(){
//        log.info("Waiting for 10 seconds to drain");
//        Uninterruptibles.sleepUninterruptibly(Duration.ofSeconds(10));
//        log.info("Finished waiting");
    final DemoSubscriber<String> threadSub = new DemoSubscriber<>("threadSub", 20L);
    emitterProcessor.subscribe(threadSub);
//      }
//    };
//    thread.start();
    emitterProcessor.onNext("test9");
    emitterProcessor.onNext("test10");

    log.info("Helllow");
    //If oncomplete is called then all onNext() calls are ignored.
//    emitterProcessor.onComplete();
//    emitterProcessor.onError(new RuntimeException("test message"));

    //Only first subscriber receives replay of elements all other subscribers receive elements emitted after their subscription.
    final DemoSubscriber<String> sub1 = new DemoSubscriber<>("sub1", 10L);
    emitterProcessor.subscribe(sub1);
    emitterProcessor.onNext("test13");
    //If any of subscribers requests less elements then emitted then elements are buffered and all subscribers stop receiving elements until
    //more elements are requested by this subscriber.
    final DemoSubscriber<String> sub2 = new DemoSubscriber<>("sub2", 2L);
    emitterProcessor.subscribe(sub2);

    emitterProcessor.onNext("test11");
    emitterProcessor.onNext("test12");
    emitterProcessor.onNext("test13");
    emitterProcessor.onNext("test14");
    emitterProcessor.onNext("test15");
    log.info("Sleep before request for 3 sec");
    Uninterruptibles.sleepUninterruptibly(Duration.ofSeconds(3));
    sub2.getSubscription().request(10);
    emitterProcessor.onNext("test16");
    emitterProcessor.onNext("test17");
    emitterProcessor.onNext("test18");
    emitterProcessor.onNext("test19");
    emitterProcessor.onNext("test20");
    emitterProcessor.onNext("test21");
    //If more then bufferSize elements are emitted then onNext() calls after that is blocked.
    log.info("Does it block");
    emitterProcessor.onNext("test22");


    //By default and if autoCancel param is true then after all subscribers are cancelled then onComplete() signal is called on processor and
    // all new subscribers receive complete signal. Otherwise if autoCancel is false then even after all subscribers are cancels new subscribers can come and
    // receive either new emissions or buffered emissions.
    sub2.getSubscription().cancel();
    sub1.getSubscription().cancel();
    threadSub.getSubscription().cancel();
    emitterProcessor.subscribe(new DemoSubscriber<>("newSubAfterCancel"));


    gate.waitForAny("onComplete");
  }
}
