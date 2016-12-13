package cc.isotopestudio.luckycat.gui;
/*
 * Created by Mars Tan on 12/11/2016.
 * Copyright ISOTOPE Studio
 */

import cc.isotopestudio.luckycat.settings.LuckySettings;
import cc.isotopestudio.luckycat.util.S;
import org.bukkit.*;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static cc.isotopestudio.luckycat.LuckyCat.plugin;
import static cc.isotopestudio.luckycat.settings.LuckySettings.awardList;

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
                setOption(i, awardList.get(i % 9));
            }
        }
        setOption(4, redGlass);
        setOption(22, redGlass);
    }

    private int displacement = 0;
    private int count = 0;
    private int key;

    private boolean announce = false;

    @Override
    public void open(Player player) {
        super.open(player);
        Set<ItemStack> playerRewards = LuckySettings.getPlayerRewards(player);
        if (playerRewards.containsAll(awardList)) {
            player.sendMessage(S.toPrefixRed("你已经将九宫格内的所有物品全部抽完了，请期待下个雨的幸运九宫格"));
            player.closeInventory();
            return;
        }
        List<ItemStack> rewardsList = new ArrayList<>(awardList);
        List<Integer> luckList = new ArrayList<>(LuckySettings.luckList);

        int i = 0;
        while (i < rewardsList.size()) {
            if (playerRewards.contains(rewardsList.get(i))) {
                rewardsList.remove(i);
                luckList.remove(i);
            } else {
                i++;
            }
        }
        double sum = 0;
        double[] luckAcc = new double[luckList.size()];
        for (i = 0; i < luckList.size(); i++) {
            double v = 1.0 / luckList.get(i);
            luckAcc[i] = sum + v;
            sum += v;
        }
        sum = luckAcc[luckList.size() - 1];
//        luckAcc[luckList.size()] = sum;
        double point = random(sum);
        i = 0;
        while (true) {
            if (luckAcc[i] < point) {
                i++;
                continue;
            }
            break;
        }
        for (int j = 0; j < awardList.size(); j++) {
            if (awardList.get(j).equals(rewardsList.get(i))) {
                key = j + 9 * 6 - 4;
                if (LuckySettings.luckList.get(j) > 85) {
                    announce = true;
                }
            }
        }
        displaceItem();
    }

    private boolean stop = false;

    private void displaceItem() {
        System.out.println(name + " " + count);
        displacement++;
        displacement %= 9;
        count++;
        if (count < 25 && count % 5 == 0)
            player.playSound(player.getLocation(), Sound.CLICK, 1, 2);
        else if (count > 25 && count % 2 == 0)
            player.playSound(player.getLocation(), Sound.CLICK, 2, 3);
        else if (count > 40)
            player.playSound(player.getLocation(), Sound.CLICK, 4, 4);

        for (int i = 9; i < 18; i++) {
            inventory.setItem(i, awardList.get((i + displacement) % 9));
        }
        if (count == key) {
            win(inventory.getItem(13));
            return;
        }
        if (!stop)
            new BukkitRunnable() {
                @Override
                public void run() {
                    displaceItem();
                }
            }.runTaskLater(plugin, 2);
    }

    private int winCount = 0;

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
        if (winCount > 20 && !stop) {
            new BukkitRunnable() {
                @Override
                public void run() {
                    player.closeInventory();
                    ItemStack itemInHand = player.getItemInHand().clone();
                    itemInHand.setAmount(1);
                    if (!itemInHand.equals(LuckySettings.lot)) {
                        return;
                    }
                    itemInHand = player.getItemInHand();
                    Firework fireWork = (Firework) player.getWorld().spawnEntity(player.getLocation(), EntityType.FIREWORK);
                    FireworkMeta fwMeta = fireWork.getFireworkMeta();

                    fwMeta.addEffect(FireworkEffect.builder().flicker(false).trail(true).with(FireworkEffect.Type.BALL).with(FireworkEffect.Type.BALL_LARGE).with(FireworkEffect.Type.STAR).withColor(Color.ORANGE).withColor(Color.YELLOW).withFade(Color.PURPLE).withFade(Color.RED).build());

                    fireWork.setFireworkMeta(fwMeta);
                    player.playSound(player.getLocation(), Sound.FIREWORK_TWINKLE, 2, 1);
                    player.playSound(player.getLocation(), Sound.LEVEL_UP, 2, 1);

                    String itemName = null;
                    if (item.hasItemMeta() && item.getItemMeta().hasDisplayName()) {
                        itemName = item.getItemMeta().getDisplayName();
                    }
                    if (announce) {
                        Bukkit.broadcastMessage(S.toPrefixYellow(" ") + player.getDisplayName() + S.toYellow(" 的运气爆棚, 抽到了极为稀有的 " + (itemName == null ? item.getType().toString() : " (" + itemName + ")")));
                    }
                    if (itemInHand.getAmount() == 1)
                        player.setItemInHand(null);
                    else
                        itemInHand.setAmount(itemInHand.getAmount() - 1);

                    LuckySettings.addPlayerReward(player, item);

                    player.getInventory().addItem(item);
                }
            }.runTaskLater(plugin, 10);
            return;
        }
        if (!stop)
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

    private static double random(double max) {
        return Math.random() * max;
    }
}
