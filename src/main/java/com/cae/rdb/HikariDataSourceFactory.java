package com.cae.rdb;

import com.zaxxer.hikari.HikariDataSource;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;


@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class HikariDataSourceFactory {

    public static HikariDataSource createDataSource(){
        var hikariConfig = HikariConfigBootstrap.createHikariConfig();
        return new HikariDataSource(hikariConfig);
    }

}
