/*
 * Copyright (c) 2013 - 2015 xxyy (Philipp Nowak; devnull@nowak-at.net). All rights reserved.
 *
 * Any usage, including, but not limited to, compiling, running, redistributing, printing,
 *  copying and reverse-engineering is strictly prohibited without explicit written permission
 *  from the original author and may result in legal steps being taken.
 *
 * See the included LICENSE file (core/src/main/resources) or email xxyy98+xyclicense@gmail.com for details.
 */

package li.l1t.common.sql;

/**
 * Interface for providing SQL access data. Keep these safe because anyone can call {@link #getSqlPwd()}.
 *
 * @author <a href="http://xxyy.github.io/">xxyy</a>
 */
public interface SqlConnectable { //Not sure if Connectible...Internetz says -able at most places

    /**
     * @return The database to use.
     */
    String getSqlDb();

    /**
     * @return The fully qualified JDBC connection string for the host to connect to, i.e.
     * jdbc:mysql://<b>HOST</b>:<i>PORT</i>/
     */
    String getSqlHost();

    /**
     * @return The password to connect with. This isn't very safe, isn't it? :(
     */
    String getSqlPwd();

    /**
     * @return The user to use to connect to SQL.
     */
    String getSqlUser();
}
