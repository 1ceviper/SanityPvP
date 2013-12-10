package net.iceviper.flyphants.sanitypvp.item.legendaries;

import net.iceviper.flyphants.sanitypvp.Main;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class TurtleArmorListener implements Listener {
	
	public static TurtleArmorListener instance;

	public TurtleArmorListener() {
		Main.instance.getServer().getPluginManager().registerEvents(this, Main.instance);
	}
	
	@EventHandler
	public void onEntityDamage(EntityDamageEvent e) {
		if (e.getCause() == DamageCause.FALL && e.getEntity() instanceof Player) {
			Player player = (Player) e.getEntity();
			if (player.getInventory().getArmorContents()[3].getType() == Material.DIAMOND_HELMET &&
				player.getInventory().getArmorContents()[2].getType() == Material.DIAMOND_CHESTPLATE &&
				player.getInventory().getArmorContents()[1].getType() == Material.DIAMOND_LEGGINGS &&
				player.getInventory().getArmorContents()[0].getType() == Material.DIAMOND_BOOTS &&
				player.getInventory().getArmorContents()[3].getItemMeta().getDisplayName().contains("Turtle"))
				if (e.getDamage() < 10)
					e.setCancelled(true);
		}
	}
	
	@EventHandler
	public void move(PlayerMoveEvent e) {
		if (e.getPlayer().getInventory().getArmorContents()[3].getType() == Material.DIAMOND_HELMET &&
			e.getPlayer().getInventory().getArmorContents()[2].getType() == Material.DIAMOND_CHESTPLATE &&
			e.getPlayer().getInventory().getArmorContents()[1].getType() == Material.DIAMOND_LEGGINGS &&
			e.getPlayer().getInventory().getArmorContents()[0].getType() == Material.DIAMOND_BOOTS &&
			e.getPlayer().getInventory().getArmorContents()[3].getItemMeta().getDisplayName().contains("Turtle"))
				e.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.JUMP, 10, -5));
	}
}
