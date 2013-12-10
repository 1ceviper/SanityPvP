package net.iceviper.flyphants.sanitypvp.inventories;

import java.util.HashMap;

import net.iceviper.flyphants.sanitypvp.Main;
import net.iceviper.flyphants.sanitypvp.item.ItemSaveUtil;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerDropItemEvent;

public class LoadoutListener implements Listener {
	
	public static LoadoutListener instance;
	
	private HashMap<Player, Loadout> storedItems = new HashMap<Player, Loadout>();
	
	public LoadoutListener() {
		instance = this;
		Main.instance.getServer().getPluginManager().registerEvents(this, Main.instance);
	}
	
	public void passLadout(Player p, Loadout l) {
		storedItems.put(p, l);
	}
	
	@EventHandler
	public void onClick(InventoryClickEvent e) {
		if (!e.getInventory().getName().equalsIgnoreCase(ChatColor.BLUE + "Spare Equipment"))
			return;
		if (e.getCurrentItem() == null)
			return;
		if (e.getCurrentItem().hasItemMeta() && e.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase(ChatColor.RED + "Border")){
			e.setCancelled(true);
		}
	}
	
	@EventHandler
	public void onClose(InventoryCloseEvent e) {
		if (!e.getInventory().getName().equalsIgnoreCase(ChatColor.BLUE + "Spare Equipment"))
			return;
		if (storedItems.containsKey(e.getPlayer()))
			storedItems.get(e.getPlayer()).close(e.getInventory());
		else {}
			//TODO equip new kit and give error
	}
	
	@EventHandler
	public void onItemDrop(PlayerDropItemEvent e) {
		if (e.getPlayer().getOpenInventory() != null && e.getPlayer().getOpenInventory().getTopInventory() != null){
			if (e.getPlayer().getOpenInventory().getTopInventory().getName().equalsIgnoreCase(ChatColor.BLUE + "Spare Equipment")) {
				e.getPlayer().getOpenInventory().getTopInventory().addItem(e.getItemDrop().getItemStack());
				e.getItemDrop().remove();
				ItemSaveUtil.updateInventory(e.getPlayer());
			}
		}
	}
}
