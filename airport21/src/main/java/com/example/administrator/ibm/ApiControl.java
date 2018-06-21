package com.ubtech.airport.ibm;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.ubtech.airport.ibm.event.ResultEvent;
import com.ubtech.airport.ibm.event.StatusEvent;
import com.ubtech.airport.ibm.rxbus.RxBus;
import com.ubtechinc.cruzr.sdk.face.CruzrFaceApi;
import com.ubtechinc.cruzr.sdk.face.FaceInfo;
import com.ubtechinc.cruzr.sdk.ros.RosConstant;
import com.ubtechinc.cruzr.sdk.ros.RosRobotApi;
import com.ubtechinc.cruzr.sdk.speech.SpeechRobotApi;
import com.ubtechinc.cruzr.serverlibutil.interfaces.SpeechASRListener;
import com.ubtechinc.cruzr.serverlibutil.interfaces.SpeechTtsListener;

import java.util.List;
import java.util.Random;

/**
 * Created by Administrator on 2017/4/28.
 */

public class ApiControl {
    private Context context;
    private static final double M_PI = 3.14159265358979323846;

    private static final double STEP = 30 * M_PI / 180;
    private static final float TIME = 1;
    /**
     * 腰10，350；
     **/
    private static final String LEFT_RUGIT = "HeadYaw";
    private static final double LEFT_RIGHT_MIN = 10 * M_PI / 180;//left
    private static final double LEFT_RIGHT_MAX = 350 * M_PI / 180;//right

    private boolean isEnergyReleased = false;

    public ApiControl(Context context) {
        this.context = context;
    }

//	public void getWifiIp() {
//		String[] info = WifiUtil.getWifiIP(context);
//		ResultEvent event = new ResultEvent();
//		event.type = ResultEvent.ANDROID_WIFI_IP;
//		event.message = info;
//		RxBus.getDefault().post(event);
//	}

    public void setRosIp() {
        String ip = RosRobotApi.get().getRosWifiIp();
        ResultEvent event = new ResultEvent();
        event.type = ResultEvent.ROS_WIFI_IP;
        event.message = ip;
        RxBus.getDefault().post(event);

    }

    public void setEnergyReleased(boolean energyReleased) {
        isEnergyReleased = energyReleased;
    }

    public boolean isEnergyReleased() {
        return isEnergyReleased;
    }

    public void moveForward() {
        int code = RosRobotApi.get().moveToward(0.2f, 0, 0);
        ResultEvent event = new ResultEvent();
        event.retcode = code;
        event.message = "前进返回 session_id =";
        RxBus.getDefault().post(event);
    }

//    public void moveBackward() {
//        int code = RosRobotApi.get().moveToward(-0.2f, 0, 0, new RemoteCommonListener() {
//
//            @Override
//            public void onResult(int sectionId, int status) {
//                Log.i("paul", "后退状态:" + status);
//                ResultEvent event = new ResultEvent();
//                event.retcode = status;
//                event.message = "后退状态=";
//                RxBus.getDefault().post(event);
//            }
//        });
//        ResultEvent event = new ResultEvent();
//        event.retcode = code;
//        event.message = "后退返回 session_id =";
//        RxBus.getDefault().post(event);
//    }

//    public void leftAround() {
//        int code = RosRobotApi.get().moveToward(0, 0, 0.2f, new RemoteCommonListener() {
//
//            @Override
//            public void onResult(int sectionId, int status) {
//                Log.i("paul", "左转状态:" + status);
//                ResultEvent event = new ResultEvent();
//                event.retcode = status;
//                event.message = "左转状态=";
//                RxBus.getDefault().post(event);
//            }
//        });
//        ResultEvent event = new ResultEvent();
//        event.retcode = code;
//        event.message = "左转返回 session_id =";
//        RxBus.getDefault().post(event);
//    }

//    public void rightAround() {
//        int code = RosRobotApi.get().moveToward(0, 0, -0.2f, new RemoteCommonListener() {
//
//            @Override
//            public void onResult(int sectionId, int status) {
//                Log.i("paul", "右转状态:" + status);
//                ResultEvent event = new ResultEvent();
//                event.retcode = status;
//                event.message = "右转状态=";
//                RxBus.getDefault().post(event);
//            }
//        });
//        ResultEvent event = new ResultEvent();
//        event.retcode = code;
//        event.message = "右转状态=";
//        RxBus.getDefault().post(event);
//    }

//    public void leftMove() {
//        int code = RosRobotApi.get().moveToward(0, 0.2f, 0, new RemoteCommonListener() {
//
//            @Override
//            public void onResult(int sectionId, int status) {
//                Log.i("paul", "返回状态:" + status);
//                ResultEvent event = new ResultEvent();
//                event.retcode = status;
//                event.message = "返回状态=";
//                RxBus.getDefault().post(event);
//            }
//        });
//        ResultEvent event = new ResultEvent();
//        event.retcode = code;
//        event.message = "左移返回 session_id =";
//        RxBus.getDefault().post(event);
//    }

//    public void rightMove() {
//        int code = RosRobotApi.get().moveToward(0, -0.2f, 0, new RemoteCommonListener() {
//            @Override
//            public void onResult(int sectionId, int status) {
//                Log.i("paul", "返回:" + status);
//                ResultEvent event = new ResultEvent();
//                event.retcode = status;
//                event.message = "返回状态=";
//                RxBus.getDefault().post(event);
//            }
//        });
//        ResultEvent event = new ResultEvent();
//        event.retcode = code;
//        event.message = "右移返回 session_id =";
//        RxBus.getDefault().post(event);
//    }

    public void pauseMove() {
        int code = RosRobotApi.get().stopMove();
        ResultEvent event = new ResultEvent();
        event.retcode = code;
        event.message = "停止返回 session_id =";
        RxBus.getDefault().post(event);

    }

    public void run() {
        int code = RosRobotApi.get().run("hug");
        ResultEvent event = new ResultEvent();
        event.retcode = code;
        event.message = "执行\"hug\" session_id =";
        RxBus.getDefault().post(event);
    }

    public void ledflash() {
        int code = RosRobotApi.get().ledSetWorkByTimes(RosConstant.Effect.LED_FLICK, 4, RosConstant.Color.GREEN, 2);
        ResultEvent event = new ResultEvent();
        event.retcode = code;
        event.message = "LED闪灯session_id =";
        RxBus.getDefault().post(event);
    }

    public void poweroff() {
        int code = RosRobotApi.get().powerOffRos(1);
        ResultEvent event = new ResultEvent();
        event.retcode = code;
        event.message = "执行\"1\"session_id =";
        RxBus.getDefault().post(event);
    }

    public void openPS3joy() {
        int code = RosRobotApi.get().startPS3joy();
        ResultEvent event = new ResultEvent();
        event.retcode = code;
        event.message = "执行PS3joy session_id =";
        RxBus.getDefault().post(event);
    }

    public void zeroSetting() {
        int code = RosRobotApi.get().zeroSetting();
        ResultEvent event = new ResultEvent();
        event.retcode = code;
        event.message = "零点设置 session_id =";
        RxBus.getDefault().post(event);
    }

    public void TtsPlay(String text, final int type) {
        Log.d("print", "TtsPlay: ");
        if (SpeechRobotApi.get().isTtsSpeaking()) {
            SpeechRobotApi.get().speechStopTTS();
        }
        ResultEvent event = new ResultEvent();
        event.type = ResultEvent.START_SPEEK;
        event.message = "开始说话";
        RxBus.getDefault().post(event);

        SpeechRobotApi.get().speechStartTTS(text, new SpeechTtsListener() {
            @Override
            public void onAbort() {

            }

            @Override
            public void onEnd() {
                Log.d("print", "onEnd: 播放结束" );
                ResultEvent event = new ResultEvent();
                event.type = type;
                event.message = "播放结束";
                RxBus.getDefault().post(event);
            }
        });
    }

    public void TtsStop() {
        SpeechRobotApi.get().speechStopTTS();
        ResultEvent event = new ResultEvent();
        event.message = "停止播报";
        RxBus.getDefault().post(event);
    }

    public void calldispatch() {
        //无需调用api，主服务自带此功能
    }

    /**
     * 开始说话
     */
    public void startAsr() {
        Log.d("print", "startAsr: ");
        SpeechRobotApi.get().startSpeechASR(4542, new SpeechASRListener() {
            StringBuilder stringBuilder = new StringBuilder();
            String string,lastStr="";
            @Override
            public void onVolumeChanged(int i) {
//                Log.d("print", "onVolumeChanged: " + i);
            }

            @Override
            public void onBegin() {
                Log.i("dan", "apirunner开始说话");
                ResultEvent event = new ResultEvent();
                event.message = "开始说话";
                event.type = ResultEvent.START_AES_BEGIN;
                RxBus.getDefault().post(event);
            }

            @Override
            public void onEnd() {
                Log.i("dan", "apirunner结束说话");
                ResultEvent event = new ResultEvent();
                event.message = stringBuilder.toString();

				event.type = ResultEvent.TO_IBM;
				if (!TextUtils.isEmpty(stringBuilder.toString())){
					RxBus.getDefault().post(event);
				}
				ResultEvent eventEnd = new ResultEvent();
                eventEnd.message = "apirunner结束说话";
                eventEnd.type = ResultEvent.START_AES_END;
                RxBus.getDefault().post(eventEnd);
            }

            @Override
            public void onResult(String text, boolean isLast) {
                Log.i("print 识别结果", text + "  " + isLast);
                ResultEvent event = new ResultEvent();
//                stringBuilder.append(text);
                if (!TextUtils.isEmpty(text) && !lastStr.equals(text)){
                    string = text;
                    stringBuilder.append(text);
                }
                if(isLast){
                    Log.i("onResult","结果:"+text);
                }
                lastStr = text;
//                if (isLast) {
//                    event.message = stringBuilder.toString();
//                    event.type = ResultEvent.TO_IBM;
//                    if (!TextUtils.isEmpty(stringBuilder.toString())) {
//                        RxBus.getDefault().post(event);
//                    }
//                }
            }

            @Override
            public void onError(int code) {
                ResultEvent event = new ResultEvent();
                event.message = "识别异常,返回码:" + code;
//                event.type = ResultEvent.START_AES_END;
                event.type = ResultEvent.SPEEK_ASR_ERR;
                RxBus.getDefault().post(event);
            }

            @Override
            public void onIllegal() {
                Log.d("print", "onIllegal:出现非法调用 ");
            }
        });
    }

    public void setface() {
        List<FaceInfo> faces = App.app.getFaces();
        ResultEvent event = new ResultEvent();
        if (faces != null && faces.size() > 0) {
            int size = faces.size();
            Random ran = new Random();
            int index = ran.nextInt(size);

            CruzrFaceApi.setCruzrFace(null, faces.get(index).faceId, true, true);
            event.message = faces.get(index).faceId + "设置表情:" + faces.get(index).faceDesc;
        } else {
            event.message = "没有获取到表情数据";
        }
        RxBus.getDefault().post(event);
    }

//	public void navgation(List<Location> list) {
//		ResultEvent event = new ResultEvent();
//		if (list != null && list.size() > 0) {
//			RosRobotApi.get().navigateTo(list.get(0).getX(), list.get(0).getY(), 0, 0.1f);
//			event.message = "正在导航到:" + list.get(0).getName();
//		} else {
//			event.message = "没有数据";
//		}
//		RxBus.getDefault().post(event);
//
//
//	}

    public void cancelNavgation() {
        ResultEvent event = new ResultEvent();
        RosRobotApi.get().cancelNavigate();
        event.message = "取消导航";
        RxBus.getDefault().post(event);
    }

    public void leftWaist() {
        Log.i("dan", "左扭腰");
        double curAngle = 0;
        double nextAngle = 0;
        curAngle = RosRobotApi.get().getJointCurrentAngle(LEFT_RUGIT);
        Log.d("dan", "get curAngle:" + curAngle);
        nextAngle = curAngle - STEP;
        Log.d("dan", "Waist control6 curAngle:" + curAngle);
        Log.d("dan", "Waist control7 nextAngle:" + nextAngle);
        if (nextAngle < LEFT_RIGHT_MIN) {
            nextAngle = LEFT_RIGHT_MIN;
            Log.d("dan", "Waist left outof range ");
        }
        ResultEvent event = new ResultEvent();
        event.message = "腰部向左转:" + nextAngle;
        RxBus.getDefault().post(event);
        move(LEFT_RUGIT, (float) nextAngle);
    }

    public void rightWaist() {
        Log.i("dan", "右扭腰");
        double curAngle = 0;
        double nextAngle = 0;
        try {
            curAngle = RosRobotApi.get().getJointCurrentAngle(LEFT_RUGIT);
            Log.d("dan", "get curAngle:" + curAngle);
        } catch (Exception e) {
            e.printStackTrace();
        }
        nextAngle = curAngle + STEP;
        Log.d("dan", "Waist curAngle:" + curAngle);
        Log.d("dan", "Waist control7 nextAngle:" + nextAngle);
        if (nextAngle > LEFT_RIGHT_MAX) {
            nextAngle = LEFT_RIGHT_MAX;
            Log.d("dan", "Waist right outof range ");
        }
        ResultEvent event = new ResultEvent();
        event.message = "腰部向右转:" + nextAngle;
        RxBus.getDefault().post(event);
        move(LEFT_RUGIT, (float) nextAngle);
    }

    private void move(String name, float angle) {
        RosRobotApi.get().setAngles(name, angle, TIME);
    }

    public void servoEnergyRelease(int cmdid) {

        int sessionid = RosRobotApi.get().servoEnergyRelease();
        StatusEvent event = new StatusEvent();
        event.session_id = sessionid;
        event.cmd_id = cmdid;
        RxBus.getDefault().post(event);
    }

//	public void switchToEnglish() {
//		Intent it = new Intent(AppConstants.SWITCH_EN_ENGINE);
//		context.sendOrderedBroadcast(it, null);
//		ResultEvent event = new ResultEvent();
//		event.message = "发送英文引擎切换";
//		RxBus.getDefault().post(event);
//	}

//	public void switchToChinese() {
//		Intent it = new Intent(AppConstants.SWITCH_CN_ENGINE);
//		context.sendOrderedBroadcast(it, null);
//		ResultEvent event = new ResultEvent();
//		event.message = "发送中文引擎切换";
//		RxBus.getDefault().post(event);
//	}

//	public void switchDigital(Button btn, CmdBean.Cmds cmd) {
//		boolean isdigital = "true".equals(SystemPropertiesHelper.get(SystemPropertiesHelper.AUDIO_DIGITAL));
//
//		setPropertiy(!isdigital, btn, cmd);
//	}

//	private void setPropertiy(final boolean isdigital, final Button btn, final CmdBean.Cmds cmd) {
//		Observable.just("")
//				.map(new Func1<String, String>() {
//					@Override
//					public String call(String aBoolean) {
//						return SystemPropertiesHelper.get(SystemPropertiesHelper.HARD_VERSION);
//					}
//				})
//				.filter(new Func1<String, Boolean>() {
//					@Override
//					public Boolean call(String s) {
//						ResultEvent event = new ResultEvent();
//						event.message = "读取硬件版本号(仅\"00\"开头的不能设置):" + s;
//						RxBus.getDefault().post(event);
//						return !TextUtils.isEmpty(s) && !s.startsWith("00");
//					}
//				})
//				.observeOn(AndroidSchedulers.mainThread())
//				.subscribe(new Action1<String>() {
//					@Override
//					public void call(String s) {
//						SystemPropertiesHelper.setString(SystemPropertiesHelper.AUDIO_DIGITAL, String.valueOf(isdigital));
//					}
//				}, new Action1<Throwable>() {
//					@Override
//					public void call(Throwable throwable) {
//
//					}
//				}, new Action0() {
//					@Override
//					public void call() {
//						ResultEvent event = new ResultEvent();
//						event.message = "设置之后结果（true:数字|false:模拟）:" + SystemPropertiesHelper.get(SystemPropertiesHelper.AUDIO_DIGITAL);
//						String reslut = SystemPropertiesHelper.get(SystemPropertiesHelper.AUDIO_DIGITAL);
//						RxBus.getDefault().post(event);
//						cmd.setName("true".equals(reslut) ? "当前:数字" : "当前:模拟");
//						btn.setText(cmd.getName());
//					}
//				});
//	}

    public void dockOnStation() {
        RosRobotApi.get().dockOnStation();
    }
}
