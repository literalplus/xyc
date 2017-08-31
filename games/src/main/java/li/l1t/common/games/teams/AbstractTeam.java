/*
 * MIT License
 *
 * Copyright (C) 2013 - 2017 Philipp Nowak (https://github.com/xxyy) and contributors.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package li.l1t.common.games.teams;

import li.l1t.common.games.maps.MapSpawn;
import org.bukkit.entity.Player;

import javax.annotation.Nonnull;
import java.util.*;

/**
 * Represents a team of players.
 *
 * @author <a href="http://xxyy.github.io/">xxyy</a>
 * @since 05/03/14
 */
public abstract class AbstractTeam implements GameTeam {
    private final String name;
    private Map<Player, Void> playerMap = new WeakHashMap<>(12); //Doubt I'm gonna need more players in a team anytime soon
    private List<MapSpawn> spawns = new ArrayList<>();

    @java.beans.ConstructorProperties({"name"})
    public AbstractTeam(String name) {
        this.name = name;
    }

    @Override
    public Collection<Player> getPlayers() {
        return playerMap.keySet();
    }

    @Override
    public void clearPlayers() {
        playerMap.clear();
    }

    @Override
    public Player getPlayer(@Nonnull String plrName) {
        for (Player plr : getPlayers()) {
            if (plr != null && plrName.equals(plr.getName())) {
                return plr;
            }
        }

        return null;
    }

    @Override
    public boolean hasPlayer(Player plr) {
        return playerMap.containsKey(plr);
    }

    @Override
    public boolean hasPlayer(@Nonnull String plrName) {
        return getPlayer(plrName) != null;
    }

    @Override
    public void removePlayer(@Nonnull Player plr) {
        playerMap.remove(plr);
    }

    @Override
    public Player removePlayer(@Nonnull String plrName) {
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
    public boolean addPlayer(Player plr) {
        if (hasPlayer(plr)) {
            return false;
        }

        playerMap.put(plr, null);
        return true;
    }

    @Override
    public void addSpawn(@Nonnull MapSpawn mapSpawn) {
        spawns.add(mapSpawn);
    }

    @Override
    public String toString() {
        return getClass().getName() + ": `" + getName() + "` (" + getSpawns().size() + " spawns, " + getPlayers().size() + " players)";
    }

    public boolean equals(Object o) {
        if (o == this) return true;
        if (!(o instanceof AbstractTeam)) return false;
        final AbstractTeam other = (AbstractTeam) o;
        if (!other.canEqual(this)) return false;
        final Object this$name = this.name;
        final Object other$name = other.name;
        return this$name == null ? other$name == null : this$name.equals(other$name);
    }

    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final Object $name = this.name;
        result = result * PRIME + ($name == null ? 0 : $name.hashCode());
        return result;
    }

    public boolean canEqual(Object other) {
        return other instanceof AbstractTeam;
    }

    @Nonnull
    public List<MapSpawn> getSpawns() {
        return this.spawns;
    }

    public String getName() {
        return this.name;
    }
}
