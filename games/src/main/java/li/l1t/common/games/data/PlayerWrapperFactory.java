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

import li.l1t.common.sql.SafeSql;
import org.bukkit.command.CommandSender;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Collection;
import java.util.UUID;

/**
 * @author <a href="http://xxyy.github.io/">xxyy</a>
 * @since 15.4.14
 * @deprecated Part of the deprecated PlayerWrapper API. See {@link PlayerWrapper} for details.
 */
@Deprecated
public interface PlayerWrapperFactory<T extends PlayerWrapper> {
    /**
     * Forces a full (re-)fetch for all products owned by this instance.
     *
     * @see PlayerWrapper#forceFullFetch()
     */
    void forceFetchAllProducts();

    /**
     * Forces a full flush for all products owned by this instance.
     *
     * @see PlayerWrapper#forceFullFlush()
     */
    void forceFlushAllProducts();

    /**
     * Frees all instances of a product by a specified UUID kept by this instance.
     *
     * @param wrpUniqueId UUID of the player whose wrappers shall be freed.
     */
    void free(UUID wrpUniqueId);

    /**
     * Wraps a {@link org.bukkit.command.CommandSender}.
     * <b>Note the dangers when using {@link org.bukkit.command.CommandSender} specified in {@link PlayerWrapper#PlayerWrapper(org.bukkit.command.CommandSender, SafeSql)}!</b>
     *
     * @param sender {@link org.bukkit.command.CommandSender} to be wrapped.
     * @return a T wrapping sender.
     * @throws ClassCastException forwarded from {@link PlayerWrapper#PlayerWrapper(org.bukkit.command.CommandSender, SafeSql)}
     * @see PlayerWrapper#PlayerWrapper(org.bukkit.command.CommandSender, SafeSql)
     */
    T getWrapper(CommandSender sender);

    /**
     * Wraps a player by name and uuid.
     *
     * @param uuid    the unique id of the wrapped player
     * @param plrName name of the wrapped player.
     * @return a T wrapping plrName.
     * @see PlayerWrapper#PlayerWrapper(java.util.UUID, String, SafeSql)
     */
    T getWrapper(@Nonnull UUID uuid, @Nullable String plrName);

    /**
     * @return All products that strong references are kept for by this instance.
     */
    Collection<T> getWrappers();
}
