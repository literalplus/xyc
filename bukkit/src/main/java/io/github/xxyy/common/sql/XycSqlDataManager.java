/*
 * Copyright (c) 2014 xxyy (Philipp Nowak; devnull@nowak-at.net). All rights reserved.
 *
 * Any usage, including, but not limited to, compiling, running, redistributing, printing, copying and reverse-engineering is strictly prohibited without permission from the original author and may result in legal steps being taken.
 */

package io.github.xxyy.common.sql;

import io.github.xxyy.common.XyHelper;

/**
 * Manages SQL data for XYC.
 *
 * @author xxyy
 */
public class XycSqlDataManager implements SqlConnectable {
    @Override
    public String getSqlDb() {
        return XyHelper.getCfg().getString("sql.db");
    }

    @Override
    public String getSqlHost() {
        return XyHelper.getCfg().getString("sql.host");
    }

    @Override
    public String getSqlPwd() {
        return XyHelper.getCfg().getString("sql.password");
    }

    @Override
    public String getSqlUser() {
        return XyHelper.getCfg().getString("sql.user");
    }
}
