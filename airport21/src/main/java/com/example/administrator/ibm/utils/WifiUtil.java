package com.ubtech.airport.ibm.utils;

import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;

/**
 * Created by Administrator on 2017/4/26.
 */

public class WifiUtil {
	public static String[] getWifiIP(Context context) {
		String[] info = new String[2];
		WifiManager wifiManager = (WifiManager) context
				.getSystemService(Context.WIFI_SERVICE);
		if (wifiManager.isWifiEnabled()) {
			WifiInfo wifiInfo = wifiManager.getConnectionInfo();
			int i = wifiInfo.getIpAddress();
			String ssid = wifiInfo.getSSID();
			String ip = (i & 0xFF) + "." + ((i >> 8) & 0xFF) + "." + ((i >> 16) & 0xFF)
					+ "." + (i >> 24 & 0xFF);
			info[0]=ip;
			info[1]=ssid;
		}
		return info;
	}
}
