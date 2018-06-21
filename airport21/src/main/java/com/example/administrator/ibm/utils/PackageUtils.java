/*
 *
 *  *
 *  * Copyright (c) 2008-2016 UBT Corporation.  All rights reserved.  Redistribution,
 *  *  modification, and use in source and binary forms are not permitted unless otherwise authorized by UBT.
 *  *
 *
 */

package com.ubtech.airport.ibm.utils;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public class PackageUtils {

	public static List<PackageInfo> getInstalledApps(Context context) {
		PackageManager pManager = context.getPackageManager();
		List<PackageInfo> apps = pManager.getInstalledPackages(0);
		return apps;
	}


	public static String getMetaStrValue(Context context, String pckName,
                                         String key) {
		String value = null;
		ApplicationInfo appInfo = null;
		try {
			appInfo = context.getPackageManager().getApplicationInfo(pckName,
					PackageManager.GET_META_DATA);
			Bundle bundle = appInfo.metaData;
			if (bundle != null) {
				value = bundle.getString(key);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return value;
	}

	public static int getMetaIntValue(Context context, String pckName,
                                      String key) {
		int appId = -1;
		ApplicationInfo appInfo = null;
		try {
			appInfo = context.getPackageManager().getApplicationInfo(pckName,
					PackageManager.GET_META_DATA);
			Bundle bundle = appInfo.metaData;
			if (bundle != null) {
				appId = bundle.getInt(key);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return appId;
	}

	public static String getAppInfo(Context context) {
		try {
			String pkName = context.getPackageName();
			String versionName = context.getPackageManager().getPackageInfo(
					pkName, 0).versionName;
			int versionCode = context.getPackageManager().getPackageInfo(
					pkName, 0).versionCode;
			return pkName + " " + versionName + " " + versionCode;
		} catch (Exception e) {
		}
		return null;
	}

	public static String getBuildTime(Context context) {
		try {
			ApplicationInfo ai = context.getApplicationInfo();
			ZipFile zf = new ZipFile(ai.sourceDir);
			ZipEntry ze = zf.getEntry("classes.dex");
			long time = ze.getTime();
			zf.close();
			SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
			Date dt = new Date(time);
			return sdf.format(dt);
		} catch (Exception e) {
		}
		return "";
	}

	public static String getMapName(String path) {
		Log.i("dan", "path=" + path);
		String name = "";
		if (!TextUtils.isEmpty(path)) {
			name = path.substring(path.lastIndexOf("/")+1);
		}
		return name;
	}
}
