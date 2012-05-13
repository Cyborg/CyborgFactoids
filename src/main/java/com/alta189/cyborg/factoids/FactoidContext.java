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

import org.pircbotx.Channel;
import org.pircbotx.User;

public class FactoidContext {
	private final LocationType locationType;
	private final Channel channel;
	private final User sender;
	private String rawArgs;

	public FactoidContext(Channel channel, User sender, LocationType locationType, String rawArgs) {
		this.locationType = locationType;
		this.channel = channel;
		this.sender = sender;
		this.rawArgs = rawArgs;
	}

	public FactoidContext(Channel channel, User sender, String rawArgs) {
		this(channel, sender, LocationType.CHANNEL_MESSAGE, rawArgs);
	}

	public FactoidContext(Channel channel, User sender) {
		this(channel, sender, LocationType.CHANNEL_MESSAGE, null);
	}

	public FactoidContext(User sender, String rawArgs) {
		this(null, sender, LocationType.PRIVATE_MESSAGE, rawArgs);
	}

	public FactoidContext(User sender) {
		this(null, sender, LocationType.PRIVATE_MESSAGE, null);
	}

	public LocationType getLocationType() {
		return locationType;
	}

	public Channel getChannel() {
		return channel;
	}

	public User getSender() {
		return sender;
	}

	public String getRawArgs() {
		return rawArgs;
	}

	public void setRawArgs(String rawArgs) {
		this.rawArgs = rawArgs;
	}
}
