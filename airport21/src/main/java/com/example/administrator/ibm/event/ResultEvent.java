package com.ubtech.airport.ibm.event;

/**
 * Created by Administrator on 2017/4/28.
 */

public class ResultEvent extends Event {

	public static final int ANDROID_WIFI_IP = 0;
	public static final int ROS_WIFI_IP = 1;
	public static final int OHTERS = 2;
	public static final int DEFAULT_RET_CODE = -1;

	public static final int TO_IBM = 999;
	public static final int RECEIVE_IBM = 1000;
	public static final int SPEEK_TTS_END = -2;//说话结束
	public static final int SING_PLAY_FRONT_1 = 998;//歌曲环节播放第一首歌曲之前
	public static final int SING_PLAY_FRONT_2 = 997;//歌曲环节播放第2首歌曲之前
	public static final int DANCE_FRONT_1 = 996;//跳舞环节1之前
	public static final int DANCE_FRONT_2 = 995;//跳舞环节2之前
	public static final int START_AES_END = 994;//录音结束 (用于录音结束后再次开始录音)
	public static final int START_AES_BEGIN = 991;//录音结束 (用于录音结束后再次开始录音)
	public static final int STOP_SEND_TO_IBM = 993;//开始播放歌曲(跳舞,机器人说话时),需要暂停录音功能或者录音内容不发送给ibm
	public static final int SPEEK_INIT_START = 992;//初始化开始
	public static final int SPEEK_ASR_ERR = 990;//录音返回异常
	public static final int STEP_1 = 01001;//
	public static final int START_SPEEK = 989;//开始说话


	public int type = OHTERS;
	public int retcode = DEFAULT_RET_CODE;
	public Object message;
}
