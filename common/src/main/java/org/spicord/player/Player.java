package org.spicord.player;

import java.util.UUID;

public interface Player {

    String getName();

    UUID getUniqueId();

    Object getHandle();

    boolean hasPermission(String permission);

    void sendMessage(String message);

    boolean isOnline();

}
