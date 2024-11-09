package com.cae.rdb;

import com.zaxxer.hikari.HikariConfig;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class HikariConfigBootstrap {

    public static HikariConfig createHikariConfigBasedOn(CaeRdbConnectionFactory caeRdbConnectionFactory) {
        var hikariConfig = new HikariConfig();
        HikariConfigBootstrap.setConnectionPropertiesIn(hikariConfig, caeRdbConnectionFactory);
        HikariConfigBootstrap.setPoolPropertiesIn(hikariConfig, caeRdbConnectionFactory);
        return hikariConfig;
    }

    private static void setConnectionPropertiesIn(HikariConfig hikariConfig, CaeRdbConnectionFactory caeRdbConnectionFactory) {
        hikariConfig.setJdbcUrl(caeRdbConnectionFactory.getJdbcUrl());
        hikariConfig.setUsername(caeRdbConnectionFactory.getDbUser());
        hikariConfig.setPassword(caeRdbConnectionFactory.getDbUserSecret());
        hikariConfig.setDriverClassName(caeRdbConnectionFactory.getDbPoolConnectionDriver());
    }

    private static void setPoolPropertiesIn(HikariConfig hikariConfig, CaeRdbConnectionFactory caeRdbConnectionFactory) {
        hikariConfig.setMaximumPoolSize(caeRdbConnectionFactory.getDbPoolConnectionMaxSize());
        hikariConfig.setMinimumIdle(caeRdbConnectionFactory.getDbPoolConnectionMinIdle());
        hikariConfig.setIdleTimeout(caeRdbConnectionFactory.getDbPoolConnectionIdleTimeout());
    }


}
