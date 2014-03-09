package io.github.xxyy.common.tests;

import io.github.xxyy.common.tests.implementations.SqlXyGamePluginImpl;
import io.github.xxyy.common.tests.implementations.TestServer;
import io.github.xxyy.common.xyplugin.SqlXyGamePlugin;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;

import java.sql.SQLException;

/**
 *
 * @author xxyy98 (http://xxyy.github.io/)
 */
public class XyCommonTest {

    public static SqlXyGamePlugin instance;
    public static boolean initialised = false;

    @Before
    public void setUp() {
//        return; //That test isn't going to work
        if (initialised) {
            return;
        }
        System.out.println("Initialising instance...");
        if(org.bukkit.Bukkit.getServer() == null) {
            org.bukkit.Bukkit.setServer(new TestServer());
        }
        instance = new SqlXyGamePluginImpl();
        instance.onEnable();
        Assert.assertNotNull("Could not connect to SQL - getSql() returns null.", instance.getSql());
//        Assert.assertNotNull("Could not connect to SQL - makeConnection() returns null", instance.getSql().getCurrentConnection());
        initialised = true;
    }

    @After
    public void tearDown() {
    }

    @org.junit.Test(timeout = 20_000)
    public void testQuery() throws SQLException {
        /*ResultSet rs = instance.getSql().executeQuery("SELECT 1");
        rs.next();
        Assert.assertEquals("Could not connect to SQL - Could not SELECT 1!", 1, rs.getInt(1));*/
    }

}
