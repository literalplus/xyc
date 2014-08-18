/*
 * Copyright (c) 2014 xxyy (Philipp Nowak; devnull@nowak-at.net). All rights reserved.
 *
 * Any usage, including, but not limited to, compiling, running, redistributing, printing, copying and reverse-engineering is strictly prohibited without permission from the original author and may result in legal steps being taken.
 */

package io.github.xxyy.common.games.teams;

import org.bukkit.DyeColor;
import org.bukkit.material.Colorable;

/**
 * Represents a team which has a color associated with it.
 * The name property of the team is set to the color's {@link org.bukkit.DyeColor#toString()}.
 *
 * @author <a href="http://xxyy.github.io/">xxyy</a>
 * @since 05/03/14
 */
public abstract class ColorableTeam extends AbstractTeam implements Colorable {
    private DyeColor color;

    public ColorableTeam(DyeColor color) {
        super(color.toString());
    }

    public void setColor(DyeColor color) {
        this.color = color;
    }

    public String toString() {
        return "io.github.xxyy.common.games.teams.ColorableTeam(color=" + this.color + ")";
    }

    public boolean equals(Object o) {
        if (o == this) return true;
        if (!(o instanceof ColorableTeam)) return false;
        final ColorableTeam other = (ColorableTeam) o;
        if (!other.canEqual(this)) return false;
        final Object this$color = this.color;
        final Object other$color = other.color;
        if (this$color == null ? other$color != null : !this$color.equals(other$color)) return false;
        return true;
    }

    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final Object $color = this.color;
        result = result * PRIME + ($color == null ? 0 : $color.hashCode());
        return result;
    }

    public boolean canEqual(Object other) {
        return other instanceof ColorableTeam;
    }

    public DyeColor getColor() {
        return this.color;
    }
}
