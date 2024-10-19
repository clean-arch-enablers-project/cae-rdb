package com.cae.rdb;

public interface TableSchema<I>{

    I getPrimaryKey();
    void setPrimaryKey(I primaryKeyValue);

}
