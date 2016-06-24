/*
 * Copyright (c) 2013 - 2015 xxyy (Philipp Nowak; devnull@nowak-at.net). All rights reserved.
 *
 * Any usage, including, but not limited to, compiling, running, redistributing, printing,
 *  copying and reverse-engineering is strictly prohibited without explicit written permission
 *  from the original author and may result in legal steps being taken.
 *
 * See the included LICENSE file (core/src/main/resources) or email xxyy98+xyclicense@gmail.com for details.
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
