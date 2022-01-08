package org.spicord.bukkit;

import java.net.URL;
import java.net.URLClassLoader;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

public class BukkitJDADetector {

    public static void checkOtherJDA(SpicordBukkit spicordPlugin) {
        for (Plugin plugin : Bukkit.getPluginManager().getPlugins()) {
            if (plugin == spicordPlugin) {
                continue;
            }

            URLClassLoader classLoader = (URLClassLoader) plugin.getClass().getClassLoader();

            URL cls = classLoader.getResource("net.dv8tion.jda.api.JDA".replace('.', '/') + ".class");

            if (cls != null) {
                spicordPlugin.getLogger().warning("Found potential incompatibility problem with plugin: " + plugin.getName());
            }
        }
    }
}
