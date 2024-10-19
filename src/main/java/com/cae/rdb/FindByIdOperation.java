package com.cae.rdb;

import java.util.Optional;

public interface FindByIdOperation <E, I>{

    Optional<E> findById(I id);
    Boolean existsById(I id);

}
