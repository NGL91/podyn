package com.citusdata.migration;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.github.strengthened.prometheus.healthchecks.HealthCheck;
import com.github.strengthened.prometheus.healthchecks.HealthStatus;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Logger;

public class AppHealthCheck extends HealthCheck {
    private final Logger logger = Logger.getLogger(this.getClass().getName());
    private final AmazonDynamoDB amazonDynamoDB;
    private final String jdbcUrl;

    public AppHealthCheck(AmazonDynamoDB amazonDynamoDB, String jdbcUrl) {
        this.amazonDynamoDB = amazonDynamoDB;
        this.jdbcUrl = jdbcUrl;
    }

    @Override
    public HealthStatus check() {
        if (amazonDynamoDB.listTables().getTableNames().isEmpty()) {
            logger.info("Can't find any DynamoDB tables");
            return HealthStatus.UNHEALTHY;
        }

        try (Connection connection = DriverManager.getConnection(jdbcUrl)) {
        } catch (SQLException e) {
            logger.info("Can not connect the Postgres!");
            return HealthStatus.UNHEALTHY;
        }

        return HealthStatus.HEALTHY;
    }
}
