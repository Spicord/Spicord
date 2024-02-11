package org.spicord.velocity;

import java.net.URL;

import com.velocitypowered.api.plugin.PluginContainer;

public class VelocityJDADetector {

    public static void checkOtherJDA(SpicordVelocity spicordPlugin) {
        for (PluginContainer plugin : SpicordVelocity.getProxyServer().getPluginManager().getPlugins()) {
            if (plugin == spicordPlugin) {
                continue;
            }

            ClassLoader classLoader = plugin.getClass().getClassLoader();

            URL cls = classLoader.getResource("net.dv8tion.jda.api.JDA".replace('.', '/') + ".class");

            if (cls != null) {
                spicordPlugin.getLogger().warning("Found potential incompatibility problem with plugin: " + plugin.getDescription().getName());
            }
        }
    }
}
