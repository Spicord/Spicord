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

package org.spicord.cli;

import java.util.logging.Logger;
import eu.mcdb.spicord.SpicordCommand;
import eu.mcdb.universal.command.UniversalCommandSender;

class SpicordConsoleCommand {

    private final UniversalCommandSender commandSender;
    private final SpicordCommand command = new SpicordCommand(() -> {});

    public SpicordConsoleCommand(final Logger logger) {
        this.command.detachChilds();
        this.command.setUsagePrefix("");
        this.command.setUsageEnabled(false);
        this.commandSender = new UniversalCommandSender(){

            @Override
            public String getName() {
                return "Spicord Console Client";
            }

            @Override
            public boolean hasPermission(String permission) {
                return true;
            }

            @Override
            public void sendMessage(String message) {
                logger.info(message);
            }
        };
    }

    public void execute(String input) {
        this.command.onCommand(this.commandSender, input.split(" "));
    }
}
