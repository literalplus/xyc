/*
 * Copyright (c) 2013 - 2016 xxyy (Philipp Nowak; xyc@l1t.li). All rights reserved.
 *
 * Any usage, including, but not limited to, compiling, running, redistributing, printing,
 *  copying and reverse-engineering is strictly prohibited without explicit written permission
 *  from the original author and may result in legal steps being taken.
 *
 * See the included LICENSE file (core/src/main/resources) for details.
 */

package li.l1t.lanatus.sql;

import li.l1t.common.sql.SqlConnectable;
import li.l1t.common.sql.SqlConnectables;
import li.l1t.common.sql.sane.SingleSql;
import li.l1t.common.sql.sane.SqlConnected;
import org.flywaydb.core.Flyway;
import org.junit.rules.ExternalResource;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

/**
 * External resource rule that sets up the database with Flyway and H2.
 *
 * @author <a href="https://l1t.li/">Literallie</a>
 * @since 2016-10-20
 */
public class DatabaseSetup extends ExternalResource implements SqlConnected {
    private SingleSql sql;

    @Override
    protected void before() throws IOException {
        Properties flywayProperties = loadFlywayPropertiesFile();
        runFlywayMigration(flywayProperties);
        connectToH2(flywayProperties);
    }

    private void loadH2Driver() {
        try {
            Class.forName("org.h2.Driver");
        } catch (ClassNotFoundException e) {
            throw new AssertionError("missing h2 driver in classpath", e);
        }
    }

    private void runFlywayMigration(Properties flywayProperties) throws IOException {
        Flyway flyway = new Flyway();
        flyway.configure(flywayProperties);
        flyway.migrate();
    }

    private Properties loadFlywayPropertiesFile() throws IOException {
        Properties properties = new Properties();
        try (FileReader reader = new FileReader(findFlywaysPropertiesPath())) {
            properties.load(reader);
        }
        return properties;
    }

    private String findFlywaysPropertiesPath() {
        String currentDirectoryName = new File("").getAbsoluteFile().getName();
        if ("sql-itest".equals(currentDirectoryName)) {
            return "./flyway.properties";
        } else {
            return "./lanatus/sql-itest/flyway.properties"; //running tests in IntelliJ IDEA
        }
    }

    private void connectToH2(Properties flywayProperties) {
        loadH2Driver();
        sql = new SingleSql(credentialsFrom(flywayProperties));
    }

    private SqlConnectable credentialsFrom(Properties flywayProperties) {
        String jdbcUrl = flywayProperties.getProperty("junit.url");
        return SqlConnectables.fromCredentials(jdbcUrl, "mt_main", "", "");
    }

    @Override
    protected void after() {
        try {
            sql.close();
        } catch (Exception e) {
            throw new AssertionError(e);
        }
    }

    @Override
    public SingleSql sql() {
        return sql;
    }
}
