package com.alna.reactor3;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Flux;

public class ApplyingMathToEmmitedValues {
  private static Logger log = LoggerFactory.getLogger(ApplyingMathToEmmitedValues.class);

  public static void main(String[] args) {
    Flux.range(1, 100).map(i -> String.format("%s -squared -> %s", i, i * i))
    .subscribe( i -> log.info("received i: {}", i));
  }
}
