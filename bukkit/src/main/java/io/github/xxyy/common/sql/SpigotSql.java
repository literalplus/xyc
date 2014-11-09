package io.github.xxyy.common.sql;

import org.bukkit.plugin.Plugin;

import java.sql.SQLException;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

/**
 * An extension to {@link io.github.xxyy.common.sql.SafeSql} providing several Spigot-specific utility methods.
 *
 * @author <a href="http://xxyy.github.io/">xxyy</a>
 * @since 09/11/14
 */
public class SpigotSql extends SafeSql {
    private final Plugin plugin;

    /**
     * Constructs a new instance.
     *
     * @param connectable {@link io.github.xxyy.common.sql.SqlConnectable} providing login data
     * @param plugin      the plugin managing this sql connector
     * @throws IllegalArgumentException If any of the arguments are {@code null}.
     */
    public SpigotSql(SqlConnectable connectable, Plugin plugin) {
        super(connectable);
        this.plugin = plugin;
    }

    /**
     * Executes an update statement in an asynchronous thread using
     * Bukkit's {@link org.bukkit.scheduler.BukkitScheduler#runTaskAsynchronously(org.bukkit.plugin.Plugin, Runnable) Scheduler API}.
     * The plugin managing this SpigotSql is used to register the task.
     * Please note that any tasks added to the returned Future will be called in an async thread.
     *
     * @param query the query string
     * @param args  the arguments to pass to the SQL driver
     * @return a future that will be populated with the return value upon completion of the update
     * @see #safelyExecuteUpdate(String, Object...)
     */
    public CompletableFuture<Integer> executeSimpleUpdateAsync(String query, Object... args) {
        CompletableFuture<Integer> future = new CompletableFuture<>();

        plugin.getServer().getScheduler().runTaskAsynchronously(plugin,
                () -> future.complete(safelyExecuteUpdate(query, args)));

        return future;
    }

    /**
     * Executes an update statement in an asynchronous thread using
     * Bukkit's {@link org.bukkit.scheduler.BukkitScheduler#runTaskAsynchronously(org.bukkit.plugin.Plugin, Runnable) Scheduler API}.
     * The plugin managing this SpigotSql is used to register the task.
     * Please note that any tasks added to the returned Future (including {@code consumer}) will be called in an async thread.
     * The consumer is expected to close the passed result. If it does not close the result, memory leaks are to be expected.
     *
     * @param query    the query string
     * @param args     the arguments to pass to the SQL driver
     * @param consumer a consumer which will be processing the update's result.
     * @return a Future which can be used to retrieve the result of the update
     * @see #executeUpdateWithGenKeys(String, Object...)
     */
    public CompletableFuture<UpdateResult> executeUpdateAsyncWithGenKeys(String query, Consumer<UpdateResult> consumer, Object... args) {
        CompletableFuture<UpdateResult> future = new CompletableFuture<>();
        future.thenAccept(consumer);

        plugin.getServer().getScheduler().runTaskAsynchronously(plugin,
                () -> {
                    try {
                        future.complete(executeUpdateWithGenKeys(query, args));
                    } catch (SQLException e) {
                        e.printStackTrace();
                        future.completeExceptionally(e);
                    }
                });

        return future;
    }

    /**
     * Executes a query statement in an asynchronous thread using
     * Bukkit's {@link org.bukkit.scheduler.BukkitScheduler#runTaskAsynchronously(org.bukkit.plugin.Plugin, Runnable) Scheduler API}.
     * The plugin managing this SpigotSql is used to register the task.
     * Please note that any tasks added to the returned Future (including {@code consumer}) will be called in an async thread.
     * The consumer is expected to close the passed result. If it does not close the result, memory leaks are to be expected.
     *
     * @param query    the query string
     * @param args     the arguments to pass to the SQL driver
     * @param consumer a consumer which will be processing the query's result.
     * @return a Future which can be used to retrieve the result of the query
     * @see #executeQueryWithResult(String, Object...) (String, Object...)
     */
    public CompletableFuture<QueryResult> executeQueryAsync(String query, Consumer<QueryResult> consumer, Object... args) {
        CompletableFuture<QueryResult> future = new CompletableFuture<>();
        future.thenAccept(consumer);

        plugin.getServer().getScheduler().runTaskAsynchronously(plugin,
                () -> {
                    try {
                        future.complete(executeQueryWithResult(query, args));
                    } catch (SQLException e) {
                        e.printStackTrace();
                        future.completeExceptionally(e);
                    }
                });

        return future;
    }
}
