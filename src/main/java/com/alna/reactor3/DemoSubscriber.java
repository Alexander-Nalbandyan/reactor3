package com.alna.reactor3;

import com.alna.reactor3.utility.GateBasedSynchronization;
import com.google.common.util.concurrent.Uninterruptibles;
import jdk.jshell.JShell;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;

import static com.google.common.base.Strings.nullToEmpty;

public class DemoSubscriber<T> implements Subscriber<T> {

  private Long initialRequestSize = Long.MAX_VALUE;
  private String name;
  private GateBasedSynchronization gate;
  private Duration delay;
  private Logger log  = LoggerFactory.getLogger(DemoSubscriber.class);
  private Subscription subscription;

  public DemoSubscriber(GateBasedSynchronization gate) {
    this.gate = gate;
    this.delay = null;
  }

  public DemoSubscriber(GateBasedSynchronization gate, Duration delay) {
    this.gate = gate;
    this.delay = delay;
  }

  public DemoSubscriber(String name) {
    this.name = name;
  }

  public DemoSubscriber(String name, Long initialRequestSize) {
    this.name = name;
    this.initialRequestSize = initialRequestSize;
  }

  public DemoSubscriber() {

  }

  @Override
  public void onSubscribe(Subscription s) {
    this.subscription = s;
    log.info("{} -> Subscribed", nullToEmpty(name));
    s.request(this.initialRequestSize);
  }

  @Override
  public void onNext(T o) {
    log.info("{}: OnNext -> {}", nullToEmpty(name), o);
    if (delay != null) {
      Uninterruptibles.sleepUninterruptibly(delay);
    }
  }

  @Override
  public void onError(Throwable t) {
    log.info("{}: OnError", nullToEmpty(name), t);
    if (gate != null) {
      gate.openGate("onError");
    }
  }

  @Override
  public void onComplete() {
    log.info("{}: OnComplete", nullToEmpty(name));
    if (gate != null) {
      gate.openGate("onComplete");
    }
  }



  public Subscription getSubscription() {
    return subscription;
  }
}
