package com.alna.reactor3.utility.processors;


import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;

import java.util.HashMap;

public class ProcessorManager {

    private HashMap<String, NamedProcessor> processorMap;

    public ProcessorManager() {
        this.processorMap = new HashMap<>();
    }

    public void registerProcessor(NamedProcessor namedProcessor) {
        processorMap.put(namedProcessor.getProcessorName(), namedProcessor);
    }

    public void deregisterProcessor(NamedProcessor namedProcessor) {
        processorMap.remove(namedProcessor.getProcessorName());
    }

    public void addEventProducer(String processorName , Publisher observable) {

        if( processorMap.containsKey(processorName)) {
            processorMap.get(processorName).addEventProducer(observable);
        }
    }

    public void addEventConsumer(String processorName, Subscriber observer ) {

        if(processorMap.containsKey(processorName)) {
           processorMap.get(processorName).addEventConsumer(observer);
        }
    }
}