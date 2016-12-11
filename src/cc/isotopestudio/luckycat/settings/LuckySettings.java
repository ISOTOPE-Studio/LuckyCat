package cc.isotopestudio.luckycat.settings;
/*
 * Created by Mars Tan on 12/11/2016.
 * Copyright ISOTOPE Studio
 */

import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

import static cc.isotopestudio.luckycat.LuckyCat.config;

public class LuckySettings {

    public static ItemStack lot;
    public static List<ItemStack> awardList = new ArrayList<>();

    public static void setLot(ItemStack item) {
        lot = item.clone();
        config.set("lot", item);
        config.save();
    }

    public static void addItem(ItemStack item) {
        awardList.add(item);
        storeAwardItems();
    }

    public static void remove(int id) {
        awardList.remove(id);
        storeAwardItems();
    }

    public static void removeAll() {
        awardList.clear();
        storeAwardItems();
    }

    private static void storeAwardItems() {
        config.set("awards", null);
        for (int i = 0; i < awardList.size(); i++) {
            config.set("awards." + i, awardList.get(i));
        }
        config.save();
    }

}
