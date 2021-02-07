package com.alna.reactor3.module4;

import com.alna.reactor3.DemoSubscriber;
import com.alna.reactor3.nitrite.NO2;
import com.alna.reactor3.nitrite.NitriteTestDatabase;
import com.alna.reactor3.nitrite.NitriteUnitOfWorkWithResult;
import com.alna.reactor3.nitrite.datasets.NitriteGreekAlphabetSchema;
import com.alna.reactor3.nitrite.entity.LetterPair;
import com.alna.reactor3.utility.GateBasedSynchronization;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.event.Level;
import reactor.core.publisher.Flux;

import java.io.IOException;
import java.util.Optional;

public class UsingExample1 {

    private final static Logger log = LoggerFactory.getLogger(UsingExample1.class);

    public static void main(String[] args) {

        GateBasedSynchronization gate = new GateBasedSynchronization();

        // Set up an Observable that needs to manage the lifetime of a
        // resource.  In this case, we will use a Nitrite document database.
        //Initializes the resource for each subscriber returns flux which  emits items from created flux and calls cleanup function
       // just before completing the publisher.
        Flux<LetterPair> flux = Flux.using(

                // The first parameter takes the resource creation function.
                UsingExample1::resourceCreator,

                // Next we provide a function that will makeObservable the Observable
                // that will watch the events on this Observable.
                UsingExample1::fluxCreator,

                // Finally, "using" takes a function that is called to dispose
                // of the resource.  In this case, that's our Nitrite database.
                UsingExample1::resourceDisposer);

                // Subscribe using our DemoSubscriber.
                flux.subscribe(new DemoSubscriber<>("sub1"));
                flux.subscribe(new DemoSubscriber<>("sub2"));

        // Wait for things to finish...
        gate.waitForAny("onComplete", "onError");

        System.exit(0);
    }

    private static NitriteTestDatabase resourceCreator() {

        try {
            log.info( "resourceCreator - opening database" );

            // Create a Nitrite database and initialize it with a collection
            // that contains LetterPairs for the Greek alphabet with English
            // word representations.
            return new NitriteTestDatabase(Optional.of(new NitriteGreekAlphabetSchema()));
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    private static Flux<LetterPair> fluxCreator(NitriteTestDatabase inDatabase) {

        log.info( "fluxCreator - opening Flux" );

        // Create an Observable that pulls the Greek alphabet from the Nitrite
        // database.
        return NO2.execute(
                // NO2 is a helper class I created to make some of the details about
                // working with Nitrite opaque for purposes of our examples.

                // NO2.execute's first parameter is the actual Nitrite instance.
                inDatabase.getNitriteDatabase(),

                // The next parameter is a NitriteUnitOfWorkWithResult that takes the
                // Nitrite database instance and passed it to a lambda expression
                // that generates our Observable.
                (NitriteUnitOfWorkWithResult<Flux<LetterPair>>) database ->

                        // This creates the Observable from an Iterable, which happens to be
                        // a Nitrite result set cursor.
                        Flux.fromIterable(

                                // Get the LetterPair repository
                                database.getRepository(LetterPair.class)

                                        // Call "find" with no query to get all of the
                                        // LetterPair documents in the collection.
                                        .find()));
    }

    private static void resourceDisposer(NitriteTestDatabase database) {

        log.info( "resourceDisposer - closing database");

        try {
            // Close the Nitrite database.
            database.close();
        } catch (IOException e) {

        }
    }
}
