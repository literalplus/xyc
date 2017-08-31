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

package li.l1t.common.games.data;

import li.l1t.common.games.GameLib;
import li.l1t.common.sql.SafeSql;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
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
 * @author <a href="http://xxyy.github.io/">xxyy</a>
 * @deprecated Part of the deprecated PlayerWrapper API. See {@link PlayerWrapper} for details.
 */
@Deprecated
public class ReflectionPlayerWrapperFactory<T extends PlayerWrapper> implements PlayerWrapperFactory<T> {

    private static final PlayerWrapperFactory<PlayerWrapper> GENERIC_FACTORY = new DefaultPlayerWrapperFactory(GameLib.getSql());
    final Class<T> clazz;
    final SafeSql ssql;
    final Map<String, UUID> nameToUuidMap = new WeakHashMap<>();
    Map<UUID, T> wrappers = new ConcurrentHashMap<>(15, 0.75F, 2);

    /**
     * Creates a new factory that is capable of managing
     * {@code T extends {@link PlayerWrapper}} instances and delegating construction of these.
     *
     * @param clazz the class to generate instances of
     * @param ssql  the SafeSql to use to get data (passed to constructor)
     */
    public ReflectionPlayerWrapperFactory(Class<T> clazz, SafeSql ssql) {
        this.clazz = clazz;
        this.ssql = ssql;
    }

    /**
     * Returns an example factory instance for use with {@link PlayerWrapper}.
     *
     * @return factory for {@link PlayerWrapper}s.
     */
    public static PlayerWrapperFactory<PlayerWrapper> getGenericFactory() {
        return GENERIC_FACTORY;
    }

    @Override
    public void forceFetchAllProducts() {
        if (this.wrappers.isEmpty()) {
            return;
        }
        for (T wrp : this.wrappers.values()) {
            wrp.forceFullFlush();
        }
    }

    @Override
    public void forceFlushAllProducts() {
        if (this.wrappers.isEmpty()) {
            return;
        }
        for (T wrp : this.wrappers.values()) {
            wrp.forceFullFlush();
        }
    }

    @Override
    public void free(UUID wrpUniqueId) {
        this.wrappers.remove(wrpUniqueId);
    }

    @Override
    public T getWrapper(CommandSender sender) {
        T rtrn;
        UUID uuid;
        if (sender instanceof ConsoleCommandSender) {
            uuid = PlayerWrapper.CONSOLE_UUID;
            rtrn = this.wrappers.get(uuid);
        } else if (sender instanceof Player) {
            uuid = ((Player) sender).getUniqueId();
            rtrn = this.wrappers.get(uuid);
        } else {
            uuid = null;
            rtrn = null;
        }

        if (rtrn == null) {
            try {
                Constructor<T> constructor = clazz.getDeclaredConstructor(CommandSender.class, SafeSql.class);
                constructor.setAccessible(true);
                rtrn = constructor.newInstance(sender, ssql);
                this.wrappers.put(uuid, rtrn);
                this.nameToUuidMap.put(rtrn.name(), rtrn.getUniqueId());
            } catch (Exception ex) { //multi-catch seems to confuse proguard
                Logger.getLogger(ReflectionPlayerWrapperFactory.class.getName()).log(Level.SEVERE,
                        "Could not acquire instance - Missing T(CommandSender, SafeSql) constructor, probably.", ex);
            }
        }
        return rtrn;
    }

    @Override
    public T getWrapper(@Nonnull UUID uuid, @Nullable String plrName) {
        T rtrn = this.wrappers.get(uuid);
        if (rtrn == null) {
            try {
                Constructor<T> constructor = clazz.getDeclaredConstructor(UUID.class, String.class, SafeSql.class);
                constructor.setAccessible(true);
                rtrn = constructor.newInstance(uuid, plrName, ssql);
                this.wrappers.put(uuid, rtrn);
                this.nameToUuidMap.put(rtrn.name(), uuid);
            } catch (Exception ex) { //multi-catch seems to confuse proguard
                Logger.getLogger(ReflectionPlayerWrapperFactory.class.getName()).log(Level.SEVERE,
                        "Could not acquire instance - Missing T(String, SafeSql) constructor, probably.", ex);
            }
        }
        return rtrn;
    }

    /**
     * @return A map mapping player names to all products that strong references are kept for by this instance.
     */
    public Map<UUID, T> getWrapperMap() {
        return this.wrappers;
    }

    @Override
    public Collection<T> getWrappers() {
        return this.wrappers.values();
    }
}
