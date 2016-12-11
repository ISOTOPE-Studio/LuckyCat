package cc.isotopestudio.luckycat.gui;
/*
 * Created by Mars Tan on 12/11/2016.
 * Copyright ISOTOPE Studio
 */

import cc.isotopestudio.luckycat.settings.LuckySettings;
import cc.isotopestudio.luckycat.util.S;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import static cc.isotopestudio.luckycat.LuckyCat.plugin;

public class LuckyGUI extends GUI implements Listener {

    public static ItemStack blackGlass = new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 15);
    public static ItemStack greenGlass = new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 5);
    public static ItemStack redGlass = new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 14);


    public LuckyGUI(Player player) {
        super(S.toBoldDarkGreen("抽奖机") + player.getName(), 27, player);
        for (int i = 0; i < 27; i++) {
            if (i < 9 || i > 17) {
                setOption(i, greenGlass);
            } else {
                setOption(i, LuckySettings.awardList.get(i % 9));
            }
        }
        setOption(4, redGlass);
        setOption(22, redGlass);
    }

    private int displacement = 0;
    private int count = 0;

    @Override
    public void open(Player player) {
        super.open(player);
        displaceItem();
    }

    private boolean stop = false;

    private void displaceItem() {
        displacement++;
        displacement %= 9;
        count++;
        for (int i = 9; i < 18; i++) {
            inventory.setItem(i, LuckySettings.awardList.get((i + displacement) % 9));
        }
        if (count > 50 + Math.random() * 10) {
            win(inventory.getItem(13));
            return;
        }
        if (!stop)
            new BukkitRunnable() {
                @Override
                public void run() {
                    displaceItem();
                    System.out.println(name + " " + displacement);
                }
            }.runTaskLater(plugin, 2);
    }

    int winCount = 0;

    private void win(ItemStack item) {
        winCount++;
        player.playSound(player.getLocation(), Sound.ORB_PICKUP, 2, 2);
        for (int i = 0; i < 27; i++) {
            if (i < 9 || i > 17) {
                if (winCount % 2 == 0) {
                    if (i % 2 == 0)
                        inventory.setItem(i, greenGlass);
                    else
                        inventory.setItem(i, redGlass);
                } else {
                    if (i % 2 == 0)
                        inventory.setItem(i, redGlass);
                    else
                        inventory.setItem(i, greenGlass);
                }
            }
        }
        if (winCount > 20) {
            new BukkitRunnable() {
                @Override
                public void run() {
                    player.getInventory().addItem(item);
                    player.closeInventory();
                    player.playEffect(player.getLocation(), Effect.FIREWORKS_SPARK, FireworkEffect.builder().trail(true));
                    player.playSound(player.getLocation(), Sound.FIREWORK_TWINKLE, 2, 1);
                }
            }.runTaskLater(plugin, 10);
            return;
        }
        new BukkitRunnable() {
            @Override
            public void run() {
                win(item);
            }
        }.runTaskLater(plugin, 2);
    }

    void Destory() {
        super.Destory();
        stop = true;
    }
}
