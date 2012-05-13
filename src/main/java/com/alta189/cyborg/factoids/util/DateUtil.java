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

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class DateUtil {

	private static final DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss.SSS z");

	static {
		formatter.setTimeZone(TimeZone.getTimeZone("GMT"));
	}

	public static Date longToDate(long timestamp) {
		Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("GMT"));
		cal.setTime(new Date(timestamp));
		return cal.getTime();
	}

	public static long datetoLong(Date date) {
		return date.getTime();
	}

	public static Date getTodayGMT() {
		return Calendar.getInstance(TimeZone.getTimeZone("GMT")).getTime();
	}

	public static long getTodayGMTTimestamp() {
		return Calendar.getInstance(TimeZone.getTimeZone("GMT")).getTimeInMillis();
	}

	public static String getFormattedGMTDate(long timestamp) {
		Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("GMT"));
		cal.setTimeInMillis(timestamp);
		return formatter.format(cal.getTime());
	}

	public static String getFormattedGMTDate(Date date) {
		Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("GMT"));
		cal.setTime(date);
		return formatter.format(cal.getTime());
	}

}