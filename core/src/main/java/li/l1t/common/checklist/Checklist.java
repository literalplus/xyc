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

package li.l1t.common.checklist;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents a checklist with items which each hold a condition and description.
 *
 * @author <a href="http://xxyy.github.io/">xxyy</a>
 * @since 3.8.14
 */
public class Checklist {
    private final List<Item> items = new ArrayList<>();

    public Checklist append(Item item) {
        items.add(item);
        return this;
    }

    public Checklist append(String description, ChecklistEvaluator evaluator) {
        return append(new Item(description, evaluator));
    }

    public boolean isDone() {
        return items.stream()
                .allMatch(Item::isChecked);
    }

    public List<Item> getItems() {
        return items;
    }

    public static class Item {
        @Nonnull
        private final String description;
        @Nonnull
        private final ChecklistEvaluator evaluator;

        public Item(@Nonnull String description, @Nonnull ChecklistEvaluator evaluator) {
            this.description = description;
            this.evaluator = evaluator;
        }

        @Nonnull
        public String getDescription() {
            return description;
        }

        /**
         * Checks whether this item is checked by calling its evaluator.
         *
         * @return whether this item is checked
         * @see ChecklistEvaluator#isChecked()
         */
        public boolean isChecked() {
            return evaluator.isChecked();
        }
    }
}
