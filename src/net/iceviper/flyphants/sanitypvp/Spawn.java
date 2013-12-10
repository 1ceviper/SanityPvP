package net.iceviper.flyphants.sanitypvp;

import org.bukkit.Location;

import com.sk89q.worldguard.bukkit.WGBukkit;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.flags.DefaultFlag;

public class Spawn {
	public static boolean isSpawn(Location loc) {
		return !isOutsideSpawn(loc);
	}
	
	public static boolean isOutsideSpawn(Location loc) {
		ApplicableRegionSet set = WGBukkit.getRegionManager(loc.getWorld()).getApplicableRegions(loc);
		return set.allows(DefaultFlag.PVP);
	}
}
