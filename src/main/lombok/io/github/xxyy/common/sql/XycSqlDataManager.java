package io.github.xxyy.common.sql;

import io.github.xxyy.common.XyHelper;

/**
 * Manages SQL data for XYC.
 * 
 * @author xxyy
 */
public class XycSqlDataManager implements SqlConnectable{
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
