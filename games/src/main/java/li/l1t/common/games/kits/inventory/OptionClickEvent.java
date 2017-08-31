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

package li.l1t.common.games.kits.inventory;

import li.l1t.common.games.kits.Kit;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;

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
