package net.iceviper.flyphants.sanitypvp.item;

import java.util.HashMap;
import java.util.logging.Level;

import net.iceviper.flyphants.sanitypvp.Main;
import net.iceviper.flyphants.sanitypvp.item.legendaries.CurvedSword;
import net.iceviper.flyphants.sanitypvp.item.legendaries.EnderBow;
import net.iceviper.flyphants.sanitypvp.item.legendaries.HerobrineHead;
import net.iceviper.flyphants.sanitypvp.item.legendaries.NinjaBoots;
import net.iceviper.flyphants.sanitypvp.item.legendaries.TNTBow;
import net.iceviper.flyphants.sanitypvp.item.legendaries.TurtleBoots;
import net.iceviper.flyphants.sanitypvp.item.legendaries.TurtleChest;
import net.iceviper.flyphants.sanitypvp.item.legendaries.TurtleHelmet;
import net.iceviper.flyphants.sanitypvp.item.legendaries.TurtleLeggings;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public abstract class LegendaryItemStack extends ItemStack {
	
	private static HashMap<String, Class<? extends LegendaryItemStack>> registeredItems = new HashMap<String, Class<? extends LegendaryItemStack>>();
	
	protected ItemStack craftMirror;
	
	public LegendaryItemStack(Material mat, short data) {
		super(mat, 1, data);
		ItemMeta meta = getItemMeta();
		meta.setDisplayName(getDisplayName());
		setItemMeta(meta);
	}
		
	public static ItemStack parse(String name) {
		if (registeredItems.containsKey(name)) {
			try {
				LegendaryItemStack re = registeredItems.get(name).newInstance();
				if (re.craftMirror == null)
					return re;
				else
					return re.craftMirror.clone();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		Main.instance.getLogger().log(Level.WARNING, "Tried to parse legendary with name '" + name + "', corresponding class not found.");
		return null;
	}
	
	public abstract String getDisplayName();
	
	public static void registerLegendary(String name, Class<? extends LegendaryItemStack> type) {
		registeredItems.put(name, type);
		Main.instance.getLogger().log(Level.INFO, "Registering Legendary '" + name + "' with class '" + type + "'");
	}
	
	public static void registerAll() {
		EnderBow.register();
		TNTBow.register();
		NinjaBoots.register();
		HerobrineHead.register();
		CurvedSword.register();
		TurtleHelmet.register();
		TurtleChest.register();
		TurtleLeggings.register();
		TurtleBoots.register();
	}
	
	public static boolean isLegendary(ItemStack i) {
		for (Class<? extends LegendaryItemStack> itemClass : registeredItems.values()) {
			try {
				if (((LegendaryItemStack)itemClass.getDeclaredField("template").get(null)).getDisplayName().equals(i.getItemMeta().getDisplayName()))
					return true;
			} catch (Exception e) {
				Main.instance.getLogger().log(Level.SEVERE, "Class " + itemClass + " does not have a template set, please report.");
			}
		}
		return false;
	}
	
	public static String getName(ItemStack i) {
		for (String item : registeredItems.keySet()) {
			Class<? extends LegendaryItemStack> itemClass = registeredItems.get(item);
			try {
				LegendaryItemStack legend = (LegendaryItemStack)itemClass.getDeclaredField("template").get(null);
				if (legend.getDisplayName().equals(i.getItemMeta().getDisplayName()))
					return item;
			} catch (Exception e) {
				Main.instance.getLogger().log(Level.SEVERE, "Class " + itemClass + " does not have a template set, please report.");
			}
		}
		Main.instance.getLogger().log(Level.WARNING, "Tried to get legendary name for non-legendary item: " + i);
		return "";
	}
}
