package io.github.xxyy.common.sql;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Stores a {@link java.sql.PreparedStatement} and {@link java.sql.ResultSet} for an already executed SQL query.
 *
 * @author <a href="http://xxyy.github.io/">xxyy</a>
 * @since 12/02/14
 */
@AllArgsConstructor
public class QueryResult implements AutoCloseable {
    @Getter
    private PreparedStatement preparedStatement;
    @Getter
    private ResultSet resultSet;
    /**
     * Return value of {@link java.sql.PreparedStatement#executeUpdate()}.
     * Set to -1 if the statement was a query.
     */
    @Getter
    private int updateReturn;

    public QueryResult(PreparedStatement statement, int updateReturn){
        this(statement, null, updateReturn);
    }

    public QueryResult(PreparedStatement statement, ResultSet rs){
        this(statement, rs, -1);
    }

    /**
     * Shortcut for {@link QueryResult#getResultSet()}.
     * @see #getResultSet()
     */
    public ResultSet rs(){
        return resultSet;
    }

    /**
     * Asserts that this result has a {@link java.sql.ResultSet} associated with it.
     * @return This object for convenient construction.
     * @throws java.lang.IllegalStateException If this result does not have a {@link java.sql.ResultSet} associated with it.
     * @see #rs()
     * @see #getResultSet()
     * @see #vouchForResultSet()
     */
    public QueryResult assertHasResultSet(){
        if(resultSet == null){
            throw new IllegalStateException("QueryResult does not have ResultSet when required!");
        }

        return this;
    }

    /**
     * Makes sure that this object has a ResultSet associated with it.
     * @return This object for convenient call chaining
     * @throws SQLException If this objects does not have a ResultSet associated with it.
     * @see #assertHasResultSet()
     */
    public QueryResult vouchForResultSet() throws SQLException {
        if(resultSet == null){
           throw new SQLException("No ResultSet associated with QueryResult when asked to vouch!");
        }

        return this;
    }

    @Override
    public void close() throws SQLException {
        if(preparedStatement == null){
            if(resultSet != null){
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
    public void tryClose(){
        try {
            close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
