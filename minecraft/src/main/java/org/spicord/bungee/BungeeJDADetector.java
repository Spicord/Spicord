package org.spicord.bungee;

import java.net.URL;
import java.net.URLClassLoader;

import net.md_5.bungee.api.plugin.Plugin;

public class BungeeJDADetector {

    public static void checkOtherJDA(SpicordBungee spicordPlugin) {
        for (Plugin plugin : spicordPlugin.getProxy().getPluginManager().getPlugins()) {
            if (plugin == spicordPlugin) {
                continue;
            }

            URLClassLoader classLoader = (URLClassLoader) plugin.getClass().getClassLoader();

            URL cls = classLoader.getResource("net.dv8tion.jda.api.JDA".replace('.', '/') + ".class");

            if (cls != null) {
                spicordPlugin.getLogger().warning("Found potential incompatibility problem with plugin: " + plugin.getDescription().getName());
            }
        }
    }
}
