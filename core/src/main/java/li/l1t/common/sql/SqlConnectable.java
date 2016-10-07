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
 * Provides credentials for connecting to a JDBC database. <p><b>Note:</b> Try to keep the scope of
 * these as small as possible. Untrusted code could easily steal credentials!</p>
 *
 * @author <a href="https://l1t.li/">Literallie</a>
 */
public interface SqlConnectable {

    /**
     * @return the database to connect to
     */
    String getSqlDb();

    /**
     * @return the fully qualified JDBC connection string for the host to connect to
     */
    String getSqlHost();

    /**
     * @return the password for authentication with the database
     */
    String getSqlPwd();

    /**
     * @return the user for authentication with the database
     */
    String getSqlUser();
}
