package cc.isotopestudio.luckycat.gui;
/*
 * Created by Mars Tan on 12/11/2016.
 * Copyright ISOTOPE Studio
 */


import java.util.Arrays;
import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;

import static cc.isotopestudio.luckycat.LuckyCat.plugin;

public abstract class GUI implements Listener {

    // From: https://bukkit.org/threads/icon-menu.108342

    final String name;
    final int size;
    String[] optionNames;
    ItemStack[] optionIcons;
    int page;
    HashMap<Integer, Integer> slotIDMap;
    final Player player;
    final String playerName;
    private boolean isDestoryed = false;

    GUI(String name, int size, Player player) {
        this.name = name;
        this.size = size;
        this.player = player;
        playerName = player.getName();
        this.optionNames = new String[size];
        this.optionIcons = new ItemStack[size];
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    void setOption(int position, ItemStack icon, String name, String... info) {
        optionNames[position] = name;
        optionIcons[position] = setItemNameAndLore(icon, name, info);
    }

    void setOption(int position, ItemStack item) {
        optionNames[position] = item.getItemMeta() == null ? item.getType().toString()
                : item.getItemMeta().getDisplayName();
        optionIcons[position] = item;
    }

    Inventory inventory;

    public void open(Player player) {
        inventory = Bukkit.createInventory(player, size, name);
        for (int i = 0; i < optionIcons.length; i++) {
            if (optionIcons[i] != null) {
                inventory.setItem(i, optionIcons[i]);
            }
        }
        player.openInventory(inventory);
    }

    void Destory() {
        isDestoryed = true;
        HandlerList.unregisterAll(this);
        optionNames = null;
        optionIcons = null;
    }


    @EventHandler(priority = EventPriority.MONITOR)
    public void onInventoryClick(final InventoryClickEvent event) {
        if (event.getInventory().getName().equals(name))
            event.setCancelled(true);
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onInventoryClose(InventoryCloseEvent event) {
        if (event.getInventory().getTitle().equals(name) && playerName.equals(event.getPlayer().getName())) {
            Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
                public void run() {
                    Destory();
                }
            }, 0);
        }
    }

    private ItemStack setItemNameAndLore(ItemStack item, String name, String[] lore) {
        ItemMeta im = item.getItemMeta();
        im.setDisplayName(name);
        im.setLore(Arrays.asList(lore));
        item.setItemMeta(im);
        return item;
    }

    static String getName(String a) {
        for (int i = 0; i <= 5; i++) {
            switch ((int) (Math.random() * 5)) {
                case (0): {
                    a += "§f";
                    break;
                }
                case (1): {
                    a += "§1";
                    break;
                }
                case (2): {
                    a += "§2";
                    break;
                }
                case (3): {
                    a += "§3";
                    break;
                }
                case (4): {
                    a += "§4";
                    break;
                }
            }
        }
        return a;
    }

}