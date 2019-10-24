package me.flail.invisy.tools;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.Calendar;
import java.util.Date;

public class Time extends Logger {

	public static Date currentDate() {
		return Calendar.getInstance().getTime();
	}

	public static Instant currentInstant() {
		return currentDate().toInstant();
	}

	public static Instant finalTime(Instant initial, long duration) {
		return initial.plusSeconds(duration);
	}

	public static Instant initialTime(Instant finalTime, long duration) {
		return finalTime.minusSeconds(duration);
	}

	public static Long timeLeft(Instant initial, long duration) {
		return Long.valueOf((finalTime(initial, duration).toEpochMilli() - currentInstant().toEpochMilli()) / 1000);
	}

	public static boolean isExpired(Date date) {
		return date.toInstant().isBefore(currentInstant());
	}

	public static String formatInstant(Instant i) {
		return new SimpleDateFormat("MMMMMMMMMMMMMM dd, yyyy '@' HH mm:ss").format(Date.from(i));
	}

	public static String formatTime(Date date) {
		return new SimpleDateFormat("MMMMMMMMMMMMMM dd, yyyy '@' HH mm:ss").format(date);
	}

	public String monthName(int month) {
		switch (month - 1) {

		case 1:
			return "January";
		case 2:
			return "Febuary";
		case 3:
			return "March";
		case 4:
			return "April";
		case 5:
			return "May";
		case 6:
			return "June";
		case 7:
			return "July";
		case 8:
			return "August";
		case 9:
			return "September";
		case 10:
			return "October";
		case 11:
			return "November";
		case 12:
			return "December";
		default:
			return month + "";

		}
	}

	public String currentDayTime() {
		String time = new SimpleDateFormat("(HH:mm:ss)").format(Calendar.getInstance().getTime());

		return time;

	}

	public String serverTime() {

		long second = System.currentTimeMillis() / 1000;

		long minute = second / 60;

		long hour = minute / 60;

		long day = hour / 24;

		String time = "Day: " + day + " Hour: " + hour + " Minute: " + minute + " Second: " + second;

		return time;

	}

}
