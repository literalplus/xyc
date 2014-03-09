package io.github.xxyy.common.sql;

/**
 * Interface for providing SQL access data.
 * 
 * @author <a href="http://xxyy.github.io/">xxyy</a>
 */
public interface SqlConnectable {
    /**
     * @return The database to use.
     * @author <a href="http://xxyy.github.io/">xxyy</a>
     */
    public String getSqlDb();
    /**
     * @return The fully qualified JDBC connectin string for the host to connect to, i.e.
     *            jdbc:mysql://<b>HOST</b>:<i>PORT</i>/
     * @author <a href="http://xxyy.github.io/">xxyy</a>
     */
    public String getSqlHost();
    /**
     * @return The password to connect with. This isn't very safe, isn't it? :(
     * @author <a href="http://xxyy.github.io/">xxyy</a>
     */
    public String getSqlPwd();
    /**
     * @return THe user to use to conenct to SQL.
     * @author <a href="http://xxyy.github.io/">xxyy</a>
     */
    public String getSqlUser();
}
