package com.ubtech.airport.ibm.event;

/**
 * Created by Administrator on 2017/7/22.
 */

public class MoveEvent extends Event {
	public String message;
	public int retcode = -1;

	public MoveEvent() {
	}

	public MoveEvent(String msg) {
		this.message = msg;
	}
}
