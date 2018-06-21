package com.ubtech.airport.ibm.utils;

import android.content.Context;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/5/26.
 */

public class ConfigReader {
	public static String read(Context context, String name) {
		StringBuilder result = new StringBuilder();
		try {
			InputStream ip = context.getAssets().open(name);
			InputStreamReader inReader = new InputStreamReader(ip, "UTF-8");
			BufferedReader br = new BufferedReader(inReader);
			String s = null;
			while ((s = br.readLine()) != null) {
				result.append(System.lineSeparator() + s);
			}
			br.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result.toString();
	}
	public static List<String> readlist(Context context, String name) {
		List<String> list = new ArrayList<>();
		try {
			InputStream ip = context.getAssets().open(name);
			InputStreamReader inReader = new InputStreamReader(ip, "UTF-8");
			BufferedReader br = new BufferedReader(inReader);
			String s = null;
			while ((s = br.readLine()) != null) {
				list.add(s);
			}
			br.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}
}
