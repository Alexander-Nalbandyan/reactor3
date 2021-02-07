package com.alna.reactor3.nitrite.aggregate;

import com.alna.reactor3.nitrite.entity.Customer;
import com.alna.reactor3.nitrite.entity.CustomerAddress;
import com.alna.reactor3.nitrite.entity.Product;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public class CustomerAggregateOperations {

    public static Mono<CustomerAggregate> aggregate(Flux<Object> customerPartObservable) {

      //Aggregates concatenated flux into mono with single element by populating CustomerAggregate object.
        return customerPartObservable.collect(
            CustomerAggregate::new,
            (customerAggregate, nextObject) -> {
                    if( nextObject instanceof Customer) {
                        customerAggregate.setCustomer((Customer)nextObject);
                    }
                    else if( nextObject instanceof CustomerAddress) {
                        customerAggregate.addCustomerAddress((CustomerAddress)nextObject);
                    }
                    else if( nextObject instanceof Product) {
                        customerAggregate.addOwnedProduct((Product)nextObject);
                    }
                }
        );
    }
}
