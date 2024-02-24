package org.spicord.api.addon;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Addon {

    String id();

    String name();

    String[] authors();

    String version();

    ServerSoftware[] supportedServers() default { ServerSoftware.ALL };

    public enum ServerSoftware {
        ALL,

        SPIGOT,
        SPONGE,
        BUNGEECORD,
        VELOCITY,
    }
}
