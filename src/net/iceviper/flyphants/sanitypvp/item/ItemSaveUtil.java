package net.iceviper.flyphants.sanitypvp.item;

import java.util.Arrays;
import java.util.logging.Level;

import net.iceviper.flyphants.sanitypvp.Main;
import net.minecraft.server.v1_6_R3.Packet103SetSlot;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_6_R3.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_6_R3.inventory.CraftItemStack;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class ItemSaveUtil {
	public static String toString(ItemStack item) {
		if (item == null)
			return "";
		if (LegendaryItemStack.isLegendary(item)) {
			return "e:" + LegendaryItemStack.getName(item);
		}
		String itemString = "";
		itemString += item.getType().name();
		itemString += ":";
		itemString += item.getDurability();
		itemString += ":";
		itemString += item.getAmount();
		for (Enchantment e : item.getEnchantments().keySet()) {
			itemString += " ";
			itemString += e.getName();
			itemString += ":";
			itemString += item.getEnchantmentLevel(e);
		}
		return itemString;
	}
	
	public static ItemStack toItem(String item) {
		if (item == null || item.equals(""))
			return null;
		try {
			if (item.startsWith("e:")) {
				item = item.replaceFirst("e:", "");
				return LegendaryItemStack.parse(item);
			}
			String[] args = item.split(" ");
			String itemName = args[0].split(":")[0];
			short damage = Short.parseShort(args[0].split(":")[1]);
			int amount = Integer.parseInt(args[0].split(":")[2]);
			ItemStack re = new ItemStack(Material.getMaterial(itemName), amount, damage);
			for (int i = 1; i < args.length; i++) {
				String enchName = args[i].split(":")[0];
				int strength = Integer.parseInt(args[i].split(":")[1]);
				re.addEnchantment(Enchantment.getByName(enchName), strength);
			}
			return re;
		} catch (Exception e) {
			Main.instance.getLogger().log(Level.WARNING, "Attempt to parse illegal item string: '" + item + "', please report.");
		}
		return getErrorStone();
	}
	
	public static ItemStack getErrorStone() {
		ItemStack re = new ItemStack(Material.STONE);
		ItemMeta meta = re.getItemMeta();
		meta.setDisplayName(ChatColor.RED + "Error Stone");
		meta.setLore(Arrays.asList(new String[]{"something went wrong, please report this bug", "include time/date of occurence", "so we can find the error in log"}));
		re.setItemMeta(meta);
		return re;
	}
	
	public static void updateInventory(Player p) {
		CraftPlayer c = (CraftPlayer) p;
		for (int i = 0;i < 36;i++) {
			int nativeindex = i;
			if (i < 9) nativeindex = i + 36;
			ItemStack olditem =  c.getInventory().getItem(i);
			Packet103SetSlot pack = new Packet103SetSlot(0, nativeindex, CraftItemStack.asNMSCopy(olditem));
			c.getHandle().playerConnection.sendPacket(pack);
		}
	}
}
