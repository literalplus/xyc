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
