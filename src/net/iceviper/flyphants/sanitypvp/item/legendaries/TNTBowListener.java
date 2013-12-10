package net.iceviper.flyphants.sanitypvp.item.legendaries;

import java.util.HashMap;

import net.iceviper.flyphants.sanitypvp.Main;
import net.iceviper.flyphants.sanitypvp.Spawn;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.entity.TNTPrimed;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.entity.ProjectileHitEvent;

public class TNTBowListener implements Listener {
	
	public static TNTBowListener instance;
	
	public HashMap<Projectile, String> shotArrows = new HashMap<Projectile, String>();
	public HashMap<TNTPrimed, Player> arrowExplosions = new HashMap<TNTPrimed, Player>();

	public TNTBowListener() {
		Main.instance.getServer().getPluginManager().registerEvents(this, Main.instance);
	}
	
	@EventHandler
	public void onShoot(EntityShootBowEvent e) {
		if (!TNTBow.template.getItemMeta().getDisplayName().equals(e.getBow().getItemMeta().getDisplayName()))
			return;
		if (e.getEntity() instanceof Player && e.getProjectile() instanceof Projectile)
			shotArrows.put((Projectile) e.getProjectile(), ((Player)e.getEntity()).getName());
	}
	
	@EventHandler
	public void onHit(ProjectileHitEvent e) {
		if (!shotArrows.containsKey(e.getEntity()))
			return;
		Player shooter = Bukkit.getPlayerExact(shotArrows.get(e.getEntity()));
		if (shooter == null)
			return;
		if (Spawn.isSpawn(e.getEntity().getLocation())) {
			shooter.sendMessage(ChatColor.GRAY + "You cannot blow up spawn.");
			return;
		}
		TNTPrimed tnt = (TNTPrimed) e.getEntity().getWorld().spawnEntity(e.getEntity().getLocation(), EntityType.PRIMED_TNT);
		tnt.setFuseTicks(0);
		tnt.setVelocity(e.getEntity().getVelocity());
		tnt.setYield(tnt.getYield() / 1.5F);
		arrowExplosions.put(tnt, shooter);
		shotArrows.remove(e.getEntity());
	}
	
	@EventHandler
	public void damage(EntityDamageByEntityEvent e) {
		if (e.getDamager() instanceof TNTPrimed && e.getEntity() instanceof Player) {
			TNTPrimed tnt = (TNTPrimed) e.getDamager();
			Player damaged = (Player) e.getEntity();
			if (arrowExplosions.containsKey(tnt)) {
				damaged.damage(Math.ceil(e.getDamage() / 1.8D), (Entity) arrowExplosions.get(tnt));
				e.setCancelled(true);
				arrowExplosions.remove(tnt);
			}
		}
	}
}
