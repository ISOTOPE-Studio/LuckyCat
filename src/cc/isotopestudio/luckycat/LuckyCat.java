package cc.isotopestudio.luckycat;

import cc.isotopestudio.luckycat.command.CommandLucky;
import cc.isotopestudio.luckycat.util.PluginFile;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;

public class LuckyCat extends JavaPlugin {

    private static final String pluginName = "LuckyCat";
    public static final String prefix = (new StringBuilder()).append(ChatColor.GOLD).append(ChatColor.BOLD).append("[")
            .append("系统").append("]").append(ChatColor.RED).toString();

    public static PluginFile config;

    @Override
    public void onEnable() {
        config = new PluginFile(this, "config.yml");

        this.getCommand("lucky").setExecutor(new CommandLucky());

        getLogger().info(pluginName + "成功加载!");
        getLogger().info(pluginName + "由ISOTOPE Studio制作!");
        getLogger().info("http://isotopestudio.cc");
    }

    @Override
    public void onDisable() {
        getLogger().info(pluginName + "成功卸载!");
    }

}
