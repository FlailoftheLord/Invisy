package me.flail.invisy.tools;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.command.Command;

import me.flail.invisy.user.User;

public class TabCompleter extends Logger {

	private Command command;

	public TabCompleter(Command command) {
		this.command = command;
	}

	public List<String> construct(String label, String[] args) {
		List<String> baseArgs = new ArrayList<>();
		if (!command.getName().equalsIgnoreCase("invisy")) {
			return baseArgs;
		}

		switch (args.length) {
		case 1:


		}

		for (String s : baseArgs.toArray(new String[] {})) {
			if (!s.startsWith(args[args.length - 1].toLowerCase())) {

				baseArgs.remove(s);
			}

		}

		return baseArgs;
	}



	protected List<String> usernames() {
		List<String> names = new ArrayList<>();
		for (User user : plugin.userMap.values()) {
			names.add(user.name());
		}
		return names;
	}

}
