package net.iceviper.flyphants.sanitypvp.inventories;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import net.iceviper.flyphants.sanitypvp.item.ItemSaveUtil;
import net.iceviper.flyphants.sanitypvp.player.PlayerKit;

public class Loadout {
	PlayerKit kit;
	
	public HashMap<Integer, ItemStack> original = new HashMap<Integer, ItemStack>();
	public ItemStack[] armor;
	private String player;
	
	public Loadout(String player) {
		kit = PlayerKit.getKit(player);
		this.player = player;
	}
	
	public void open() {
		Player p = Bukkit.getPlayerExact(player);
		if (p == null)
			return;
		for (int i = 0; i < 36; i++) {
			if (p.getInventory().getItem(i) != null)
				original.put(i, p.getInventory().getItem(i));
		}
		armor = p.getInventory().getArmorContents().clone();
		p.getInventory().clear();
		HashMap<Integer, ItemStack> kitItems = kit.getSlots();
		for (int i : kitItems.keySet()) {
			if (i < 36)
				p.getInventory().setItem(i, kitItems.get(i));
		}
		Inventory spareView = Bukkit.createInventory(p, 54, ChatColor.BLUE + "Spare Equipment");
		ItemStack border = new ItemStack(Material.IRON_FENCE);
		ItemMeta meta = border.getItemMeta();
		meta.setDisplayName(ChatColor.RED + "Border");
		meta.setLore(Arrays.asList(new String[]{"Border Item", "Useless."}));
		border.setItemMeta(meta);
		for (int i = 1; i < 54; i += 9) {
			spareView.setItem(i, border);
		}
		spareView.setItem(36, border);
		spareView.setItem(45, border);
		spareView.setItem(0, kitItems.get(103));
		spareView.setItem(9, kitItems.get(102));
		spareView.setItem(18, kitItems.get(101));
		spareView.setItem(27, kitItems.get(100));
		for (ItemStack i : kit.getSpare()) {
			spareView.addItem(i);
		}
		p.openInventory(spareView);
		LoadoutListener.instance.passLadout(p, this);
	}
	
	public void close(Inventory view) {
		Player p = Bukkit.getPlayerExact(player);
		if (p == null)
			return;
		kit.setSlot(100, view.getItem(27));
		kit.setSlot(101, view.getItem(18));
		kit.setSlot(102, view.getItem(9));
		kit.setSlot(103, view.getItem(0));
		for (int i = 0; i < 36; i++) {
			kit.setSlot(i, p.getInventory().getItem(i));
		}
		ArrayList<ItemStack> spare = new ArrayList<ItemStack>();
		for (int i = 0; i < 54; i++) {
			if (i % 9 != 0 && i % 9 != 1 && view.getItem(i) != null)
				spare.add(view.getItem(i));
		}
		kit.setSpare(spare);
		p.getInventory().clear();
		for (int i : original.keySet())
			p.getInventory().setItem(i, original.get(i));
		p.getInventory().setArmorContents(armor);
		ItemSaveUtil.updateInventory(p);
	}
}
