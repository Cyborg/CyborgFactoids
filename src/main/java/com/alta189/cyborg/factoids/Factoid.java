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

import com.alta189.cyborg.factoids.util.DateUtil;
import com.alta189.simplesave.Field;
import com.alta189.simplesave.Id;
import com.alta189.simplesave.Table;
import org.pircbotx.User;

@Table("factoids")
public class Factoid {
	@Id
	private int id;
	@Field
	private String name;
	@Field
	private String location;
	@Field
	private String handler;
	@Field
	private String contents;
	@Field
	private String author;
	@Field
	private boolean locked = false;
	@Field
	private String locker;
	@Field
	private boolean forgotten = false;
	@Field
	private String forgetter;
	@Field
	private long timestamp;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public Factoid setName(String name) {
		this.name = name;
		return this;
	}

	public String getLocation() {
		return location;
	}

	public Factoid setLocation(String location) {
		this.location = location;
		return this;
	}

	public String getHandler() {
		return handler;
	}

	public Factoid setHandler(String handler) {
		this.handler = handler;
		return this;
	}

	public String getContents() {
		return contents;
	}

	public Factoid setContents(String contents) {
		this.contents = contents;
		return this;
	}

	public String getAuthor() {
		return author;
	}

	public Factoid setAuthor(User user) {
		this.author = user.getNick() + "!" + user.getLogin() + "@" + user.getHostmask();
		return this;
	}

	public Factoid setAuthor(String author) {
		this.author = author;
		return this;
	}

	public boolean isLocked() {
		return locked;
	}

	public Factoid setLocked(boolean locked) {
		this.locked = locked;
		return this;
	}

	public boolean isForgotten() {
		return forgotten;
	}

	public Factoid setForgotten(boolean forgotten) {
		this.forgotten = forgotten;
		return this;
	}

	public String getLocker() {
		return locker;
	}

	public Factoid setLocker(User user) {
		this.locker = user.getNick() + "!" + user.getLogin() + "@" + user.getHostmask();
		return this;
	}

	public Factoid setLocker(String locker) {
		this.locker = locker;
		return this;
	}

	public String getForgetter() {
		return forgetter;
	}

	public Factoid setForgetter(User user) {
		this.forgetter = user.getNick() + "!" + user.getLogin() + "@" + user.getHostmask();
		return this;
	}

	public Factoid seForgetter(String forgetter) {
		this.forgetter = forgetter;
		return this;
	}

	public long getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(long timestamp) {
		this.timestamp = timestamp;
	}

	public String getDate() {
		return DateUtil.getFormattedGMTDate(timestamp);
	}

	public String getInfo() {
		StringBuilder builder = new StringBuilder();
		builder.append(name).append(" ");
		builder.append("<author: \"").append(author).append("\"> ");
		if (locked) {
			builder.append("<locked: \"").append(locker).append("\"> ");
		}
		if (forgotten) {
			builder.append("<forgotten: \"").append(forgetter).append("\"> ");
		}
		builder.append("<modified: \"").append(getDate()).append("\">");
		return builder.toString();
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append(name);
		if (locked) {
			builder.append(" <locked>");
		}
		if (forgotten) {
			builder.append(" <forgotten>");
		}
		builder.append(" <" + handler + "> ");
		builder.append(contents);
		return builder.toString();
	}
}
