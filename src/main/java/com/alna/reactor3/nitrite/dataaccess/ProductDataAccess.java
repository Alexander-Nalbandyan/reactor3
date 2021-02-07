package com.alna.reactor3.nitrite.dataaccess;

import com.alna.reactor3.nitrite.entity.Product;
import org.dizitart.no2.Nitrite;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Flux;

import java.util.Iterator;
import java.util.Observable;

public class ProductDataAccess {

    private final static Logger log = LoggerFactory.getLogger(com.alna.reactor3.nitrite.dataaccess.ProductDataAccess.class);

    public static Flux<Product> select(Nitrite db) {
        return Flux.create(emitter -> {

            try {
                Iterator<Product> iterator = db.getRepository(Product.class)
                        .find()
                        .iterator();

                while (iterator.hasNext()) {
                    Product nextProduct = iterator.next();
                    //log.info(nextProduct.toString());
                    emitter.next(nextProduct);
                }
                emitter.complete();
            }
            catch( Throwable t ) {
                emitter.error(t);
            }
        });
    }
}
