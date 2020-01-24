package eu.mcdb.spicord.api.services.linking;

import eu.mcdb.spicord.api.services.Service;
import eu.mcdb.universal.player.UniversalPlayer;

public interface LinkingService extends Service {

    boolean isPending(UniversalPlayer player);
    boolean isLinked(UniversalPlayer player);
    LinkData link(PendingLinkData data, long id);
    boolean unlink(LinkData data);
    boolean addPendingLink(PendingLinkData data);

    default boolean isValidMinecraftName(String name) {
        return name.length() >= 3
                && name.length() <= 16
                && name.replaceAll("[A-Za-z0-9_]", "").length() == 0;
    }
}
