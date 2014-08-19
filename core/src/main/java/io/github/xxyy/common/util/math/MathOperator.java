/*
 * Copyright (c) 2013 - 2014 xxyy (Philipp Nowak; devnull@nowak-at.net). All rights reserved.
 *
 * Any usage, including, but not limited to, compiling, running, redistributing, printing,
 *  copying and reverse-engineering is strictly prohibited without explicit written permission
 *  from the original author and may result in legal steps being taken.
 */

package io.github.xxyy.common.util.math;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Represents an operator that can perform addition and subtraction.
 * Also holds the 0 value for that Number type.
 * This interface also holds some static members for commonly-used Numbers.
 *
 * @author <a href="http://xxyy.github.io/">xxyy</a>
 * @since 3.4.14
 */
public interface MathOperator<N> {
    /**
     * Adds two objects of this number type together.
     *
     * @param n1 First number
     * @param n2 Second number
     * @return the result of the addition
     */
    N add(N n1, N n2);

    /**
     * Subtracts two objects of this number.
     * Example:{@code minuend - subtrahend = result}
     *
     * @param minuend    Minuend (The one that comes first)
     * @param subtrahend Subtrahend (The one that comes second)
     * @return the result of the subtraction
     */
    N subtract(N minuend, N subtrahend);

    /**
     * @return an object of this number type, representing the number zero.
     */
    N getZero();

    /**
     * Fetches a value of this number type from a ResultSet.
     * This will throw some exception if the column is of an incompatible type, depending on implementation.
     *
     * @param columnLabel Label of the column to fetch data from
     * @param resultSet   ResultSet to use
     * @return The value of columnLabel in the given ResultSet, as this number type.
     * @throws SQLException If an error occurs while accessing the ResultSet.
     */
    N getFromResultSet(String columnLabel, ResultSet resultSet) throws SQLException;

    /**
     * Fetches a value of this number type from a ResultSet.
     * This will throw some exception if the column is of an incompatible type, depending on implementation.
     *
     * @param columnIndex Index of the column to fetch data from - 1 is the first column, 2 is the second, ...
     * @param resultSet   ResultSet to use
     * @return The value of columnLabel in the given ResultSet, as this number type.
     * @throws SQLException If an error occurs while accessing the ResultSet.
     */
    N getFromResultSet(int columnIndex, ResultSet resultSet) throws SQLException;

    ////////// COMMONLY-USED IMPLEMENTATIONS ///////////////////////////////////////////////////////////////////////////

    MathOperator<Integer> INTEGER_MATH_OPERATOR = new MathOperator<Integer>() {
        @Override
        public Integer add(Integer n1, Integer n2) {
            return n1 + n2;
        }

        @Override
        public Integer subtract(Integer minuend, Integer subtrahend) {
            return minuend - subtrahend;
        }

        @Override
        public Integer getZero() {
            return 0;
        }

        @Override
        public Integer getFromResultSet(String columnLabel, ResultSet resultSet) throws SQLException {
            return resultSet.getInt(columnLabel);
        }

        @Override
        public Integer getFromResultSet(int columnIndex, ResultSet resultSet) throws SQLException {
            return resultSet.getInt(columnIndex);
        }
    };

    MathOperator<Long> LONG_MATH_OPERATOR = new MathOperator<Long>() {
        @Override
        public Long add(Long n1, Long n2) {
            return n1 + n2;
        }

        @Override
        public Long subtract(Long minuend, Long subtrahend) {
            return minuend - subtrahend;
        }

        @Override
        public Long getZero() {
            return 0L;
        }

        @Override
        public Long getFromResultSet(String columnLabel, ResultSet resultSet) throws SQLException {
            return resultSet.getLong(columnLabel);
        }

        @Override
        public Long getFromResultSet(int columnIndex, ResultSet resultSet) throws SQLException {
            return resultSet.getLong(columnIndex);
        }
    };

    MathOperator<Double> DOUBLE_MATH_OPERATOR = new MathOperator<Double>() {
        @Override
        public Double add(Double n1, Double n2) {
            return n1 + n2;
        }

        @Override
        public Double subtract(Double minuend, Double subtrahend) {
            return minuend - subtrahend;
        }

        @Override
        public Double getZero() {
            return 0D;
        }

        @Override
        public Double getFromResultSet(String columnLabel, ResultSet resultSet) throws SQLException {
            return resultSet.getDouble(columnLabel);
        }

        @Override
        public Double getFromResultSet(int columnIndex, ResultSet resultSet) throws SQLException {
            return resultSet.getDouble(columnIndex);
        }
    };

    MathOperator<Float> FLOAT_MATH_OPERATOR = new MathOperator<Float>() {
        @Override
        public Float add(Float n1, Float n2) {
            return n1 + n2;
        }

        @Override
        public Float subtract(Float minuend, Float subtrahend) {
            return minuend - subtrahend;
        }

        @Override
        public Float getZero() {
            return 0F;
        }

        @Override
        public Float getFromResultSet(String columnLabel, ResultSet resultSet) throws SQLException {
            return resultSet.getFloat(columnLabel);
        }

        @Override
        public Float getFromResultSet(int columnIndex, ResultSet resultSet) throws SQLException {
            return resultSet.getFloat(columnIndex);
        }
    };
}
