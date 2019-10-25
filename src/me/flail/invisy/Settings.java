package me.flail.invisy;

import java.util.HashMap;
import java.util.Map;

import me.flail.invisy.tools.DataFile;
import me.flail.invisy.tools.Logger;
import me.flail.invisy.user.User;

public class Settings extends Logger {
	private DataFile settings;

	protected Settings(User user) {
		settings = user.dataFile();
	}

	public Settings() {
		settings = new DataFile("Settings.yml");

	}

	public DataFile data() {
		return settings;
	}

	public DataFile file() {
		return data();
	}

	public void load() {
		settings.setHeader(header);

		loadDefaultValues();
	}

	private void loadDefaultValues() {
		Map<String, Object> values = new HashMap<>();

		values.put("MobsIgnoreInvisiblePlayers", Boolean.valueOf(true));
		values.put("VanishStatePersistent", Boolean.valueOf(true));

		setValues(settings, values);
	}

	private String header =  "-----------------------------------------------------------------\r\n" +
			"==================================================================#\r\n" +
			"                                                                  #\r\n" +
			"                 Invisy by FlailoftheLord.                        #\r\n" +
			"         -=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-                  #\r\n" +
			"           If you have any Questions or feedback                  #\r\n" +
			"              Join my discord server here:                        #\r\n" +
			"               https://discord.gg/wuxW5PS                         #\r\n" +
			"   ______               __        _____                           #\r\n" +
			"   |       |           /  \\         |        |                    #\r\n" +
			"   |__     |          /____\\        |        |                    #\r\n" +
			"   |       |         /      \\       |        |                    #\r\n" +
			"   |       |_____   /        \\    __|__      |______              #\r\n" +
			"                                                                  #\r\n" +
			"==================================================================#\r\n" +
			"-----------------------------------------------------------------\r\n" +
			"- - -\r\n";

	protected void setValues(DataFile file, Map<String, Object> values) {
		for (String key : values.keySet()) {
			if (!file.hasValue(key)) {
				file.setValue(key, values.get(key));
			}
		}
	}

}

