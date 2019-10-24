package me.flail.invisy.tools;

import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.java.JavaPlugin;

import me.flail.invisy.Invisy;

public class LegacyUtils {
	protected static Invisy plugin = JavaPlugin.getPlugin(Invisy.class);

	protected ItemStack addLegacyTag(ItemStack item, String key, String tag) {
		ItemMeta meta = item.getItemMeta();
		NamespacedKey nkey = new NamespacedKey(plugin, "MicroTools-" + key);

		meta.getPersistentDataContainer().set(nkey, PersistentDataType.STRING, tag);

		item.setItemMeta(meta);
		return item;
	}

	protected ItemStack removeLegacyTag(ItemStack item, String key) {
		ItemMeta meta = item.getItemMeta();
		NamespacedKey nkey = new NamespacedKey(plugin, "MicroTools-" + key);

		meta.getPersistentDataContainer().remove(nkey);

		item.setItemMeta(meta);
		return item;
	}

	protected String getLegacyTag(ItemStack item, String key) {
		ItemMeta meta = item.getItemMeta();
		NamespacedKey nkey = new NamespacedKey(plugin, "MicroTools-" + key);

		if (hasLegacyTag(item, key)) {
			return meta.getPersistentDataContainer().get(nkey, PersistentDataType.STRING);
		}

		return "null";
	}

	protected static boolean hasLegacyTag(ItemStack item, String key) {
		if ((item != null) && item.hasItemMeta()) {

			ItemMeta meta = item.getItemMeta();
			NamespacedKey nkey = new NamespacedKey(plugin, "MicroTools-" + key);

			return meta.getPersistentDataContainer().has(nkey, PersistentDataType.STRING) ? true : false;
		}
		return false;
	}

}
