package com.cae.rdb;

import com.zaxxer.hikari.HikariDataSource;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;


@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class HikariDataSourceFactory {

    public static HikariDataSource createDataSourceBasedOn(CaeRdbConnectionFactory caeRdbConnectionFactory){
        var hikariConfig = HikariConfigBootstrap.createHikariConfigBasedOn(caeRdbConnectionFactory);
        return new HikariDataSource(hikariConfig);
    }

}
