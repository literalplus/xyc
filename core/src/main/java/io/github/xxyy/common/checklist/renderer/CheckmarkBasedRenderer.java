/*
 * Copyright (c) 2013 - 2014 xxyy (Philipp Nowak; devnull@nowak-at.net). All rights reserved.
 *
 * Any usage, including, but not limited to, compiling, running, redistributing, printing,
 *  copying and reverse-engineering is strictly prohibited without explicit written permission
 *  from the original author and may result in legal steps being taken.
 */

package io.github.xxyy.common.checklist.renderer;

import io.github.xxyy.common.checklist.Checklist;

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
