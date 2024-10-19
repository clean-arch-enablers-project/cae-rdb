package com.cae.rdb;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public abstract class TableSchemaFromEntity<E, I> implements TableSchema<I> {

    private final E entity;

}
