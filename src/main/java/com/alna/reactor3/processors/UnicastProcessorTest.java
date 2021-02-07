package com.alna.reactor3.processors;

import com.alna.reactor3.DemoSubscriber;
import com.alna.reactor3.utility.GateBasedSynchronization;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.UnicastProcessor;

import java.util.concurrent.LinkedBlockingDeque;
@Slf4j
public class UnicastProcessorTest {
  public static void main(String[] args) {
    GateBasedSynchronization gate = new GateBasedSynchronization();

    //Allows only single subscriber
//    final UnicastProcessor<Object> unicastProcessor = UnicastProcessor.create();
    final LinkedBlockingDeque<Object> queue = new LinkedBlockingDeque<>(2);
    //If the provided queue is bounded then exceeding elements are rejected without notice.
    //The provided callback is called on each rejected value
    //And the last disposable argument will be called after processor is completed.
//    final UnicastProcessor<Object> unicastProcessor = UnicastProcessor.create(queue, (rejValue) -> log.info("This value: {} was rejected because of bounded queue", rejValue),
//                                                                              () -> {log.info("Processor is completed !!!");});

    //This version allows to provide queue impl and end callback.
    final UnicastProcessor<Object> unicastProcessor = UnicastProcessor.create(queue, () -> {log.info("Processor is completed !!!");});

    unicastProcessor.onNext("test1");
    //If oncomplete is called then all onNext() calls are ignored.
//    unicastProcessor.onComplete();
//    unicastProcessor.onError(new RuntimeException("test message"));

    //If subscriber requests less elements then other elements are just buffered without emitting complete signal to subscriber.
    unicastProcessor.subscribe(new DemoSubscriber<>("sub1", 6L));
    //Allows only single subscriber.
//    unicastProcessor.subscribe(new DemoSubscriber<>("sub2", 3L));

    unicastProcessor.onNext("test2");
    unicastProcessor.onNext("test3");
    unicastProcessor.onNext("test4");
    unicastProcessor.onNext("test5");
    unicastProcessor.onNext("test6");
    log.info("Queue Size: {}", queue.size());
    unicastProcessor.onNext("test7");
    log.info("Queue Size: {}", queue.size());
    unicastProcessor.onNext("test8");
    log.info("Queue Size before onNext9 : {}", queue.size());
    unicastProcessor.onNext("test9");
    unicastProcessor.onComplete();

    log.info("Queue Size: {}", queue.size());

    gate.waitForAny("onComplete");
  }
}
