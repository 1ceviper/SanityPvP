package net.iceviper.flyphants.sanitypvp.item.legendaries;

import java.util.HashMap;
import net.iceviper.flyphants.sanitypvp.Main;
import net.iceviper.flyphants.sanitypvp.Spawn;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.entity.ProjectileHitEvent;

public class EnderBowListener implements Listener {
	
	public static EnderBowListener instance;
	
	public HashMap<Projectile, String> shotArrows = new HashMap<Projectile, String>();

	public EnderBowListener() {
		Main.instance.getServer().getPluginManager().registerEvents(this, Main.instance);
	}
	
	@EventHandler
	public void onShoot(EntityShootBowEvent e) {
		if (!EnderBow.template.getItemMeta().getDisplayName().equals(e.getBow().getItemMeta().getDisplayName()))
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
			shooter.sendMessage(ChatColor.GRAY + "You cannot teleport into spawn.");
			return;
		}
		e.getEntity().getWorld().playEffect(shooter.getEyeLocation(), Effect.ENDER_SIGNAL, -1);
		Location loc = e.getEntity().getLocation().clone();
		loc.setYaw(shooter.getLocation().getYaw());
		loc.setPitch(shooter.getLocation().getPitch());
		shooter.teleport(loc);
		e.getEntity().getWorld().playEffect(shooter.getEyeLocation(), Effect.ENDER_SIGNAL, -1);
		shooter.damage(5.0);
		shotArrows.remove(e.getEntity());
	}
}
