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

package li.l1t.common.chat;

import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.HoverEvent.Action;
import net.md_5.bungee.chat.ComponentSerializer;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.stream.Collectors;

/**
 * Builds item data for use in {@link Action#SHOW_ITEM} type hover events in chat components.
 *
 * @author <a href="http://xxyy.github.io/">xxyy</a>
 * @since 2015-12-27
 */
public class ItemComponentBuilder {
    private final ItemStack stack;
    private final String text;

    public ItemComponentBuilder(ItemStack stack, String text) {
        this.stack = stack;
        this.text = text;
    }

    /**
     * Creates an array of base components representing this builder's item for use in hover events.
     *
     * @return an array of base components
     */
    public BaseComponent[] create() {
        StringBuilder tagBuilder = new StringBuilder("{");
        if (stack.hasItemMeta()){
            ItemMeta meta = stack.getItemMeta();
            boolean hasDisplayName = meta.hasDisplayName();
            boolean hasLore = meta.hasLore();
            if (hasDisplayName || hasLore){
                tagBuilder.append("display:{");
                if (hasDisplayName){
                    tagBuilder.append("Name:").append(meta.getDisplayName());
                    if (hasLore){
                        tagBuilder.append(",");
                    }
                }
                if (hasLore){
                    tagBuilder.append("Lore:")
                            .append(meta.getLore().stream()
                                    .collect(Collectors.joining("\\\", \\\"", "[\\\"", "\\\"]")));
                }
                tagBuilder.append("}");
            }

        }
        tagBuilder.append("}");


        //noinspection deprecation
        return ComponentSerializer.parse(String.format(
                "{text:\"%s\", hoverEvent: {" +
                        "action:show_item," +
                        "value:\"{" +
                        "  id:minecraft:%s," +
                        "  Count: %d" +
                        "  tag:%s" +
                        "  }" +
                        "}\"" +
                        "}}",
                text,
                stack.getType().name().toLowerCase(),
                stack.getAmount(),
                tagBuilder.toString()
        ));
    }
}
