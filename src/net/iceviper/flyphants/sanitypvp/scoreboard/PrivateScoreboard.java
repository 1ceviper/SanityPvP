package net.iceviper.flyphants.sanitypvp.scoreboard;

import java.util.HashMap;

import net.iceviper.flyphants.sanitypvp.player.PlayerStats;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.Scoreboard;

public class PrivateScoreboard {
	
	private static HashMap<String, PrivateScoreboard> loaded = new HashMap<String, PrivateScoreboard>();
	
	public String player;
	public Scoreboard board;
	
	private PrivateScoreboard(String player) {
		this.player = player;
		board = Bukkit.getScoreboardManager().getNewScoreboard();
		reload();
		loaded.put(player, this);
	}
	
	public static PrivateScoreboard parse(String player) {
		if (loaded.containsKey(player))
			return loaded.get(player);
		return new PrivateScoreboard(player);
	}
	
	public void reload() {
		board = Bukkit.getScoreboardManager().getNewScoreboard();
		Objective health = board.registerNewObjective("health", "health");
		health.setDisplayName(ChatColor.RED + "❤");
		health.setDisplaySlot(DisplaySlot.BELOW_NAME);
		for (Player p : Bukkit.getOnlinePlayers()) {
			health.getScore(p).setScore((int) p.getHealth());
		}
		Objective values = board.registerNewObjective("values", "dummy");
		values.setDisplayName(ChatColor.GREEN + "Stats:");
		values.setDisplaySlot(DisplaySlot.SIDEBAR);
		PlayerStats stats = new PlayerStats(player);
		Score temp = values.getScore(Bukkit.getOfflinePlayer(ChatColor.AQUA + "Credits:"));
		temp.setScore(stats.getCredits());
		temp = values.getScore(Bukkit.getOfflinePlayer(ChatColor.AQUA + "Rating:"));
		temp.setScore(stats.getRating());
		temp = values.getScore(Bukkit.getOfflinePlayer(ChatColor.AQUA + "Kills:"));
		temp.setScore(stats.getKills());
		temp = values.getScore(Bukkit.getOfflinePlayer(ChatColor.AQUA + "Deaths:"));
		temp.setScore(stats.getDeaths());
		temp = values.getScore(Bukkit.getOfflinePlayer(ChatColor.AQUA + "Killstreak:"));
		temp.setScore(stats.getKillStreak());
		send();
	}
	
	public void update() {
		Objective values;
		try {
			values = board.getObjective(DisplaySlot.SIDEBAR);
		} catch (IllegalArgumentException e) {
			reload();
			return;
		}
		values.setDisplayName(ChatColor.GREEN + "Stats:");
		values.setDisplaySlot(DisplaySlot.SIDEBAR);
		PlayerStats stats = new PlayerStats(player);
		Score temp = values.getScore(Bukkit.getOfflinePlayer(ChatColor.AQUA + "Credits:"));
		temp.setScore(stats.getCredits());
		temp = values.getScore(Bukkit.getOfflinePlayer(ChatColor.AQUA + "Rating:"));
		temp.setScore(stats.getRating());
		temp = values.getScore(Bukkit.getOfflinePlayer(ChatColor.AQUA + "Kills:"));
		temp.setScore(stats.getKills());
		temp = values.getScore(Bukkit.getOfflinePlayer(ChatColor.AQUA + "Deaths:"));
		temp.setScore(stats.getDeaths());
		temp = values.getScore(Bukkit.getOfflinePlayer(ChatColor.AQUA + "Killstreak:"));
		temp.setScore(stats.getKillStreak());
	}
	
	public void send() {
		if (Bukkit.getPlayerExact(player) != null)
			Bukkit.getPlayerExact(player).setScoreboard(board);
		else
			loaded.remove(player);
	}
	
	public static void reloadAll() {
		for (PrivateScoreboard ps : loaded.values())
			ps.reload();
	}
	
	public static void updateAll() {
		for (PrivateScoreboard ps : loaded.values())
			ps.update();
	}
}
