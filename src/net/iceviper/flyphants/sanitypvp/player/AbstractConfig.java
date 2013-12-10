package net.iceviper.flyphants.sanitypvp.player;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.logging.Level;

import net.iceviper.flyphants.sanitypvp.Main;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

public abstract class AbstractConfig {
	protected FileConfiguration configFile;
	protected String fileName;
	
	protected static HashMap<String, AbstractConfig> loadedConfigs = new HashMap<String, AbstractConfig>();
	
	public AbstractConfig(String fileName) {
		this.fileName = fileName;
		reload();
	}
	
	public void reload() {
		File file = new File(Main.instance.getDataFolder(), fileName);
		configFile = YamlConfiguration.loadConfiguration(file);
	}
	
	public int getInt(String path) {
		return configFile.getInt(path);
	}
	
	public String getString(String path) {
		return configFile.getString(path);
	}
	
	public FileConfiguration getConfig() {
		if (configFile == null) {
			reload();
		}
		return configFile;
	}
	
	public void set(String path, Object o) {
		configFile.set(path, o);
		save();
	}
	
	public void save() {
		File file = new File(Main.instance.getDataFolder(), fileName);
		try {
			file.createNewFile();
			configFile.save(file);
		} catch (IOException e) {
			Main.instance.getLogger().log(Level.SEVERE, "Could not save configuration to " + file, e);
		}
	}
	
	public static void reloadAll() {
		for (AbstractConfig pc : loadedConfigs.values()) {
			pc.reload();
		}
	}
}
