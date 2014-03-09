package io.github.xxyy.common.games.teams;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.bukkit.DyeColor;
import org.bukkit.material.Colorable;

/**
 * Represents a team which has a color associated with it.
 * The name property of the team is set to the color's {@link org.bukkit.DyeColor#toString()}.
 *
 * @author <a href="http://xxyy.github.io/">xxyy</a>
 * @since 05/03/14
 */
@Data
@EqualsAndHashCode(callSuper = false)
public abstract class ColorableTeam extends AbstractTeam implements Colorable {
    @Getter
    private DyeColor color;

    public ColorableTeam(DyeColor color) {
        super(color.toString());
    }
}
