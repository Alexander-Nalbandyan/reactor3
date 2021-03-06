package com.alna.reactor3.dataaggregation;

import com.alna.reactor3.nitrite.NitriteTestDatabase;
import com.alna.reactor3.nitrite.aggregate.CustomerAggregate;
import com.alna.reactor3.nitrite.aggregate.CustomerAggregateOperations;
import com.alna.reactor3.nitrite.dataaccess.CustomerAddressDataAccess;
import com.alna.reactor3.nitrite.dataaccess.CustomerDataAccess;
import com.alna.reactor3.nitrite.dataaccess.CustomerProductPurchaseHistoryDataAccess;
import com.alna.reactor3.nitrite.datasets.NitriteCustomerDatabaseSchema;
import com.alna.reactor3.nitrite.entity.Customer;
import com.alna.reactor3.nitrite.entity.CustomerAddress;
import com.alna.reactor3.nitrite.entity.Product;
import com.google.common.base.Functions;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.io.IOException;
import java.util.Optional;
import java.util.function.Function;

@Slf4j
public class ServiceAggregationWithConcurrencyControl {
  public static void main(String[] args) {
    final NitriteCustomerDatabaseSchema schema = new NitriteCustomerDatabaseSchema();
    try (NitriteTestDatabase testDatabase = new NitriteTestDatabase(Optional.of(schema))) {

      //Subscribe on io scheduler
      final Flux<Customer> customerFlux =
          CustomerDataAccess.select(testDatabase.getNitriteDatabase(), schema.Customer1UUID)
          .subscribeOn(Schedulers.elastic());

      final Flux<CustomerAddress> customerAddressFlux = CustomerAddressDataAccess.select(
          testDatabase.getNitriteDatabase(), schema.Customer1UUID)
          .subscribeOn(Schedulers.elastic());

      final Flux<Product> ownedProductList = CustomerProductPurchaseHistoryDataAccess.selectOwnedProducts(
          testDatabase.getNitriteDatabase(), schema.Customer1UUID).subscribeOn(Schedulers.elastic());

      //Cast casts produced type to the given type after which Flux becomes of that result type.
      //Until now nothing is executed(even after we concat) as we remember flux is only executed on subscription.

      //This version in contrast to merge allows to control how many publishers will be subscribed concurrently.
      final Flux<Object> customerAggregateStream =
          Flux.fromArray(new Flux[] {customerFlux, customerAddressFlux, ownedProductList})
              //First argument contains the function for converting emitted items from original flux which in our case are publishers into
              // new publishers which in this case we want to be the same.
          .flatMap((Function) Functions.identity(), 2);


      final Mono<CustomerAggregate> aggregate = CustomerAggregateOperations.aggregate(customerAggregateStream);

      //This is when actually concatenated flux gets subscribed and sequentially consumes items from 3 sources and aggregates them with CustomerAggregateOperations.
      final CustomerAggregate finalCustomer = aggregate.block();

      log.info("Customer -----> {}", finalCustomer.toString());

    } catch (IOException e) {
      log.error("Error when aggregating data: {}", e.getMessage(), e);


    }
  }
}
