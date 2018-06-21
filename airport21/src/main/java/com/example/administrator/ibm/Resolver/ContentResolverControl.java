package com.ubtech.airport.ibm.Resolver;

import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;

import java.util.ArrayList;

/**
 * Created by Administrator on 2017/5/11.
 */

public class ContentResolverControl {
	//pointModel表的字段
	public static final String Map_NAME = "mapName";
	public static final String NAME = "pointName";
	public static final String X = "map_x";
	public static final String Y = "map_y";
	public static final String CONTENT = "description";
	public static final String POINT_TYPE = "pointType";
	private static final Uri ALL_POINT_URI = Uri.parse("content://com.ubtechinc.cruzr.map.positionProvider/position");
	private String[] wayGuideClomuns = {NAME, X, Y, CONTENT};
	//travelModel表的字段
	public static final String PATH_POINTS = "path_points";

	public ArrayList<Location> getUnSortWGPoints(ContentResolver resolver, String curMap) {
		ArrayList<Location> locs = new ArrayList<>();
		Location loc;
		String[] args = {"travel_position", curMap};
		Cursor cursor = resolver.query(ALL_POINT_URI, wayGuideClomuns, POINT_TYPE + " = ? and " + Map_NAME + "= ?", args, null);
		if (cursor != null && cursor.moveToFirst()) {
			while (!cursor.isAfterLast()) {
				loc = new Location();
				loc.setName(cursor.getString(cursor.getColumnIndexOrThrow(NAME)));
				loc.setX(stringToFloat(cursor.getString(cursor.getColumnIndexOrThrow(X))));
				loc.setY(stringToFloat(cursor.getString(cursor.getColumnIndexOrThrow(Y))));
				loc.setContent(cursor.getString(cursor.getColumnIndexOrThrow(CONTENT)));
				locs.add(loc);
				cursor.moveToNext();
			}
			cursor.close();
		}
		return locs;
	}

	private float stringToFloat(String s) {
		return Float.parseFloat(s);
	}
}
