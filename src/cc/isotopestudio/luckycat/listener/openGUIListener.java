package cc.isotopestudio.luckycat.listener;
/*
 * Created by Mars Tan on 12/11/2016.
 * Copyright ISOTOPE Studio
 */

import cc.isotopestudio.luckycat.gui.LuckyGUI;
import cc.isotopestudio.luckycat.settings.LuckySettings;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import static org.bukkit.event.block.Action.RIGHT_CLICK_AIR;
import static org.bukkit.event.block.Action.RIGHT_CLICK_BLOCK;

public class openGUIListener implements Listener {
    @EventHandler
    public void onRightClick(PlayerInteractEvent event) {
        if (!(event.getAction() == RIGHT_CLICK_AIR || event.getAction() == RIGHT_CLICK_BLOCK)) {
            return;
        }
        final Player player = event.getPlayer();
        ItemStack itemInHand = player.getItemInHand();
        if (itemInHand == null || itemInHand.getType() == Material.AIR) {
            return;
        }
        itemInHand.setAmount(1);
        if (!itemInHand.equals(LuckySettings.lot)) {
            return;
        }
        event.setCancelled(true);
        new LuckyGUI(player).open(player);
    }
}
