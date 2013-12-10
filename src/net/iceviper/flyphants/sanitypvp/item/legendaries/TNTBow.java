package net.iceviper.flyphants.sanitypvp.item.legendaries;

import java.util.ArrayList;

import net.iceviper.flyphants.sanitypvp.item.LegendaryItemStack;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.meta.ItemMeta;

public class TNTBow extends LegendaryItemStack {
	
	public static TNTBow template = new TNTBow();
	
	public TNTBow() {
		super(Material.BOW, (short)365);
		ItemMeta meta = getItemMeta();
		ArrayList<String> lore = new ArrayList<String>();
		lore.add("Make big BOOM!");
		meta.setLore(lore);
		this.setItemMeta(meta);
	}
	
	public static void register() {
		LegendaryItemStack.registerLegendary("tntBow", TNTBow.class);
		TNTBowListener.instance = new TNTBowListener();
	}
	
	public String getDisplayName() {
		return ChatColor.GOLD + "Boom Boom Bow";
	}
}
