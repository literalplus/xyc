/*
 * Copyright (c) 2013 - 2014 xxyy (Philipp Nowak; devnull@nowak-at.net). All rights reserved.
 *
 * Any usage, including, but not limited to, compiling, running, redistributing, printing,
 *  copying and reverse-engineering is strictly prohibited without explicit written permission
 *  from the original author and may result in legal steps being taken.
 */

package io.github.xxyy.common.sql;

/**
 * Interface for providing SQL access data. Keep these safe because anyone can call {@link #getSqlPwd()}.
 *
 * @author <a href="http://xxyy.github.io/">xxyy</a>
 */
public interface SqlConnectable { //Not sure if Connectible...Internetz says -able at most places

    /**
     * @return The database to use.
     */
    public String getSqlDb();

    /**
     * @return The fully qualified JDBC connection string for the host to connect to, i.e.
     * jdbc:mysql://<b>HOST</b>:<i>PORT</i>/
     */
    public String getSqlHost();

    /**
     * @return The password to connect with. This isn't very safe, isn't it? :(
     */
    public String getSqlPwd();

    /**
     * @return The user to use to connect to SQL.
     */
    public String getSqlUser();
}
