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
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import java.util.function.Function;

/**
 * A checkmark-based renderer which sends colored output to a {@link CommandSender}.
 *
 * @author <a href="http://xxyy.github.io/">xxyy</a>
 * @since 3.8.14
 */
public class CommandSenderRenderer extends CheckmarkBasedRenderer {

    protected CommandSenderRenderer(Function<Boolean, String> markFactory) {
        super(markFactory);
    }

    /**
     * Sends a rendered checklist to someone.
     *
     * @param sender    the receiver of the rendered items
     * @param checklist the checklist which contains the items
     * @return always TRUE
     */
    public boolean renderFor(CommandSender sender, Checklist checklist) {
        renderEach(checklist).forEach(sender::sendMessage);
        return true;
    }

    /**
     * Builder for {@link CommandSenderRenderer}.
     * Note to call the most specific methods (e.g. {@link #checkedColor(ChatColor)} first to prevent casting back.
     */
    public static class Builder extends CheckmarkBasedRendererBuilder<CommandSenderRenderer> {
        private ChatColor checkedColor = ChatColor.DARK_GREEN; //TODO: Find some solution for BungeeCord's separate ChatColor enum
        private ChatColor uncheckedColor = ChatColor.DARK_RED;
        private ChatColor itemColor = ChatColor.YELLOW;

        /**
         * Sets the color of checked items' checkmarks for this builder.
         *
         * @param checkedColor the color of checked items' checkmarks
         * @return this builder
         */
        public Builder checkedColor(ChatColor checkedColor) {
            this.checkedColor = checkedColor;
            return this;
        }

        /**
         * Sets the color of unchecked items' checkmarks for this builder.
         *
         * @param uncheckedColor the color of unchecked items' checkmarks
         * @return this builder
         */
        public Builder uncheckedColor(ChatColor uncheckedColor) {
            this.uncheckedColor = uncheckedColor;
            return this;
        }

        /**
         * Sets the color of checked items for this builder.
         *
         * @param itemColor the color of checked items
         * @return this builder
         */
        public Builder itemColor(ChatColor itemColor) {
            this.itemColor = itemColor;
            return this;
        }

        @Override
        protected CommandSenderRenderer getInstance(Function<Boolean, String> renderer) {
            Function<Boolean, String> finalRenderer = ckd ->
                    (ckd ? checkedColor : uncheckedColor) + //Color for checked/unchecked
                            renderer.apply(ckd) + //Call parent logic
                            itemColor; //Item is printed directly afterwards, so set that color

            return new CommandSenderRenderer(finalRenderer);
        }
    }
}
