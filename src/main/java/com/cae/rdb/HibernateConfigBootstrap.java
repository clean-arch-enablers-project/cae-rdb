package com.cae.rdb;

import com.cae.mapped_exceptions.specifics.InternalMappedException;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class HibernateConfigBootstrap {

    public static SessionFactory createNewBasedOn(CaeRdbConnectionFactory caeRdbConnectionFactory) {
        try {
            var dataSource = HikariDataSourceFactory.createDataSourceBasedOn(caeRdbConnectionFactory);
            var hibernateConfig = new Configuration();
            hibernateConfig.getProperties().put("hibernate.connection.datasource", dataSource);
            hibernateConfig.setProperty("hibernate.dialect", caeRdbConnectionFactory.getDialectOption());
            hibernateConfig.setProperty("hibernate.show_sql", caeRdbConnectionFactory.getShowSqlOption().toString());
            hibernateConfig.setProperty("hibernate.hbm2ddl.auto", caeRdbConnectionFactory.getDdlAutoTypeOption());
            EntityClassesProvider.getClasses().forEach(hibernateConfig::addAnnotatedClass);
            return hibernateConfig.buildSessionFactory();
        } catch (Exception e) {
            throw new InternalMappedException(
                    "Failed to create sessionFactory object.",
                    "More details: " + e);
        }
    }

}
