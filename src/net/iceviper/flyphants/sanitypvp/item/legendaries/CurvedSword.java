package net.iceviper.flyphants.sanitypvp.item.legendaries;

import java.util.ArrayList;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.meta.ItemMeta;

import net.iceviper.flyphants.sanitypvp.item.LegendaryItemStack;

public class CurvedSword extends LegendaryItemStack {
	
	public static CurvedSword template = new CurvedSword();
	
	public CurvedSword() {
		super(Material.IRON_SWORD, (short)0);
		ItemMeta meta = getItemMeta();
		ArrayList<String> lore = new ArrayList<String>();
		lore.add("Make enemies bleed!");
		meta.setLore(lore);
		setItemMeta(meta);
	}
	
	public static void register() {
		LegendaryItemStack.registerLegendary("curvedSword", CurvedSword.class);
		CurvedSwordListener.instance = new CurvedSwordListener();
	}
	
	public String getDisplayName() {
		return ChatColor.GOLD + "Curved Sword";
	}
}
