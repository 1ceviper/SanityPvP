package net.iceviper.flyphants.sanitypvp.command;

import java.util.ArrayList;
import java.util.Arrays;

import net.iceviper.flyphants.sanitypvp.inventories.Loadout;
import net.iceviper.flyphants.sanitypvp.item.ItemSaveUtil;
import net.iceviper.flyphants.sanitypvp.item.LegendaryItemStack;
import net.iceviper.flyphants.sanitypvp.player.PlayerKit;
import net.iceviper.flyphants.sanitypvp.player.PlayerStats;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class CommandHandler {
	private ArrayList<String> registeredCommands;
	
	public CommandHandler() {
		registeredCommands = new ArrayList<String>(Arrays.asList(new String[]{"help", "loadout", "legendary", "addcredits", "addtokit"}));
	}
	
	public boolean isRegistered(String command) {
		return registeredCommands.contains(command);
	}
	
	public void execute(CommandSender cmdSender, String cmd, String[] args) {
		if (isRegistered(cmd)) {
			try {
				getClass().getDeclaredMethod(cmd.toLowerCase(), CommandSender.class, String[].class).invoke(this, new Object[]{cmdSender, args});
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	public void loadout(CommandSender cmdSender, String[] args) {
		if (!(cmdSender instanceof Player))
			cmdSender.sendMessage(ChatColor.RED + "This command can only be performed by a player.");
		else {
			new Loadout(((Player) cmdSender).getName()).open();
		}
	}
	
	public void legendary(CommandSender cmdSender, String[] args) {
		if (cmdSender instanceof Player) {
			((Player) cmdSender).getInventory().addItem(LegendaryItemStack.parse(args[0]));
		}
	}
	
	public void addcredits(CommandSender cmdSender, String[] args) {
		if (!cmdSender.isOp()) {
			cmdSender.sendMessage(ChatColor.RED + "This command can only be performed by an op.");
			return;
		}
		if (args.length != 2) {
			cmdSender.sendMessage(ChatColor.RED + "'/addcredits <player> <amount>' requires 2 arguments.");
			return;
		}
		if (Bukkit.getPlayer(args[0]) == null) {
			cmdSender.sendMessage(ChatColor.RED + "Player '" + args[0] + "' was not found.");
			return;
		}
		try {
			Integer.parseInt(args[1]);
		} catch (NumberFormatException e) {
			cmdSender.sendMessage(ChatColor.RED + "'" + args[1] + "' is not a number.");
			return;
		}
		PlayerStats stats = new PlayerStats(Bukkit.getPlayer(args[0]).getName());
		stats.setCredits(stats.getCredits() + Integer.parseInt(args[1]));
	}
	
	public void addtokit(CommandSender cmdSender, String[] args) {
		if (!cmdSender.isOp()) {
			cmdSender.sendMessage(ChatColor.RED + "You do not have access to that command.");
			return;
		}
		if (args.length < 2) {
			cmdSender.sendMessage(ChatColor.RED + "'/sanity addtokit <player> <item> [enchantID1:power] [enchantID2:power] etc.' requires at least 4 arguments");
			return;
		}
		if (Bukkit.getPlayer(args[0]) == null) {
			cmdSender.sendMessage(ChatColor.RED + "Player not found!");
			return;
		}
		if (ItemSaveUtil.toItem(args[1]) == null) {
			cmdSender.sendMessage(ChatColor.RED + "Unknown item! Accepted items are <id:data> or e:<elite weapon name>");
			return;
		}
		String name = Bukkit.getPlayer(args[0]).getName();
		String item = args[1];
		for (int i = 2; i < args.length; i++) {
			item = item + " " + args[i];
		}
		PlayerKit kit = PlayerKit.getKit(name);
		ArrayList<ItemStack> spare = kit.getSpare();
		spare.add(ItemSaveUtil.toItem(item));
		kit.setSpare(spare);
		cmdSender.sendMessage(ChatColor.GREEN + "You succesfully added en item to the player's kit.");
	}
}
