package cc.isotopestudio.luckycat.command;
/*
 * Created by Mars Tan on 12/11/2016.
 * Copyright ISOTOPE Studio
 */

import cc.isotopestudio.luckycat.settings.LuckySettings;
import cc.isotopestudio.luckycat.util.S;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class CommandLucky implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("lucky")) {
            if (!(sender instanceof Player)) {
                sender.sendMessage(S.toPrefixRed("玩家执行的命令"));
                return true;
            }
            Player player = (Player) sender;
            if (!player.hasPermission("luckycat.admin")) {
                player.sendMessage(S.toPrefixRed("你没有权限"));
                return true;
            }
            if (args.length < 1) {
                player.sendMessage(S.toPrefixGreen("帮助菜单"));
                player.sendMessage(S.toYellow("/" + label + " setlot - 设置手中的物品为抽奖卷"));
                player.sendMessage(S.toYellow("/" + label + " add <幸运值> - 添加手中的物品为奖品"));
                player.sendMessage(S.toYellow("/" + label + " remove <ID> - 删除一个奖品(列表中的ID)"));
                player.sendMessage(S.toYellow("/" + label + " removeall - 删除所有奖品"));
                player.sendMessage(S.toYellow("/" + label + " list - 查看奖品列表"));
                return true;
            }
            if (args[0].equalsIgnoreCase("setlot")) {
                ItemStack item = player.getItemInHand();
                if (item == null) {
                    player.sendMessage(S.toPrefixRed("你手中没有东西(╯▔皿▔)╯"));
                    return true;
                }
                if (item.getAmount() != 1) {
                    player.sendMessage(S.toPrefixRed("手中物品的数量必须为一个"));
                    return true;
                }
                LuckySettings.setLot(item);
                player.sendMessage(S.toPrefixGreen("成功设置"));
                return true;
            }
            if (args[0].equalsIgnoreCase("add")) {
                if (args.length < 2) {
                    player.sendMessage(S.toYellow("/" + label + " add <幸运值> - 添加手中的物品为奖品"));
                    return true;
                }
                ItemStack item = player.getItemInHand();
                if (item == null) {
                    player.sendMessage(S.toPrefixRed("你手中没有东西(╯▔皿▔)╯"));
                    return true;
                }
                int luck;
                try {
                    luck = Integer.parseInt(args[1]);
                } catch (Exception e) {
                    player.sendMessage(S.toPrefixRed("数字不对"));
                    return true;
                }
                if (luck < 0) {
                    player.sendMessage(S.toPrefixRed("数字不对"));
                    return true;
                }
                LuckySettings.addItem(item, luck);
                player.sendMessage(S.toPrefixGreen("成功设置"));
                return true;
            }
            if (args[0].equalsIgnoreCase("remove")) {
                if (args.length < 2) {
                    player.sendMessage(S.toYellow("/" + label + " remove <ID> - 删除一个奖品(列表中的ID)"));
                    return true;
                }
                int id;
                try {
                    id = Integer.parseInt(args[1]);
                } catch (Exception e) {
                    player.sendMessage(S.toPrefixRed("数字不对"));
                    return true;
                }
                if (id < 0 || id > LuckySettings.awardList.size()) {
                    player.sendMessage(S.toPrefixRed("数字不对"));
                    return true;
                }
                LuckySettings.remove(id);
                player.sendMessage(S.toPrefixGreen("成功删除"));
                return true;
            }
            if (args[0].equalsIgnoreCase("removeall")) {
                LuckySettings.removeAll();
                return true;
            }
            if (args[0].equalsIgnoreCase("list")) {
                player.sendMessage(S.toPrefixYellow("奖品列表"));
                for (int i = 0; i < LuckySettings.awardList.size(); i++) {
                    ItemStack item = LuckySettings.awardList.get(i);
                    String name = null;
                    if (item.hasItemMeta() && item.getItemMeta().hasDisplayName()) {
                        name = item.getItemMeta().getDisplayName();
                    }
                    player.sendMessage(S.toPrefixGray("[" + i + "] ") +
                            S.toGreen(item.getType().name()) +
                            (name == null ? "" : " (" + name + ")") +
                            S.toGold(" x" + item.getAmount())
                    );
                }
                return true;
            }
            player.sendMessage(S.toPrefixRed("未知命令, 输入 /" + label + " 查看帮助"));
            return true;
        }
        return false;
    }
}