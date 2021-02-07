package com.alna.reactor3.processors;

public class ProcessorsTest {
  public static void main(String[] args) {
    //Processor can be used both for publishing elements and for listening to elements.
    //In most of the case you should avoid using it because it is error phrone and harder to use correctly.
    //If you want to decide whether you need Processor or not you first need to check if you can't accomplish the goal using
    // operators or using generate() which allows to bridge non legacy api by pupulating stream of data or signaling end when it terminates.

    //First issue is that if the processor is somehow leaked and available to the other parties then they can call onNext() onError() and can cause unexpected results.
    //Second multiple threads can call onNext() at the same time thus violating contract that onNext() calls must be single thread thus it is safer to use sink() instead


  }
}
