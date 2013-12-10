package net.iceviper.flyphants.sanitypvp.scoreboard;

import net.iceviper.flyphants.sanitypvp.Main;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class ScoreboardListener implements Listener {
	
	public ScoreboardListener instance;
	
	public ScoreboardListener() {
		instance = this;
		Main.instance.getServer().getPluginManager().registerEvents(this, Main.instance);
	}
	
	@EventHandler
	public void onJoin(PlayerJoinEvent e) {
		PrivateScoreboard.parse(e.getPlayer().getName()).reload();
	}
}
