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

package li.l1t.common.checklist.renderer;

import li.l1t.common.checklist.Checklist;

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
        checklist.getItems().forEach(item -> render(sb, item).append("\n"));
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
