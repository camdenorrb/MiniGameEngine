package dev.twelveoclock.minigameengine.utils;

public final class StringUtils {

	private StringUtils() {}

	public static String titleCase(final String string) {
		return string.substring(0, 1).toUpperCase() + string.substring(1).toLowerCase();
	}

}
