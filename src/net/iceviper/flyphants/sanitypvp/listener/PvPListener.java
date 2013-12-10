package net.iceviper.flyphants.sanitypvp.listener;

import java.util.HashMap;

import net.iceviper.flyphants.sanitypvp.Main;
import net.iceviper.flyphants.sanitypvp.player.PlayerStats;
import net.iceviper.flyphants.sanitypvp.scoreboard.PrivateScoreboard;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.entity.Zombie;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.ItemStack;

public class PvPListener implements Listener {
	
	public PvPListener instance;
	
	private HashMap<String, Zombie> logoutNpcs = new HashMap<String, Zombie>();
	private HashMap<Player, Player> lastDamageBy = new HashMap<Player, Player>();
	
	public PvPListener() {
		instance = this;
		Main.instance.getServer().getPluginManager().registerEvents(this, Main.instance);
	}
	
	@EventHandler
	public void onDamage(EntityDamageByEntityEvent e) {
		if (e.getDamager() instanceof Zombie) {
			if (logoutNpcs.containsValue(e.getDamager())) {
				e.setCancelled(true);
				return;
			}
		}
		if ((e.getEntity() instanceof Zombie)) {
			if (!(e.getDamager() instanceof Player || e.getDamager() instanceof Arrow && ((Arrow)e.getDamager()).getShooter() instanceof Player)) {
				e.setCancelled(true);
			} else {
				Zombie damaged = (Zombie) e.getEntity();
				if ((damaged.isCustomNameVisible()) && (damaged.getHealth() - e.getDamage() < 1) && (e.getDamage() > 0)) {
					damaged.getWorld().dropItem(damaged.getLocation(), new ItemStack(Material.GOLDEN_APPLE));
					damaged.remove();
					OfflinePlayer died = Bukkit.getOfflinePlayer(damaged.getCustomName());
					this.logoutNpcs.remove(died.getName());
					Player damager = null;
					if ((e.getDamager() instanceof Player)) {
						damager = (Player) e.getDamager();
					} else if ((e.getDamager() instanceof Projectile)) {
						Projectile p = (Projectile) e.getDamager();
						if ((p.getShooter() instanceof Player)) {
							damager = (Player) p.getShooter();
						}
					}
					if (damager != null) {
						damager.getInventory().addItem(new ItemStack(Material.ARROW, 5));
						new PlayerStats(damager.getName()).calculateKillOn(died.getName());
						Bukkit.broadcastMessage(ChatColor.BLUE + damager.getName() + ChatColor.WHITE + " killed " + ChatColor.RED + died.getName() + ChatColor.WHITE + " after logout.");
						PrivateScoreboard.updateAll();
					}
				}
			}
		}
		if (e.getDamager() instanceof Player && e.getEntity() instanceof Player) {
			lastDamageBy.put((Player) e.getEntity(), (Player) e.getDamager());
		}
	}
	
	@EventHandler
	public void onDeath(PlayerDeathEvent e) {
		e.getDrops().clear();
		if (e.getEntity().getLastDamageCause() != null && e.getEntity().getLastDamageCause().getCause() != EntityDamageEvent.DamageCause.SUICIDE) {
			e.getDrops().add(new ItemStack(Material.GOLDEN_APPLE, 1));
		}
		Player killer = e.getEntity().getKiller();
		if (lastDamageBy.containsKey(e.getEntity()))
			killer = lastDamageBy.remove(e.getEntity());
		if (killer != null) {
			killer.getInventory().addItem(new ItemStack[] { new ItemStack(Material.ARROW, 5) });
			new PlayerStats(killer.getName()).calculateKillOn(e.getEntity().getName());
		} else {
			PlayerStats killedStats = new PlayerStats(e.getEntity().getName());
			if (killedStats.getKillStreak() > 9)
				Main.instance.getServer().broadcastMessage(ChatColor.BLUE + "Mother Nature" + ChatColor.WHITE + " has ended " + ChatColor.RED + e.getEntity().getName() + ChatColor.WHITE + "'s killstreak of " + killedStats.getKillStreak());
			killedStats.setKillStreak(0);
		}
		PrivateScoreboard.updateAll();
		e.setDeathMessage(ChatColor.BLUE + (killer == null ? "Mother Nature" :killer.getDisplayName()) + ChatColor.WHITE + " killed " + ChatColor.RED + e.getEntity().getDisplayName());
	}
}
