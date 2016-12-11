package cc.isotopestudio.luckycat.util;

/*
  Created by Mars on 6/20/2016.
  Copyright ISOTOPE Studio
 */

import com.google.common.io.Files;
import org.apache.commons.lang.Validate;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import org.yaml.snakeyaml.DumperOptions;

import java.io.*;
import java.lang.reflect.Field;

public class PluginFile extends YamlConfiguration {

    private final File file;
    private final String defaults;
    private final JavaPlugin plugin;

    /**
     * Creates new PluginFile, with defaults
     *
     * @param plugin       - Your plugin
     * @param fileName     - Name of the file
     * @param defaultsName - Name of the defaults
     */
    public PluginFile(JavaPlugin plugin, String fileName, String defaultsName) {
        this.plugin = plugin;
        this.defaults = defaultsName;
        this.file = new File(plugin.getDataFolder(), fileName);
        reload();
    }

    public PluginFile(JavaPlugin plugin, String fileName) {
        this.plugin = plugin;
        this.defaults = null;
        this.file = new File(plugin.getDataFolder(), fileName);
        reload();
    }

    /**
     * Reload configuration
     */
    public void reload() {

        if (!file.exists()) {
            try {
                file.getParentFile().mkdirs();
                file.createNewFile();

            } catch (IOException exception) {
                exception.printStackTrace();
                plugin.getLogger().severe("Error while creating file " + file.getName());
            }
        }

        try {
            load(file);

            if (defaults != null) {
                FileConfiguration defaultsConfig = YamlConfiguration.loadConfiguration(plugin.getResource(defaults));

                setDefaults(defaultsConfig);
                options().copyDefaults(true);

                save();
            }

        } catch (IOException | InvalidConfigurationException exception) {
            exception.printStackTrace();
            plugin.getLogger().severe("Error while loading file " + file.getName());

        }

        DumperOptions yamlOptions = null;
        try {
            Field f = YamlConfiguration.class.getDeclaredField("yamlOptions");   //��ȡ��YamlConfiguration�������yamlOptions�ֶ�
            f.setAccessible(true);

            yamlOptions = new DumperOptions() {  //��yamlOptions�ֶ��滻Ϊһ��DumperOptions�������ڲ��࣬�����滻��setAllowUnicode����������Զ�޷�����Ϊtrue
                @Override
                public void setAllowUnicode(boolean allowUnicode) {
                    super.setAllowUnicode(false);
                }

                @Override
                public void setLineBreak(LineBreak lineBreak) {
                    super.setLineBreak(LineBreak.getPlatformLineBreak());
                }
            };

            yamlOptions.setLineBreak(DumperOptions.LineBreak.getPlatformLineBreak());
            f.set(this, yamlOptions); //���µ�yamlOptions͵��������ȥ
        } catch (ReflectiveOperationException ex) {
            ex.printStackTrace();
        }

    }

    /**
     * Save configuration
     */
    public void save() {
        try {
            options().indent(2);
            save(file);
        } catch (IOException exception) {
            exception.printStackTrace();
            plugin.getLogger().severe("Error while saving file " + file.getName());
        }
    }

    @Override
    public void save(File file) throws IOException {
        Validate.notNull(file, "File cannot be null");
        Files.createParentDirs(file);
        String data = this.saveToString();
        Writer writer = new OutputStreamWriter(new FileOutputStream(file));
        try {
            writer.write(data);
        } finally {
            writer.close();
        }
    }

    @Override
    public void load(File file) throws IOException, InvalidConfigurationException {
        Validate.notNull(file, "File cannot be null");
        super.load(new FileInputStream(file));
    }

}