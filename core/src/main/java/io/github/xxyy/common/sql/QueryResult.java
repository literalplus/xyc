/*
 * Copyright (c) 2014 xxyy (Philipp Nowak; devnull@nowak-at.net). All rights reserved.
 *
 * Any usage, including, but not limited to, compiling, running, redistributing, printing, copying and reverse-engineering is strictly prohibited without permission from the original author and may result in legal steps being taken.
 */

package io.github.xxyy.common.sql;

import org.jetbrains.annotations.Nullable;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Stores a {@link java.sql.PreparedStatement} and {@link java.sql.ResultSet} for an already executed SQL query.
 *
 * @author <a href="http://xxyy.github.io/">xxyy</a>
 * @since 12/02/14
 */
public class QueryResult implements AutoCloseable {
    @Nullable
    private PreparedStatement preparedStatement;
    @Nullable
    private ResultSet resultSet;
    /**
     * Return value of {@link java.sql.PreparedStatement#executeUpdate()}.
     * Set to -1 if the statement was a query.
     */
    private int updateReturn;

    public QueryResult(PreparedStatement statement, int updateReturn) {
        this(statement, null, updateReturn);
    }

    public QueryResult(PreparedStatement statement, ResultSet rs) {
        this(statement, rs, -1);
    }

    public QueryResult(@Nullable PreparedStatement preparedStatement, @Nullable ResultSet resultSet, int updateReturn) {
        this.preparedStatement = preparedStatement;
        this.resultSet = resultSet;
        this.updateReturn = updateReturn;
    }

    /**
     * Shortcut for {@link QueryResult#getResultSet()}.
     *
     * @see #getResultSet()
     */
    public ResultSet rs() {
        return resultSet;
    }

    /**
     * Asserts that this result has a {@link java.sql.ResultSet} associated with it.
     *
     * @return This object for convenient construction.
     * @throws java.lang.IllegalStateException If this result does not have a {@link java.sql.ResultSet} associated with it.
     * @see #rs()
     * @see #getResultSet()
     * @see #vouchForResultSet()
     */
    public QueryResult assertHasResultSet() {
        if (resultSet == null) {
            throw new IllegalStateException("QueryResult does not have ResultSet when required!");
        }

        return this;
    }

    /**
     * Makes sure that this object has a ResultSet associated with it.
     *
     * @return This object for convenient call chaining
     * @throws SQLException If this objects does not have a ResultSet associated with it.
     * @see #assertHasResultSet()
     */
    public QueryResult vouchForResultSet() throws SQLException {
        if (resultSet == null) {
            throw new SQLException("No ResultSet associated with QueryResult when asked to vouch!");
        }

        return this;
    }

    @Override
    public void close() throws SQLException {
        if (preparedStatement == null) {
            if (resultSet != null) {
                resultSet.close();
                resultSet = null;
            }
            return;
        }
        preparedStatement.close();
        preparedStatement = null;
        resultSet = null;
    }

    /**
     * Tries to close the statement and result set held by this QueryResult.
     * They are set to {@code null}.
     */
    public void tryClose() {
        try {
            close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Nullable
    public PreparedStatement getPreparedStatement() {
        return this.preparedStatement;
    }

    @Nullable
    public ResultSet getResultSet() {
        return this.resultSet;
    }

    public int getUpdateReturn() {
        return this.updateReturn;
    }
}
