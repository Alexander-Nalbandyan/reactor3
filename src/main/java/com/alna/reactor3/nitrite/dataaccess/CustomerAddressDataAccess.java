package com.alna.reactor3.nitrite.dataaccess;

import com.alna.reactor3.nitrite.entity.CustomerAddress;
import org.dizitart.no2.Nitrite;
import org.dizitart.no2.objects.filters.ObjectFilters;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Flux;

import java.util.Iterator;
import java.util.Observable;
import java.util.UUID;

public class CustomerAddressDataAccess {

    private static final Logger log = LoggerFactory.getLogger(com.alna.reactor3.nitrite.dataaccess.CustomerAddressDataAccess.class);

    public static Flux<CustomerAddress> select(Nitrite db, UUID customerId) {

        return Flux.generate(
            () -> db.getRepository(CustomerAddress.class).find(
                ObjectFilters.eq("customerId", customerId)).iterator(),
            (customerAddressIterator, emitter) -> {
                    try {
                        if (customerAddressIterator.hasNext()) {
                            CustomerAddress nextAddress = customerAddressIterator.next();
                            log.info( "onNext - {}", nextAddress);
                            emitter.next(nextAddress);
                        } else {
                            log.info("onComplete");
                            emitter.complete();
                        }
                    }
                    catch( Throwable t ) {
                        log.error(t.getMessage(),t);
                        emitter.error(t);
                    }
                    return customerAddressIterator;
                }
                ).doOnSubscribe(disposable -> {
                    log.info("onSubscribe");
                })
                .cast(CustomerAddress.class);
    }
}
