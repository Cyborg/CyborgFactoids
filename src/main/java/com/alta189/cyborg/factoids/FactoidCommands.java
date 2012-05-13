/*
 * Copyright (C) 2012 CyborgDev <cyborg@alta189.com>
 *
 * This file is part of CyborgFactoids
 *
 * CyborgFactoids is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * CyborgFactoids is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.alta189.cyborg.factoids;

import static com.alta189.cyborg.factoids.FactoidManager.getDatabase;
import static com.alta189.cyborg.perms.PermissionManager.hasPerm;

import com.alta189.cyborg.api.command.CommandContext;
import com.alta189.cyborg.api.command.CommandSource;
import com.alta189.cyborg.api.command.annotation.Command;
import com.alta189.cyborg.api.util.StringUtils;

public class FactoidCommands {

	@Command(name = "remember", desc = "Remembers a factoid", aliases = {"r"})
	public String remember(CommandSource source, CommandContext context) {
		if (source.getSource() != CommandSource.Source.USER)
			return "You cannot register factoids from the terminal";

		if (hasPerm(source.getUser(), "factoids.deny"))
			return "You are not allowed to create factoids";

		String raw = StringUtils.toString(context.getArgs(), " ");
		
		String loc = null;
		String handler = null;
		String body = null;
		String name = null;
		int start = -1;
		int end = -1;

		name = raw.substring(0, raw.indexOf(" ")).toLowerCase();
		int firstIndex = raw.indexOf(" ");
		String first = raw.substring(firstIndex + 1, firstIndex  + 2);
		if (first.equals("<") || first.equals("[")) {
			if (raw.contains("<") && raw.contains(">")) {

				start = raw.indexOf("<");
				end = raw.indexOf(">");
				if (start < end) {
					handler = raw.substring(start + 1, end).toLowerCase();
				}
			}

			if (raw.contains("[") && raw.contains("]")) {
				int i = raw.indexOf("[");
				int y = raw.indexOf("]");
				if (start == -1 || i < start) {
					if (i < y) {
						loc = raw.substring(i + 1, y).toLowerCase();
						if (end == -1)
							end = y;
					}
				}
			}
		}

		if (loc == null || loc.isEmpty()) {
			loc = "global";
		}

		if (handler == null || handler.isEmpty()) {
			handler = "reply";
		}

		if (end == -1) {
			body = raw.substring(raw.indexOf(" ") + 1);
			boolean test = body.startsWith(" ");
			while (test) {
				body = body.substring(1);
				test = body.startsWith(" ");
			}
		} else {
			body = raw.substring(end + 1);
			boolean test = body.startsWith(" ");
			while (test) {
				body = body.substring(1);
				test = body.startsWith(" ");
			}
		}
		
		if (loc.equalsIgnoreCase("local") && context.getLocationType() == CommandContext.LocationType.PRIVATE_MESSAGE)
			return "You cannot define a local factoid in a private message";
		
		Factoid factoid = new Factoid();
		factoid.setName(name.toLowerCase());
		factoid.setLocation(loc.equalsIgnoreCase("local") ? context.getLocation() : loc.toLowerCase());
		factoid.setHandler(handler);
		factoid.setAuthor(source.getUser());
		factoid.setContents(body);
		
		if (getDatabase().select(Factoid.class).where().equal("name", factoid.getName()).and().equal("location", factoid.getLocation()).execute().findOne() != null)
			return "Factoid already exists!";
		
		getDatabase().save(Factoid.class, factoid);

		return "The factoid has been created!";
	}



}
