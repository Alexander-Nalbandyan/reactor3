package com.alna.reactor3.utility.processors;



import org.reactivestreams.Processor;
import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;
import reactor.core.publisher.DirectProcessor;

import java.util.HashMap;

public class SelectableProcessor<TEventType> {

    private Processor<TEventType, TEventType> internalSubject;
    private HashMap<Publisher<TEventType>, Subscription> producerTrackingMap;
    private HashMap<Subscriber<TEventType>, Subscription> consumerTrackingMap;

    public SelectableProcessor() {
        this.producerTrackingMap = new HashMap<>();
        this.consumerTrackingMap = new HashMap<>();
        this.internalSubject = DirectProcessor.create();
    }

    public SelectableProcessor(Processor<TEventType, TEventType> subjectToUse) {
        this.producerTrackingMap = new HashMap<>();
        this.consumerTrackingMap = new HashMap<>();
        this.internalSubject = subjectToUse;
    }

    public synchronized void addEventProducer(Publisher<TEventType> newEventSource) {

        // Have the internalSubject subscribe to the incoming event source.
        newEventSource.subscribe(new Subscriber<>() {
          @Override
          public void onSubscribe(Subscription s) {
            internalSubject.onSubscribe(s);
            // Intercept the onSubscribe and track the associated disposable.
            producerTrackingMap.put(newEventSource, s);
          }

          @Override
          public void onNext(TEventType tEventType) {
            internalSubject.onNext(tEventType);
          }

          @Override
          public void onError(Throwable t) {
            internalSubject.onError(t);
          }

          @Override
          public void onComplete() {
            internalSubject.onComplete();
          }
        });
    }

    public synchronized void removeEventProducer(Publisher<TEventType> eventSourceToRemove) {

        if( producerTrackingMap.containsKey(eventSourceToRemove)) {
            // Remove the tracking reference and call dispose to stop
            // the flow of messages to the subject.
            producerTrackingMap.remove(eventSourceToRemove).cancel();
        }
    }

    public synchronized void addEventConsumer(Subscriber<TEventType> newConsumer) {

      internalSubject.subscribe(new Subscriber<>() {
        @Override
        public void onSubscribe(Subscription s) {
          // Intercept the disposable for this subscription
          newConsumer.onSubscribe(s);
          // Pass the onSubscribe along to the Observer.
          consumerTrackingMap.put(newConsumer, s);
        }

        @Override
        public void onNext(TEventType tEventType) {
          newConsumer.onNext(tEventType);
        }

        @Override
        public void onError(Throwable t) {
          newConsumer.onError(t);
        }

        @Override
        public void onComplete() {
          newConsumer.onComplete();
        }
      });
    }

    public synchronized void detachEventConsumer(Subscriber<TEventType> consumerToRemove) {

        if( consumerTrackingMap.containsKey(consumerToRemove)) {

            // Remove the reference from the tracking map and then
            // call dispose to stop the flow of events.
            consumerTrackingMap.remove(consumerToRemove).cancel();
        }
    }
}
