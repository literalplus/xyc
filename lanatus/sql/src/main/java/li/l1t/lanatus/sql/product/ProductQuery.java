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

package li.l1t.lanatus.sql.product;

import com.google.common.base.Preconditions;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Represents a compiled query for a collection of products.
 *
 * @author <a href="https://l1t.li/">Literallie</a>
 * @since 2016-10-10
 */
class ProductQuery {
    private final SqlProductQueryBuilder builder;
    private final List<String> andConditions = new ArrayList<>(3);
    private final List<Object> parameters = new ArrayList<>();
    private String whereClause = "";

    ProductQuery(SqlProductQueryBuilder builder) {
        Preconditions.checkNotNull(builder, "builder");
        this.builder = builder;
        compile();
    }

    private void compile() {
        compileConditionsFrom(builder);
        String whereConditions = andConditions.stream()
                .collect(Collectors.joining(" AND "));
        if (!whereConditions.isEmpty()) {
            whereClause = "WHERE " + whereConditions;
        }
    }

    private void compileConditionsFrom(SqlProductQueryBuilder builder) {
        Preconditions.checkState(andConditions.isEmpty(), "conditions already compiled!");
        if (builder.getModule() != null) {
            addCondition("module = ?", builder.getModule());
        }
        if (!builder.getSearchTerm().isEmpty()) {
            addCondition(
                    "module LIKE CONCAT('%', ?, '%') OR " +
                            "displayname LIKE CONCAT('%', ?, '%') OR " +
                            "description LIKE CONCAT('%', ?, '%')",
                    builder.getSearchTerm(), builder.getSearchTerm(), builder.getSearchTerm()
            );
        }
        if (builder.isActiveOnly()) {
            addCondition("active = ?", true);
        }
    }

    private void addCondition(String whereClause, Object... params) {
        andConditions.add(whereClause);
        Arrays.stream(params).forEach(parameters::add);
    }

    public String getWhereClause() {
        return whereClause;
    }

    public Object[] getParameters() {
        return parameters.toArray(new Object[parameters.size()]);
    }
}
