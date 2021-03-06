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

package li.l1t.common.games.teams;

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
        return this$color == null ? other$color == null : this$color.equals(other$color);
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

    public void setColor(DyeColor color) {
        this.color = color;
    }
}
