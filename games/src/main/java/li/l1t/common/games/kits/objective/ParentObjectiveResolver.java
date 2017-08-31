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

package li.l1t.common.games.kits.objective;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;

/**
 * Resolves objectives by propagating calls to child objective resolvers using a special
 * objective string format:
 * <p>
 * {@code objective type};;{@code objective parameters}
 * </p>
 * <p>
 * The objective strings passed to child resolvers only contain the parameters to simplify
 * parsing since every child resolver only resolves a single objective type. Note that objective
 * types are case-sensitive.
 * </p>
 * <p>
 * Access to unknown objective types is always denied.
 * </p>
 *
 * @author <a href="http://xxyy.github.io/">xxyy</a>
 * @since 2016-06-21
 */
public class ParentObjectiveResolver implements ObjectiveResolver {
    public static final String TYPE_SEPARATOR = ";;";

    // array indexes for objective format
    private static final int TYPE = 0;
    private static final int PARAM = 1;

    private final Map<String, ObjectiveResolver> childResolvers = new HashMap<>();

    /**
     * Adds a child resolver for a specific objective type. See
     * {@link ParentObjectiveResolver the class JavaDoc} for details. Existing resolvers for the
     * same type will be replaced.
     *
     * @param type  the objective type the child resolves
     * @param child the resolver for that type
     * @return this resolver for call chaining
     */
    public ParentObjectiveResolver addChild(String type, ObjectiveResolver child) {
        childResolvers.put(type, child);
        return this;
    }

    /**
     * Removes a child resolver by the handled objective type.
     *
     * @param type the type to remove the resolver for
     * @return the removed resolver, or null if none was registered
     */
    public ObjectiveResolver removeChild(String type) {
        return childResolvers.remove(type);
    }

    /**
     * Gets the child resolver handling given objective type.
     *
     * @param type the type to get the resolver for
     * @return the resolver for given type, or null if none was registered
     */
    public ObjectiveResolver getChild(String type) {
        return childResolvers.get(type);
    }

    @Override
    public boolean isKnown(String objective) {
        return getChild(splitObjective(objective)[TYPE]) != null;
    }

    @Override
    public boolean isCompleted(String objective, Player player) {
        String[] args = splitObjective(objective);
        String type = args[TYPE];
        String parameters = args[PARAM];

        ObjectiveResolver resolver = getChild(type);
        //noinspection SimplifiableIfStatement
        if (resolver == null) {
            return false; //unknown objectives are denied
        } else {
            return resolver.isCompleted(parameters, player);
        }
    }

    @Override
    public String getName(String objective) {
        String[] args = splitObjective(objective);
        ObjectiveResolver resolver = getChild(args[TYPE]);
        return resolver == null ? null : resolver.getName(args[PARAM]);
    }

    @Override
    public ItemStack getUnavailIcon(String objective) {
        String[] args = splitObjective(objective);
        ObjectiveResolver resolver = getChild(args[TYPE]);
        return resolver == null ? null : resolver.getUnavailIcon(args[PARAM]);
    }

    private String[] splitObjective(String objective) {
        String[] args = objective.split(TYPE_SEPARATOR, 2);
        if (args.length == 1) { // we need an empty string there if we want to avoid checking every time
            return new String[]{args[TYPE], ""}; //there are cleaner ways to do this, but this is
            // simpler when we only have a length of two
        } else {
            return args;
        }
    }
}
