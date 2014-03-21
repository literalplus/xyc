package io.github.xxyy.common.games.data;

import io.github.xxyy.common.games.GameLib;
import io.github.xxyy.common.sql.SafeSql;
import org.bukkit.command.CommandSender;

import java.lang.reflect.Constructor;
import java.util.Collection;
import java.util.Map;
import java.util.UUID;
import java.util.WeakHashMap;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Factory class that produces implementations of {@link PlayerWrapper} and stores the instances.
 *
 * @param <T> Implementation of {@link PlayerWrapper} that is produced by this factory.
 *
 * @author <a href="http://xxyy.github.io/">xxyy</a>
 */
public class PlayerWrapperFactory<T extends PlayerWrapper<T>> {

    private static final PlayerWrapperFactory<GenericPlayerWrapper> GENERIC_FACTORY = new PlayerWrapperFactory<>(GenericPlayerWrapper.class, GameLib.
            getSql());
    Map<String, T> wrappers = new ConcurrentHashMap<>(15, 0.75F, 2);
    final Class<T> clazz;
    final SafeSql ssql;
    final Map<UUID, String> uuidToNameMap = new WeakHashMap<>();

    /**
     * Creates a new factory that is capable of managing
     * <code>T extends {@link PlayerWrapper}</code> instances and delegating construction of these.
     */
    public PlayerWrapperFactory(Class<T> clazz, SafeSql ssql) {
        this.clazz = clazz;
        this.ssql = ssql;
    }

    /**
     * Forces a full (re-)fetch for all products owned by this instance.
     *
     * @see PlayerWrapper#forceFullFetch()
     */
    public void forceFetchAllProducts() {
        if (this.wrappers.isEmpty()) {
            return;
        }
        for (T wrp : this.wrappers.values()) {
            wrp.forceFullFlush();
        }
    }

    /**
     * Forces a full flush for all products owned by this instance.
     *
     * @see PlayerWrapper#forceFullFlush()
     */
    public void forceFlushAllProducts() {
        if (this.wrappers.isEmpty()) {
            return;
        }
        for (T wrp : this.wrappers.values()) {
            wrp.forceFullFlush();
        }
    }

    /**
     * Frees all instances of a product by a specified name kept by this instance.
     *
     * @param wrpName Name of the player whose wrappers shall be freed.
     */
    public void free(String wrpName) {
        this.wrappers.remove(wrpName);
    }

    /**
     * Wraps a {@link CommandSender}.
     * <b>Note the dangers when using {@link CommandSender} specified in {@link PlayerWrapper#PlayerWrapper(CommandSender, SafeSql)}!</b>
     *
     * @param sender {@link CommandSender} to be wrapped.
     *
     * @return a T wrapping sender.
     * @see PlayerWrapper#PlayerWrapper(CommandSender, SafeSql)
     * @throws ClassCastException forwarded from {@link PlayerWrapper#PlayerWrapper(CommandSender, SafeSql)}
     */
    public T getWrapper(CommandSender sender) {
        T rtrn = this.wrappers.get(sender.getName());
        if (rtrn == null) {
            try {
                Constructor<T> constr = clazz.getDeclaredConstructor(CommandSender.class, SafeSql.class);
                constr.setAccessible(true);
                rtrn = constr.newInstance(sender, ssql);
                this.wrappers.put(sender.getName(), rtrn);
                this.uuidToNameMap.put(rtrn.getUniqueId(), rtrn.name());
            } catch (Exception ex) { //multi-catch seems to confuse proguard
                Logger.getLogger(PlayerWrapperFactory.class.getName()).log(Level.SEVERE,
                        "Could not aquire instance - Mising T(CommandSender) constructor, probably.", ex);
            }
        }
        return rtrn;
    }

    /**
     * Wraps a player by name.
     *
     * @param plrName name of the wrapped player.
     *
     * @return a T wrapping plrName.
     * @see PlayerWrapper#PlayerWrapper(String, SafeSql)
     */
    public T getWrapper(String plrName) {
        T rtrn = this.wrappers.get(plrName);
        if (rtrn == null) {
            try {
                Constructor<T> constr = clazz.getDeclaredConstructor(String.class, SafeSql.class);
                constr.setAccessible(true);
                rtrn = constr.newInstance(plrName, ssql);
                this.wrappers.put(plrName, rtrn);
                this.uuidToNameMap.put(rtrn.getUniqueId(), rtrn.name());
            } catch (Exception ex) { //multi-catch seems to confuse proguard
                Logger.getLogger(PlayerWrapperFactory.class.getName()).log(Level.SEVERE,
                        "Could not aquire instance - Mising T(String) constructor, probably.", ex);
            }
        }
        return rtrn;
    }

    /**
     * Wraps a player by UUID.
     *
     * @param uuid UUID of the wrapped player.
     *
     * @return a T wrapping plrName.
     * @see PlayerWrapper#PlayerWrapper(String, SafeSql)
     */
    public T getWrapper(UUID uuid) {
        String plrName = this.uuidToNameMap.get(uuid);
        T rtrn = null;
        if(plrName != null) {
            rtrn = getWrapper(plrName);
        }

        if (rtrn == null) {
            try {
                Constructor<T> constr = clazz.getDeclaredConstructor(UUID.class, SafeSql.class);
                constr.setAccessible(true);
                rtrn = constr.newInstance(uuid, ssql);
                this.wrappers.put(rtrn.name(), rtrn);
                this.uuidToNameMap.put(rtrn.getUniqueId(), rtrn.name());
            } catch (Exception ex) { //multi-catch seems to confuse proguard
                Logger.getLogger(PlayerWrapperFactory.class.getName()).log(Level.SEVERE,
                        "Could not aquire instance - Mising T(UUID, SafeSql) constructor, probably.", ex);
            }
        }
        return rtrn;
    }

    /**
     * @return A map mapping player names to all products that strong references are kept for by this instance.
     */
    public Map<String, T> getWrapperMap() {
        return this.wrappers;
    }

    /**
     * @return All products that strong references are kept for by this instance.
     */
    public Collection<T> getWrappers() {
        return this.wrappers.values();
    }

    /**
     * Returns an example factory instance for use with {@link GenericPlayerWrapper}.
     *
     * @return factory for {@link GenericPlayerWrapper}s.
     */
    public static PlayerWrapperFactory<GenericPlayerWrapper> getGenericFactory() {
        return GENERIC_FACTORY;
    }
}
