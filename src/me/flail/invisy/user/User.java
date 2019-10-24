package me.flail.invisy.user;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.potion.PotionEffect;

import me.flail.invisy.tools.DataFile;
import me.flail.invisy.tools.Message;
import me.flail.invisy.tools.Time;

/**
 * Definitely <b>NOT</b> a user Object.
 * 
 * @author FlailoftheLord
 */
public class User extends UserData {

	public User(UUID uuid) {
		super(uuid);
	}

	/**
	 * TBH i don't even know why i put this here... lol
	 * 
	 * @return myself.
	 */
	public User me() {
		return this;
	}

	public UUID uuid() {
		return playerUuid;
	}

	public String id() {
		return uuid().toString();
	}

	/**
	 * @return This user's {@link Player} object if online. null otherwise.
	 */
	public Player player() {
		return offlinePlayer().isOnline() ? plugin.server.getPlayer(uuid()) : null;
	}

	public OfflinePlayer offlinePlayer() {
		return plugin.server.getOfflinePlayer(uuid());
	}

	public DataFile dataFile() {
		return this.getDataFile();
	}

	/**
	 * Loads the user's data file. Always trigger this when they join the server.
	 */
	public void setup(boolean verbose) {
		plugin.server.getScheduler().scheduleSyncDelayedTask(plugin, () -> {
			setOnline(true);

			dataFile().load();
			loadDefaultValues(this);
			if (verbose) {
				console("Loaded UserData for &7" + name() + "&8[" + ip() + "]" + "  &8(" + uuid() + ")");
			}

			setGamemode(GameMode.valueOf(gamemode().toUpperCase()));

		}, 20L);
	}

	protected void setupOffline() {
		if (!dataFile().hasList("Name")) {
			List<String> names = new ArrayList<>();
			names.add(name());

			dataFile().setValue("Name", names);
		}
	}

	public void logout() {
		setOnline(false);
		plugin.userMap.remove(this.uuid());
	}

	public String name() {
		return offlinePlayer().getName();
	}

	public String ip() {
		return isOnline() ? player().getAddress().toString().replace("/", "") : "user.not.online";
	}

	public String playerGamemode() {
		return isOnline() ? player().getGameMode().toString().toLowerCase() : "user not online";
	}

	public String gamemode() {
		return dataFile().hasValue("Gamemode") ? dataFile().getValue("Gamemode") : playerGamemode();
	}

	public boolean isBanned() {
		return dataFile().getBoolean("Banned");
	}

	public boolean isMuted() {
		return dataFile().getBoolean("Muted");
	}

	public boolean isFrozen() {
		return dataFile().getBoolean("Frozen");
	}

	public boolean isDead() {
		return player().isDead() ? true : false;
	}

	public boolean isReported() {
		return dataFile().getBoolean("Reported");
	}

	public boolean hasPermission(String permission) {
		if (isOnline()) {
			return player().hasPermission(permission);
		}

		return false;
	}

	public boolean command(String command) {
		return isOnline() ? player().performCommand(command) : false;
	}

	public boolean operatorCommand(String command) {
		return isOnline() ? plugin.server.dispatchCommand(player(), command) : false;
	}

	public boolean isOnline() {
		return offlinePlayer().isOnline();
	}

	/**
	 * @return this User's online status as "online" or "offline"
	 */
	public String onlineStatus() {
		return isOnline() ? "online" : "offline";
	}

	public void setOnline(boolean status) {
		dataFile().setValue("Online", Boolean.valueOf(status));
		if (!status) {
			plugin.userMap.remove(uuid());
			return;
		}

		plugin.userMap.put(uuid(), this);
	}

	public void setMessageCooldown(String messageKey, int duration) {
		Set<String> msgs = new HashSet<>();
		if (plugin.msgCooldowns.containsKey(uuid())) {
			msgs = plugin.msgCooldowns.get(uuid());
		}

		if (!msgs.contains(messageKey)) {
			msgs.add(messageKey);

			plugin.msgCooldowns.put(uuid(), msgs);

			Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, () -> {
				Set<String> newSet = new HashSet<>();
				if (plugin.msgCooldowns.containsKey(uuid())) {
					newSet.addAll(plugin.msgCooldowns.get(uuid()));

				}
				plugin.msgCooldowns.remove(uuid());

				newSet.remove(messageKey);
				plugin.msgCooldowns.put(uuid(), newSet);
			}, duration * 20L);

		}

	}

	public boolean hasMessageCooldown(String messageKey) {
		return plugin.msgCooldowns.containsKey(uuid()) ? plugin.msgCooldowns.get(uuid()).contains(messageKey) : false;
	}

	public void toggleFly(User operator) {
		if (isOnline()) {
			if (player().getAllowFlight()) {
				player().setFlying(false);
				player().setAllowFlight(false);

				return;
			}

			player().setAllowFlight(true);
			player().setFlying(true);
			return;
		}

	}

	public void teleport(User target) {
		if (isOnline() && target.isOnline()) {
			player().teleport(target.player());
		}
	}

	public void ouch() {
		player().damage(0.1);
		sendMessage(chat("&4&l<3"));
	}

	public boolean sendMessage(String message) {
		if (isOnline()) {
			player().sendMessage(chat(message));
		}

		return isOnline();
	}

	public void kill(Message message) {
		if (isOnline()) {
			player().setHealth(0);
			message.send(this, null);
		}
	}

	public void heal(boolean removePotionEffects) {
		player().setHealth(20);
		feed(22);

		if (removePotionEffects) {
			for (PotionEffect effect : player().getActivePotionEffects()) {
				player().removePotionEffect(effect.getType());
			}
		}
	}

	public void feed(int level) {
		player().setFoodLevel(level);
	}

	public void setGamemode(GameMode mode) {
		if (isOnline()) {
			player().setGameMode(mode);
		}

		dataFile().setValue("Gamemode", mode.toString().toLowerCase());
	}

	public void burn(boolean toggle, int time) {
		if (toggle) {
			player().setFireTicks(time);
			return;
		}
		player().setFireTicks(0);
	}

	public void backupInventory() {
		DataFile invData = new DataFile("InventoryData.yml");
		List<ItemStack> storedList = new ArrayList<>();

		ItemStack[] invContents = player().getInventory().getContents();
		for (ItemStack item : invContents) {
			if (item != null) {
				storedList.add(item);
			}
		}

		if (!storedList.isEmpty()) {
			invData.setValue(me().id() + ".InventoryBackup." + Time.currentDate().toString().replace(":", "_"), storedList);
		}
	}

	public void clearInventory(boolean backup) {
		if (backup) {
			this.backupInventory();
		}

		player().getInventory().clear();
	}

	@SuppressWarnings("unchecked")
	public void restoreInv(String date) {
		DataFile invData = new DataFile("InventoryData.yml");
		String key = id() + ".InventoryBackup." + date;
		if (invData.hasValue(key)) {
			List<ItemStack> itemList = (List<ItemStack>) invData.getObj(key);

			ItemStack[] currentInv = player().getInventory().getContents();
			for (ItemStack item : currentInv) {
				if (item != null) {
					player().getWorld().dropItemNaturally(player().getLocation(), item);
				}
			}
			player().getInventory().clear();

			for (ItemStack item : itemList) {
				if (item != null) {
					HashMap<Integer, ItemStack> leftovers = player().getInventory().addItem(item);

					if (!leftovers.isEmpty()) {
						for (ItemStack leftover : leftovers.values()) {
							if (leftover != null) {
								player().getWorld().dropItem(player().getLocation(), leftover);
							}
						}
					}
				}

			}

			invData.setValue(key, null);
			return;
		}

		console("doesn't have value");
	}

	public ItemStack getSkull() {
		ItemStack item = new ItemStack(Material.PLAYER_HEAD);
		SkullMeta meta = (SkullMeta) item.getItemMeta();
		meta.setOwningPlayer(offlinePlayer());
		item.setItemMeta(meta);

		item = this.addTag(item, "user", id());

		return item;
	}

	/**
	 * A Mapping of common user placeholders.
	 */
	public Map<String, String> userPl() {
		Map<String, String> pl = new HashMap<>();
		pl.put("%player%", name());
		pl.put("%uuid%", id());
		pl.put("%gamemode%", gamemode());


		return pl;
	}

}
