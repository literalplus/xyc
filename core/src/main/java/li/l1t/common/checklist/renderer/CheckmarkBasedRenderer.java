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

import java.util.function.Function;

/**
 * A basic checklist renderer which renders lists using icons and new lines for new items.
 *
 * @author <a href="http://xxyy.github.io/">xxyy</a>
 * @since 3.8.14
 */
public class CheckmarkBasedRenderer extends AbstractRenderer {

    protected final Function<Boolean, String> checkmarkSymbolFactory;

    protected CheckmarkBasedRenderer(Function<Boolean, String> checkmarkSymbolFactory) {
        this.checkmarkSymbolFactory = checkmarkSymbolFactory;
    }

    @Override
    public StringBuilder render(StringBuilder sb, Checklist.Item item) {
        return sb.append(checkmarkSymbolFactory.apply(item.isChecked()))
                .append(" ")
                .append(item.getDescription());
    }

    public static final class Builder extends CheckmarkBasedRendererBuilder<CheckmarkBasedRenderer> { //In case you're wondering, there were originally more checkmark-based renderers.

        @Override
        protected CheckmarkBasedRenderer getInstance(Function<Boolean, String> renderer) {
            return new CheckmarkBasedRenderer(renderer);
        }
    }
}
