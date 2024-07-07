package cat.meowkotuk606.lib;

import java.util.List;

public class Colors {
	public static String translateColorCodes(String msg, String rgbformat) {
		for (int i = 0; i < msg.length(); i++) {
			if (msg.length() - i > 8) {
				String tempString = msg.substring(i, i + 8);
				if (tempString.startsWith(rgbformat)) {
					char[] tempChars = tempString.replaceFirst(rgbformat, "").toCharArray();
					StringBuilder rgbColor = new StringBuilder();
					rgbColor.append("§x");
					for (char tempChar : tempChars)
						rgbColor.append("§").append(tempChar);
					msg = msg.replace(tempString, rgbColor.toString());
				}
			}
		}
		for (String code : List.of("0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "a", "b", "c", "d", "e", "f",
				"x", "r", "l", "k", "o", "m")) {
			msg = msg.replace("&" + code, "§" + code);
			msg = msg.replace("&" + code.toUpperCase(), "§" + code.toUpperCase());
		}
		return msg;
	}

	public static String removeSymbol(String msg, String symbol) {
		if (symbol != null) {
			String newmsg = msg.substring(symbol.length());
			return newmsg;
		}
		return msg;
	}
}
