package me.flail.invisy;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import me.flail.invisy.tools.DataFile;
import me.flail.invisy.tools.Logger;
import me.flail.invisy.user.User;

public class Settings extends Logger {
	private DataFile file;

	protected Settings(User user) {
		file = user.dataFile();
	}

	public Settings() {
		file = new DataFile("Settings.yml");

	}

	private DataFile data() {
		return file;
	}

	public DataFile file() {
		return data();
	}

	public void load() {
		file.setHeader(header);

		loadDefaultValues();
	}

	private void loadDefaultValues() {
		Map<String, Object> values = new HashMap<>();

		values.put("MobsIgnoreInvisiblePlayers", Boolean.valueOf(true));
		values.put("VanishStatePersistent", Boolean.valueOf(true));
		values.put("FlyOnVanish", Boolean.valueOf(true));
		values.put("VanishFromTablist", Boolean.valueOf(false));
		values.put("VanishStatusMessage", "&7You are invisible to mobs and players");
		values.put("ShowOffline", Boolean.valueOf(false));

		setValues(file, values);
	}

	private String header = "-----------------------------------------------------------------\r\n" +
			"==================================================================#\r\n" +
			"                                                                  #\r\n" +
			"                 Invisy by FlailoftheLord.                        #\r\n" +
			"         -=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-                  #\r\n" +
			"   ______               __        _____                           #\r\n" +
			"   |       |           /  \\         |        |                    #\r\n" +
			"   |__     |          /____\\        |        |                    #\r\n" +
			"   |       |         /      \\       |        |                    #\r\n" +
			"   |       |_____   /        \\    __|__      |______              #\r\n" +
			"                                                                  #\r\n" +
			"==================================================================#\r\n" +
			"-----------------------------------------------------------------\r\n" +
			"- - -\r\n" +
			" Hide & See permissions are layered from 0 - 100, with 100 being the highest:\r\n" +
			" invisy.hide.100  and  invisy.see.100\r\n";

	protected void setValues(DataFile file, Map<String, Object> values) {
		for (Entry<String, Object> key : values.entrySet()) {
			String k = key.getKey();
			if (!file.hasValue(k)) {
				file.setValue(k, values.get(k));
			}
		}
	}

}
