package com.cae.rdb;


public interface BasicCrudOperations<T extends TableSchema<I>, I> extends CreateOperation<T>,
        RetrieveAllOperation<T>,
        RetrievePaginatedOperation<T>,
        FindByIdOperation<T, I>,
        UpdateOperation<T>,
        DeleteOperation<I>{
}
