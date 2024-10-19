package com.cae.rdb;

import java.util.List;

public abstract class CaeRdbConnectionFactory {

    protected abstract List<DefaultBasicCrudOperations<? extends TableSchema<?>, ?>> initializeAllRepositories();

    public void startConnection(){
        var allRepositories = this.initializeAllRepositories();
        var sessionFactory = HibernateConfigBootstrap.SESSION_FACTORY;
        allRepositories.forEach(repository -> repository.sessionFactory = sessionFactory);
    }

    public void endConnection(){
        HibernateConfigBootstrap.shutdown();
    }

}
