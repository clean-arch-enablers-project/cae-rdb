package com.cae.rdb;

import com.cae.env_vars.EnvVarRetriever;
import com.zaxxer.hikari.HikariConfig;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class HikariConfigBootstrap {

    public static HikariConfig createHikariConfig() {
        var hikariConfig = new HikariConfig();
        HikariConfigBootstrap.setConnectionPropertiesIn(hikariConfig);
        HikariConfigBootstrap.setPoolPropertiesIn(hikariConfig);
        return hikariConfig;
    }

    private static void setConnectionPropertiesIn(HikariConfig hikariConfig) {
        hikariConfig.setJdbcUrl(EnvVarRetriever.getEnvVarByNameAsString("DB_JDBC_URL"));
        hikariConfig.setUsername(EnvVarRetriever.getEnvVarByNameAsString("DB_USER"));
        hikariConfig.setPassword(EnvVarRetriever.getEnvVarByNameAsString("DB_PASS"));
        hikariConfig.setDriverClassName(EnvVarRetriever.getEnvVarByNameAsString("DB_POOL_CONNECTION_DRIVER"));
    }

    private static void setPoolPropertiesIn(HikariConfig hikariConfig) {
        hikariConfig.setMaximumPoolSize(EnvVarRetriever.getEnvVarByNameAsInteger("DB_POOL_CONNECTION_MAX_SIZE"));
        hikariConfig.setMinimumIdle(EnvVarRetriever.getEnvVarByNameAsInteger("DB_POOL_CONNECTION_MIN_IDLE"));
        hikariConfig.setIdleTimeout(EnvVarRetriever.getEnvVarByNameAsInteger("DB_POOL_CONNECTION_IDLE_TIMEOUT"));
    }


}
