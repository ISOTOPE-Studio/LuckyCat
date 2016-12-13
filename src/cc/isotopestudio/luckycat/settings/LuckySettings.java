package cc.isotopestudio.luckycat.settings;
/*
 * Created by Mars Tan on 12/11/2016.
 * Copyright ISOTOPE Studio
 */

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.*;

import static cc.isotopestudio.luckycat.LuckyCat.config;
import static cc.isotopestudio.luckycat.LuckyCat.playerData;

public class LuckySettings {

    public static ItemStack lot;
    public static List<ItemStack> awardList = new ArrayList<>();
    public static List<Integer> luckList = new ArrayList<>();

    public static void setLot(ItemStack item) {
        lot = item.clone();
        config.set("lot", item);
        config.save();
    }

    public static void addItem(ItemStack item, int luck) {
        awardList.add(item);
        luckList.add(luck);
        storeAwardItems();
    }

    public static void remove(int id) {
        awardList.remove(id);
        luckList.remove(id);
        storeAwardItems();
    }

    public static void removeAll() {
        awardList.clear();
        luckList.clear();
        storeAwardItems();
    }

    public static void removeAllRecords() {
        for (String key : playerData.getKeys(false))
            playerData.set(key, null);
        playerData.save();
    }

    private static void storeAwardItems() {
        config.set("awards", null);
        for (int i = 0; i < awardList.size(); i++) {
            config.set("awards." + i + ".item", awardList.get(i));
            config.set("awards." + i + ".luck", luckList.get(i));
        }
        config.save();
    }

    public static void addPlayerReward(Player player, ItemStack item) {
        playerData.set(player.getName() + ".temp", "temp");
        ConfigurationSection playerSection = playerData.getConfigurationSection(player.getName());
        playerData.set(player.getName() + ".temp", null);
        int max = 0;
        for (String key : playerSection.getKeys(false)) {
            int k = Integer.parseInt(key);
            if (k > max) max = k;
        }
        playerSection.set((max + 1) + "", item);
        playerData.save();
    }

    public static Set<ItemStack> getPlayerRewards(Player player) {
        Set<ItemStack> result = new HashSet<>();
        ConfigurationSection playerSection = playerData.getConfigurationSection(player.getName());
        if (playerSection != null)
            for (String key : playerSection.getKeys(false)) {
                result.add(playerSection.getItemStack(key));
            }
        return result;
    }
}
