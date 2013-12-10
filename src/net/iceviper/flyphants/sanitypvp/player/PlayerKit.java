package net.iceviper.flyphants.sanitypvp.player;

import java.util.ArrayList;
import java.util.HashMap;

import net.iceviper.flyphants.sanitypvp.Main;
import net.iceviper.flyphants.sanitypvp.item.ItemSaveUtil;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

public class PlayerKit {
	
	private static HashMap<String, PlayerKit> loadedKits = new HashMap<String, PlayerKit>();
	
	private PlayerConfig playerConfig;
	
	private PlayerKit(String player) {
		loadedKits.put(player, this);
		playerConfig = PlayerConfig.getConfig(player);
	}
	
	public static PlayerKit getKit(String player) {
		if (loadedKits.containsKey(player)) {
			return loadedKits.get(player);
		}
		return new PlayerKit(player);
	}
	
	public ItemStack getSlot(int slot) {
		String item = playerConfig.getString("kit.slot." + slot);
		return ItemSaveUtil.toItem(item);
	}
	
	public void setSlot(int slot, ItemStack i) {
		String itemStr = ItemSaveUtil.toString(i);
		playerConfig.set("kit.slot." + slot, itemStr);
	}
	
	public void setSpare(ArrayList<ItemStack> items) {
		String totalStr = "";
		for (ItemStack i : items) {
			String itemStr = ItemSaveUtil.toString(i);
			totalStr += ";" + itemStr;
		}
		totalStr.replaceFirst(";", "");
		playerConfig.set("kit.spare", totalStr);
	}
	
	public ArrayList<ItemStack> getSpare() {
		String totalStr = playerConfig.getString("kit.spare");
		ArrayList<ItemStack> re = new ArrayList<ItemStack>();
		if (totalStr == null)
			return re;
		String[] items = totalStr.split(";");
		for (String s : items) {
			ItemStack i = ItemSaveUtil.toItem(s);
			if (i != null)
				re.add(i);
		}
		return re;
	}
	
	public HashMap<Integer, ItemStack> getSlots() {
		HashMap<Integer, ItemStack> re = new HashMap<Integer, ItemStack>();
		if (playerConfig.getConfig().getConfigurationSection("kit.slot") == null) {
			return getDefaultKit();
		}
		for(String s : playerConfig.getConfig().getConfigurationSection("kit.slot").getKeys(false)) {
			re.put(Integer.valueOf(s), ItemSaveUtil.toItem(playerConfig.getString("kit.slot." + s)));
		}
		return re;
	}
	
	public HashMap<Integer, ItemStack> getDefaultKit() {
		setSlot(100, new ItemStack(Material.IRON_BOOTS));
		setSlot(101, new ItemStack(Material.IRON_LEGGINGS));
		setSlot(102, new ItemStack(Material.IRON_CHESTPLATE));
		setSlot(103, new ItemStack(Material.IRON_HELMET));
		setSlot(0, new ItemStack(Material.IRON_SWORD));
		setSlot(1, new ItemStack(Material.BOW));
		setSlot(8, new ItemStack(Material.ARROW, 16));
		return getSlots();
	}
	
	public void equip(PlayerInventory inv) {
		inv.clear();
		HashMap<Integer, ItemStack> content = getSlots();
		inv.setHelmet(content.remove(103));
		inv.setChestplate(content.remove(102));
		inv.setLeggings(content.remove(101));
		inv.setBoots(content.remove(100));
		for (int i : content.keySet()) {
			try {
				inv.setItem(i, content.get(i));
			} catch (Exception ex) {
				System.out.println("(Please Report - SanityPvP) I: " + i);
			}
		}
	}
}
