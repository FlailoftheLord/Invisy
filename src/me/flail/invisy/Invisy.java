package me.flail.invisy;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import org.bukkit.Server;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import me.flail.invisy.tools.CommonUtilities;
import me.flail.invisy.tools.Logger;
import me.flail.invisy.tools.TabCompleter;
import me.flail.invisy.user.User;

public class Invisy extends JavaPlugin {

	public Server server;
	public Map<UUID, User> userMap = new LinkedHashMap<>(4);
	public Map<UUID, Set<String>> msgCooldowns = new HashMap<>();

	public Settings settings;

	public boolean mobsIgnoreInvisPlayers = true;

	@Override
	public void onLoad() {
		server = getServer();

	}

	@Override
	public void onEnable() {
		settings = new Settings();
		settings.load();

		mobsIgnoreInvisPlayers = settings.file().getBoolean("MobsIgnoreInvisiblePlayers");

		loadOnlinePlayers();

		for (String cmd : getDescription().getCommands().keySet()) {
			getCommand(cmd).setExecutor(this);
		}

		server.getPluginManager().registerEvents(new InvisyEventListener(), this);

	}

	@Override
	public void onDisable() {
		server.getScheduler().cancelTasks(this);
		userMap.clear();

		Logger.sendConsole("&cDisabled Invisy.");
	}

	public void reload() {
		Logger.sendConsole("&6Reloading Invisy...");

		onDisable();
		onLoad();
		onEnable();

		Logger.sendConsole("&aReloaded Invisy Successfully!");
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (!sender.hasPermission("invisy.command")) {
			sender.sendMessage(CommonUtilities.chatFormat("&cYou don't have permission to use that."));

			return true;
		}
		if ((args.length >= 1) && args[0].equalsIgnoreCase("reload")) {

			this.reload();
			sender.sendMessage(CommonUtilities.chatFormat("&aReloaded Invisy's Settings."));
			return true;
		}

		sender.sendMessage(CommonUtilities.chatFormat("&cUsage&8: &7/invisy reload"));
		return true;
	}

	@Override
	public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
		return new TabCompleter(command).construct(label, args);
	}

	public void loadOnlinePlayers() {
		userMap.clear();
		for (Player p : server.getOnlinePlayers()) {
			userMap.put(p.getUniqueId(), new User(p.getUniqueId()));
		}

	}

}
