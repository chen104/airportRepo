package com.ubtech.airport.ibm;

import android.content.Context;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.ubtech.airport.ibm.Resolver.ContentResolverControl;
import com.ubtech.airport.ibm.Resolver.Location;
import com.ubtech.airport.ibm.config.DataConfig;
import com.ubtech.airport.ibm.event.ResultEvent;
import com.ubtech.airport.ibm.event.StatusEvent;
import com.ubtech.airport.ibm.rxbus.RxBus;
import com.ubtech.airport.ibm.utils.PackageUtils;
import com.ibm.watson.developer_cloud.conversation.v1.model.InputData;
import com.ibm.watson.developer_cloud.conversation.v1.model.MessageOptions;
import com.ibm.watson.developer_cloud.conversation.v1.model.MessageResponse;
import com.ubtechinc.cruzr.sdk.ros.RosConstant;
import com.ubtechinc.cruzr.sdk.ros.RosRobotApi;
import com.ubtechinc.cruzr.sdk.speech.SpeechRobotApi;
import com.ubtechinc.cruzr.serverlibutil.interfaces.RemoteCommonListener;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.subscriptions.CompositeSubscription;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Button step_1, step_2, step_3, step_4, step_5, step_6, step_7, step_8, step_9, step_10, step_11, step_12;
    private Button dance1, dance2, dance3, dance4, dance5, dance6, dance7, dance8,listener,to_adim;
    private String TAG = "print";
    private long time, startTime;
    private ApiControl control;

    private ContentResolverControl contentResolverControl;
    private CompositeSubscription subscriptions = new CompositeSubscription();
    private HashMap<Integer, Integer> cmdmaps = new HashMap<>();
    ArrayList<Location> listlocation;
//    private CmdBean bean;

    private MediaPlayer mp;//mediaPlayer对象

    private Subscription dances;
    ArrayList<String[]> dancelist1 = new ArrayList<>();
    ArrayList<String[]> dancelist2 = new ArrayList<>();

    //    private String[] dance2 = {"yankee1", "yankee1", "danceend"};
//    private String[] dance3 = {"arabia1", "arabia2", "arabia1", "danceend"};
//    private String[] dance4 = {"spanish1", "spanish2", "danceend"};
    public static final float PI_ = 3.141592653589793f;

    private boolean isSend = true;
    private Context context;
    private TextView tv_statu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d("print", "onCreate: ");
        control = new ApiControl(this);
        context = this;
        step_1 = (Button) findViewById(R.id.step_1);
        step_2 = (Button) findViewById(R.id.step_2);
        step_3 = (Button) findViewById(R.id.step_3);
        step_4 = (Button) findViewById(R.id.step_4);
        step_5 = (Button) findViewById(R.id.step_5);
        step_6 = (Button) findViewById(R.id.step_6);
        step_7 = (Button) findViewById(R.id.step_7);
        step_8 = (Button) findViewById(R.id.step_8);
        step_9 = (Button) findViewById(R.id.step_9);
        step_10 = (Button) findViewById(R.id.step_10);
        step_11 = (Button) findViewById(R.id.step_11);
        step_12 = (Button) findViewById(R.id.step_12);
        dance1 = (Button) findViewById(R.id.dance1);
        dance2 = (Button) findViewById(R.id.dance2);
        dance3 = (Button) findViewById(R.id.dance3);
        dance4 = (Button) findViewById(R.id.dance4);
        dance5 = (Button) findViewById(R.id.dance5);
        dance6 = (Button) findViewById(R.id.dance6);
        dance7 = (Button) findViewById(R.id.dance7);
        dance8 = (Button) findViewById(R.id.dance8);
        listener = (Button) findViewById(R.id.listener);
        to_adim = (Button) findViewById(R.id.to_adim);
        tv_statu = (TextView) findViewById(R.id.tv_statu);

        step_1.setOnClickListener(this);
        step_2.setOnClickListener(this);
        step_3.setOnClickListener(this);
        step_4.setOnClickListener(this);
        step_5.setOnClickListener(this);
        step_6.setOnClickListener(this);
        step_7.setOnClickListener(this);
        step_8.setOnClickListener(this);
        step_9.setOnClickListener(this);
        step_10.setOnClickListener(this);
        step_11.setOnClickListener(this);
        step_12.setOnClickListener(this);
        dance1.setOnClickListener(this);
        dance2.setOnClickListener(this);
        dance3.setOnClickListener(this);
        dance4.setOnClickListener(this);
        dance5.setOnClickListener(this);
        dance6.setOnClickListener(this);
        dance7.setOnClickListener(this);
        dance8.setOnClickListener(this);
        listener.setOnClickListener(this);
        to_adim.setOnClickListener(this);
        initResultEventObserver();
        initStatusEventObserver();
        initcontent();
//        App.RobotInit(getApplicationContext());

//        App.RobotInit(this);
//        registfeedback();

//        SpeechRobotApi.get().speechSetMic(true);
    }


    private void initResultEventObserver() {
        subscriptions.add(RxBus.getDefault().toObservable(ResultEvent.class)
                //	.sample(100, TimeUnit.MILLISECONDS)
                .onBackpressureBuffer()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<ResultEvent>() {
                    @Override
                    public void call(ResultEvent resultEvent) {
                        Log.i("dan", Thread.currentThread().getName() + "" + resultEvent.retcode);
                        handleEvent(resultEvent);
                    }
                }));
    }

    private void initStatusEventObserver() {
        subscriptions.add(RxBus.getDefault().toObservable(StatusEvent.class)
                .onBackpressureBuffer()
                .observeOn(AndroidSchedulers.mainThread())
                .filter(new Func1<StatusEvent, Boolean>() {
                    @Override
                    public Boolean call(StatusEvent statusEvent) {
                        return !cmdmaps.containsKey(statusEvent.session_id);
                    }
                })
                .subscribe(new Action1<StatusEvent>() {
                    @Override
                    public void call(StatusEvent statusEvent) {
                        cmdmaps.put(statusEvent.session_id, statusEvent.cmd_id);
                    }
                }));
    }

    private void handleEvent(ResultEvent event) {
        Object message = event.message;
        int retcode = event.retcode;
        Log.d(TAG, "handleEvent: " + message);
        switch (event.type) {
            case ResultEvent.TO_IBM:
                Log.d(TAG, "handleEvent: " + isSend);
                if (!SpeechRobotApi.get().isTtsSpeaking()&& isSend) {
                    String comment = (String) message;
                    Log.d(TAG, "handleEvent: send " + message);
                    senMessageToIBM(comment);
                }
                break;
            case ResultEvent.SING_PLAY_FRONT_1:
                isSend = false;
                playMusic(R.raw.sing_1);
                break;
            case ResultEvent.SING_PLAY_FRONT_2:
                isSend = false;
                playMusic(R.raw.sing_2);
                break;
            case ResultEvent.DANCE_FRONT_1:
                isSend = false;
                dances1();
                break;
            case ResultEvent.DANCE_FRONT_2:
                isSend = false;
                dances2();
                break;
            case ResultEvent.START_AES_END:
                break;
            case ResultEvent.SPEEK_INIT_START:
//                control.startAsr();
                break;
            case ResultEvent.SPEEK_TTS_END:
                break;
            case ResultEvent.SPEEK_ASR_ERR:
                break;
            case ResultEvent.START_AES_BEGIN:
                Toast.makeText(context, "开始聆听", Toast.LENGTH_SHORT).show();
                break;
            case ResultEvent.STEP_1:

                break;
            case ResultEvent.START_SPEEK://开始说话
                break;
        }
//
////        if (comment.contains("你好")) {
////            control.TtsPlay("主人你好，我已准备就绪，很荣幸认识你");
////        }

//        switch (event.type) {
//            case ResultEvent.ANDROID_WIFI_IP:
//                String[] info = (String[]) message;
//                String ip = info[0];
//                String ssid = info[1];
//                if (!TextUtils.isEmpty(ip)) {
//                    String origin = "当前WIFI(" + ssid + ")的IP:" + ip;
//                    SpannableString spa = new SpannableString(origin);
//                    int start = origin.indexOf("(") + 2;
//                    int end = origin.indexOf(")的IP:") - 1;
//                    spa.setSpan(new ForegroundColorSpan(Color.GREEN), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
//                    end += 6;
//                    spa.setSpan(new ForegroundColorSpan(Color.GREEN), end, spa.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
////                    wifiip.setText(spa);
//                } else {
////                    wifiip.setText("当前WIFI(" + ssid + ")ip无法获取");
//                }
//                break;
//            case ResultEvent.ROS_WIFI_IP:
//                if (!TextUtils.isEmpty((String) message)) {
//                    String origin = "当前ROS系统ip:" + message;
//                    SpannableString spa = new SpannableString("当前ROS系统ip:" + message);
//                    int start = origin.indexOf(":") + 1;
//                    spa.setSpan(new ForegroundColorSpan(Color.GREEN), start, origin.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
////                    rosip.setText(spa);
//                } else {
////                    rosip.setText("当前ROS系统ip无法获取");
//                }
//                break;
//            case ResultEvent.OHTERS:
//                String msg = (String) message;
//                if (retcode != ResultEvent.DEFAULT_RET_CODE) {
//                    msg += retcode;
//                }
//                addconsole(msg);
//                break;
//            case ResultEvent.TO_IBM:
//                String comment = (String) message;
//                Log.d(TAG, "handleEvent: send " + message);
//                senMessageToIBM(comment);
//                break;
//            default:
//
//                break;
//        }
    }

    public void playMusic(int resid) {
//        SpeechRobotApi.get().stopSpeechASR();
        try {
//            mp.reset();
            mp = MediaPlayer.create(this, resid);//重新设置要播放的音频
            mp.start();//开始播放
        } catch (Exception e) {
            e.printStackTrace();//输出异常信息
        }
        mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {

            @Override
            public void onCompletion(MediaPlayer arg0) {
                // TODO 歌曲播放完成 重新开始录音
                isSend = true;
            }
        });
    }


    private void senMessageToIBM(final String message) {
        final StringBuilder builder = new StringBuilder();
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    //构造发送的文本信息
                    InputData input = new InputData.Builder(message).build();
                    startTime = System.currentTimeMillis();

                    //带上下文构造信息，上下文存储或定义多伦对话变量或其他设定，含义与作用 ，根据业务以及开发设计来确定
                    //context 上下文是一个Map<String,Object> 对象，发送信息返回的结果中带有上下文对象
                    MessageOptions newMessage;
                    if (App.IBMcontext != null) {
                        newMessage = new MessageOptions.Builder(App.workspace_id).input(input).context(App.IBMcontext).build();
                    } else {
                        newMessage = new MessageOptions.Builder(App.workspace_id).input(input).build();
                    }

                    //发送信息,并获得结果
                    MessageResponse response = App.conversationService.message(newMessage).execute();
                    //获取上下文  上下文简要 继承 Context extends DynamicModel extends HashMap<String, Object>;
                    //获取文本信息，返回一个list对象，返回可能包含多个句子
                    ArrayList responseList = (ArrayList) response.getOutput().get("text");


                    for (int i = 0; i < responseList.size(); i++) {
                        Log.d(TAG, "senMessageToIBM: i " + responseList.get(i).toString());
                        builder.append(responseList.get(i).toString());
                    }
                    if (!TextUtils.isEmpty(builder.toString())){
                        App.IBMcontext = response.getContext();
                        Object function = App.IBMcontext.get("function");

                        if (function != null) {
                            String functionStr = function.toString();
                            Log.d(TAG, "functionStr: " + functionStr);
                            switch (functionStr) {
                                case "sing1":
                                    control.TtsPlay(builder.toString(), ResultEvent.SING_PLAY_FRONT_1);
                                    break;
                                case "sing2":
                                    control.TtsPlay(builder.toString(), ResultEvent.SING_PLAY_FRONT_2);
                                    break;
                                case "dance1":
                                    control.TtsPlay(builder.toString(), ResultEvent.DANCE_FRONT_1);
                                    break;
                                case "dance2":
                                    control.TtsPlay(builder.toString(), ResultEvent.DANCE_FRONT_2);
                                    break;
                                default:
                                    Log.d(TAG, "run1: ibm 返回 " + builder.toString());
                                    if (!TextUtils.isEmpty(builder.toString())) {
                                        control.TtsPlay(builder.toString(), ResultEvent.SPEEK_TTS_END);
                                        time = System.currentTimeMillis() - startTime;
                                        Log.d(TAG, "run:1 " + time);
                                    }

                                    break;
                            }
                        } else {
                            Log.d(TAG, "run:2 ibm 返回 " + builder.toString());
                            control.TtsPlay(builder.toString(), ResultEvent.SPEEK_TTS_END);
                            time = System.currentTimeMillis() - startTime;
                            Log.d(TAG, "run:2 " + time);
                        }
                        isAction();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }).start();
    }

    private void isAction() {
        Object action = App.IBMcontext.get("action");
        if (action != null) {
            String actionStr = action.toString();
            Log.d(TAG, "actionStr: " + actionStr);
            switch (actionStr) {
                case "1":
                    RosRobotApi.get().run("talk1", new RemoteCommonListener() {
                        @Override
                        public void onResult(int sectionId, int status, String s) {
                            Log.d(TAG, "onResult: " + status);
                            if (status == 2 || status == 4) {
                            }
                        }
                    });
                    break;
                case "2":
                    RosRobotApi.get().run("shy", new RemoteCommonListener() {
                        @Override
                        public void onResult(int sectionId, int status, String s) {
                            Log.d(TAG, "onResult: " + status);
                            if (status == 2 || status == 4) {
                            }
                        }
                    });
                    break;
                case "3":
                    RosRobotApi.get().run("nod", new RemoteCommonListener() {
                        @Override
                        public void onResult(int sectionId, int status, String s) {
                            Log.d(TAG, "onResult: " + status);
                            if (status == 2 || status == 4) {
                            }
                        }
                    });
                    break;
                case "4":
                    RosRobotApi.get().run("cute", new RemoteCommonListener() {
                        @Override
                        public void onResult(int sectionId, int status, String s) {
                            Log.d(TAG, "onResult: " + status);
                            if (status == 2 || status == 4) {
                            }
                        }
                    });
                    break;
                case "5":
                    RosRobotApi.get().run("talk4", new RemoteCommonListener() {
                        @Override
                        public void onResult(int sectionId, int status, String s) {
                            Log.d(TAG, "onResult: " + status);
                            if (status == 2 || status == 4) {
                            }
                        }
                    });
                    break;
                case "6":
                    RosRobotApi.get().run("nod", new RemoteCommonListener() {
                        @Override
                        public void onResult(int sectionId, int status, String s) {
                            Log.d(TAG, "onResult: " + status);
                            if (status == 2 || status == 4) {
                            }
                        }
                    });
                    break;
                case "7":
                    RosRobotApi.get().run("pose3", new RemoteCommonListener() {
                        @Override
                        public void onResult(int sectionId, int status, String s) {
                            Log.d(TAG, "onResult: " + status);
                            if (status == 2 || status == 4) {
                            }
                        }
                    });
                    break;
                case "8":
                    RosRobotApi.get().run("pose2", new RemoteCommonListener() {
                        @Override
                        public void onResult(int sectionId, int status, String s) {
                            Log.d(TAG, "onResult: " + status);
                            if (status == 2 || status == 4) {
                            }
                        }
                    });
                    break;
                case "9":
                    RosRobotApi.get().run("talk10", new RemoteCommonListener() {
                        @Override
                        public void onResult(int sectionId, int status, String s) {
                            Log.d(TAG, "onResult: " + status);
                            if (status == 2 || status == 4) {
                            }
                        }
                    });
                    break;
                case "10":
                    RosRobotApi.get().run("talk9", new RemoteCommonListener() {
                        @Override
                        public void onResult(int sectionId, int status, String s) {
                            Log.d(TAG, "onResult: " + status);
                            if (status == 2 || status == 4) {
                            }
                        }
                    });
                    break;
                case "11":
                    RosRobotApi.get().run("talk8", new RemoteCommonListener() {
                        @Override
                        public void onResult(int sectionId, int status, String s) {
                            Log.d(TAG, "onResult: " + status);
                            if (status == 2 || status == 4) {
                            }
                        }
                    });
                    break;
                case "12":
                    RosRobotApi.get().run("nod", new RemoteCommonListener() {
                        @Override
                        public void onResult(int sectionId, int status, String s) {
                            Log.d(TAG, "onResult: " + status);
                            if (status == 2 || status == 4) {
                            }
                        }
                    });
                    break;
                case "13":
                    RosRobotApi.get().run("applause", new RemoteCommonListener() {
                        @Override
                        public void onResult(int sectionId, int status, String s) {
                            Log.d(TAG, "onResult: " + status);
                            if (status == 2 || status == 4) {
                            }
                        }
                    });
                    break;
                case "14":
                    RosRobotApi.get().run("danceend", new RemoteCommonListener() {
                        @Override
                        public void onResult(int sectionId, int status, String s) {
                            Log.d(TAG, "onResult: " + status);
                            if (status == 2 || status == 4) {
                            }
                        }
                    });
                    break;
                case "15":
                    RosRobotApi.get().run("pose2", new RemoteCommonListener() {
                        @Override
                        public void onResult(int sectionId, int status, String s) {
                            Log.d(TAG, "onResult: " + status);
                            if (status == 2 || status == 4) {
                            }
                        }
                    });
                    break;
                case "16":
                    RosRobotApi.get().run("talk5", new RemoteCommonListener() {
                        @Override
                        public void onResult(int sectionId, int status, String s) {
                            Log.d(TAG, "onResult: " + status);
                            if (status == 2 || status == 4) {
                            }
                        }
                    });
                    break;
                case "17":
                    RosRobotApi.get().run("talk4", new RemoteCommonListener() {
                        @Override
                        public void onResult(int sectionId, int status, String s) {
                            Log.d(TAG, "onResult: " + status);
                            if (status == 2 || status == 4) {
                            }
                        }
                    });
                    break;
                case "18":
                    RosRobotApi.get().run("talk8", new RemoteCommonListener() {
                        @Override
                        public void onResult(int sectionId, int status, String s) {
                            Log.d(TAG, "onResult: " + status);
                            if (status == 2 || status == 4) {
                            }
                        }
                    });
                    break;
            }
        }

    }
    public void setSuperAppIdDelay(final  int appid){
        try {
            Log.d("print","rqh the start setSuperAppId  " + appid );
            SpeechRobotApi api = SpeechRobotApi.get();
            Class<? extends SpeechRobotApi> apicliass = api.getClass();
            Method mm = apicliass.getDeclaredMethod("setSuperAppId", new Class[]{int.class});
            mm.setAccessible(true);
            mm.invoke(api, appid);
            Log.d("print","rqh the setSuperAppId success " + appid );
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.listener:
                break;
            case R.id.to_adim:
                Log.d(TAG, "onClick: to_adim ");
                if (App.appid == 4542){
                    setSuperAppIdDelay(-1);
                    App.appid = -1;
                    tv_statu.setVisibility(View.VISIBLE);
                }else {
                    setSuperAppIdDelay(4542);
                    App.appid = 4542;
                    tv_statu.setVisibility(View.GONE);
                }
                break;
            case R.id.step_1:
                senMessageToIBM("小呗，介绍一下你自己");
                break;
            case R.id.step_2:
                senMessageToIBM("小呗小呗，给我们唱首歌");
                break;
            case R.id.step_3:
                senMessageToIBM("小呗小呗，让我们进入猜歌环节");
                break;
            case R.id.step_4:
                senMessageToIBM("小呗小呗，小朋友的答案是");
                break;
            case R.id.step_5:
                senMessageToIBM("小呗小呗，来给我们再唱一曲");
                break;
            case R.id.step_6:
                senMessageToIBM("小呗小呗，让我们开始第二轮竞猜环节");
                break;
            case R.id.step_7:
                senMessageToIBM("小呗小呗，小朋友的答案是");
                break;
            case R.id.step_8:
                senMessageToIBM("小呗小呗，给我们跳支舞");
                break;
            case R.id.step_9:
                senMessageToIBM("小呗小呗，你能不能教我们学跳舞");
                break;
            case R.id.step_10:
                senMessageToIBM("小呗小呗，你开始发问吧，我们准备好了");
                break;
            case R.id.step_11:
                senMessageToIBM("小呗小呗，小朋友的答案是");
                break;
            case R.id.step_12:
                senMessageToIBM("小呗小呗，小朋友的答案是");
                break;
            case R.id.dance1:
                RosRobotApi.get().run("poppin1", new RemoteCommonListener() {
                    @Override
                    public void onResult(int sectionId, int status, String s) {
                        Log.d(TAG, "onResult: " + status);
                        if (status == 2 || status == 4) {
                            isSend = true;
                            mp.stop();
                        }
                    }
                });
                break;
            case R.id.dance2:
                RosRobotApi.get().run("poppin2", new RemoteCommonListener() {
                    @Override
                    public void onResult(int sectionId, int status, String s) {
                        Log.d(TAG, "onResult: " + status);
                        if (status == 2 || status == 4) {
                            isSend = true;
                            mp.stop();
                        }
                    }
                });
                break;
            case R.id.dance3:
                RosRobotApi.get().run("poppin3", new RemoteCommonListener() {
                    @Override
                    public void onResult(int sectionId, int status, String s) {
                        Log.d(TAG, "onResult: " + status);
                        if (status == 2 || status == 4) {
                            isSend = true;
                            mp.stop();
                        }
                    }
                });
                break;
            case R.id.dance4:
                RosRobotApi.get().run("doitbetter", new RemoteCommonListener() {
                    @Override
                    public void onResult(int sectionId, int status, String s) {
                        Log.d(TAG, "onResult: " + status);
                        if (status == 2 || status == 4) {
                            isSend = true;
                            mp.stop();
                        }
                    }
                });
                break;
            case R.id.dance5:
                RosRobotApi.get().run("doitbetter01", new RemoteCommonListener() {
                    @Override
                    public void onResult(int sectionId, int status, String s) {
                        Log.d(TAG, "onResult: " + status);
                        if (status == 2 || status == 4) {
                            isSend = true;
                            mp.stop();
                        }
                    }
                });
                break;
            case R.id.dance6:
                RosRobotApi.get().run("danceend", new RemoteCommonListener() {
                    @Override
                    public void onResult(int sectionId, int status, String s) {
                        Log.d(TAG, "onResult: " + status);
                        if (status == 2 || status == 4) {
                            isSend = true;
                            mp.stop();
                        }
                    }
                });
                break;
            case R.id.dance7:
                RosRobotApi.get().run("yankee1", new RemoteCommonListener() {
                    @Override
                    public void onResult(int sectionId, int status, String s) {
                        Log.d(TAG, "onResult: " + status);
                        if (status == 2 || status == 4) {
                            isSend = true;
                            mp.stop();
                        }
                    }
                });
                break;
            case R.id.dance8:
                RosRobotApi.get().run("yankee2", new RemoteCommonListener() {
                    @Override
                    public void onResult(int sectionId, int status, String s) {
                        Log.d(TAG, "onResult: " + status);
                        if (status == 2 || status == 4) {
                            isSend = true;
                            mp.stop();
                        }
                    }
                });
                break;
        }
    }

    private void dances1() {
        playMusic(R.raw.sing_3);
        String[] dance1 = {"yankee1", "poppin3","danceend"};
        RosRobotApi.get().run(dance1, new RemoteCommonListener() {
            @Override
            public void onResult(int sectionId, int status, String s) {
                Log.d(TAG, "onResult: " + status);
                if (status == 2 || status == 4) {
                    mp.stop();
                    isSend = true;
                }
            }
        });
        RosRobotApi.get().moveTo(0, 0, PI_*1.9f, 50, new RemoteCommonListener() {
            @Override
            public void onResult(int i, int i1, String s) {
                Log.d(TAG, "moveTo: ");
            }
        });
    }

    private void dances2() {
        playMusic(R.raw.sing_4);
        String[] dance1 = {"poppin1", "poppin2"};
        RosRobotApi.get().run(dance1, new RemoteCommonListener() {
            @Override
            public void onResult(int sectionId, int status, String s) {
                Log.d(TAG, "onResult: " + status);
                if (status == 2 || status == 4) {
                    isSend = true;
                    mp.stop();
                }
            }
        });


    }


    private void initcontent() {
        Observable.timer(1, TimeUnit.SECONDS).subscribe(new Action1<Long>() {
            @Override
            public void call(Long aLong) {
                contentResolverControl = new ContentResolverControl();
                listlocation = contentResolverControl.getUnSortWGPoints(getContentResolver(), PackageUtils.getMapName(RosRobotApi.get().getCurrentMap()));

            }
        }, new Action1<Throwable>() {
            @Override
            public void call(Throwable throwable) {
                Log.i("dan", "地图数据读取异常");
                addconsole("地图数据获取异常");
            }
        });
    }

//    private void initBean() {
//        Gson gson = new Gson();
//        String data = ConfigReader.read(this, "config.json");
//        bean  = gson.fromJson(data, CmdBean.class);
//        //应为有些信息是要实时从系统里面读取的，所以下面方法就是获取新的数据来刷新bean对象
//        bean.refreshProp();
//    }


//    private void registfeedback() {
//        subscriptions.add(Observable.timer(2000, TimeUnit.MILLISECONDS).subscribe(new Action1<Long>() {
//            @Override
//            public void call(Long aLong) {
//                RosRobotApi.get().registerCommonCallback(commonListener);
//                RosRobotApi.get().registerDiagnosticDataCallback(new RemoteDiagnosticDataListener() {
//                    @Override
//                    public void onResult(int key, String info) {
//                        Log.i("dan", "key =" + key + "info" + info);
//                    }
//                });
//            }
//        }));
//    }

//    RemoteCommonListener commonListener = new RemoteCommonListener() {
//        @Override
//        public void onResult(int sessionid, int status) {
//            Log.i("print", "sessionid=" + sessionid + "status=" + status);
//            handleFeedBack(sessionid, status);
//        }
//    };

    private void handleFeedBack(int sessionid, int status) {
        if (cmdmaps.containsKey(sessionid)) {
            switch (cmdmaps.get(sessionid)) {
                case DataConfig.CMD_ID_25:
                    if (RosConstant.Action.ACTION_FINISHED == status) {
                        control.setEnergyReleased(true);
                        ResultEvent event = new ResultEvent();
                        event.message = "释能执行完成";
                        RxBus.getDefault().post(event);
                    }
                    break;
                default:
                    break;
            }
        }
        ResultEvent event = new ResultEvent();
        event.message = "底层返回状态码:";
        event.retcode = status;
        RxBus.getDefault().post(event);
    }


    private void addconsole(String text) {
//        console.append(text);
//        console.append("\n");
        Observable.timer(200, TimeUnit.MILLISECONDS)
                .subscribe(new Action1<Long>() {
                    @Override
                    public void call(Long aLong) {
                        Log.i("dan", "当前线程：" + Thread.currentThread().getName());
//                        scrollViewconsole.fullScroll(ScrollView.FOCUS_DOWN);
                    }
                });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        if (subscriptions != null && !subscriptions.isUnsubscribed()) {
//            subscriptions.unsubscribe();
//        }
        SpeechRobotApi.get().destory();
        RosRobotApi.get().destory();
/*		RefWatcher refWatcher = App.getRefWatcher(this);
        refWatcher.watch(this);*/
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
//        return super.onKeyDown(keyCode, event);
        return false;
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
//        return super.onKeyUp(keyCode, event);
        return false;
    }
}
