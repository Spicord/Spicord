/*
 * Copyright (C) 2020  OopsieWoopsie
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 * 
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package eu.mcdb.universal;

import org.spicord.server.impl.ServerUtil;
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

    static {
        instance = buildServer(serverType = ServerType.auto());
    }

    private static Server buildServer(final ServerType serverType) {
        return ServerUtil.buildServer(serverType);
    }

    public boolean isProxy() {
        return serverType == ServerType.VELOCITY
            || serverType == ServerType.BUNGEECORD;
    }
}
