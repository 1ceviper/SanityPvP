package net.iceviper.flyphants.sanitypvp;

import java.util.Random;

import net.iceviper.flyphants.sanitypvp.command.CommandHandler;
import net.iceviper.flyphants.sanitypvp.inventories.LoadoutListener;
import net.iceviper.flyphants.sanitypvp.item.LegendaryItemStack;
import net.iceviper.flyphants.sanitypvp.listener.PlayerMiscListener;
import net.iceviper.flyphants.sanitypvp.listener.PvPListener;
import net.iceviper.flyphants.sanitypvp.player.PlayerConfig;
import net.iceviper.flyphants.sanitypvp.scoreboard.PrivateScoreboard;
import net.iceviper.flyphants.sanitypvp.scoreboard.ScoreboardListener;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {
	
	public static Main instance;
	public static Random rand;
	
	public CommandHandler cmdHandler;
	
	@Override
	public void onEnable() {
		instance = this;
		rand = new Random();
		cmdHandler = new CommandHandler();
		LegendaryItemStack.registerAll();
		for (Player p : Bukkit.getOnlinePlayers())
			PrivateScoreboard.parse(p.getName());
		PrivateScoreboard.reloadAll();
		new ScoreboardListener();
		new PvPListener();
		new PlayerMiscListener();
		new LoadoutListener();
		PlayerConfig.firstTimeCheck();
	}
	
	@Override
	public boolean onCommand(CommandSender cmdSender, Command cmd, String label, String[] args) {
		if (cmd.getName().equalsIgnoreCase("flyphants") || cmd.getName().equalsIgnoreCase("sanity") || cmd.getName().equalsIgnoreCase("fp")) {
			if (args.length > 0) {
				if (cmdHandler.isRegistered(args[0])) {
					String[] newArgs = new String[args.length - 1];
					for (int i = 0; i < newArgs.length; i++)
						newArgs[i] = args[i + 1];
					cmdHandler.execute(cmdSender, args[0], newArgs);
				} else {
					cmdHandler.execute(cmdSender, "help", args);
				}
				return true;
			}
		} else if (cmdHandler.isRegistered(cmd.getName())) {
			cmdHandler.execute(cmdSender, cmd.getName(), args);
			return true;
		}
		return false;
	}
}
