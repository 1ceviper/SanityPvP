package net.iceviper.flyphants.sanitypvp.player;

import java.io.File;
import java.io.InputStream;
import net.iceviper.flyphants.sanitypvp.Main;

import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

public class PlayerConfig extends AbstractConfig {
		
	private String player;
	
	private PlayerConfig(String player) {
		super("/players/" + player + ".yml");
		this.player = player;
	}
	
	public static PlayerConfig getConfig(String player) {
		if (loadedConfigs.containsKey("/players/" + player + ".yml"))
			return (PlayerConfig) loadedConfigs.get("/players/" + player + ".yml");
		return new PlayerConfig(player);
	}
	
	public static PlayerConfig getConfig(Player player) {
		return getConfig(player.getName());
	}
	
	@Override
	public void reload() {
		super.reload();
		InputStream defaults = Main.instance.getResource("player.yml");
		if (defaults != null) {
			YamlConfiguration defConf = YamlConfiguration.loadConfiguration(defaults);
			configFile.setDefaults(defConf);
		}
	}
	
	public PlayerStats getPlayerStats() {
		return new PlayerStats(player);
	}
	
	public String getPlayer() {
		return player;
	}
	
	public static void firstTimeCheck() {
		if (!Main.instance.getDataFolder().exists())
			Main.instance.getDataFolder().mkdir();
		File file = new File(Main.instance.getDataFolder(), "/players/");
		if (!file.exists())
			file.mkdir();
	}
}
