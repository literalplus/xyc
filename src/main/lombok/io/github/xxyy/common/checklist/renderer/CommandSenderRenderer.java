package io.github.xxyy.common.checklist.renderer;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import io.github.xxyy.common.checklist.Checklist;

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
     * @param sender the receiver of the rendered items
     * @param checklist the checklist which contains the items
     * @return always TRUE
     */
    public boolean renderFor(CommandSender sender, Checklist checklist) {
        renderEach(checklist).stream()
                .forEach(sender::sendMessage);
        return true;
    }

    public static class Builder extends CheckmarkBasedRendererBuilder<CommandSenderRenderer> {
        private ChatColor checkedColor = ChatColor.DARK_GREEN; //TODO: Find some solution for BungeeCord's separate ChatColor enum
        private ChatColor uncheckedColor = ChatColor.DARK_RED;
        private ChatColor itemColor = ChatColor.YELLOW;

        /**
         * Sets the color of checked items' checkmarks for this builder.
         * @param checkedColor the color of checked items' checkmarks
         * @return this builder
         */
        public Builder checkedColor(ChatColor checkedColor) {
            this.checkedColor = checkedColor;
            return this;
        }

        /**
         * Sets the color of unchecked items' checkmarks for this builder.
         * @param uncheckedColor the color of unchecked items' checkmarks
         * @return this builder
         */
        public Builder uncheckedColor(ChatColor uncheckedColor) {
            this.uncheckedColor = uncheckedColor;
            return this;
        }

        /**
         * Sets the color of checked items for this builder.
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
