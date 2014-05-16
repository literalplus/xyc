package io.github.xxyy.common.sql;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import org.apache.commons.lang.Validate;

/**
 * Statuic utilities for the {@link io.github.xxyy.common.sql.SqlConnectable} class.
 *
 * @author <a href="http://xxyy.github.io/">xxyy</a>
 * @since 14.5.14
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class SqlConnectables {

    /**
     * Returns the host string for the given Connectable. The host string consists of
     * the database URL, as returned by {@link SqlConnectable#getSqlHost()}, of a slash (/)
     * and the database name, as returned by {@link SqlConnectable#getSqlDb()}.
     * If the database name is already appended to the host, it will not be appended another time.
     *
     * @param connectable Connectable to get information from
     * @return Host string, as accepted by database drivers.
     */
    public static String getHostString(@NonNull SqlConnectable connectable){
        String sqlHost = connectable.getSqlHost();

        Validate.notNull(sqlHost);

        return sqlHost.contains(connectable.getSqlDb()) ? sqlHost : ((sqlHost.endsWith("/")) ? sqlHost + connectable.
                getSqlDb() : sqlHost + "/" + connectable.getSqlDb());
    }
}
