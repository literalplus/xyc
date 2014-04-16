/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package io.github.xxyy.common.games.data;

import io.github.xxyy.common.sql.SafeSql;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.UUID;

/**
 * Example implementation of {@link PlayerWrapper}. Allows for access to the API, even if no own data needs to be saved.
 *
 * @author xxyy
 *
 * @deprecated No longer necessary. Use {@link io.github.xxyy.common.games.data.PlayerWrapper}.
 */
@Deprecated
public class GenericPlayerWrapper extends PlayerWrapper {

    protected GenericPlayerWrapper(final CommandSender sender, final SafeSql ssql) {
        super(sender, ssql);
    }

    protected GenericPlayerWrapper(Player plr, SafeSql ssql) {
        super(plr, ssql);
    }

    protected GenericPlayerWrapper(UUID uuid, String plrName, SafeSql ssql) {
        super(uuid, plrName, ssql);
    }

    @Override
    protected void impFetch() {
        //nothing to do here
    }

    @Override
    protected void impFlush() {
        //nothing to do here
    }
}
