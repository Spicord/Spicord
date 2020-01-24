package eu.mcdb.spicord.api.services.linking;

import java.util.UUID;
import eu.mcdb.universal.player.UniversalPlayer;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class PendingLinkData {

    private final String name;
    private final UUID uniqueId;

    public LinkData create(long id) {
        return new LinkData(id, name, uniqueId.toString());
    }

    public static PendingLinkData of(UniversalPlayer player) {
        return new PendingLinkData(player.getName(), player.getUniqueId());
    }
}
