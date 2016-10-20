/*
 * Copyright (c) 2013 - 2015 xxyy (Philipp Nowak; devnull@nowak-at.net). All rights reserved.
 *
 * Any usage, including, but not limited to, compiling, running, redistributing, printing,
 *  copying and reverse-engineering is strictly prohibited without explicit written permission
 *  from the original author and may result in legal steps being taken.
 *
 * See the included LICENSE file (core/src/main/resources) or email xxyy98+xyclicense@gmail.com for details.
 */

package li.l1t.lanatus.sql.product;

import com.google.common.base.Preconditions;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Represents a compiled query for a collection of products.
 *
 * @author <a href="https://l1t.li/">Literallie</a>
 * @since 2016-10-10
 */
class ProductQuery {
    private final SqlProductQueryBuilder builder;
    private final Map<String, Object> conditions = new HashMap<>();
    private String whereClause = "";

    ProductQuery(SqlProductQueryBuilder builder) {
        Preconditions.checkNotNull(builder, "builder");
        this.builder = builder;
        compile();
    }

    private void compile() {
        compileConditionsFrom(builder);
        String whereConditions = conditions.keySet().stream()
                .collect(Collectors.joining(" AND "));
        if (!whereConditions.isEmpty()) {
            whereClause = "WHERE " + whereConditions;
        }
    }

    private void compileConditionsFrom(SqlProductQueryBuilder builder) {
        Preconditions.checkState(conditions.isEmpty(), "conditions already compiled!");
        if (builder.getModule() != null) {
            conditions.put("module = ?", builder.getModule());
        }
        if (builder.getName() != null) {
            conditions.put("name = ?", builder.getName());
        }
        if (builder.isActiveOnly()) {
            conditions.put("active = ?", true);
        }
    }

    public String getWhereClause() {
        return whereClause;
    }

    public Object[] getParameters() {
        return conditions.values().toArray(new Object[conditions.size()]);
    }
}
