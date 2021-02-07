package com.alna.reactor3.processors;

import com.alna.reactor3.utility.MutableReference;
import com.alna.reactor3.utility.ThreadHelper;
import reactor.core.publisher.Flux;

import java.util.Observable;
import java.util.concurrent.TimeUnit;

public class UserServiceEventPublisher {

    private static final String[] emailList = new String[] {
        "test@test.com",
        "harold@pottery.com",
        "lazerus@moonslider.com",
        "mrwaterwings@deepwater.com"
    };

    public static Flux<EventBase> userServiceEventGenerator() {

        return Flux.generate(
                () -> new MutableReference<Integer>(0),
                (offset, eventBaseEmitter) -> {

                    // Restrict the offset to the size of our email list.
                    if( offset.getValue() >= emailList.length ) {

                        // If we are at the end of the list, then send
                        // the onComplete.
                        eventBaseEmitter.complete();
                    }
                    else {
                        // We are still in the list...send an update event with
                        // the correct email address.
                        eventBaseEmitter.next(new AccountCredentialsUpdatedEvent(emailList[offset.getValue()]));
                    }

                    // Increment out array offset
                    offset.setValue(offset.getValue() + 1);

                    // Slow things down
                    ThreadHelper.sleep(1, TimeUnit.SECONDS);

                    return offset;
                }
        );


    }
}