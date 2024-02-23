package org.spicord.config;

import java.util.List;

import com.google.gson.annotations.SerializedName;

public class SpicordConfig {

    // private int config_version; // not used yet

    private int loadDelay;

    @SerializedName("integrated_addon_footer")
    private String integratedAddonFooter;

    private Bot[] bots;

    @SerializedName("jda_messages")
    private JDALogging jdaLogging;

    public SpicordConfig() {
        this.jdaLogging = new JDALogging();
    }

    public int getLoadDelay() {
        return loadDelay;
    }

    public String getIntegratedAddonFooter() {
        return integratedAddonFooter;
    }

    public Bot[] getBots() {
        return bots;
    }

    public JDALogging getJdaLogging() {
        return jdaLogging;
    }

    public static class Bot {

        private String name;

        private boolean enabled;

        private String token;

        private boolean initialCommandCleanup = true;

        @SerializedName("command_support")
        private boolean commandSupport;

        @SerializedName("command_prefix")
        private String commandPrefix;

        private List<String> addons;

        public String getName() {
            return name;
        }

        public boolean isEnabled() {
            return enabled;
        }

        public String getToken() {
            return token;
        }

        public boolean isInitialCommandCleanupEnabled() {
            return initialCommandCleanup;
        }

        public boolean isCommandSupportEnabled() {
            return commandSupport;
        }

        public String getCommandPrefix() {
            return commandPrefix;
        }

        public List<String> getAddons() {
            return addons;
        }
    }

    public static class JDALogging {

        private boolean enabled;
        private boolean debug;

        public boolean isEnabled() {
            return enabled;
        }

        public boolean isDebug() {
            return debug;
        }
    }
}
