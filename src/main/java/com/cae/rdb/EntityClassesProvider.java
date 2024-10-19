package com.cae.rdb;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class EntityClassesProvider {

    private static final List<Class<?>> CLASSES = new ArrayList<>();

    public static void addEntityClass(Class<?> clazz){
        EntityClassesProvider.CLASSES.add(clazz);
    }

    public static List<Class<?>> getClasses(){
        return EntityClassesProvider.CLASSES;
    }

}
