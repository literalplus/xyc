/*
 * Copyright (c) 2013 - 2015 xxyy (Philipp Nowak; devnull@nowak-at.net). All rights reserved.
 *
 * Any usage, including, but not limited to, compiling, running, redistributing, printing,
 *  copying and reverse-engineering is strictly prohibited without explicit written permission
 *  from the original author and may result in legal steps being taken.
 *
 * See the included LICENSE file (core/src/main/resources) or email xxyy98+xyclicense@gmail.com for details.
 */

package li.l1t.common.sql.builder;

import li.l1t.common.sql.PreparedStatementFactory;
import li.l1t.common.sql.QueryResult;
import org.apache.commons.lang.Validate;
import org.jetbrains.annotations.Nullable;

import javax.annotation.Nonnull;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Builds a MySQL query from {@link SimpleQuerySnapshot}s, a table name,
 * identification column and a lot of duct tape.
 *
 * @author <a href="http://xxyy.github.io/">xxyy</a>
 * @since 23.3.14
 */
public class QueryBuilder { //TODO make concurrent
    /**
     * Name of the table this query is targeted to.
     */
    @Nonnull
    private final String tableName;

    /**
     * Unique identifiers used to identify the target row(s).
     * Identifiers are tried FIFO (first-in-first-out).
     * If this is null, no limitations are placed and all rows will be selected.
     */
    @Nullable
    private Queue<QuerySnapshot> uniqueIdentifiers = null;

    /**
     * {@link SimpleQuerySnapshot}s to be applied to the remote database.
     * If this is null, no query will be executed.
     */
    @Nullable
    private Set<QuerySnapshot> queryParts = null;

    public QueryBuilder(@Nonnull final String tableName) {
        this.tableName = tableName;
    }

    public QueryBuilder(@Nonnull final QueryBuilder source) {
        this.tableName = source.getTableName();
        this.uniqueIdentifiers = source.getUniqueIdentifiers();
        this.queryParts = source.getQueryParts();
    }

    /**
     * Adds given snapshots to this builder. Snapshots are added to identifiers or parts based on their {@link QuerySnapshot#getType()}.
     *
     * @param snapshots       Snapshots to add
     * @param omitIdentifiers If this is set to {@code true}, identifiers are ignored.
     * @return This object, for convenient construction.
     * @see #addPart(QuerySnapshot)
     * @see #addUniqueIdentifier(QuerySnapshot)
     */
    @Nonnull
    public QueryBuilder addAll(@Nonnull final Collection<? extends QuerySnapshot> snapshots, boolean omitIdentifiers) {
        for (QuerySnapshot snapshot : snapshots) {
            if (snapshot.getType() == QuerySnapshot.Type.OBJECT_IDENTIFIER && !omitIdentifiers) {
                addUniqueIdentifier(snapshot);
            } else {
                addPart(snapshot);
            }
        }

        return this;
    }

    /**
     * Adds an unique identifier to the query, used for identification of the target row(s).
     * Identifiers are FIFO (first-in-first-out), i.e. the first identifier to be registered is going
     * to be the first one checked. If the first one does not return any rows, the next one is tried.
     *
     * @param identifier snapshot of the column to be used as an identifier.
     * @return This object, for convenient construction.
     * @throws java.lang.IllegalArgumentException If {@link SimpleQuerySnapshot#getType()} does not return {@link QuerySnapshot.Type#OBJECT_UPDATE} for {@code identifier}.
     */
    @Nonnull
    public QueryBuilder addUniqueIdentifier(@Nonnull final QuerySnapshot identifier) {
        Validate.isTrue(identifier.getType() == QuerySnapshot.Type.OBJECT_IDENTIFIER ||
                identifier.getType() == QuerySnapshot.Type.NEGATED_OBJECT_IDENTIFIER, "Identifier type must be object identifier!");

        if (this.uniqueIdentifiers == null) {
            this.uniqueIdentifiers = new LinkedBlockingQueue<>();
        }

        this.uniqueIdentifiers.add(identifier);

        return this;
    }

    /**
     * Consumes the first identifier added to the identifier queue.
     *
     * @return This object, for convenient construction.
     * @see java.util.Queue#poll()
     */
    @Nonnull
    public QueryBuilder pollUniqueIdentifier() {
        if (this.uniqueIdentifiers != null) {
            this.uniqueIdentifiers.poll();
        }

        return this;
    }

    /**
     * Clears the queue of unique identifiers maintained by this builder.
     *
     * @return This object, for convenient construction.
     */
    @Nonnull
    public QueryBuilder clearUniqueIdentifiers() {
        if (this.uniqueIdentifiers != null) {
            this.uniqueIdentifiers.clear();
        }

        return this;
    }

    /**
     * Adds a part to the query.
     *
     * @param part snapshot of the column to be written.
     * @return This object, for convenient construction.
     */
    @Nonnull
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
     * Adds a part to the query from a snapshot factory.
     *
     * @param factory where to get a snapshot from
     * @return This object, for convenient construction.
     */
    @Nonnull
    public QueryBuilder addPart(@Nullable final QuerySnapshot.Factory factory) {
        if (factory == null) {
            return this;
        }

        return addPart(factory.produceSnapshot());
    }

    /**
     * Adds parts to the query.
     *
     * @param parts Collection of the snapshots to be added
     * @return This object, for convenient construction.
     */
    @Nonnull
    public QueryBuilder addParts(@Nonnull final Collection<QuerySnapshot> parts) {
        for (QuerySnapshot snapshot : parts) {
            addPart(snapshot);
        }

        return this;
    }

    /**
     * Clears the list of query parts maintained by this builder.
     *
     * @return This object, for convenient construction.
     */
    @Nonnull
    public QueryBuilder clearParts() {
        if (this.queryParts != null) {
            this.queryParts.clear();
        }

        return this;
    }

    /**
     * Builds an update query from this builder. (Using SQL {@code INSERT ON DUPLICATE KEY UPDATE})
     * Will use ? arguments to infer values.
     *
     * @param statementFactory Where to get the statement from
     * @return A {@link java.sql.PreparedStatement} derived from this builder, or {@code null} if no parts have been defined.
     * @throws java.sql.SQLException if a sql error occurs
     */
    @Nullable
    public PreparedStatement buildUpdate(@Nonnull final PreparedStatementFactory statementFactory) throws SQLException {
        if (this.queryParts == null || this.queryParts.isEmpty()) {
            return null; //Nothing to do then
        }

        StringBuilder queryStringBuilder = new StringBuilder("INSERT INTO ").append(getTableName()).append(" SET ");
        List<Object> args = new LinkedList<>();

        Set<QuerySnapshot> snapshots = new HashSet<>(this.queryParts);

        if (this.uniqueIdentifiers != null && !this.uniqueIdentifiers.isEmpty()) {
            snapshots.addAll(this.uniqueIdentifiers);
        }

        for (QuerySnapshot snapshot : snapshots) {
            if (snapshot != null) {
                queryStringBuilder.append(QuerySnapshot.Type.OBJECT_UPDATE.getOperator(snapshot.getColumnName()))
                        .append(','); //NUMBER_MODIFICATION has name=name+value, which is not allowed in INSERT
                args.add(snapshot.getSnapshot());
            }
        }

        queryStringBuilder.deleteCharAt(queryStringBuilder.length() - 1) //Last char will always be ','
                .append(" ON DUPLICATE KEY UPDATE ");

        for (QuerySnapshot snapshot : snapshots) {
            if (snapshot != null) {
                queryStringBuilder.append(snapshot.getType().getOperator(snapshot.getColumnName()))
                        .append(",");
            }
        }
        args.addAll(args);

        queryStringBuilder.deleteCharAt(queryStringBuilder.length() - 1); //Last char will always be ',';

        final PreparedStatement stmt = statementFactory.prepareStatement(queryStringBuilder.toString());
        statementFactory.fillStatement(stmt, args.toArray());
        return stmt;
    }

    /**
     * Builds a true update query from this builder. (Using pure SQL {@code UPDATE})
     * Will use ? arguments to infer values.
     * This uses less Text than {@link #buildUpdate(PreparedStatementFactory)}, but will fail (silently)
     * if there is no row matched by the identifiers.
     * <b>This will return null if no identifiers are specified to prevent accidental destruction of whole tables. Use an identifier of 1=1 to circumvent that.</b>
     *
     * @param statementFactory Where to get the statement from
     * @return A {@link java.sql.PreparedStatement} derived from this builder, or {@code null} if no parts or identifiers have been defined.
     * @throws java.sql.SQLException if a database error occurs
     */
    @Nullable
    public PreparedStatement buildTrueUpdate(@Nonnull final PreparedStatementFactory statementFactory) throws SQLException {
        if ((this.queryParts == null || this.queryParts.isEmpty()) || (this.uniqueIdentifiers == null || this.uniqueIdentifiers.isEmpty())) {
            return null; //Nothing to do then
        }

        StringBuilder queryStringBuilder = new StringBuilder("UPDATE ").append(getTableName()).append(" SET ");
        List<Object> args = new LinkedList<>();

        for (QuerySnapshot snapshot : this.queryParts) {
            if (snapshot != null) {
                queryStringBuilder.append(snapshot.getType().getOperator(snapshot.getColumnName()))
                        .append(','); //NUMBER_MODIFICATION has name=name+value, which is not allowed in INSERT
                args.add(snapshot.getSnapshot());
            }
        }

        queryStringBuilder.deleteCharAt(queryStringBuilder.length() - 1) //Last char will always be ','
                .append(" WHERE ");

        boolean prependAnd = false;
        for (QuerySnapshot snapshot : this.uniqueIdentifiers) {
            if (prependAnd) {
                queryStringBuilder.append(" AND ");
            }

            queryStringBuilder.append(snapshot.getType().getOperator(snapshot.getColumnName()));
            args.add(snapshot.getSnapshot());

            prependAnd = true;
        }

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
     * @see #buildUpdate(PreparedStatementFactory)
     */
    public int executeUpdate(@Nonnull final PreparedStatementFactory statementFactory) throws SQLException {
        PreparedStatement statement = buildUpdate(statementFactory);
        if (statement == null) {
            return -1;
        }

        int rtrn = statement.executeUpdate();
        statement.close();
        return rtrn;
    }

    /**
     * Executes this query builder as an UPDATE statement.
     * The builder can be reused after this operation.
     * This will close the created {@link java.sql.PreparedStatement}.
     *
     * @param statementFactory Where to get a statement from
     * @return (1) The number of rows affected (2) 0 for statements that return nothing (3) -1 if this builder did not contain sufficient information to build a statement.
     * @throws SQLException If a SQL error occurs.
     * @see java.sql.PreparedStatement#executeUpdate()
     * @see #buildTrueUpdate(PreparedStatementFactory)
     */
    public int executeTrueUpdate(@Nonnull final PreparedStatementFactory statementFactory) throws SQLException {
        PreparedStatement statement = buildTrueUpdate(statementFactory);
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
     * @param selectStar       If this is true, {@code SELECT *} will be used, even if identifiers or parts are present.
     * @return a {@link java.sql.PreparedStatement} derived from the current state of this builder.
     * @throws java.sql.SQLException if a database error occurs
     */
    @Nullable
    public PreparedStatement buildSelect(@Nonnull final PreparedStatementFactory statementFactory, final boolean selectStar) throws SQLException {
        Validate.notNull(statementFactory, "statementFactory");
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

        if (this.uniqueIdentifiers != null && !this.uniqueIdentifiers.isEmpty()) {
            queryStringBuilder.append(" WHERE ");
            boolean prependAnd = false;
            for (QuerySnapshot snapshot : this.uniqueIdentifiers) {
                if (prependAnd) {
                    queryStringBuilder.append(" AND ");
                }

                queryStringBuilder.append(snapshot.getType().getOperator(snapshot.getColumnName()));
                args.add(snapshot.getSnapshot());

                prependAnd = true;
            }
        }

        final PreparedStatement statement = statementFactory.prepareStatement(queryStringBuilder.toString());
        statementFactory.fillStatement(statement, args.toArray());
        return statement;
    }

    /**
     * This executes the select statement derived from this builder. (See {@link #buildSelect(PreparedStatementFactory, boolean)} JavaDoc for details on that)
     * <b>This will NOT close the {@link java.sql.PreparedStatement}! Make sure to ALWAYS close that!</b>
     *
     * @param statementFactory Where to get the statement from
     * @param selectStar       If this is true, {@code SELECT *} will be used, even if identifiers or parts are present.
     * @return A {@link QueryResult} created by the request, or {@code null} if {@link #buildSelect(PreparedStatementFactory, boolean)} returned {@code null}.
     * @throws SQLException if a database error occurs
     */
    public QueryResult executeSelect(@Nonnull final PreparedStatementFactory statementFactory, final boolean selectStar) throws SQLException {
        PreparedStatement statement = buildSelect(statementFactory, selectStar);
        if (statement == null) {
            return null;
        }

        return new QueryResult(statement, statement.executeQuery());
    }

    @Nonnull
    public String getTableName() {
        return this.tableName;
    }

    @Nullable
    public Queue<QuerySnapshot> getUniqueIdentifiers() {
        return this.uniqueIdentifiers;
    }

    @Nullable
    public Set<QuerySnapshot> getQueryParts() {
        return this.queryParts;
    }
}
