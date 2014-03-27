package io.github.xxyy.common.games.data;

import io.github.xxyy.common.games.GameLib;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;


/**
 *
 * @deprecated Parent {@link PlayerWrapperProducer} has been deprecated. No longer needed.
 * @author <a href="http://xxyy.github.io/">xxyy</a>
 */
@Deprecated
public class GenericPlayerWrapperProvider implements PlayerWrapperProducer<GenericPlayerWrapper> {

    @Deprecated //consistency! :)
    private static GenericPlayerWrapperProvider instance = null;
//    private PlayerWrapperFactory<GenericPlayerWrapper> factory = new PlayerWrapperFactory<>(this);

    /**
     * @return The {@link PlayerWrapperFactory} used to create {@link PlayerWrapper}s.
     */
    @Deprecated
    public PlayerWrapperFactory<?> getFactory(){
        return PlayerWrapperFactory.getGenericFactory();
    }

    @Deprecated
    public GenericPlayerWrapper newWrapper(CommandSender sender)    {
        return new GenericPlayerWrapper(sender, GameLib.getSql());
    }

    @Deprecated
    public GenericPlayerWrapper newWrapper(Player plr)    {
        return new GenericPlayerWrapper(plr, GameLib.getSql());
    }
    
    @Deprecated
    public GenericPlayerWrapper newWrapper(String plrName)    {
        return new GenericPlayerWrapper(plrName, GameLib.getSql());
    }
    
    /**
     * @return The one and only instance of {@link GenericPlayerWrapperProvider}.
     */
    @Deprecated
    public static GenericPlayerWrapperProvider instance() {
        if (GenericPlayerWrapperProvider.instance == null)        {
            GenericPlayerWrapperProvider.instance = new GenericPlayerWrapperProvider();
        }
        return GenericPlayerWrapperProvider.instance;
    }
}
