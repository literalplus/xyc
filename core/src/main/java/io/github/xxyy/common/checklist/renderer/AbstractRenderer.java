/*
 * Copyright (c) 2013 - 2015 xxyy (Philipp Nowak; devnull@nowak-at.net). All rights reserved.
 *
 * Any usage, including, but not limited to, compiling, running, redistributing, printing,
 *  copying and reverse-engineering is strictly prohibited without explicit written permission
 *  from the original author and may result in legal steps being taken.
 *
 * See the included LICENSE file (core/src/main/resources) or email xxyy98+xyclicense@gmail.com for details.
 */

package io.github.xxyy.common.checklist.renderer;

import io.github.xxyy.common.checklist.Checklist;

import java.util.Collection;
import java.util.stream.Collectors;

/**
 * An abstract checklist renderer which implements basic operations.
 *
 * @author <a href="http://xxyy.github.io/">xxyy</a>
 * @since 3.8.14
 */
public abstract class AbstractRenderer {

    /**
     * Renders all items of given checklist using this renderer.
     * Rendered items are separated by newlines (e.g. each item is on its own line)
     * If the checklist contains items, the last character of the returned String will be \n.
     * If the checklist contains no items, an empty String is returned.
     *
     * @param checklist the checklist to render
     * @return a newline-separated String of rendered items.
     */
    public String renderAll(Checklist checklist) {
        StringBuilder sb = new StringBuilder();
        checklist.getItems().stream()
                .forEach(item -> render(sb, item).append("\n"));
        return sb.toString();
    }

    /**
     * Renders each of the passed checklist's items using this renderer.
     * Note that if you want to output the checklist with one item per line, {@link #renderAll(Checklist)} is more efficient.
     *
     * @param checklist the checklist to render
     * @return a collection of every checklist item's string representation as output by this renderer.
     */
    public Collection<String> renderEach(Checklist checklist) {
        return checklist.getItems().stream()
                .map(this::render)
                .collect(Collectors.toList());
    }

    /**
     * Renders a single item using this renderer.
     *
     * @param item the item to render
     * @return a String representing the input item.
     */
    public String render(Checklist.Item item) {
        return render(new StringBuilder(), item).toString();
    }

    /**
     * Renders a single item using this renderer and appends the result to a {@link StringBuilder}.
     *
     * @param sb   the StringBuilder to use
     * @param item the item to render
     * @return {@code sb}
     */
    public abstract StringBuilder render(StringBuilder sb, Checklist.Item item);
}
