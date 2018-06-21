package com.ubtech.airport.ibm.utils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Created by Administrator on 2017/6/30.
 */

public class SystemPropertiesHelper {
	public final static String HARD_VERSION = "ro.hardware.version";
	public final static String AUDIO_DIGITAL = "persist.sys.audiodigital";

	public static String get(String key) {
		String result = "";
		try {
			Class<?> c = Class.forName("android.os.SystemProperties");

			Method get = c.getMethod("get", String.class);
			result = (String) get.invoke(c, key);
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return result;
	}

	public static void setString(String key, String val) {
		try {
			Class<?> c = Class.forName("android.os.SystemProperties");
			Method set = c.getMethod("set", String.class, String.class);
			set.invoke(c, key, val);
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}
