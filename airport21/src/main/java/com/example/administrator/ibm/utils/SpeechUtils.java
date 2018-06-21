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

import java.util.Locale;

/**
 * @author paul.zhang@ubtrobot.com
 * @date 2016/12/27
 * @Description 工具类、主要提供静态方法
 * @modifier
 * @modify_time
 */

public class SpeechUtils {
	private static final int PI = 180;
	/**
	 * @Description 获取当前系统语言
	 * @param 上下文
	 * @return 当前系统语言
	 * @throws
	 */

	public static String getSystemLanguage(Context context) {
		Locale locale = context.getResources().getConfiguration().locale;
		String language = locale.getLanguage();
		return language;
	}
	/**
	 * @Description 根据当前角度获取相应弧度
	 * @param 角度
	 * @return 弧度 pi
	 * @throws
	 */

	public static float getRadian(int angle) {
		return (float) (angle * (Math.PI / 180));
	}
	/**
	 * @Description 根据当前弧度获取相应角度
	 * @param 角度
	 * @return 弧度 pi
	 * @throws
	 */

	public static float getAngle(float radian) {
		return (float) ((radian * 180 )/ Math.PI);
	}
}
