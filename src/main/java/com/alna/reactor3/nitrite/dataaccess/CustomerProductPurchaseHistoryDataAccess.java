package com.alna.reactor3.nitrite.dataaccess;

import com.alna.reactor3.nitrite.entity.CustomerProductPurchaseHistory;
import com.alna.reactor3.nitrite.entity.Product;
import org.dizitart.no2.Nitrite;
import org.dizitart.no2.objects.filters.ObjectFilters;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Flux;

import java.util.Observable;
import java.util.UUID;

public class CustomerProductPurchaseHistoryDataAccess {

    private final static Logger log = LoggerFactory.getLogger(
        com.alna.reactor3.nitrite.dataaccess.CustomerProductPurchaseHistoryDataAccess.class);

    public static Flux<CustomerProductPurchaseHistory> selectByCustomer(Nitrite db, UUID customerId) {

        return Flux.generate(() -> db.getRepository(CustomerProductPurchaseHistory.class)
                .find(ObjectFilters.eq("customerId", customerId))
                .iterator(),
                                   (historyIterator, emitter) -> {


                    try {
                        if (historyIterator.hasNext()) {
                            CustomerProductPurchaseHistory nextHistory = historyIterator.next();
                            log.info("onNext - {}", nextHistory);
                            emitter.next(nextHistory);
                        } else {
                            log.info("onComplete");
                            emitter.complete();
                        }
                    }
                    catch(Throwable t) {
                        log.error(t.getMessage(),t);
                        emitter.error(t);
                    }
                    return historyIterator;
                }
                )
                .doOnSubscribe(disposable -> log.info("onSubscribe"))
                .cast(CustomerProductPurchaseHistory.class);
    }

    public static Flux<Product> selectOwnedProducts(Nitrite db, UUID customerId) {

        return selectByCustomer(db,customerId)
                // Map the purchase history into a list of products
                .map(customerProductPurchaseHistory -> ProductCache.getProduct(db, customerProductPurchaseHistory.getProductId()));
    }
}
