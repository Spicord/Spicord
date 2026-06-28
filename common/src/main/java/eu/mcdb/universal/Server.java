package eu.mcdb.universal;

import org.spicord.Spicord;

import eu.mcdb.universal.command.UniversalCommand;
import lombok.Getter;
import lombok.Setter;

/**
 * This class provides methods that can be accessed no matter what server
 * software you are using.
 */
public abstract class Server implements ServerInterface {

    @Getter private static ServerType serverType;
    @Getter private static Server instance;
    @Getter @Setter private boolean debugEnabled; // false by default

    public static void setInstance(Server instance, ServerType serverType) {
        Server.instance = instance;
        Server.serverType = serverType;
    }

    public boolean isProxy() {
        return serverType == ServerType.VELOCITY
            || serverType == ServerType.BUNGEECORD;
    }

    public void registerCommandAsSpicord(UniversalCommand command) {
        command.register(Spicord.getInstance().getPlugin());
    }
}
