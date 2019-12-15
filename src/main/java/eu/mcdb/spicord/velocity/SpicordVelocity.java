package eu.mcdb.spicord.velocity;

import com.google.inject.Inject;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.proxy.ProxyServer;
import eu.mcdb.spicord.SpicordCommand;
import eu.mcdb.spicord.SpicordLoader;
import eu.mcdb.universal.MCDB;
import eu.mcdb.universal.plugin.VelocityPlugin;

@Plugin(id = "spicord", name = "Spicord", version = "2.6.0", authors = { "OopsieWoopsie" })
public class SpicordVelocity extends VelocityPlugin {

    @Inject
    public SpicordVelocity(ProxyServer proxyServer) {
        super(proxyServer);
    }

    @Override
    public void onEnable() {
        SpicordLoader loader = new SpicordLoader(getLogger(), getDataFolder());
        loader.load();
        MCDB.registerCommand(this, new SpicordCommand(() -> {}));
    }
}
