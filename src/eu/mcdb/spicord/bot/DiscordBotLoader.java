/*
 * Copyright (C) 2019  OopsieWoopsie
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

package eu.mcdb.spicord.bot;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import net.dv8tion.jda.core.JDA;

public class DiscordBotLoader {

	public static boolean startBot(DiscordBot bot) {
		try {
			// TODO: Look for a better way to do this.
			final CountDownLatch latch = new CountDownLatch(1);
			final List<JDA> jda = new ArrayList<JDA>();
			new Thread(new Runnable() {

				@Override
				public void run() {
					jda.add(bot.startBot().getJda());
					latch.countDown();
				}
			}).start();
			latch.await();
			return !jda.isEmpty();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
}