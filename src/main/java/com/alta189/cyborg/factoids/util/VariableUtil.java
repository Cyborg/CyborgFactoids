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
package com.alta189.cyborg.factoids.util;

import com.alta189.cyborg.api.util.StringUtils;
import com.alta189.cyborg.factoids.FactoidContext;
import com.alta189.cyborg.factoids.LocationType;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class VariableUtil {
	public static final Pattern varPattern = Pattern.compile("%([0-9][0-9]{0,2}|10000)%");
	public static final Pattern varPatternRange = Pattern.compile("%([0-9][0-9]{0,2}|10000)-([0-9][0-9]{0,2}|10000)%");
	public static final Pattern varPatternInfinite = Pattern.compile("%([0-9][0-9]{0,2}|10000)-%");
	public static final Pattern nickPattern = Pattern.compile("%nick%");
	public static final Pattern chanPattern = Pattern.compile("%chan%");
	public static final Pattern urlPattern = Pattern.compile("%readurl\\(.*\\)%");
	public static final Pattern readRandLinePattern = Pattern.compile("%readRandLine\\(.*\\)%");
	public static final Pattern urlPatternURL = Pattern.compile("\\((\".*\")\\)");
	public static final Pattern lineBreakPattern = Pattern.compile("%rn%");

	public static String replaceVars(String raw, FactoidContext context) {
		Matcher matcher = null;
		String[] args;
		if (context.getRawArgs() == null || context.getRawArgs().isEmpty()) {
			args = new String[0];
		} else {
			args = context.getRawArgs().split(" ");
		}

		matcher = urlPattern.matcher(raw);
		while (matcher.find()) {
			String match = matcher.group();
			Matcher urlMather = urlPatternURL.matcher(match);
			if (urlMather.find()) {
				String url =  urlMather.group();
				if (url != null && url.length() > 4) {
					url = url.substring(2, url.length() - 2);
					String result = HTTPUtil.readURL(url);
					if (result != null && !result.isEmpty()) {
						if (result.contains("\r\n")) {
							result = result.substring(0,result.indexOf("\r\n"));
						} else if (result.contains("\n")) {
							result = result.substring(0,result.indexOf("\n"));
						}
						raw = raw.replace(match, result);
					} else {
						raw = raw.replace(match, "");
					}
				} else {
					raw = raw.replace(match, "");
				}
			} else {
				raw = raw.replace(match, "");
			}
		}

		matcher = readRandLinePattern.matcher(raw);
		while (matcher.find()) {
			String match = matcher.group();
			Matcher urlMather = urlPatternURL.matcher(match);
			if (urlMather.find()) {
				String url =  urlMather.group();
				if (url != null && url.length() > 4) {
					url = url.substring(2, url.length() - 2);
					String result = HTTPUtil.readRandomLine(url);
					if (result == null || result.isEmpty()) {
						raw = raw.replace(match, "");
					} else {
						raw = raw.replace(match, result);
					}
				} else {
					raw = raw.replace(match, "");
				}
			} else {
				raw = raw.replace(match, "");
			}
		}

		matcher = nickPattern.matcher(raw);
		raw = matcher.replaceAll(context.getSender().getNick());

		if (context.getLocationType() == LocationType.CHANNEL_MESSAGE) {
			matcher = chanPattern.matcher(raw);
			raw = matcher.replaceAll(context.getChannel().getName());
		}


		matcher = varPattern.matcher(raw);
		while (matcher.find()) {
			String match = matcher.group();
			int index = Integer.valueOf(match.substring(1, match.length() - 1));
			if (args.length - 1 >= index) {
				raw = raw.replace(match, args[index]);
			} else {
				raw = raw.replace(match, "");
			}
		}

		matcher = varPatternInfinite.matcher(raw);
		while (matcher.find()) {
			String match = matcher.group();
			int index = Integer.valueOf(match.substring(1, match.length() - 2));
			if (args.length - 1 >= index) {
				raw = raw.replace(match, StringUtils.toString(args, index, " "));
			} else {
				raw = raw.replace(match, "");
			}
		}

		matcher = varPatternRange.matcher(raw);
		while (matcher.find()) {
			String match = matcher.group();
			int index = Integer.valueOf(match.substring(1, match.indexOf("-")));
			int end = Integer.valueOf(match.substring(match.indexOf("-") + 1, match.length() - 1));
			System.out.println("index = '" + index + "'");
			System.out.println("end = '" + end + "'");
			if (args.length - 1 >= index) {
				raw = raw.replace(match, StringUtils.toString(args, index, end, " "));
			} else {
				raw = raw.replace(match, "");
			}
		}

		matcher = lineBreakPattern.matcher(raw);
		raw = matcher.replaceAll(System.getProperty("line.separator"));

		return raw;
	}
}
