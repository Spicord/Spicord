package eu.mcdb.spicord.api.services.linking;

import java.util.UUID;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class LinkData {

    @Getter private final Long id;
    @Getter private final String name;
    private final String uuid;

    public UUID getUniqueId() {
        return UUID.fromString(uuid);
    }
}
