package me.flail.invisy.tools;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import org.bukkit.ChatColor;

/**
 * Basically, make all your classes extend this one.
 * Contains all the basic utilities all bundled neatly into one. (including an instance of the main
 * plugin class)
 * 
 * @author FlailoftheLord
 */
public class Logger extends CommonUtilities {

	public void console(String string) {
		sendConsole(string);
	}

	public static void sendConsole(String message) {
		plugin.server.getConsoleSender().sendMessage(formatString("[" + plugin.getDescription().getPrefix() + "] " + message));
	}

	public Logger nl() {
		plugin.server.getConsoleSender().sendMessage("");
		return this;
	}

	public void log(String msg) {
		BufferedWriter logWriter = null;

		Time time = new Time();


		// create a temporary file
		String timeLog = new SimpleDateFormat("MMM dd_yyyy").format(Calendar.getInstance().getTime()).toString();

		File logFile = new File(plugin.getDataFolder() + "/logs/" + timeLog + ".txt");
		if (!logFile.exists()) {
			try {
				logFile.createNewFile();
			} catch (IOException e) {
			}
		}

		try {
			logWriter = new BufferedWriter(new FileWriter(logFile, true));

			logWriter.newLine();
			logWriter.write(time.currentDayTime() + " " + ChatColor.stripColor(msg));
		} catch (Exception e) {

			e.printStackTrace();

		} finally {

			try {
				logWriter.close();

			} catch (Exception e) {
			}

		}

	}

}
