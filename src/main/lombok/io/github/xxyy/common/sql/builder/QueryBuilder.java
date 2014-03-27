package io.github.xxyy.common.sql.builder;

import io.github.xxyy.common.sql.PreparedStatementFactory;
import io.github.xxyy.common.sql.QueryResult;
import lombok.Getter;
import lombok.NonNull;
import org.apache.commons.lang.Validate;
import org.jetbrains.annotations.Nullable;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.*;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Builds a MySQL query from {@link io.github.xxyy.common.sql.builder.QuerySnapshot}s, a table name,
 * identification column and a lot of duct tape.
 *
 * @author <a href="http://xxyy.github.io/">xxyy</a>
 * @since 23.3.14
 */
public class QueryBuilder {
    /**
     * Name of the table this query is targeted to.
     */
    @Getter
    @NonNull
    private final String tableName;

    /**
     * Unique identifiers used to identify the target row(s).
     * Identifiers are tried FIFO (first-in-first-out).
     * If this is null, no limitations are placed and all rows will be selected.
     */
    @Getter
    @Nullable
    private Queue<QuerySnapshot> uniqueIdentifiers = null;

    /**
     * {@link io.github.xxyy.common.sql.builder.QuerySnapshot}s to be applied to the remote database.
     * If this is null, no query will be executed.
     */
    @Getter
    @Nullable
    private Set<QuerySnapshot> queryParts = null;

    public QueryBuilder(@NonNull final String tableName) {
        this.tableName = tableName;
    }

    public QueryBuilder(@NonNull final QueryBuilder source) {
        this.tableName = source.getTableName();
        this.uniqueIdentifiers = source.getUniqueIdentifiers();
        this.queryParts = source.getQueryParts();
    }

    /**
     * Adds an unique identifier to the query, used for identification of the target row(s).
     * Identifiers are FIFO (first-in-first-out), i.e. the first identifier to be registered is going
     * to be the first one checked. If the first one does not return any rows, the next one is tried.
     *
     * @param identifier snapshot of the column to be used as an identifier.
     * @return This object, for convenient construction.
     * @throws java.lang.IllegalArgumentException If {@link QuerySnapshot#getType()} does not return {@link io.github.xxyy.common.sql.builder.QuerySnapshot.Type#OBJECT_UPDATE} for {@code identifier}.
     */
    @NonNull
    public QueryBuilder addUniqueIdentifier(@NonNull final QuerySnapshot identifier) {
        Validate.isTrue(identifier.getType() == QuerySnapshot.Type.OBJECT_UPDATE, "Identifier type must be object update!");

        if (this.uniqueIdentifiers == null) {
            this.uniqueIdentifiers = new LinkedBlockingQueue<>();
        }

        this.uniqueIdentifiers.add(identifier);

        return this;
    }

    /**
     * Adds a part to the query.
     *
     * @param part snapshot of the column to be written.
     * @return This object, for convenient construction.
     */
    @NonNull
    public QueryBuilder addPart(@Nullable final QuerySnapshot part) {
        if (part == null) {
            return this;
        }

        if (this.queryParts == null) {
            this.queryParts = new LinkedHashSet<>();
        }


        this.queryParts.add(part);

        return this;
    }

    /**
     * Builds an update query from this builder. (Using SQL {@code INSERT ON DUPLICATE KEY UPDATE})
     * Will use arguments to infer values.
     *
     * @param statementFactory Where to get the statement from
     * @return A {@link java.sql.PreparedStatement} derived from this builder, or {@code null} if no parts have been defined.
     */
    @Nullable
    public PreparedStatement buildUpdate(@NonNull final PreparedStatementFactory statementFactory) throws SQLException {
        if (this.queryParts == null || this.queryParts.isEmpty()) {
            return null; //Nothing to do then
        }

        StringBuilder queryStringBuilder = new StringBuilder("INSERT INTO ").append(getTableName()).append(" SET ");
        List<Object> args = new LinkedList<>();

        List<QuerySnapshot> snapshots = new ArrayList<>(this.queryParts);

        if (this.uniqueIdentifiers != null && !this.uniqueIdentifiers.isEmpty()) {
            snapshots.addAll(this.uniqueIdentifiers);
        }

        for (QuerySnapshot snapshot : snapshots) {
            queryStringBuilder.append(QuerySnapshot.Type.OBJECT_UPDATE.getOperator(snapshot.getColumnName()))
                    .append(','); //INTEGER_MODIFICATION has name=name+value, which is not allowed in INSERT
            args.add(snapshot.getObjectValue());
        }

        queryStringBuilder.deleteCharAt(queryStringBuilder.length() - 1) //Last char will always be ','
                .append(" ON DUPLICATE KEY UPDATE ");

        for (QuerySnapshot snapshot : snapshots) {
            queryStringBuilder.append(snapshot.getType().getOperator(snapshot.getColumnName()))
                    .append(",");
            args.add(snapshot.getObjectValue());
        }

        queryStringBuilder.deleteCharAt(queryStringBuilder.length() - 1); //Last char will always be ',';

        final PreparedStatement stmt = statementFactory.prepareStatement(queryStringBuilder.toString());
        statementFactory.fillStatement(stmt, args.toArray());
        return stmt;
    }

    /**
     * Executes this query builder as an INSERT INTO ON DUPLICATE KEY UPDATE statement.
     * The builder can be reused after this operation.
     * This will close the created {@link java.sql.PreparedStatement}.
     *
     * @param statementFactory Where to get a statement from
     * @return (1) The number of rows affected (2) 0 for statements that return nothing (3) -1 if this builder did not contain sufficient information to build a statement.
     * @throws SQLException If a SQL error occurs.
     * @see java.sql.PreparedStatement#executeUpdate()
     * @see #buildUpdate(io.github.xxyy.common.sql.PreparedStatementFactory)
     */
    public int executeUpdate(@NonNull final PreparedStatementFactory statementFactory) throws SQLException {
        PreparedStatement statement = buildUpdate(statementFactory);
        if (statement == null) {
            return -1;
        }

        int rtrn = statement.executeUpdate();
        statement.close();
        return rtrn;
    }

    /**
     * Builds a select query from this builder. (Using SQL {@code SELECT}).
     * Will use arguments to infer values.
     * If parts or identifiers are present, only these columns are selected.  (This behaviour can be overwritten by setting {@code selectStar} to {@code true})
     * If no parts or identifiers are present, {@code SELECT *} will be used.
     * If identifiers are present, these will be included in a {@code WHERE} clause.
     *
     * @param statementFactory Where to get the statement from
     * @param selectStar If this is true, {@code SELECT *} will be used, even if identifiers or parts are present.
     * @return a {@link java.sql.PreparedStatement} derived from the current state of this builder.
     */
    @Nullable
    public PreparedStatement buildSelect(@NonNull final PreparedStatementFactory statementFactory, final boolean selectStar) throws SQLException {
        StringBuilder queryStringBuilder = new StringBuilder("SELECT ");
        List<Object> args = new LinkedList<>();

        List<QuerySnapshot> snapshots = new ArrayList<>();

        if (!selectStar && this.queryParts != null) {
            snapshots.addAll(this.queryParts);
        }

        if (!selectStar && this.uniqueIdentifiers != null) {
            snapshots.addAll(this.uniqueIdentifiers);
        }

        if (snapshots.isEmpty()) { //Is also empty if selectStar is true
            queryStringBuilder.append("*");
        } else {
            for (QuerySnapshot snapshot : snapshots) {
                queryStringBuilder.append(snapshot.getColumnName()).append(',');
            }

            queryStringBuilder.deleteCharAt(queryStringBuilder.length() - 1); //Last char will always be ','
        }

        queryStringBuilder.append(" FROM ")
                .append(this.tableName);

        if (this.uniqueIdentifiers != null && !this.uniqueIdentifiers.isEmpty()){
            queryStringBuilder.append(" WHERE ");
            boolean prependAnd = false;
            for(QuerySnapshot snapshot : this.uniqueIdentifiers){
                if(prependAnd){
                    queryStringBuilder.append(" AND ");
                }

                queryStringBuilder.append(snapshot.getType().getOperator(snapshot.getColumnName()));
                args.add(snapshot.getObjectValue());

                prependAnd = true;
            }
        }

        final PreparedStatement statement = statementFactory.prepareStatement(queryStringBuilder.toString());
        statementFactory.fillStatement(statement, args.toArray());
        return statement;
    }

    /**
     * This executes the select statement derived from this builder. (See {@link #buildSelect(io.github.xxyy.common.sql.PreparedStatementFactory, boolean)} JavaDoc for details on that)
     * <b>This will NOT close the {@link java.sql.PreparedStatement}! Make sure to ALWAYS close that!</b>
     * @param statementFactory Where to get the statement from
     * @param selectStar If this is true, {@code SELECT *} will be used, even if identifiers or parts are present.
     * @return A {@link io.github.xxyy.common.sql.QueryResult} created by the request, or {@code null} if {@link #buildSelect(io.github.xxyy.common.sql.PreparedStatementFactory, boolean)} returned {@code null}.
     * @throws SQLException
     */
    public QueryResult executeSelect(@NonNull final PreparedStatementFactory statementFactory, final boolean selectStar) throws SQLException {
        PreparedStatement statement = buildSelect(statementFactory, selectStar);
        if (statement == null) {
            return null;
        }

        return new QueryResult(statement, statement.executeQuery());
    }
}
