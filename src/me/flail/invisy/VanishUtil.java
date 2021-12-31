package me.flail.invisy;

import java.util.UUID;

import org.bukkit.Bukkit;

import me.flail.invisy.tools.Logger;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;

public class VanishUtil extends Logger {

	public static void runVanishStatus() {
		plugin.server.getScheduler().cancelTasks(plugin);
		plugin.server.getScheduler().scheduleSyncRepeatingTask(plugin, VanishStatus.getInstance(), 2L, 32L);

	}

	protected static class VanishStatus implements Runnable {
		public static VanishStatus getInstance() {
			return new VanishStatus();
		}

		@Override
		public void run() {

			for (UUID uuid : plugin.invisibleUsers.toArray(new UUID[] {})) {
				TextComponent message = new TextComponent(chat(plugin.vanishStatusMsg));

				Bukkit.getPlayer(uuid).spigot().sendMessage(ChatMessageType.ACTION_BAR, message);
			}
		}

	}

}
