package net.iceviper.flyphants.sanitypvp.item.legendaries;

import java.util.UUID;

import net.iceviper.flyphants.sanitypvp.item.LegendaryItemStack;
import net.minecraft.server.v1_6_R3.NBTTagCompound;
import net.minecraft.server.v1_6_R3.NBTTagList;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_6_R3.inventory.CraftItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class TurtleChest  extends LegendaryItemStack {
	
	public static TurtleChest template = new TurtleChest();
	
	public TurtleChest() {
		super(Material.DIAMOND_CHESTPLATE, (short)0);
		ItemMeta meta = getItemMeta();
		meta.setDisplayName(getDisplayName());
		setItemMeta(meta);
		net.minecraft.server.v1_6_R3.ItemStack craftItem = CraftItemStack.asNMSCopy(this);
		NBTTagCompound tag = craftItem.tag;
		if (tag == null) {
			tag = new NBTTagCompound();
			craftItem.tag = tag;
		}
		NBTTagList list = new NBTTagList();
		NBTTagCompound health = new NBTTagCompound();
		UUID uuid = UUID.fromString("CB3F55D3-645C-4F38-A497-9C13A33DB5CF");
		health.setString("AttributeName", "generic.movementSpeed");
		health.setDouble("Amount", -0.1d);
		health.setInt("Operation", 2);
		health.setString("Name", "Speed");
		health.setLong("UUIDMost", uuid.getMostSignificantBits());
		health.setLong("UUIDLeast", uuid.getLeastSignificantBits());
		list.add(health);
		tag.set("AttributeModifiers", list);
		craftMirror = CraftItemStack.asCraftMirror(craftItem);
	}
	
	public static void register() {
		LegendaryItemStack.registerLegendary("turtleChest", TurtleChest.class);
		TurtleArmorListener.instance = new TurtleArmorListener();
	}
	
	public String getDisplayName() {
		return ChatColor.GOLD + "Turtle Chestplate";
	}
}
