package net.iceviper.flyphants.sanitypvp.player;

import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import net.iceviper.flyphants.sanitypvp.Main;
import net.iceviper.flyphants.sanitypvp.MathShiz;
import net.iceviper.flyphants.sanitypvp.scoreboard.PrivateScoreboard;

public class PlayerStats {
	
	PlayerConfig playerConfig;
	
	public PlayerStats(String player) {
		this.playerConfig = PlayerConfig.getConfig(player);
	}
	
	public int getKills() {
		return playerConfig.getInt("kills");
	}
	
	public int getCredits() {
		return playerConfig.getInt("credits");
	}
	
	public int getKillStreak() {
		return playerConfig.getInt("killstreak");
	}
	
	public int getRating() {
		return playerConfig.getInt("rating");
	}
	
	public int getDeaths() {
		return playerConfig.getInt("deaths");
	}
	
	public void setKills(int i) {
		set("kills", i);
	}
	
	public void setDeaths(int i) {
		set("deaths", i);
	}
	
	public void setRating(int i) {
		set("rating", i);
	}
	
	public void setKillStreak(int i) {
		set("killstreak", i);
	}
	
	public void setCredits(int i) {
		set("credits", i);
	}
	
	public void calculateKillOn(String killed) {
		String killer = playerConfig.getPlayer();
		if (Bukkit.getOfflinePlayer(killed) == null || Bukkit.getOfflinePlayer(killer) == null) {
			Main.instance.getLogger().log(Level.WARNING, "Attempted to calculate new stats with non-existent player.");
			return;
		}
		if (killer == killed)
			return;
		PlayerStats killedStats = new PlayerStats(killed);
		int dRating = killedStats.getRating() - getRating();
		double expected = 0.5 * (1 + MathShiz.erf(dRating/(200*Math.sqrt(2.0))));
		int kFactor = 32;
		if (getKills() + getDeaths() > 100)
			kFactor = 24;
		if (getKills() + getDeaths() > 300) {
			if (getRating() <= 2000)
				kFactor = 16;
			else if (getRating() < 2200)
				kFactor = 12;
			else
				kFactor = 10;
		}
		int killedKF = 32;
		if (killedStats.getKills() + killedStats.getDeaths() > 100)
			killedKF = 24;
		if (killedStats.getKills() + killedStats.getDeaths() > 300) {
			if (killedStats.getRating() <= 2000)
				killedKF = 16;
			else if (killedStats.getRating() < 2200)
				killedKF = 12;
			else
				killedKF = 10;
		}
		int dWinner = (int) ((expected) * kFactor);
		if (dWinner < 1)
			dWinner = 1;
		int dLoser = (int) (-expected * killedKF);
		if (dLoser > -1)
			dLoser = -1;
		int dCredits = 1;
		Player pKiller = Bukkit.getPlayerExact(killer);
		if (pKiller != null) {
			pKiller.sendMessage(ChatColor.GREEN + "You beat a player with rank " + killedStats.getRating() + " (change: +" + dWinner + ")");
			if (pKiller.hasPermission("sanity.doublexp"))
				dCredits = 2;
		}
		if (Bukkit.getPlayerExact(killed) != null)
			Bukkit.getPlayerExact(killed).sendMessage(ChatColor.GREEN + "You got beaten by a player with rank " + getRating() + " (change: " + dLoser + ")");
		setRating(getRating() + dWinner);
		killedStats.setRating(killedStats.getRating() + dLoser);
		setCredits(getCredits() + dCredits);
		if (killedStats.getKillStreak() > 9)
			Main.instance.getServer().broadcastMessage(killer+ " has ended " + killed + "'s killstreak of " + killedStats.getKillStreak());
		killedStats.setKillStreak(0);
		killedStats.setDeaths(killedStats.getDeaths() + 1);
		setKills(getKills() + 1);
		setKillStreak(getKillStreak() + 1);
		if (pKiller != null)
			KillStreak.giveKillStreakReward(pKiller, getKillStreak());
	}

	
	private void set(String name, int value) {
		playerConfig.set(name, value);
		PrivateScoreboard.parse(playerConfig.getPlayer()).update();
	}
	
	public void save() {
		playerConfig.save();
	}
}
