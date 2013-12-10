package net.iceviper.flyphants.sanitypvp.listener;

import java.util.HashMap;

import net.iceviper.flyphants.sanitypvp.Main;
import net.iceviper.flyphants.sanitypvp.Spawn;
import net.iceviper.flyphants.sanitypvp.player.PlayerConfig;
import net.iceviper.flyphants.sanitypvp.player.PlayerKit;
import net.iceviper.flyphants.sanitypvp.scoreboard.PrivateScoreboard;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Zombie;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class PlayerMiscListener  implements Listener {
	
	public PlayerMiscListener instance;
	
	private HashMap<String, Zombie> logoutNpcs = new HashMap<String, Zombie>();
	public static HashMap<Player, Boolean> loggingOut = new HashMap<Player, Boolean>();
	
	public PlayerMiscListener() {
		instance = this;
		Main.instance.getServer().getPluginManager().registerEvents(this, Main.instance);
	}
	
	@EventHandler
	public void onRespawn(PlayerRespawnEvent e) {
		e.getPlayer().getActivePotionEffects().clear();
		PlayerKit.getKit(e.getPlayer().getName()).equip(e.getPlayer().getInventory());
	}
	
	@EventHandler
	public void onJoin(PlayerJoinEvent e) {
		PrivateScoreboard.updateAll();
		e.getPlayer().getActivePotionEffects().clear();
		if (e.getPlayer().getLastPlayed() == 0L)
			PlayerKit.getKit(e.getPlayer().getName()).equip(e.getPlayer().getInventory());
		if (this.logoutNpcs.containsKey(e.getPlayer().getName())) {
			Zombie npc = (Zombie) this.logoutNpcs.get(e.getPlayer().getName());
			e.getPlayer().getEquipment().setArmorContents(npc.getEquipment().getArmorContents());
			e.getPlayer().setHealth(npc.getHealth());
			e.getPlayer().teleport(npc.getLocation());
			npc.remove();
			this.logoutNpcs.remove(e.getPlayer().getName());
		}
		e.setJoinMessage(ChatColor.GOLD + e.getPlayer().getDisplayName() + ChatColor.WHITE + " joined the game!");
	}
	
	@EventHandler
	public void onLeave(final PlayerQuitEvent e) {
		boolean spawnNPC = true;
		if ((loggingOut.containsKey(e.getPlayer())) && (loggingOut.get(e.getPlayer()))) {
			loggingOut.remove(e.getPlayer());
			spawnNPC = false;
		}
		if (Spawn.isSpawn(e.getPlayer().getLocation()))
			spawnNPC = false;
		if (spawnNPC) {
			e.setQuitMessage(ChatColor.GOLD + e.getPlayer().getDisplayName() + ChatColor.RED + " did not log out safely.");
			Zombie dummy = (Zombie) e.getPlayer().getWorld().spawnEntity(e.getPlayer().getLocation(), EntityType.ZOMBIE);
			dummy.setCustomName(e.getPlayer().getName());
			dummy.setHealth(1d);
			dummy.setCustomNameVisible(true);
			ItemStack[] armor = e.getPlayer().getEquipment().getArmorContents();
			armor[0] = new ItemStack(Material.SKULL_ITEM,1);
			armor[0].setDurability((short) 3);
			SkullMeta skullMeta = (SkullMeta) armor[0].getItemMeta();
			skullMeta.setOwner(e.getPlayer().getName());
			armor[0].setItemMeta(skullMeta);
			dummy.getEquipment().setArmorContents(armor);
			dummy.getEquipment().setItemInHand(new ItemStack(Material.IRON_SWORD));
			dummy.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 300, 10, true));
			final Zombie zom = dummy;
			this.logoutNpcs.put(e.getPlayer().getName(), zom);
			Main.instance.getServer().getScheduler().runTaskLater(Main.instance, new Runnable() {
				public void run() {
					PlayerMiscListener.this.logoutNpcs.remove(e.getPlayer().getName());
					zom.remove();
				}
			}, 300L);
		} else {
			e.setQuitMessage(ChatColor.GOLD + e.getPlayer().getDisplayName() + ChatColor.GREEN + " logged out safely.");
			PlayerConfig.getConfig(e.getPlayer().getName()).set("logout", true);
		}
	}
}
