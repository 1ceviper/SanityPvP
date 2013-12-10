package net.iceviper.flyphants.sanitypvp.item.legendaries;

import java.util.ArrayList;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.meta.ItemMeta;

import net.iceviper.flyphants.sanitypvp.item.LegendaryItemStack;

public class EnderBow extends LegendaryItemStack {
	
	public static EnderBow template = new EnderBow();
	
	public EnderBow() {
		super(Material.BOW, (short)365);
		ItemMeta meta = getItemMeta();
		ArrayList<String> lore = new ArrayList<String>();
		lore.add("Teleport to the arrow on impact");
		meta.setLore(lore);
		this.setItemMeta(meta);
	}
	
	public static void register() {
		LegendaryItemStack.registerLegendary("enderBow", EnderBow.class);
		EnderBowListener.instance = new EnderBowListener();
	}
	
	public String getDisplayName() {
		return ChatColor.GOLD + "Ender bow";
	}
}
