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

package li.l1t.common.util.math;

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

    /**
     * Adds two objects of this number type together.
     *
     * @param n1 First number
     * @param n2 Second number
     * @return the result of the addition
     */
    N add(N n1, N n2);

    ////////// COMMONLY-USED IMPLEMENTATIONS ///////////////////////////////////////////////////////////////////////////

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
}
