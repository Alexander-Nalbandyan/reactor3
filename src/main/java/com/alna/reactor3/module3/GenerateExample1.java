package com.alna.reactor3.module3;


import com.alna.reactor3.DemoSubscriber;
import com.alna.reactor3.utility.GateBasedSynchronization;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Flux;

import java.util.Observable;
import java.util.concurrent.Callable;

public class GenerateExample1 {

    private final static Logger log = LoggerFactory.getLogger(GenerateExample1.class);

    public static void main(String[] args) {

        Flux<Integer> geometricSequence = makeObservable(1, 2, 8);
      GateBasedSynchronization gate = new GateBasedSynchronization();
      geometricSequence.subscribe(new DemoSubscriber<>(gate));
    }

    public static Flux<Integer> makeObservable(int start , int multiplier, int totalNumbers ) {

      //Emits items 1 by 1 using provided function with receives state and emitter(SynchronousSink) and on this emitter it can call complete()|next()
      //Also this function should return new state to be passed on the next call. As the first argument it receives initial state.
      //It also has version without state or without initial state.
      return Flux.generate(
                    () -> new GeometricSequenceState(start, multiplier, totalNumbers),
                    (state, emitter) -> {

                    // If we have reached the end, then emit and onComplete.
                    if(state.getCount() >= state.getTotalNumbers()) {
                        emitter.complete();
                        return state;
                    }

                    // Increment the number of values we have emitted
                    state.incrementCount();

                    // Emit the currently calculated
                    // value of the geometric sequence.
                    emitter.next(state.getCurrentValue());

                    // Calculate the next value in the sequence.
                    state.generateNextValue();

                    //Do we need to creat new state on generate ???
                    return state;
                });

    }


    public static class GeometricSequenceState {

        private final int multiplier;
        private final int totalNumbers;

        private int count;
        private int currentValue;

        public GeometricSequenceState(int start, int multiplier, int totalNumbers) {
            this.multiplier = multiplier;
            this.totalNumbers = totalNumbers;

            this.count = 0;
            this.currentValue = start;
        }

        public int getTotalNumbers() {
            return totalNumbers;
        }

        public int getCount() {
            return count;
        }

        public int getCurrentValue() {
            return currentValue;
        }

        public void incrementCount() {
            ++this.count;
        }

        public void generateNextValue() {
            this.currentValue = this.currentValue * this.multiplier;
        }
    }
}
