/*
 * Copyright (c) 2013 - 2015 xxyy (Philipp Nowak; devnull@nowak-at.net). All rights reserved.
 *
 * Any usage, including, but not limited to, compiling, running, redistributing, printing,
 *  copying and reverse-engineering is strictly prohibited without explicit written permission
 *  from the original author and may result in legal steps being taken.
 *
 * See the included LICENSE file (core/src/main/resources) or email xxyy98+xyclicense@gmail.com for details.
 */

package io.github.xxyy.common.games.kits.inventory;

import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;

import io.github.xxyy.common.games.kits.Kit;

/**
 * Represents an event where an option got clicked in a kit selector.
 *
 * @author <a href="http://xxyy.github.io/">xxyy</a>
 */
public class OptionClickEvent implements Cancellable {
    private final Player player;
    private final KitSelector selector;
    private final Kit kit;

    private boolean close;
    private boolean cancelled;

    /**
     * Constructs a new click event.
     *
     * @param player   the player who clicked.
     * @param kit      the kit located in this slot.
     * @param selector the kit selector causing this event
     */
    public OptionClickEvent(Player player, KitSelector selector, Kit kit) {
        this.player = player;
        this.selector = selector;
        this.kit = kit;
        this.close = true;
        this.cancelled = false;
    }

    /**
     * @return the kit located in the clicked slot.
     */
    public Kit getKit() {
        return this.kit;
    }

    /**
     * @return Who clicked
     */
    public Player getPlayer() {
        return this.player;
    }

    /**
     * @return the kit selector causing this event
     */
    public KitSelector getSelector() {
        return selector;
    }

    /**
     * {@inheritDoc}
     *
     * Note that, if this is true initially, this means that access to the kit was denied by an
     * objective.
     */
    @Override
    public boolean isCancelled() {
        return this.cancelled;
    }

    @Override
    public void setCancelled(boolean cancel) {
        this.cancelled = cancel;
    }

    /**
     * @param close whether the inventory will close.
     */
    public void setWillClose(boolean close) {
        this.close = close;
    }

    /**
     * @return Whether the inventory will close.
     */
    public boolean willClose() {
        return this.close;
    }
}
