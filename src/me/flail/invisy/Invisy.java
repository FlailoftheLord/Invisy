package me.flail.invisy;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import org.bukkit.OfflinePlayer;
import org.bukkit.Server;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import com.comphenix.protocol.ProtocolLibrary;

import me.flail.invisy.tools.CommonUtilities;
import me.flail.invisy.tools.Logger;
import me.flail.invisy.tools.TabCompleter;
import me.flail.invisy.user.User;
import protocollib.EntityHider;
import protocollib.EntityHider.Policy;

public class Invisy extends JavaPlugin {

	public Server server;
	public Map<UUID, User> userMap = new LinkedHashMap<>(4);
	public Set<UUID> invisibleUsers = new HashSet<>();
	public Map<UUID, Set<String>> msgCooldowns = new HashMap<>();

	public Settings settings;

	public boolean mobsIgnoreInvisPlayers = true, persistVanish = false;

	public EntityHider hider;

	@Override
	public void onLoad() {
		server = getServer();

	}

	@Override
	public void onEnable() {
		settings = new Settings();
		settings.load();

		hider = new EntityHider(this, Policy.BLACKLIST);

		mobsIgnoreInvisPlayers = settings.file().getBoolean("MobsIgnoreInvisiblePlayers");
		persistVanish = settings.file().getBoolean("VanishStatePersistent");

		loadOnlinePlayers();

		for (String cmd : getDescription().getCommands().keySet()) {
			getCommand(cmd).setExecutor(this);
		}

		ProtocolLibrary.getProtocolManager().addPacketListener(new InvisyEventListener());
		server.getPluginManager().registerEvents(new InvisyEventListener(), this);

		server.getScheduler().scheduleSyncDelayedTask(this, () -> {
			if (persistVanish) {
				for (UUID uuid : userMap.keySet()) {
					User user = new User(uuid);

					if (user.isVanished()) {
						invisibleUsers.add(uuid);
					}
				}

			}

			loadVanishedPlayers();
		}, 64L);

	}

	@Override
	public void onDisable() {
		server.getScheduler().cancelTasks(this);
		userMap.clear();
		invisibleUsers.clear();

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
		if (command.getName().equalsIgnoreCase("vanish")) {
			return new VanishCommand(sender, args).execute();
		}

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

	public void loadVanishedPlayers() {
		Collection<UUID> onlinePlayers = userMap.keySet();

		for (UUID uuid : onlinePlayers) {
			OfflinePlayer player = server.getOfflinePlayer(uuid);
			if (player.isOnline()) {

				if (invisibleUsers.contains(uuid)) {

					setVanishState((Player) player, true);
					return;
				}

				setVanishState((Player) player, false);
			}

		}

	}

	public void loadPlayer(Player player) {


	}

	protected void setVanishState(Player player, boolean state) {

		for (UUID u : userMap.keySet()) {
			Player p = server.getPlayer(u);

			if (state) {
				if (!canSee(p, player)) {
					hider.hideEntity(p, player);
				}

				continue;
			}

			hider.showEntity(p, player);
		}

	}

	protected boolean canSee(Player subject, Player target) {
		int min = 0, max = 100;
		int subjectPerm = -1, targetPerm = -1;

		for (int p = max; p >= min; p--) {
			if ((subjectPerm == -1) && subject.hasPermission("invisy.see." + p)) {
				subjectPerm = p;
			}

			if ((targetPerm == -1) && target.hasPermission("invisy.hide." + p)) {
				targetPerm = p;
			}
		}

		return subjectPerm >= targetPerm;
	}

}
