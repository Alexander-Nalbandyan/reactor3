package com.alna.reactor3.schedulers;

import reactor.core.publisher.Flux;

public class ZipTests {
  public static void main(String[] args) {
    final Flux<Integer> flux1 = Flux.range(1, 10);
    final Flux<Integer> flux2 = Flux.range(1, 5);

    //Zip operator merges 2 fluxes together pairing values emitted by them in order.
    //If one of them completes or signals an error immediately as well.
//    Flux.zip(flux1, flux2).log().subscribe();
    //It operates values one at a time not in parallel even if we provide specific scheduler to run on.
//    flux1.zipWith(Flux.error(new Throwable("Asda"))).log().subscribe();
  }
}
