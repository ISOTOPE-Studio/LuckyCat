package cc.isotopestudio.luckycat.settings;
/*
 * Created by Mars Tan on 12/11/2016.
 * Copyright ISOTOPE Studio
 */

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import static cc.isotopestudio.luckycat.LuckyCat.config;
import static cc.isotopestudio.luckycat.gui.LuckyGUI.blackGlass;
import static cc.isotopestudio.luckycat.gui.LuckyGUI.greenGlass;
import static cc.isotopestudio.luckycat.gui.LuckyGUI.redGlass;

public class UpdateSettings extends BukkitRunnable {

    @Override
    public void run() {
        ItemStack lot = config.getItemStack("lot");
        if (lot != null)
            LuckySettings.lot = lot;
        ConfigurationSection awardsSection = config.getConfigurationSection("awards");
        LuckySettings.awardList.clear();
        LuckySettings.luckList.clear();
        if (awardsSection != null) {
            for (String key : awardsSection.getKeys(false)) {
                LuckySettings.awardList.add(awardsSection.getItemStack(key + ".item"));
                LuckySettings.luckList.add(awardsSection.getInt(key + ".luck"));
            }
        }
        blackGlass.getItemMeta().setDisplayName(" ");
        greenGlass.getItemMeta().setDisplayName(" 抽奖中 ");
        redGlass.getItemMeta().setDisplayName(" 抽奖中 ");
    }
}
