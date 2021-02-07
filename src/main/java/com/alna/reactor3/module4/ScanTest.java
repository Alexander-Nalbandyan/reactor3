package com.alna.reactor3.module4;

import reactor.core.publisher.Flux;

import java.util.ArrayList;

public class ScanTest {
  public static void main(String[] args) {
//    Reduces values in initial source using provided combiner as initial value the first emission will be used.
    Flux.range(1, 10).log().scan((x, y) -> x + y).log().subscribe();

    //This version provides a way to provide initial value, it will reuse the same initial value for all subscribers.
//    final Flux<ArrayList<Integer>> scanExample1 = Flux.range(1, 10).log().scan(new ArrayList<Integer>(), (list, i) -> {
//      list.add(i);
//      return list;
//    }).log();
//    scanExample1.subscribe();
//    scanExample1.subscribe();

    //In contrast with scan() the scanWith() provides lazy supplier which is called for each subscriber and thus will be initialized each time
    //someone subscribes to it.
//    final Flux<ArrayList<Object>> scanWithExample = Flux.range(1, 10).log().scanWith(ArrayList::new, (list, i) -> {
//      list.add(i);
//      return list;
//    }).log();
//    scanWithExample.subscribe();
//    scanWithExample.subscribe();
//    scanWithExample.subscribe();
  }
}
