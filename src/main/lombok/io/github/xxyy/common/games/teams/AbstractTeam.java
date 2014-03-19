package io.github.xxyy.common.games.teams;

import io.github.xxyy.common.games.maps.MapSpawn;
import io.github.xxyy.common.util.CommandHelper;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.*;

/**
 * Represents a team of players.
 *
 * @author <a href="http://xxyy.github.io/">xxyy</a>
 * @since 05/03/14
 */
@RequiredArgsConstructor
@EqualsAndHashCode(of = {"name"})
public abstract class AbstractTeam implements GameTeam {
    private Map<Player, Void> playerMap = new WeakHashMap<>(12); //Doubt I'm gonna need more players in a team anytime soon

    @Getter
    private List<MapSpawn> spawns = null;

    @Getter
    private final String name;

    @Override
    public Collection<Player> getPlayers(){
        return playerMap.keySet();
    }

    @Override
    public void clearPlayers(){
        playerMap.clear();
    }

    @Override
    public Player getPlayer(@NotNull @NonNull String plrName){
        for(Player plr : getPlayers()){
            if(plr != null && plrName.equals(plr.getName())){
                return plr;
            }
        }

        return null;
    }

    @Override
    public boolean hasPlayer(Player plr){
        return playerMap.containsKey(plr);
    }

    @Override
    public boolean hasPlayer(@NotNull @NonNull String plrName){
        return getPlayer(plrName) != null;
    }

    @Override
    public void removePlayer(@NotNull @NonNull Player plr){
        playerMap.remove(plr);
    }

    @Override
    public Player removePlayer(@NotNull @NonNull String plrName){
        for (Iterator<Player> iterator = getPlayers().iterator(); iterator.hasNext(); ) {
            Player plr = iterator.next();
            if (plr != null && plrName.equals(plr.getName())) {
                iterator.remove();
                return plr;
            }
        }

        return null;
    }

    @Override
    public boolean addPlayer(Player plr){
        if (hasPlayer(plr)) {
            return false;
        }

        playerMap.put(plr, null);
        return true;
    }

    @Override
    public void addSpawn(@NotNull @NonNull MapSpawn mapSpawn){
        if(spawns == null){
            spawns = new ArrayList<>();
        }

        spawns.add(mapSpawn);
    }

    @Override
    public String toString(){
        return getClass().getName() + ": `" + getName() + "` (" + CommandHelper.safeSize(getSpawns()) + " spawns, "+CommandHelper.safeSize(getPlayers())+" players)";
    }
}
