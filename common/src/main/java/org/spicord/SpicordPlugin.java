package org.spicord;

import org.spicord.plugin.PluginInterface;

public interface SpicordPlugin extends PluginInterface {

    void reloadSpicord();

    Spicord getSpicord();

}
