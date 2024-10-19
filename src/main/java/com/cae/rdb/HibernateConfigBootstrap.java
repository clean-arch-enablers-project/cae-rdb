package com.cae.rdb;

import com.cae.env_vars.EnvVarRetriever;
import com.cae.mapped_exceptions.specifics.InternalMappedException;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class HibernateConfigBootstrap {

    public static final SessionFactory SESSION_FACTORY;

    static {
        try {
            var dataSource = HikariDataSourceFactory.createDataSource();
            var hibernateConfig = new Configuration();
            hibernateConfig.getProperties().put("hibernate.connection.datasource", dataSource);
            hibernateConfig.setProperty("hibernate.dialect", EnvVarRetriever.getEnvVarByNameAsString("HIBERNATE_DIALECT"));
            hibernateConfig.setProperty("hibernate.show_sql", EnvVarRetriever.getEnvVarByNameAsString("HIBERNATE_SHOW_SQL"));
            hibernateConfig.setProperty("hibernate.hbm2ddl.auto", EnvVarRetriever.getEnvVarByNameAsString("HIBERNATE_DDL_AUTO_TYPE"));
            EntityClassesProvider.getClasses().forEach(hibernateConfig::addAnnotatedClass);
            SESSION_FACTORY = hibernateConfig.buildSessionFactory();
        } catch (Exception e) {
            throw new InternalMappedException(
                    "Failed to create sessionFactory object.",
                    "More details: " + e);
        }
    }

    public static void shutdown() {
        SESSION_FACTORY.close();
    }
    
}
