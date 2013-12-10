package net.iceviper.flyphants.sanitypvp.item.legendaries;

import net.iceviper.flyphants.sanitypvp.Main;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;

public class NinjaBootsListener implements Listener {
	
	public static NinjaBootsListener instance;

	public NinjaBootsListener() {
		Main.instance.getServer().getPluginManager().registerEvents(this, Main.instance);
	}
	
	@EventHandler
	public void onEntityDamage(EntityDamageEvent e) {
		if (e.getCause() == DamageCause.FALL && e.getEntity() instanceof Player) {
			Player player = (Player) e.getEntity();
			if (player.getInventory().getArmorContents()[0].getType() == Material.LEATHER_BOOTS && player.getInventory().getArmorContents()[0].getItemMeta().getDisplayName().contains("Ninja"))
				e.setDamage(e.getDamage() / 2.0);
		}
	}
}
