package net.vexelon.jdevlog.helpers;

public class StringHelper {
	
	public static String excerpt(String message, int maxSize) {
		if (message.length() > maxSize) {
			return message.substring(0, maxSize).concat("...");
		}
		
		return message;
	}

}
