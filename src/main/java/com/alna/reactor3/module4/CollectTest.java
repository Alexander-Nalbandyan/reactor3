package com.alna.reactor3.module4;

import com.alna.reactor3.utility.GateBasedSynchronization;
import reactor.core.publisher.Flux;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.function.Function;
import java.util.stream.Collectors;

public class CollectTest {
  public static void main(String[] args) {
    GateBasedSynchronization gate = new GateBasedSynchronization();

    //Combines all emitted elements into list and emits single list element from the resulting flux.
//    Flux.range(1, 10).log().collectList().log().subscribe();


    // Collects elements emitted from given flux using provided collector then
    // emmits collected data from mono when the source completes.
//    Flux.range(1, 10).log().collect(Collectors.toList()).log().subscribe();


    //Collects emitted elements from Flux into container provided as first argument and adds elements using the second argument function.
//    Flux.range(1, 10).log().collect(ArrayList::new, ArrayList::add).log().subscribe();

    // Collects elements emitted from given flux into map with key extracted by given function. If same key  is extracted for several items
    // then the last item will be saved in map.
//    Flux.range(1, 10).log().collectMap(i -> i % 5).log().subscribe();

    //This version allows to supply value extractor as well.
//    Flux.range(1, 10).log().collectMap(i -> i % 5, Function.identity()).log().subscribe();

    //This version allows to supply map factory method to create instance of Map for each subscriber.
//    Flux.range(1, 10).log().collectMap(i -> i % 5, Function.identity(), HashMap::new).log().subscribe();

    //Instead of overriding value in case of same key this version combines those values associated with same key into a list.
//    Flux.range(1, 10).log().collectMultimap(i -> i % 5).log().subscribe();

    //This  version allows to specify value extractor like in case of collectMap()
//    Flux.range(1, 10).log().collectMultimap(i -> i % 5, Function.identity()).log().subscribe();

    //This  version allows to specify map factory method like in case of collectMap()
//    Flux.range(1, 10).log().collectMultimap(i -> i % 5, Function.identity(), HashMap::new).log().subscribe();

    //Collects elements emitted from source then sorts them in natural order into a list.
//    Flux.just(5, 2, 1, 3, 10, 4, 9).log().collectSortedList().log().subscribe();

    //This version allows to provide comparator to use for sorting.
    Flux.just(5, 2, 1, 3, 10, 4, 9).log().collectSortedList(Comparator.reverseOrder()).log().subscribe();

    gate.waitForAny("onError", "onComplete");
  }
}
