package net.iceviper.flyphants.sanitypvp.item.legendaries;

import net.iceviper.flyphants.sanitypvp.Main;
import net.iceviper.flyphants.sanitypvp.Spawn;

import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class CurvedSwordListener implements Listener {
	
	public static CurvedSwordListener instance;
	
	public CurvedSwordListener() {
		Main.instance.getServer().getPluginManager().registerEvents(this, Main.instance);
	}
	
	@EventHandler
	public void onHit(EntityDamageByEntityEvent e) {
		if (!(e.getDamager() instanceof Player))
			return;
		if (((Player) e.getDamager()).getItemInHand().getItemMeta().getDisplayName() != CurvedSword.template.getDisplayName())
			return;
		if (Spawn.isOutsideSpawn(e.getEntity().getLocation())) {
			((LivingEntity) e.getEntity()).addPotionEffect(new PotionEffect(PotionEffectType.WITHER, 100, 0));
		}
	}
}
