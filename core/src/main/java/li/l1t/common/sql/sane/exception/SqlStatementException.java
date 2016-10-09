/*
 * Copyright (c) 2013 - 2015 xxyy (Philipp Nowak; devnull@nowak-at.net). All rights reserved.
 *
 * Any usage, including, but not limited to, compiling, running, redistributing, printing,
 *  copying and reverse-engineering is strictly prohibited without explicit written permission
 *  from the original author and may result in legal steps being taken.
 *
 * See the included LICENSE file (core/src/main/resources) or email xxyy98+xyclicense@gmail.com for details.
 */

package li.l1t.common.sql.sane.exception;

import li.l1t.common.exception.DatabaseException;

/**
 * Thrown if an error occurs while sending a SQL statement to the data source.
 *
 * @author <a href="https://l1t.li/">Literallie</a>
 * @since 2016-10-09
 */
public class SqlStatementException extends DatabaseException {
    public SqlStatementException(Exception cause) {
        super("Fehler bei der Kommunikation mit der Datenbank.", cause);
    }
}
