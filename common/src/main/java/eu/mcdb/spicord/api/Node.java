package eu.mcdb.spicord.api;

import java.util.logging.Logger;
import eu.mcdb.spicord.Spicord;
import eu.mcdb.util.Server;

public interface Node {

    /**
     * Get the Spicord instance.
     * 
     * @return the Spicord instance
     */
    default Spicord getSpicord() {
        return Spicord.getInstance();
    }

    /**
     * Get the universal server instance.
     * 
     * @return the {link Server} instance
     */
    default Server getServer() {
        return Server.getInstance();
    }

    /**
     * Get the Spicord logger instance.
     * 
     * @return the spicord logger instance
     */
    default Logger getLogger() {
        return getSpicord().getLogger();
    }
}
