package com.cae.rdb;

import org.hibernate.SessionFactory;

import java.util.List;
import java.util.Optional;

public abstract class CaeRdbConnectionFactory {

    private SessionFactory sessionFactory;

    protected abstract List<DefaultBasicCrudOperations<? extends TableSchema<?>, ?>> initializeAllRepositories();
    protected abstract String getJdbcUrl();
    protected abstract String getDbUser();
    protected abstract String getDbUserSecret();
    protected abstract String getDbPoolConnectionDriver();
    protected abstract Integer getDbPoolConnectionMaxSize();
    protected abstract Integer getDbPoolConnectionMinIdle();
    protected abstract Integer getDbPoolConnectionIdleTimeout();
    protected abstract String getDialectOption();
    protected abstract Boolean getShowSqlOption();
    protected abstract String getDdlAutoTypeOption();

    public void startConnection(){
        if (this.sessionFactory == null){
            var allRepositories = this.initializeAllRepositories();
            this.sessionFactory = HibernateConfigBootstrap.createNewBasedOn(this);
            allRepositories.forEach(repository -> repository.sessionFactory = this.sessionFactory);
        }
    }

    public void endConnection(){
        Optional.ofNullable(this.sessionFactory).ifPresent(SessionFactory::close);
    }

}
