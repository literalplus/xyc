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

package li.l1t.common.util;

import org.bukkit.entity.*;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;

import java.util.Optional;

/**
 * Provides static utility methods for dealing with damage events.
 *
 * @author <a href="https://l1t.li/">Literallie</a>
 * @since 2017-04-12
 */
public class DamageHelper {
    private DamageHelper() {

    }

    /**
     * Attempts to find the actual damager of given event by looking at the event damager and then resolving the player
     * who owns that damager, if any. This especially applies to things like arrows and wolves. If the damager is a
     * player, they are returned directly.
     *
     * @param event the event to inspect
     * @return the player that actually caused given event's damage
     */
    public static Optional<Player> findActualDamager(EntityDamageEvent event) {
        if (event instanceof EntityDamageByEntityEvent) {
            Entity damager = ((EntityDamageByEntityEvent) event).getDamager();
            if (damager instanceof Player) {
                return Optional.of((Player) damager);
            } else if (damager instanceof Projectile) {
                return optionalIfPlayer(((Projectile) damager).getShooter());
            } else if (damager instanceof Wolf) {
                return optionalIfPlayer(((Wolf) damager).getOwner());
            }
        }
        return Optional.empty();
    }

    private static Optional<Player> optionalIfPlayer(Object possiblePlayer) {
        if (possiblePlayer instanceof Player) {
            return Optional.of((Player) possiblePlayer);
        } else {
            return Optional.empty();
        }
    }

    /**
     * @param event the event to inspect
     * @return whether given event would cause the affected entity to die
     */
    public static boolean isFatalHit(EntityDamageEvent event) {
        Entity entity = event.getEntity();
        return entity instanceof Damageable &&
                (((Damageable) entity).getHealth() - event.getFinalDamage()) <= 0D;
    }

    /**
     * Cancels given event and, if the damage cause was a wolf, resets their target so that they won't immediately
     * attack the same player again.
     *
     * @param event the event to operate on
     */
    public static void cancelAndStopWolves(EntityDamageEvent event) {
        event.setCancelled(true);
        if (event instanceof EntityDamageByEntityEvent) {
            Entity damager = ((EntityDamageByEntityEvent) event).getDamager();
            if (damager.getType() == EntityType.WOLF) {
                Wolf wolf = (Wolf) damager;
                AnimalTamer prevOwner = wolf.getOwner();
                wolf.setOwner((Player) event.getEntity());
                wolf.setOwner(prevOwner);
            }
        }
    }
}
