package me.flail.invisy.tools;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import org.bukkit.ChatColor;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class CommonUtilities extends BaseUtilities {

	protected static String formatString(String message) {
		DataFile messages = new DataFile("Messages.yml");
		message = message.toString();
		message = message.replace("%prefix%", messages.getValue("Prefix"));

		return ChatColor.translateAlternateColorCodes('&', message);
	}

	protected String chat(String message) {
		return formatString(message);
	}

	public static String chatFormat(String message) {
		return ChatColor.translateAlternateColorCodes('&', message);
	}

	/**
	 * Converts a string, by translating the following placeholders with their counterparts defined in
	 * the provided Map of placeholders.
	 * 
	 * @param message
	 * @param placeholders
	 *                         Formatted as
	 *                         <code>{@literal Map<String placeholder, String value>}</code>
	 * @return the new String.
	 */
	public String placeholders(String message, Map<String, String> placeholders) {
		if (!placeholders.isEmpty() && (message != null)) {
			for (String p : placeholders.keySet()) {
				if (p != null) {
					message = message.replace(p, placeholders.get(p));
				}
			}
		}
		return chat(message);
	}

	/**
	 * Replaces any placeholders provided in the displayname and lore of this ItemStack
	 * 
	 * @param item
	 * @param placeholders
	 * @return the new Item with the updated placeholders.
	 */
	public ItemStack itemPlaceholders(ItemStack item, Map<String, String> placeholders) {
		if ((item != null) && item.hasItemMeta()) {
			ItemMeta meta = item.getItemMeta();
			if (meta.hasLore()) {
				List<String> lore = meta.getLore();

				List<String> newLore = new ArrayList<>();
				for (String line : lore) {
					newLore.add(this.placeholders(line, placeholders));
				}
				meta.setLore(newLore);
				meta.setDisplayName(this.placeholders(meta.getDisplayName(), placeholders));

				item.setItemMeta(meta);
			}
		}

		return item;
	}

	/**
	 * Updates the placeholders on all items in the inventory.
	 * 
	 * @param inv
	 * @param placeholders
	 * @return the new Inventory with the updated items.
	 */
	public Inventory updateItemPlaceholders(Inventory inv, Map<String, String> placeholders) {
		if (inv != null) {
			for (ItemStack item : inv.getContents()) {
				item = this.itemPlaceholders(item, placeholders);
			}
		}

		return inv;
	}

	/**
	 * Some simple checks i made for simpler code.
	 * 
	 */
	public boolean msgCheck(String message, String text, String type) {
		switch (type.toLowerCase()) {
		case "starts":
			return message.startsWith(text.toLowerCase());
		case "ends":
			return message.endsWith(text.toLowerCase());
		case "contains":
			return message.contains(text.toLowerCase());
		default:
			return false;

		}
	}

	/**
	 * this is completely pointless... but it's still cool right?
	 */
	public String replaceText(String message, String text, String replacement) {
		return message = message.replaceAll("(?i)" + Pattern.quote(text), replacement);
	}

	/**
	 * converts an array to a single string.
	 */
	public String convertArray(String[] values, int start) {
		StringBuilder builder = new StringBuilder();
		while (start < values.length) {
			builder.append(values[start] + " ");

			start++;
		}

		return builder.toString();
	}

	public static String enumName(Enum<?> e) {
		String name = "";

		for (String s : e.toString().toLowerCase().split("_")) {

			s = s.replace(s.charAt(0) + "", (s.charAt(0) + "").toUpperCase());
			name = name.concat(s + " ");
		}

		return name;
	}

}
