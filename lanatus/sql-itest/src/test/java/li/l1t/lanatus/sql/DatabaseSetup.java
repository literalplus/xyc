/*
 * MIT License
 *
 * Copyright (C) 2013 - 2017 Philipp Nowak (https://github.com/xxyy) and contributors.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
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

    private void runFlywayMigration(Properties flywayProperties) {
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
