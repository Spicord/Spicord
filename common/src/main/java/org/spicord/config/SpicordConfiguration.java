package org.spicord.config;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Logger;

import org.spicord.Spicord;
import org.spicord.bot.DiscordBot;

import com.moandjiezana.toml.Toml;
import com.moandjiezana.toml.TomlWriter;

import lombok.Getter;

public final class SpicordConfiguration {

    private final Spicord spicord;

    @Getter private final Set<DiscordBot> bots;
    @Getter private final File dataFolder;
    @Getter private boolean debugEnabled;
    @Getter private boolean jdaMessagesEnabled;
    @Getter private String integratedAddonFooter;
    @Getter private int loadDelay;

    private final File configFile;
    private final TomlWriter writer;
    private final Logger logger;
    private SpicordConfig config;

    @Getter private final ConfigurationManager manager;

    public SpicordConfiguration(Spicord spicord, final File dataFolder) {
        this.spicord = spicord;

        this.bots = Collections.synchronizedSet(new HashSet<DiscordBot>());

        this.dataFolder = dataFolder;
        this.dataFolder.mkdirs();
        this.configFile = new File(dataFolder, "config.toml");
        this.logger = spicord.getLogger();

        this.writer = new TomlWriter.Builder()
                .indentValuesBy(2)
                .padArrayDelimitersBy(1)
                .build();

        this.load();

        this.manager = new ConfigurationManager(config);
    }

    public void load() {
        this.saveDefault();

        final Toml toml = new Toml().read(configFile);
        this.config = toml.to(SpicordConfig.class);

        for (final SpicordConfig.Bot botData : config.getBots()) {
            final DiscordBot bot = new DiscordBot(
                    spicord,
                    botData.getName(),
                    botData.getToken(),
                    botData.isEnabled(),
                    botData.getAddons(),
                    botData.isInitialCommandCleanupEnabled(),
                    botData.isCommandSupportEnabled(),
                    botData.getCommandPrefix()
                );

            bots.add(bot);
        }

        this.loadDelay = config.getLoadDelay() >= 10 ? config.getLoadDelay() : 10;
        this.jdaMessagesEnabled = config.getJdaLogging().isEnabled();
        this.debugEnabled = config.getJdaLogging().isDebug();
        this.integratedAddonFooter = config.getIntegratedAddonFooter();

        long disabledCount = bots.stream().filter(DiscordBot::isDisabled).count();
        logger.info("Loaded " + bots.size() + " bots, " + disabledCount + " disabled.");
    }

    public void save() {
        this.saveDefault();

        try (final ByteArrayOutputStream baos = new ByteArrayOutputStream();
                final FileOutputStream fos = new FileOutputStream(configFile)) {
            writer.write(config, baos);
            String str = fixIndentation(new String(baos.toByteArray()));
            fos.write(str.getBytes(Charset.forName("UTF-8")));
            fos.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void saveDefault() {
        if (!configFile.exists()) {
            try (final InputStream in = getClass().getResourceAsStream("/config.toml")) {
                Files.copy(in, configFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /*
     * This adds the comment at the top of the file and
     * fixes the toml indentation bug for some values
     */
    private String fixIndentation(String content) {
        String[] lines = content.split("\n");
        List<String> res = new ArrayList<String>();
        res.add("# +--------------------------------------------------+");
        res.add("# | Project: Spicord                                 |");
        res.add("# | GitHub: https://github.com/OopsieWoopsie/Spicord |");
        res.add("# +--------------------------------------------------+");
        res.add("");
        boolean in = false;
        for (String line : lines) {
            if (line.trim().isEmpty()) {
                in = false;
            }
            if (in) {
                if (!line.startsWith("  ")) {
                    line = "  ".concat(line);
                }
            }
            if (line.startsWith("[")) {
                in = true;
            }
            res.add(line);
        }
        return fixArrayIndentation(res);
    }

    /*
     * Splits the array in multiline to make a good indentation :)
     */
    private String fixArrayIndentation(List<String> lines) {
        for (int lineIndex = 0; lineIndex < lines.size(); lineIndex++) {
            String line = lines.get(lineIndex);
            int i = line.indexOf(" = [ ");

            if (i != -1) {
                char[] c = line.toCharArray();
                int spaceCount = -1;
                while (c[spaceCount+=1] == 32);

                String spaces = repeat(" ", spaceCount);
                String indentation = spaces.concat("  ");

                // format the array
                String values = line.substring(i + 5, line.length() - 2).replace("\", \"", "\",\n" + indentation + "\"");
                line = line.substring(0, i + 4) + "\n" + indentation + values + "\n" + spaces + "]";

                // save it :)
                lines.set(lineIndex, line);
            }
        }
        return String.join("\n", lines);
    }

    private static String repeat(String string, int times) {
        final StringBuilder builder = new StringBuilder();

        for (int i = 0; i < times; i++)
            builder.append(string);

        return builder.toString();
    }

    public class ConfigurationManager {

        private final SpicordConfig conf;

        public ConfigurationManager(SpicordConfig conf) {
            this.conf = conf;
        }

        public void addAddonToBot(String addonKey, String botName) {
            for (SpicordConfig.Bot b : conf.getBots()) {
                if (b.getName().equals(botName)) {
                    b.getAddons().add(addonKey);
                    save();
                    return;
                }
            }
        }

        public void removeAddonFromBot(String addonKey, String botName) {
            for (SpicordConfig.Bot b : conf.getBots()) {
                if (b.getName().equals(botName)) {
                    b.getAddons().remove(addonKey);
                    save();
                    return;
                }
            }
        }
    }
}
