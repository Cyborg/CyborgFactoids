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

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.alta189.cyborg.factoids.handlers.Handler;
import com.alta189.simplesave.Configuration;
import com.alta189.simplesave.Database;
import com.alta189.simplesave.DatabaseFactory;
import com.alta189.simplesave.exceptions.ConnectionException;
import com.alta189.simplesave.exceptions.TableRegistrationException;

public class FactoidManager {
	
	private static final Map<String, Handler> handlers = new HashMap<String, Handler>();
	private static Database db;

	protected static void init(Configuration config) {
		db = DatabaseFactory.createNewDatabase(config);

		try {
			db.registerTable(Factoid.class);
		} catch (TableRegistrationException e) {
			throw new RuntimeException(e);
		}

		try {
			db.connect();
		} catch (ConnectionException e) {
			throw new RuntimeException(e);
		}
	}
	
	protected static void close() {
		try {
			db.close();
		} catch (ConnectionException e) {
			e.printStackTrace();
		}
	}

	public static Collection<Handler> getHandlers() {
		return Collections.unmodifiableCollection(handlers.values());
	}
	
	public static void registerHandler(Handler handler) {
		handlers.put(handler.getName().toLowerCase(), handler);
	}
	
	public static Handler getHandler(String name) {
		return handlers.get(name.toLowerCase());
	}

	public static List<Factoid> getFactoids(String name) {
		return db.select(Factoid.class).where().equal("name", name).and().equal("forgotten", false).execute().find();
	}

	public static Factoid getFactoid(String name) {
		return db.select(Factoid.class).where().equal("name", name).and().equal("forgotten", false).execute().findOne();
	}
	
	public static Factoid getFactoid(String name, String location) {
		return db.select(Factoid.class).where().equal("name", name).and().equal("location", location).and().equal("forgotten", false).execute().findOne();
	}
	
	public static Database getDatabase() {
		return db;
	}
	
	public static void saveFactoid(Factoid factoid) {
		db.save(Factoid.class, factoid);
	}
	
	public static String getFactoidFromRaw(String raw) {
		String prefix = getPrefix(raw);
		if (prefix == null)
			return null;
		raw = raw.substring(1);

		if (raw.contains(" ")) {
			return raw.substring(0, raw.indexOf(" "));
		}
		return raw;
	}

	public static String getPrefix(String raw) {
		String p = raw.substring(0, 1);
		if (p.equals("!") || p.equals("?"))
			return p;
		return null;
	}

	public static String getArgs(String raw) {
		if (!raw.contains(" ") || raw.equals(" "))
			return null;

		int firstSpace = raw.indexOf(" ");
		if (firstSpace + 1 >= raw.length())
			return null;
		return raw.substring(firstSpace + 1);
	}
	
}
