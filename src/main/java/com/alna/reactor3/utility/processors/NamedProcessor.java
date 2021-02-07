package com.alna.reactor3.utility.processors;

import org.reactivestreams.Processor;

public class NamedProcessor<TEventType> extends SelectableProcessor<TEventType> {

    private String processorName;

    public NamedProcessor(String processorName) {
        this.processorName = processorName;
    }

    public NamedProcessor(String subjectName, Processor<TEventType, TEventType> subjectToUse) {
        super(subjectToUse);
        this.processorName = subjectName;
    }

    public String getProcessorName() {
        return processorName;
    }
}