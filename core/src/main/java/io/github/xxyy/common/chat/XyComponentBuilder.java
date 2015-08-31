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

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;

/**
 * A drop-in replacement for {@link XyComponentBuilder} which adds some additional convenience
 * methods in order to achieve simpler configuration.
 *
 * @author <a href="http://xxyy.github.io/">xxyy</a>
 * @since 2015-08-31
 */
public class XyComponentBuilder extends ComponentBuilder {
    public XyComponentBuilder(XyComponentBuilder original) {
        super(original);
    }

    public XyComponentBuilder(String text) {
        super(text);
    }

    /**
     * Sets the {@link HoverEvent} with type of {@link HoverEvent.Action#SHOW_TEXT}
     * and the given message for the current part. Use {@code \n} for newlines.
     *
     * @param legacyText the legacy text to add as tooltip
     * @return this builder for chaining
     */
    public XyComponentBuilder tooltip(String legacyText) {
        event(new HoverEvent(HoverEvent.Action.SHOW_TEXT,
                TextComponent.fromLegacyText(legacyText)));
        return this;
    }

    /**
     * Sets the {@link ClickEvent} with type of {@link ClickEvent.Action#RUN_COMMAND}
     * and the given command for the current part.
     *
     * @param command the command to run on click
     * @return this builder for chaining
     */
    public XyComponentBuilder command(String command) {
        event(new ClickEvent(ClickEvent.Action.RUN_COMMAND, command));
        return this;
    }

    /**
     * Sets the {@link ClickEvent} with type of {@link ClickEvent.Action#SUGGEST_COMMAND}
     * and the given command for the current part.
     *
     * @param command the command to suggest in the player's chat box on click
     * @return this builder for chaining
     */
    public XyComponentBuilder suggest(String command) {
        event(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, command));
        return this;
    }

    /**
     * Sets the {@link ClickEvent} with type of {@link ClickEvent.Action#OPEN_URL}
     * and the given URL for the current part.
     *
     * @param url the URL to open on click
     * @return this builder for chaining
     */
    public XyComponentBuilder openUrl(String url) {
        event(new ClickEvent(ClickEvent.Action.OPEN_URL, url));
        return this;
    }

    /**
     * Sets the {@link ClickEvent} with type of {@link ClickEvent.Action#OPEN_FILE}
     * and the given file for the current part.
     *
     * @param fileName the file name to open on click
     * @return this builder for chaining
     */
    public XyComponentBuilder openFile(String fileName) {
        event(new ClickEvent(ClickEvent.Action.OPEN_FILE, fileName));
        return this;
    }

    /**
     * Appends the text to the builder and makes it the current target for
     * formatting.
     *
     * @param text  the text to append
     * @param color the color to set
     * @return this ComponentBuilder for chaining
     */
    public XyComponentBuilder append(String text, ChatColor color) {
        append(text);
        color(color);
        return this;
    }

    /**
     * Appends the text to the builder and makes it the current target for
     * formatting. You can specify the amount of formatting retained.
     *
     * @param text      the text to append
     * @param color     the color to set
     * @param retention the formatting to retain
     * @return this ComponentBuilder for chaining
     */
    public XyComponentBuilder append(String text, ChatColor color, FormatRetention retention) {
        append(text, retention);
        color(color);
        return this;
    }

    /**
     * Appends the text to the builder and makes it the current target for
     * formatting.
     *
     * @param text       the text to append
     * @param color      the color to set
     * @param formatting the only formatting(s) to retain
     * @return this ComponentBuilder for chaining
     */
    public XyComponentBuilder append(String text, ChatColor color, ChatColor... formatting) {
        append(text, FormatRetention.EVENTS);
        color(color);
        for (ChatColor format : formatting) {
            switch (format) {
                case UNDERLINE:
                    underlined(true);
                    break;
                case STRIKETHROUGH:
                    strikethrough(true);
                    break;
                case ITALIC:
                    italic(true);
                    break;
                case BOLD:
                    bold(true);
                    break;
                case MAGIC:
                    obfuscated(true);
                    break;
                default:
                    throw new IllegalArgumentException("not a formatting code, color? " + format);
            }
        }

        return this;
    }

    //////////////////////////////////////////////////////////////////////////////////
    // The rest of this class is just passing calls to super while retaining
    // XyComponentBuilder return type
    //////////////////////////////////////////////////////////////////////////////////

    @Override
    public XyComponentBuilder append(String text) {
        return (XyComponentBuilder) super.append(text);
    }

    @Override
    public XyComponentBuilder append(String text, FormatRetention retention) {
        return (XyComponentBuilder) super.append(text, retention);
    }

    @Override
    public XyComponentBuilder color(ChatColor color) {
        return (XyComponentBuilder) super.color(color);
    }

    @Override
    public XyComponentBuilder bold(boolean bold) {
        return (XyComponentBuilder) super.bold(bold);
    }

    @Override
    public XyComponentBuilder italic(boolean italic) {
        return (XyComponentBuilder) super.italic(italic);
    }

    @Override
    public XyComponentBuilder underlined(boolean underlined) {
        return (XyComponentBuilder) super.underlined(underlined);
    }

    @Override
    public XyComponentBuilder strikethrough(boolean strikethrough) {
        return (XyComponentBuilder) super.strikethrough(strikethrough);
    }

    @Override
    public XyComponentBuilder obfuscated(boolean obfuscated) {
        return (XyComponentBuilder) super.obfuscated(obfuscated);
    }

    @Override
    public XyComponentBuilder event(ClickEvent clickEvent) {
        return (XyComponentBuilder) super.event(clickEvent);
    }

    @Override
    public XyComponentBuilder event(HoverEvent hoverEvent) {
        return (XyComponentBuilder) super.event(hoverEvent);
    }

    @Override
    public XyComponentBuilder reset() {
        return (XyComponentBuilder) super.reset();
    }

    @Override
    public XyComponentBuilder retain(FormatRetention retention) {
        return (XyComponentBuilder) super.retain(retention);
    }
}
