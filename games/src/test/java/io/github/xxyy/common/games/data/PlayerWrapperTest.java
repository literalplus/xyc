package io.github.xxyy.common.games.data;

import org.bukkit.Bukkit;
import org.junit.BeforeClass;
import org.junit.Test;

import io.github.xxyy.common.games.GameLib;
import io.github.xxyy.common.sql.SafeSql;
import io.github.xxyy.common.test.util.MockHelper;
import io.github.xxyy.common.xyplugin.SqlXyGamePlugin;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class PlayerWrapperTest {
    @BeforeClass
    public static void prepareClass() {
        if(Bukkit.getServer() == null) {
            MockHelper.mockServer();
        }
        GameLib.registerPlugin(mock(SqlXyGamePlugin.class));
    }

    @Test
    public void testGetNick() throws Exception {
        SafeSql fakeSql = mock(SafeSql.class);
        when(fakeSql.makeConnection()).thenThrow(new UnsupportedOperationException());

        PlayerWrapper wrp = new PlayerWrapper(PlayerWrapper.CONSOLE_UUID, "CONSOLE", fakeSql);

        wrp.getNick();
        wrp.getNick();
    }

    @Test
    public void testSetMetadata() throws Exception {

    }

    @Test
    public void testGetMetadata() throws Exception {

    }

    @Test
    public void testHasMetadata() throws Exception {

    }

    @Test
    public void testRemoveMetadata() throws Exception {

    }
}
