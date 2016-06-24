/*
 * Copyright (c) 2013 - 2015 xxyy (Philipp Nowak; devnull@nowak-at.net). All rights reserved.
 *
 * Any usage, including, but not limited to, compiling, running, redistributing, printing,
 *  copying and reverse-engineering is strictly prohibited without explicit written permission
 *  from the original author and may result in legal steps being taken.
 *
 * See the included LICENSE file (core/src/main/resources) or email xxyy98+xyclicense@gmail.com for details.
 */

package li.l1t.common.util;

import li.l1t.common.misc.XyLocation;
import org.bukkit.Location;
import org.bukkit.World;
import org.junit.Test;

import static junit.framework.TestCase.assertFalse;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;

public class LocationHelperTest {
    private final World WORLD_1 = mock(World.class);
    private final World WORLD_2 = mock(World.class);

    @Test
    public void testIsBlockBetween() throws Exception {
        Location boundary1 = new Location(WORLD_1, 0, 0, 0);
        Location boundary2 = new Location(WORLD_1, 50, 50, 50);

        Location obviousInBetween = new Location(WORLD_1, 1, 1, 1);
        Location borderInBetween = new Location(WORLD_1, 0, 1, 1);

        Location notInBetween = new Location(WORLD_1, -12, -5, 48);
        Location differentWorld = new Location(WORLD_2, 1, 1, 1);

        assertTrue("Obviously in-between location not recognized!", LocationHelper.isBlockBetween(obviousInBetween, boundary1, boundary2));
        assertTrue("Border location not recognized!", LocationHelper.isBlockBetween(borderInBetween, boundary1, boundary2));

        assertFalse("Obviously not-in-between location falsely recognized!", LocationHelper.isBlockBetween(notInBetween, boundary1, boundary2));
        assertFalse("Location in different world falsely recognized!", LocationHelper.isBlockBetween(differentWorld, boundary1, boundary2));
    }

    @Test
    public void testSoftEqual() throws Exception {
        Location location1 = new Location(WORLD_1, 0, 42, 56);
        Location location1Clone = location1.clone();
        Location location1Same = new Location(WORLD_1, 0, 42, 56);
        Location location1DifferentWorld = new Location(WORLD_2, 0, 42, 56);
        Location location2 = new Location(WORLD_1, 15, 78, 56);
        Location location3 = new Location(WORLD_1, 15, 42, 56);

        assertTrue("Cloned location not soft-equal!", LocationHelper.softEqual(location1, location1Clone));
        assertTrue("Same location not soft-equal!", LocationHelper.softEqual(location1, location1Same));

        assertFalse("Different location soft-equal! (1)", LocationHelper.softEqual(location1, location2));
        assertFalse("Different location soft-equal! (2)", LocationHelper.softEqual(location1, location3));
        assertFalse("Different location soft-equal! (3)", LocationHelper.softEqual(location3, location2));
        assertFalse("Different world location soft-equal!", LocationHelper.softEqual(location1DifferentWorld, location1));
    }

    @Test
    public void testSerialize() {
        XyLocation location = new XyLocation(WORLD_1, 16, 67, 87, 56.865f, 234.8f);
        String serialized = location.serializeToString();
        assertThat(LocationHelper.deserialize(serialized, WORLD_1), equalTo(location));
    }
}
