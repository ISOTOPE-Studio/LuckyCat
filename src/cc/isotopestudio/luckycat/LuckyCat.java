package cc.isotopestudio.luckycat;

import cc.isotopestudio.luckycat.command.CommandLucky;
import cc.isotopestudio.luckycat.listener.openGUIListener;
import cc.isotopestudio.luckycat.settings.UpdateSettings;
import cc.isotopestudio.luckycat.util.PluginFile;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;

public class LuckyCat extends JavaPlugin {

    private static final String pluginName = "LuckyCat";
    public static final String prefix = (new StringBuilder()).append(ChatColor.GOLD).append(ChatColor.BOLD).append("[")
            .append("LUCKY").append("]").append(ChatColor.RED).toString();

    public static LuckyCat plugin;

    public static PluginFile config;

    @Override
    public void onEnable() {
        plugin = this;

        config = new PluginFile(this, "config.yml");

        this.getCommand("lucky").setExecutor(new CommandLucky());

        Bukkit.getPluginManager().registerEvents(new openGUIListener(), this);

        new UpdateSettings().run();

        getLogger().info(pluginName + "成功加载!");
        getLogger().info(pluginName + "由ISOTOPE Studio制作!");
        getLogger().info("http://isotopestudio.cc");
    }

    @Override
    public void onDisable() {
        getLogger().info(pluginName + "成功卸载!");
    }

}
