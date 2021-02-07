package com.alna.reactor3.nitrite.dataaccess;

import com.alna.reactor3.nitrite.entity.Customer;
import com.alna.reactor3.nitrite.utility.NitriteFlowableCursorState;
import org.dizitart.no2.Nitrite;
import org.dizitart.no2.objects.filters.ObjectFilters;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Flux;

import java.util.Iterator;
import java.util.UUID;

public class CustomerDataAccess {

    private final static Logger log = LoggerFactory.getLogger(com.alna.reactor3.nitrite.dataaccess.CustomerDataAccess.class);

    public static Flux<Customer> select(Nitrite db, UUID customerId) {

        return Flux.generate(() -> db.getRepository(Customer.class)
                .find(ObjectFilters.eq("customerId", customerId)).iterator(),
                (customers, objectEmitter) -> {
                    try {
                        if (customers.hasNext()) {
                            Customer nextCustomer = customers.next();
                            log.info("onNext - {}", nextCustomer);
                            objectEmitter.next(nextCustomer);
                        } else {
                            log.info("onComplete");
                            objectEmitter.complete();
                        }
                    }
                    catch(Throwable t) {
                        log.error(t.getMessage(),t);
                        objectEmitter.error(t);
                    }
                  return customers;
                }
                )
                .doOnSubscribe(disposable -> log.info("onSubscribe"))
                .cast(Customer.class);
    }

    public static Flux<Customer> select(Nitrite db){

        return Flux.generate(
            () -> new NitriteFlowableCursorState(db.getRepository(Customer.class).find()),
                (state, customerEmitter) -> {
                    try {
                        Iterator<Customer> customerIterator = state.getIterator();
                        if (customerIterator.hasNext() == false) {
                            customerEmitter.complete();
                        } else {

                            customerEmitter.next(customerIterator.next());
                        }
                    }
                    catch(Throwable t) {
                        customerEmitter.error(t);
                    }
                    return state;
                }
        );
    }
}
