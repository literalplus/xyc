/*
 * Copyright (c) 2013 - 2015 xxyy (Philipp Nowak; devnull@nowak-at.net). All rights reserved.
 *
 * Any usage, including, but not limited to, compiling, running, redistributing, printing,
 *  copying and reverse-engineering is strictly prohibited without explicit written permission
 *  from the original author and may result in legal steps being taken.
 *
 * See the included LICENSE file (core/src/main/resources) or email xxyy98+xyclicense@gmail.com for details.
 */

package io.github.xxyy.common.chat;

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
