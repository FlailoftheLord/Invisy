package me.flail.invisy;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.flail.invisy.tools.Logger;
import me.flail.invisy.user.User;

public class VanishCommand extends Logger {

	private CommandSender operator;

	public VanishCommand(CommandSender op, String[] args) {
		operator = op;
	}

	public boolean execute() {
		if (!(operator instanceof Player)) {
			operator.sendMessage(chat("&cYou must be a player to use this command"));

			return true;
		}

		if (operator.hasPermission("invisy.vanish")) {
			User user = new User((Player) operator);

			user.setVanished(!user.isVanished());

			plugin.loadVanishedPlayers();

			if (user.isVanished()) {
				user.sendMessage("&aYou are now invisible!");

				return true;
			}

			user.sendMessage("&6You are no longer invisible.");
		}


		return true;
	}


}
