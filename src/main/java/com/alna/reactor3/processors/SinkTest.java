package com.alna.reactor3.processors;

import com.alna.reactor3.DemoSubscriber;
import okio.Sink;
import reactor.core.publisher.DirectProcessor;
import reactor.core.publisher.FluxSink;
import reactor.core.publisher.ReplayProcessor;

public class SinkTest {
  public static void main(String[] args) {
    final ReplayProcessor<String> replayProcessor = ReplayProcessor.create();
    //Safe way for producing items to processor from multiple threads is by using sink.
    //for consuming items we still need to use processor itself.
//    final FluxSink<String> fluxSink = replayProcessor.sink();
    //Allows to provide custom overflow strategy to use for backpressure(when the consumer can't keep up) handling.
    final FluxSink<String> fluxSink = replayProcessor.sink(FluxSink.OverflowStrategy.DROP);

    fluxSink.next("test1");
    fluxSink.next("test2");
    fluxSink.next("test3");

    new Thread(() -> fluxSink.next("test asynch1")).start();

    fluxSink.next("test4");

    new Thread(() -> fluxSink.next("test asynch2")).start();

    fluxSink.next("test5");
    replayProcessor.subscribe(new DemoSubscriber<>("sub1"));
  }
}
