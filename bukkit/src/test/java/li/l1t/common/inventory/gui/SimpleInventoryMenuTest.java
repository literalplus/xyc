/*
 * Copyright (c) 2013 - 2015 xxyy (Philipp Nowak; devnull@nowak-at.net). All rights reserved.
 *
 * Any usage, including, but not limited to, compiling, running, redistributing, printing,
 *  copying and reverse-engineering is strictly prohibited without explicit written permission
 *  from the original author and may result in legal steps being taken.
 *
 * See the included LICENSE file (core/src/main/resources) or email xxyy98+xyclicense@gmail.com for details.
 */

package li.l1t.common.inventory.gui;

import li.l1t.common.inventory.gui.element.Placeholder;
import li.l1t.common.test.util.MockHelper;
import li.l1t.common.test.util.mokkit.MockServer;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.junit.Test;

import java.util.UUID;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

/**
 * Tests some parts of SimpleInventoryMenu.
 *
 * @author <a href="http://xxyy.github.io/">xxyy</a>
 * @since 2016-08-14
 */
public class SimpleInventoryMenuTest {
    @Test
    public void redraw__singleItem() throws Exception {
        //given
        MockServer server = MockHelper.mockServer();
        Player player = MockHelper.mockPlayer(UUID.randomUUID(), "Hans");
        SimpleInventoryMenu menu = new SimpleInventoryMenu(MockHelper.mockPlugin(server), "lel", player);
        ItemStack stack = new ItemStack(Material.MELON);
        menu.addElement(5, new Placeholder(stack));
        //when
        menu.redraw();
        //then
        assertThat("item must be drawn correctly", menu.getInventory().getItem(5), is(stack));
    }

}
