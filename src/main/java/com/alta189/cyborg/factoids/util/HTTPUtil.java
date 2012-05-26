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

import com.google.gson.Gson;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

public class HTTPUtil {
	
	public static final String hastebin = "http://hastebin.com/documents";
	
	public static String readURL(String url) {
		HttpGet request = new HttpGet(url);
		HttpClient httpClient = new DefaultHttpClient();
		try {
			HttpResponse response = httpClient.execute(request);
			return EntityUtils.toString(response.getEntity());
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static String readRandomLine(String url) {
		HttpGet request = new HttpGet(url);
		HttpClient httpClient = new DefaultHttpClient();
		try {
			HttpResponse response = httpClient.execute(request);
			BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
			List<String> lines = new ArrayList<String>();
			String line = "";
			while ((line = rd.readLine()) != null) {
				lines.add(line);
			}
			
			if (lines.size() > 1) {
				int i = randomNumber(0, lines.size() + 1);
				return lines.get(i);
			} else if (lines.size() == 1) {
				return lines.get(0);
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static String hastebin(String data)  {
		HttpClient client = new DefaultHttpClient();
		HttpPost post = new HttpPost(hastebin);

		try {
			post.setEntity(new StringEntity(data));

			HttpResponse response = client.execute(post);

			String result = EntityUtils.toString(response.getEntity());
			return "http://hastebin.com/" + new Gson().fromJson(result, Hastebin.class).getKey();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	private static int randomNumber(int min, int max) {
		return min + (new Random()).nextInt(max-min);
	}

}
