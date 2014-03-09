package io.github.xxyy.common.games.data;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * Produces implementations of {@link PlayerWrapper}.

 *
 * @param <T> Extension class of {@link PlayerWrapper} that is being produced by this {@link PlayerWrapperProducer}
 *
 * @author <a href="http://xxyy.github.io/">xxyy</a>
 * @deprecated
 */
@Deprecated
public interface PlayerWrapperProducer<T extends PlayerWrapper>
{
    /**
     * @param sender {@link CommandSender} to wrap.
     * @return a new T.
     * @see PlayerWrapper#PlayerWrapper(org.bukkit.command.CommandSender, io.github.xxyy.common.sql.SafeSql)
     */
    public T newWrapper(CommandSender sender);
    
    /**
     * @param plr {@link Player} to wrap.
     * @return a new T.
     * @see PlayerWrapper#PlayerWrapper(org.bukkit.entity.Player, io.github.xxyy.common.sql.SafeSql)
     */
    public T newWrapper(Player plr);
    
    /**
     * @param plrName name of the player to wrap.
     * @return a new T.
     * @see PlayerWrapper#PlayerWrapper(String, io.github.xxyy.common.sql.SafeSql)
     */
    public T newWrapper(String plrName);
}
