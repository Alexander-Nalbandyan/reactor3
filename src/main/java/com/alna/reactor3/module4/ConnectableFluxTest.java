package com.alna.reactor3.module4;

import com.alna.reactor3.DemoSubscriber;
import com.alna.reactor3.utility.GateBasedSynchronization;
import com.google.common.util.concurrent.Uninterruptibles;
import lombok.extern.slf4j.Slf4j;
import reactor.core.Disposable;
import reactor.core.publisher.ConnectableFlux;
import reactor.core.publisher.Flux;

import java.time.Duration;

@Slf4j
public class ConnectableFluxTest {
  public static void main(String[] args) {

    //When calling publish() on any Flux it returns connectableFlux in result which means that no item will be published until connect() method is called on it.
    //and also the flux is turned into hot flux which means that when new subscriber comes in the old items will not be replayed.
    //Even if one of them unsubscribes from flux the remaining subscribers keep receiving items from the flux.
//    final ConnectableFlux<Long> connectableFlux = Flux.interval(Duration.ofMillis(200)).publish();
//    final DemoSubscriber<Long> sub1 = new DemoSubscriber<>("sub1");
//    connectableFlux.subscribe(sub1);
//    final DemoSubscriber<Long> sub2 = new DemoSubscriber<>("sub2");
//    connectableFlux.subscribe(sub2);
//
//    log.info("Before Connect nothing happens");
//    Uninterruptibles.sleepUninterruptibly(Duration.ofSeconds(1));
//    connectableFlux.connect();
//      log.info("After connect !!!");
//
//    Uninterruptibles.sleepUninterruptibly(Duration.ofSeconds(1));
//    log.info("Canceling first subscriber");
//    sub1.getSubscription().cancel();
//

    //This version allows to control prefetch size from original source.
//    final ConnectableFlux<Long> connectableFlux = Flux.interval(Duration.ofMillis(200)).log().publish(15);
//    final DemoSubscriber<Long> sub1 = new DemoSubscriber<>("sub1");
//    connectableFlux.subscribe(sub1);
//    final DemoSubscriber<Long> sub2 = new DemoSubscriber<>("sub2");
//    connectableFlux.subscribe(sub2);
//
//    log.info("Before Connect nothing happens");
//    Uninterruptibles.sleepUninterruptibly(Duration.ofSeconds(1));
//    connectableFlux.connect();
//      log.info("After connect !!!");
//
//    Uninterruptibles.sleepUninterruptibly(Duration.ofSeconds(1));
//    log.info("Canceling first subscriber");
//    sub1.getSubscription().cancel();

    //Not understandable version ????
//    final Flux<String> connectableFlux = Flux.interval(Duration.ofMillis(200)).take(10)
//        .publish(flux -> flux.map(i -> i + " -> " + i * 10).log());
//    final DemoSubscriber<String> sub1 = new DemoSubscriber<>("sub1");
//    connectableFlux.subscribe(sub1);
//    final DemoSubscriber<String> sub2 = new DemoSubscriber<>("sub2");
//    connectableFlux.subscribe(sub2);
//
//    Uninterruptibles.sleepUninterruptibly(Duration.ofSeconds(1));
//    log.info("Canceling first subscriber");
//    sub1.getSubscription().cancel();

    //Automatically connects when first subscriber connects to the flux and stops(disconnects) when the last subscriber disconnects.
//    final Flux<Long> connectableFlux = Flux.interval(Duration.ofMillis(200)).publish().refCount();
//    final DemoSubscriber<Long> sub1 = new DemoSubscriber<>("sub1");
//    connectableFlux.subscribe(sub1);
//    final DemoSubscriber<Long> sub2 = new DemoSubscriber<>("sub2");
//    connectableFlux.subscribe(sub2);
//
//
//    Uninterruptibles.sleepUninterruptibly(Duration.ofSeconds(1));
//    log.info("Canceling first subscriber");
//    sub1.getSubscription().cancel();
//
//
//    Uninterruptibles.sleepUninterruptibly(Duration.ofSeconds(2));
//    log.info("Canceling second subscriber");
//    sub2.getSubscription().cancel();


    //Automatically connects to the flux when the given number of subscribers are connected and disconnects when all subscribers are disconnected or
    // the source is completed.
//    final Flux<Long> connectableFlux = Flux.interval(Duration.ofMillis(200)).publish().refCount(2);
//    final DemoSubscriber<Long> sub1 = new DemoSubscriber<>("sub1");
//    connectableFlux.subscribe(sub1);
//    log.info("Nothing happens until second subcriber connects after 2 sec");
//    Uninterruptibles.sleepUninterruptibly(Duration.ofSeconds(2));
//
//    log.info("Connecting second subscriber");
//    final DemoSubscriber<Long> sub2 = new DemoSubscriber<>("sub2");
//    connectableFlux.subscribe(sub2);
//
//
//    Uninterruptibles.sleepUninterruptibly(Duration.ofSeconds(1));
//    log.info("Canceling first subscriber");
//    sub1.getSubscription().cancel();
//
//
//    Uninterruptibles.sleepUninterruptibly(Duration.ofSeconds(2));
//    log.info("Canceling second subscriber");
//    sub2.getSubscription().cancel();

    //Allows to control the time after which the source will be disconnected when last subscriber disconnects.
    // Note: if there is a new subscriber before the period was elapsed then the source is reconnected and keep emitting items.
    //But if new connection is made after the period was elapsed then nothing happens.
    //Note: if the source is already completed or signaled an error then it disconnected immediately ignoring given period.
    // There is also a version which allows to pass scheduler on which the duration must be calculated.
//    final Flux<Long> connectableFlux = Flux.interval(Duration.ofMillis(200)).log().publish().refCount(2, Duration.ofSeconds(2));
//    final DemoSubscriber<Long> sub1 = new DemoSubscriber<>("sub1");
//    connectableFlux.subscribe(sub1);
//    log.info("Nothing happens until second subcriber connects after 2 sec");
//    Uninterruptibles.sleepUninterruptibly(Duration.ofSeconds(2));
//
//    log.info("Connecting second subscriber");
//    final DemoSubscriber<Long> sub2 = new DemoSubscriber<>("sub2");
//    connectableFlux.subscribe(sub2);
//
//
//    Uninterruptibles.sleepUninterruptibly(Duration.ofSeconds(1));
//    log.info("Canceling first subscriber");
//    sub1.getSubscription().cancel();
//
//
//    Uninterruptibles.sleepUninterruptibly(Duration.ofSeconds(2));
//    log.info("Canceling second subscriber");
//    sub2.getSubscription().cancel();
//
//    Uninterruptibles.sleepUninterruptibly(Duration.ofSeconds(1));
//    log.info("Reconnecting after 1 second before grace period was elapsed");
//    connectableFlux.subscribe(sub2);
//
//    Uninterruptibles.sleepUninterruptibly(Duration.ofSeconds(2));
//    log.info("Canceling subscriber");
//    sub2.getSubscription().cancel();
//
//    Uninterruptibles.sleepUninterruptibly(Duration.ofSeconds(3));
//    log.info("Reconnecting after 3 second when grace period was elapsed");
//    connectableFlux.subscribe(sub2);


    //Like refCount() connects to the source when first subscriber is subscribed but in contrast with refCount() it doesnt disconnect
    // from source when the last subscriber disconnects so the initial source keeps emitting values and after a while someone can reconnect and
    //get ongoing values from source.
//    final Flux<Long> flux = Flux.interval(Duration.ofMillis(200)).log().publish().autoConnect();
//    final DemoSubscriber<Long> sub1 = new DemoSubscriber<>("sub1");
//    flux.subscribe(sub1);
//    final DemoSubscriber<Long> sub2 = new DemoSubscriber<>("sub2");
//    flux.subscribe(sub2);
//
//    log.info("Before Connect nothing happens");
//    Uninterruptibles.sleepUninterruptibly(Duration.ofSeconds(1));
//    log.info("After connect !!!");
//
//    Uninterruptibles.sleepUninterruptibly(Duration.ofSeconds(1));
//    log.info("Canceling first subscriber");
//    sub1.getSubscription().cancel();
//
//    Uninterruptibles.sleepUninterruptibly(Duration.ofSeconds(2));
//    log.info("Canceling second subscriber");
//    sub2.getSubscription().cancel();
//
//    Uninterruptibles.sleepUninterruptibly(Duration.ofSeconds(2));
//    log.info("Reconnecting second subscriber");
//    flux.subscribe(sub2);

    //This version allows to provide the minimum number of subscribers after which to subscribe.
    //Even if subscriber subscribes and unsubscribes it is still counted for connection.
//    final Flux<Long> flux = Flux.interval(Duration.ofMillis(200)).log().publish().autoConnect(2);
//    final DemoSubscriber<Long> sub1 = new DemoSubscriber<>("sub1");
//    flux.subscribe(sub1);
//    log.info("Before second subscriber nothing happens");
//    Uninterruptibles.sleepUninterruptibly(Duration.ofSeconds(1));
//
//    log.info("Connecting second subscriber");
//    final DemoSubscriber<Long> sub2 = new DemoSubscriber<>("sub2");
//    flux.subscribe(sub2);
//
//
//    Uninterruptibles.sleepUninterruptibly(Duration.ofSeconds(1));
//    log.info("Canceling first subscriber");
//    sub1.getSubscription().cancel();
//
//    Uninterruptibles.sleepUninterruptibly(Duration.ofSeconds(2));
//    log.info("Canceling second subscriber");
//    sub2.getSubscription().cancel();
//
//    Uninterruptibles.sleepUninterruptibly(Duration.ofSeconds(2));
//    log.info("Reconnecting second subscriber");
//    flux.subscribe(sub2);

    //This version allows to provide consumer which will receive the disposable to use for disconnecting from source after it is disconnected no more
    //items get emitted from initial source and if someone tries to subscribes after it just doesn't receive any signal.
//    final Disposable[] disposable1 = {null};
//    final Flux<Long> flux = Flux.interval(Duration.ofMillis(200)).log().publish().autoConnect(2, disposable -> disposable1[0] = disposable);
//    final DemoSubscriber<Long> sub1 = new DemoSubscriber<>("sub1");
//    flux.subscribe(sub1);
//    log.info("Before second subscriber nothing happens");
//    Uninterruptibles.sleepUninterruptibly(Duration.ofSeconds(1));
//
//    log.info("Connecting second subscriber");
//    final DemoSubscriber<Long> sub2 = new DemoSubscriber<>("sub2");
//    flux.subscribe(sub2);
//
//
//    Uninterruptibles.sleepUninterruptibly(Duration.ofSeconds(1));
//    log.info("Canceling first subscriber");
//    sub1.getSubscription().cancel();
//
//    Uninterruptibles.sleepUninterruptibly(Duration.ofSeconds(2));
//    log.info("Canceling second subscriber");
//    sub2.getSubscription().cancel();
//
//    Uninterruptibles.sleepUninterruptibly(Duration.ofSeconds(2));
//    log.info("Disconnecting with disposable");
//    disposable1[0].dispose();
//
//    Uninterruptibles.sleepUninterruptibly(Duration.ofSeconds(2));
//    log.info("Reconnecting second subscriber");
//    flux.subscribe(sub2);

    //This is the alias for publish().refCount()
//    final Flux<Long> connectableFlux = Flux.interval(Duration.ofMillis(200)).share();
//    final DemoSubscriber<Long> sub1 = new DemoSubscriber<>("sub1");
//    connectableFlux.subscribe(sub1);
//    final DemoSubscriber<Long> sub2 = new DemoSubscriber<>("sub2");
//    connectableFlux.subscribe(sub2);
//
//
//    Uninterruptibles.sleepUninterruptibly(Duration.ofSeconds(1));
//    log.info("Canceling first subscriber");
//    sub1.getSubscription().cancel();
//
//
//    Uninterruptibles.sleepUninterruptibly(Duration.ofSeconds(2));
//    log.info("Canceling second subscriber");
//    sub2.getSubscription().cancel();

    new GateBasedSynchronization().waitForAny("onComplete");


  }
}
