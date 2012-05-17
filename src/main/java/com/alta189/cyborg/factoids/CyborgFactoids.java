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

import com.alta189.cyborg.api.command.annotation.EmptyConstructorInjector;
import com.alta189.cyborg.api.plugin.CommonPlugin;
import com.alta189.cyborg.api.util.yaml.YAMLFormat;
import com.alta189.cyborg.api.util.yaml.YAMLProcessor;
import com.alta189.cyborg.factoids.handlers.ActionHandler;
import com.alta189.cyborg.factoids.handlers.AliasHandler;
import com.alta189.cyborg.factoids.handlers.HandlerAlias;
import com.alta189.cyborg.factoids.handlers.NoticeHandler;
import com.alta189.cyborg.factoids.handlers.ReplyHandler;
import com.alta189.simplesave.mysql.MySQLConfiguration;
import com.alta189.simplesave.mysql.MySQLConstants;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Level;

import static com.alta189.cyborg.factoids.FactoidManager.registerHandler;

public class CyborgFactoids extends CommonPlugin {
	@Override
	public void onEnable() {
		getLogger().log(Level.INFO, "Enabling...");

		YAMLProcessor config = setupConfig(new File(getDataFolder(), "config.yml"));

		try {
			config.load();
		} catch (IOException e) {
			e.printStackTrace();
		}

		MySQLConfiguration dbConfig = new MySQLConfiguration();
		dbConfig.setHost(config.getString("database.mysql.host", "127.0.0.1"));
		dbConfig.setPort(config.getInt("database.mysql.port", MySQLConstants.DefaultPort));
		dbConfig.setDatabase(config.getString("database.mysql.database"));
		dbConfig.setUser(config.getString("database.mysql.user", MySQLConstants.DefaultUser));
		dbConfig.setPassword(config.getString("database.mysql.password", MySQLConstants.DefaultPass));

		FactoidManager.init(dbConfig);

		registerHandler(new ReplyHandler());
		registerHandler(new NoticeHandler());
		registerHandler(new ActionHandler());
		registerHandler(new HandlerAlias("act", "action"));
		registerHandler(new AliasHandler());

		getCyborg().getCommandManager().registerCommands(this, FactoidCommands.class, new EmptyConstructorInjector());

		getCyborg().getEventManager().registerEvents(new FactoidsListener(), this);

		getLogger().log(Level.INFO, "Successfully enabled!");
	}

	@Override
	public void onDisable() {
		getLogger().log(Level.INFO, "Disabling...");
		FactoidManager.close();
		getLogger().log(Level.SEVERE, "Successfully disabled!");
	}

	private YAMLProcessor setupConfig(File file) {
		if (!file.exists()) {
			try {
				InputStream input = getClass().getResource("config.yml").openStream();
				if (input != null) {
					FileOutputStream output = null;
					try {
						if (file.getParentFile() != null) {
							file.getParentFile().mkdirs();
						}
						output = new FileOutputStream(file);
						byte[] buf = new byte[8192];
						int length;

						while ((length = input.read(buf)) > 0) {
							output.write(buf, 0, length);
						}
					} catch (Exception e) {
						e.printStackTrace();
					} finally {
						try {
							input.close();
						} catch (Exception ignored) {
						}
						try {
							if (output != null) {
								output.close();
							}
						} catch (Exception e) {
						}
					}
				}
			} catch (Exception e) {
			}
		}

		return new YAMLProcessor(file, false, YAMLFormat.EXTENDED);
	}
}
