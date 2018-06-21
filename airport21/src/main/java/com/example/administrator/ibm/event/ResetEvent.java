package com.ubtech.airport.ibm.event;

/**
 * Created by Administrator on 2017/8/3.
 */

public class ResetEvent extends Event {

	public static final int DEFAULT_RET_CODE = -1;
	public int retcode = DEFAULT_RET_CODE;
	public Object message;
}
