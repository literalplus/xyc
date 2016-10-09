/*
 * Copyright (c) 2013 - 2015 xxyy (Philipp Nowak; devnull@nowak-at.net). All rights reserved.
 *
 * Any usage, including, but not limited to, compiling, running, redistributing, printing,
 *  copying and reverse-engineering is strictly prohibited without explicit written permission
 *  from the original author and may result in legal steps being taken.
 *
 * See the included LICENSE file (core/src/main/resources) or email xxyy98+xyclicense@gmail.com for details.
 */

package li.l1t.common.sql.sane;

import li.l1t.common.exception.DatabaseException;
import li.l1t.common.sql.QueryResult;
import li.l1t.common.sql.UpdateResult;

/**
 * Manages a connection to a JDBC database, providing methods to query and update it with plain SQL
 * statements. <p>Where applicable, prepared statements are used to inject parameters safely into
 * statements. {@code Object...} parameters will be added to statements in order. The MySQL driver
 * replaces occurrences of {@code ?} with these.</p>
 *
 * @author <a href="https://l1t.li/">Literallie</a>
 * @since 2016-10-07
 */
public interface SaneSql {
    /**
     * Executes a SQL query on the underlying database. No guarantees are made about what connection
     * is used. <p><b>Note:</b> Care should be taken at all times when querying SQL databases.
     * Requests may take long, and therefore should be off-loaded from the main server thread
     * whereever possible. Further, manually concatenating anything into the query string bears the
     * risk of SQL Injection. Use of {@code ?} placeholders in the query and the params array should
     * be preferred.</p> <p><b>Important:</b> To prevent memory leaks, the resulting {@link
     * QueryResult} should <b>always</b> be used with try-with-resources. Otherwise, leftover
     * prepared statement and result set objects will unnecessarily pollute the heap, possibly
     * leading to an {@link OutOfMemoryError} in the long run. Take care.</p>
     *
     * @param sqlQuery the SQL query to execute in the database
     * @param params   the parameters for {@code ?} placeholders in the query string, in order
     * @return an object containing the result of the query
     * @throws DatabaseException if an error occurs communicating with the database
     */
    QueryResult query(String sqlQuery, Object... params) throws DatabaseException;

    /**
     * Executes a SQL update statement on the underlying database. No guarantees are made about what
     * connection is used. <p><b>Note:</b> Care should be taken at all times when querying SQL
     * databases. Requests may take long, and therefore should be off-loaded from the main server
     * thread whereever possible. Further, manually concatenating anything into the query string
     * bears the risk of SQL Injection. Use of {@code ?} placeholders in the query and the params
     * array should be preferred.</p> <p><b>Important:</b> To prevent memory leaks, the resulting
     * {@link UpdateResult} should <b>always</b> be used with try-with-resources. Otherwise,
     * leftover prepared statement and result set objects will unnecessarily pollute the heap,
     * possibly leading to an {@link OutOfMemoryError} in the long run. Take care.</p>
     *
     * @param sqlQuery the SQL query to execute in the database
     * @param params   the parameters for {@code ?} placeholders in the query string, in order
     * @return an object containing the result of the update
     * @throws DatabaseException if an error occurs communicating with the database
     * @see #updateRaw(String, Object...) alternative that doesn't fetch generated keys
     */
    UpdateResult update(String sqlQuery, Object... params) throws DatabaseException;

    /**
     * Executes a SQL update statement on the underlying database. No guarantees are made about what
     * connection is used. This method differs from {@link #update(String, Object...)} in that it
     * does not fetch generated keys or anything like that, but just returns the {@link
     * java.sql.PreparedStatement#executeUpdate(String) raw JDBC result code}. However, it does
     * <b>close</b> the {@link java.sql.PreparedStatement} used to execute the
     * update.<p><b>Note:</b> Care should be taken at all times when querying SQL databases.
     * Requests may take long, and therefore should be off-loaded from the main server thread
     * wherever possible. Further, manually concatenating anything into the query string bears the
     * risk of SQL Injection. Use of {@code ?} placeholders in the query and the params array should
     * be preferred.</p> <p>Note that the underlying statement will already be closed when this
     * method returns, so there is no need for cleanup.</p>
     *
     * @param sqlQuery the SQL query to execute in the database
     * @param params   the parameters for {@code ?} placeholders in the query string, in order
     * @return the {@link java.sql.PreparedStatement#executeUpdate(String) raw JDBC result code}
     * @throws DatabaseException if an error occurs communicating with the database
     */
    int updateRaw(String sqlQuery, Object... params) throws DatabaseException;

    // We could also add batch update support like SafeSql has, but only if necessary
}
