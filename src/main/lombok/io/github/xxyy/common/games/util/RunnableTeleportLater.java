package io.github.xxyy.common.games.util;

import io.github.xxyy.common.util.task.NonAsyncBukkitRunnable;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.EnumMap;
import java.util.Locale;

/**
 * Teleports a Player to another location at a later time, providing they haven't moved and haven't took any damage.
 * This class supports an optional {@link io.github.xxyy.common.games.util.RunnableTeleportLater.TeleportCompleteHandler}.
 * <p>
 * Note that {@code attemptsAllowed} does not re-schedule the task automatically - use {@link #runTaskTimer(org.bukkit.plugin.Plugin, long, long)}
 * for that. If the teleport completes successfully, the task is cancelled automatically.
 * <p>
 * <b>This class is NOT thread-safe and any calls to runTask(...)Asynchronously will result in exceptions!</b>
 *
 * @author <a href="http://xxyy.github.io/">xxyy</a>
 * @since 20.7.14
 */
public class RunnableTeleportLater extends NonAsyncBukkitRunnable {
    @Nullable
    private final TeleportCompleteHandler handler;
    private final int attemptsAllowed;
    private Player plr;
    private Location to;
    private Location from;
    private double initialHealth;
    private int failedAttemptCount = 0;

    /**
     * Creates a new instance. (such javadoc)
     * Please make sure to read the notes provided in the class Javadoc.
     *
     * @param plr             Player to target
     * @param to              Target location
     * @param attemptsAllowed How many attempts should be made to teleport the player. Set to 1 or below to disable this feature.
     */
    public RunnableTeleportLater(@NotNull Player plr, @NotNull Location to, int attemptsAllowed) {
        this(plr, to, attemptsAllowed, null);
    }

    /**
     * Creates a new instance. (such javadoc)
     * Please make sure to read the notes provided in the class Javadoc.
     *
     * @param plr             Player to target
     * @param to              Target location
     * @param attemptsAllowed How many attempts should be made to teleport the player. Set to 1 or below to disable this feature.
     * @param handler         Handler to call after a teleport attempt
     */
    public RunnableTeleportLater(@NotNull Player plr, @NotNull Location to, int attemptsAllowed, @Nullable TeleportCompleteHandler handler) {
        this.handler = handler;
        this.plr = plr;
        this.to = to;
        this.attemptsAllowed = attemptsAllowed;

        from = plr.getLocation();
        initialHealth = plr.getHealth();
    }

    /**
     * Resets this object's state to the defaults, just as if the constructor was called with given parameters.
     *
     * @param plr Player to target
     * @param to  Target location
     */
    public void reset(@NotNull Player plr, @NotNull Location to) {
        this.cancel();

        this.plr = plr;
        this.to = to;
        this.from = plr.getLocation();
        this.initialHealth = plr.getHealth();
        this.failedAttemptCount = 0;
    }

    @Override
    public void run() {
        TeleportFailureReason failureReason = null;

        if (!plr.isOnline()) {
            failureReason = TeleportFailureReason.LEFT;
        } else if (plr.getHealth() != initialHealth) {
            failureReason = TeleportFailureReason.DAMAGED;
        } else if (plr.getLocation() != from) {
            failureReason = TeleportFailureReason.MOVED;
        }

        if (this.handler != null) {
            handler.handleTeleport(this, failureReason);
        }

        if (failureReason != null) { //if teleport failed
            if (++failedAttemptCount < getAttemptsAllowed()) {
                return; //Continue allowing executions
            }
        }

        this.tryCancel(); //This is only reached if the teleport succeeded or the amount of allowed attempts is exceeded.
        this.plr = null;
    }

    /**
     * @return the Player reference used by this task. This is NULL after this task is cancelled (e.g. {@link #getTaskId()} == -1)
     */
    public Player getPlayer() {
        return plr;
    }

    public Location getTo() {
        return to;
    }

    public int getAttemptsAllowed() {
        return attemptsAllowed;
    }

    public int getFailedAttemptCount() {
        return failedAttemptCount;
    }

    // Inner stuff

    public static interface TeleportCompleteHandler {
        /**
         * Handles an (attempted) teleport. This is called regardless of whether the teleport succeeded.
         *
         * @param cause         Runnable which caused this event
         * @param failureReason Reason of the teleport failureReason or NULL if the teleport succeeded.
         */
        void handleTeleport(RunnableTeleportLater cause, TeleportFailureReason failureReason);
    }

    public enum TeleportFailureReason {
        MOVED,
        DAMAGED,
        LEFT
    }

    /**
     * A simple implementation of {@link io.github.xxyy.common.games.util.RunnableTeleportLater.TeleportCompleteHandler},
     * which sends a defined message to the player upon teleport completion.
     */
    public static class MessageTeleportCompleteHandler implements TeleportCompleteHandler {
        private static final MessageTeleportCompleteHandler DEFAULT_DE = new MessageTeleportCompleteHandler()
                .setMessage(null, "§aDu wurdest erfolgreich teleportiert!")
                .setMessage(TeleportFailureReason.MOVED, "§cDu hast dich bewegt und konntest daher nicht teleportiert werden!")
                .setMessage(TeleportFailureReason.DAMAGED, "§cDu hast Schaden erhalten und konntest daher nicht teleportiert werden!");
        private static final MessageTeleportCompleteHandler DEFAULT_EN = new MessageTeleportCompleteHandler()
                .setMessage(null, "§aYou were successfully teleported!")
                .setMessage(TeleportFailureReason.MOVED, "§cYour teleport was cancelled because you moved!")
                .setMessage(TeleportFailureReason.DAMAGED, "§cYour teleport was cancelled because you took damage!");

        private final EnumMap<TeleportFailureReason, String> messages = new EnumMap<>(TeleportFailureReason.class);
        private String successMessage;

        public MessageTeleportCompleteHandler() {

        }

        public MessageTeleportCompleteHandler(MessageTeleportCompleteHandler toCopy) {
            messages.putAll(toCopy.messages);
            successMessage = toCopy.successMessage;
        }

        /**
         * Sets a message.
         * This fails if called for {@link io.github.xxyy.common.games.util.RunnableTeleportLater.TeleportFailureReason#LEFT} for obvious reasons.
         *
         * @param failureReason Failure reason to set the message for
         * @param message       Message to set (NULL if none - default)
         * @return this object for convenient construction
         */
        public MessageTeleportCompleteHandler setMessage(TeleportFailureReason failureReason, String message) {
            if (failureReason == null) {
                successMessage = message;
            } else if (failureReason == TeleportFailureReason.LEFT) {
                throw new IllegalArgumentException("Can't set message for LEFT since it is not possible to send messages to offline players (dem logic)");
            } else {
                messages.put(failureReason, message);
            }

            return this;
        }

        public String getMessage(TeleportFailureReason failureReason) {
            return failureReason == null ? successMessage : messages.get(failureReason);
        }

        @Override
        public void handleTeleport(RunnableTeleportLater cause, TeleportFailureReason failureReason) {
            if (cause.getPlayer() != null) {
                cause.getPlayer().sendMessage(getMessage(failureReason));
            }
        }

        public static MessageTeleportCompleteHandler getHandler(Locale locale) { //TODO This should use some kind of file to store languages or so
            return locale.getLanguage().equals(Locale.GERMAN.getLanguage()) ? DEFAULT_DE : DEFAULT_EN;
        }
    }
}
