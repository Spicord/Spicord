package eu.mcdb.util.chat;

public class ChatColor {

    public static String stripColor(String str) {
        return str.replaceAll("(?i)(&|§)[a-f0-9klmnor]", "");
    }

    // temp. solution, this is not safe
    public static String translateAlternateColorCodes(char magic, String text) {
        return text.replace(magic, '\u00a7');
    }
}
