package com.alna.reactor3;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.concurrent.Callable;
import java.util.concurrent.CompletableFuture;

public class CreatingMono {
  public static void main(String[] args) {
    Mono.just("2");
    Mono.fromCallable(new Callable<Object>() {
      @Override
      public Object call() throws Exception {
        return null;
      }
    });

    Mono.from(Flux.just(1, 2, 3));

    final Mono<Object> objectMono = Mono.fromFuture(new CompletableFuture<>());

  }

  // Both example give the same result
  Integer getAnyInteger() throws Exception {
    throw new RuntimeException("An error as occured for no reason.");
  }

  // Now, comparison between the two methods
  void compareMonoCreationMethods() {
    //Mono.callable() The passed callable can throw exception and that exception is  automatically wrapped in Mono.error()
    Mono<Integer> fromCallable = Mono.fromCallable(this::getAnyInteger);
    // result -> Mono.error(RuntimeException("An error as occured for no reason."))


    //In case of Mono.defer() the passed supplier must return Mono and you need to catch exception yourself and wrap it with
    // Mono.error() if needed.
    Mono<Integer> defer = Mono.defer(() -> {
      try { Integer res = this.getAnyInteger(); return Mono.just(res);}
      catch(Exception e) { return Mono.error(e); }
    });
    // result -> Mono.error(RuntimeException("An error as occured for no reason."))
  }

  void monoCreateExample() {
    //Mono.create() is low level api and in this case one needs to send low level signals like success(), error()
    Mono<Integer> t= Mono.create(callback -> {
      try { callback.success(this.getAnyInteger()); }
      catch (Exception e) { callback.error(e); }

    });
  }
}
