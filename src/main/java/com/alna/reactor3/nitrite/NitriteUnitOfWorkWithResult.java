package com.alna.reactor3.nitrite;

import org.dizitart.no2.Nitrite;

@FunctionalInterface
public interface NitriteUnitOfWorkWithResult<T> {
    T apply(Nitrite database) throws Exception;
}
