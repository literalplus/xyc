/*
 * Copyright (c) 2013 - 2016 xxyy (Philipp Nowak; xyc@l1t.li). All rights reserved.
 *
 * Any usage, including, but not limited to, compiling, running, redistributing, printing,
 *  copying and reverse-engineering is strictly prohibited without explicit written permission
 *  from the original author and may result in legal steps being taken.
 *
 * See the included LICENSE file (core/src/main/resources) for details.
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
