package net.iceviper.flyphants.sanitypvp.item.legendaries;

import java.util.UUID;

import net.iceviper.flyphants.sanitypvp.item.LegendaryItemStack;
import net.minecraft.server.v1_6_R3.NBTTagCompound;
import net.minecraft.server.v1_6_R3.NBTTagList;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_6_R3.inventory.CraftItemStack;
import org.bukkit.inventory.meta.SkullMeta;

public class HerobrineHead extends LegendaryItemStack {
	
	public static HerobrineHead template = new HerobrineHead();
	
	public HerobrineHead() {
		super(Material.SKULL_ITEM, (short)3);
		SkullMeta skullMeta = (SkullMeta) getItemMeta();
		skullMeta.setOwner("Herobrine");
		setItemMeta(skullMeta);
		net.minecraft.server.v1_6_R3.ItemStack craftItem = CraftItemStack.asNMSCopy(this);
		NBTTagCompound tag = craftItem.tag;
		if (tag == null) {
			tag = new NBTTagCompound();
			craftItem.tag = tag;
		}
		NBTTagList list = new NBTTagList();
		NBTTagCompound health = new NBTTagCompound();
		UUID uuid = UUID.fromString("CB3F55D3-645C-4F38-A497-9C13A33DB5CF");
		health.setString("AttributeName", "generic.maxHealth");
		health.setDouble("Amount", 6.0d);
		health.setInt("Operation", 0);
		health.setString("Name", "Max Health");
		health.setLong("UUIDMost", uuid.getMostSignificantBits());
		health.setLong("UUIDLeast", uuid.getLeastSignificantBits());
		list.add(health);
		tag.set("AttributeModifiers", list);
		craftMirror = CraftItemStack.asCraftMirror(craftItem);
	}
	
	public static void register() {
		LegendaryItemStack.registerLegendary("herobrineHead", HerobrineHead.class);
	}
	
	public String getDisplayName() {
		return ChatColor.GOLD + "Herobrine's Head";
	}
}
