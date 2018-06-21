package com.ubtech.airport.ibm;

import android.app.Application;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;

import com.ubtech.airport.ibm.Resolver.ContentResolverControl;
import com.ubtech.airport.ibm.airport.ActivityManager;
import com.ubtech.airport.ibm.airport.ChatActivity;
import com.ubtech.airport.ibm.airport.ChatBean;
import com.ubtech.airport.ibm.airport.ChatPopuWindow;
import com.ubtech.airport.ibm.airport.CommonUtil;
import com.ubtech.airport.ibm.airport.Result;
import com.ubtech.airport.ibm.event.ResultEvent;
import com.ubtech.airport.ibm.event.StatusEvent;
import com.ubtech.airport.ibm.rxbus.RxBus;
import com.ibm.watson.developer_cloud.conversation.v1.Conversation;
import com.ibm.watson.developer_cloud.conversation.v1.model.Context;
import com.ibm.watson.developer_cloud.conversation.v1.model.InputData;
import com.ibm.watson.developer_cloud.conversation.v1.model.MessageOptions;
import com.ibm.watson.developer_cloud.conversation.v1.model.MessageResponse;
import com.ubtechinc.cruzr.sdk.face.FaceInfo;
import com.ubtechinc.cruzr.sdk.ros.RosRobotApi;
import com.ubtechinc.cruzr.sdk.speech.ISpeechContext;
import com.ubtechinc.cruzr.sdk.speech.SpeechRobotApi;
import com.ubtechinc.cruzr.serverlibutil.interfaces.InitListener;
import com.ubtechinc.cruzr.serverlibutil.interfaces.RemoteCommonListener;
import com.ubtechinc.cruzr.serverlibutil.interfaces.SpeechTtsListener;
import com.ubtechinc.cruzr.serverlibutil.interfaces.UpdateLexiconResultListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by Administrator on 2018/2/2.
 */

public class AirportApp extends Application {
    public static String workspace_id;
    public static String conversation_password;
    public static String conversation_username;
    public static Conversation conversationService;
    public static Context IBMcontext;
    public static String IBMShow;
    public static String IBMIntent;

    public static AirportApp app;
    public static int appid = 4542;

    private List<FaceInfo> faces;
    private ChatPopuWindow chatPopuWindow;

    private List<ChatBean> chatBeans;

    private ContentResolverControl contentResolverControl;
    private CompositeSubscription subscriptions = new CompositeSubscription();
    private HashMap<Integer, Integer> cmdmaps = new HashMap<>();
    private String TAG = "AirportApp";
    private boolean isSend = true;

    public static String JUKEAppKey = "f728bf7987cd74ff3710b1abb17b554f";
    public static String JUKEUrl = "http://op.juhe.cn/flight/df/fs";
    private ActivityManager activityManager = null; // activity管理类

    @Override
    public void onCreate() {
        super.onCreate();
        app = this;
        activityManager = ActivityManager.getInstance(); // 获得实例
        conversation_username = "1786320b-99d9-41b1-b1cf-a4dee72b9628";
        conversation_password = "TfmVqRqYdGBR";
        workspace_id = "b3becac0-0350-4be3-92b5-d14bfe10863c";
        conversationService = new Conversation(Conversation.VERSION_DATE_2017_05_26);
        conversationService.setUsernameAndPassword(conversation_username, conversation_password);
        RobotInit(getApplicationContext());
        chatBeans = new ArrayList<>();
//        //开启悬浮窗
        initResultEventObserver();
        initStatusEventObserver();
        actions = new ArrayList<>();
        actions.add("pose3");
        actions.add("talk1");
        actions.add("talk2");
        actions.add("talk4");
        actions.add("talk7");
        actions.add("talk8");
        actions.add("talk9");
        actions.add("nod");
        actions.add("applause");
        actions.add("guideright");
        actions.add("guideleft");
        actions.add("zhilu");
    }

    public ActivityManager getActivityManager() {
        return activityManager;
    }

    public void setSuperAppIdDelay(final int appid) {
        try {
            Log.d("print", "rqh the start setSuperAppId  " + appid);
            SpeechRobotApi api = SpeechRobotApi.get();
            Class<? extends SpeechRobotApi> apicliass = api.getClass();
            Method mm = apicliass.getDeclaredMethod("setSuperAppId", new Class[]{int.class});
            mm.setAccessible(true);
            mm.invoke(api, appid);
            Log.d("print", "rqh the setSuperAppId success " + appid);
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

    public void RobotInit(android.content.Context context) {
        RosRobotApi.get().initializ(context, null);
        SpeechRobotApi.get().initializ(context, 4542, new InitListener() {

            @Override
            public void onInit() {
                setSuperAppIdDelay(4542);
                SpeechRobotApi.get().registerSpeech(new ISpeechContext() {
                    @Override
                    public void onStart() {
                        Log.d("print", "onStart: ");
                    }

                    @Override
                    public void onStop() {
                        Log.d("print", "onStop: ");
                    }

                    @Override
                    public void onPause() {
                        Log.d("print", "onPause: ");
                    }

                    @Override
                    public void onResume() {
                        Log.d("print", "onResume: ");
                    }

                    @Override
                    public void onResult(String speechTxt) {
                        Log.d("print", "text 成功 " + speechTxt);
                        ResultEvent event = new ResultEvent();
                        String string = "";
                        event.type = ResultEvent.TO_IBM;
                        try {
                            JSONObject json = new JSONObject(speechTxt);
                            if (json != null) {
                                string = json.getString("request");
                            } else {
                                string = speechTxt;
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            string = speechTxt;
                        }
//                        Log.d(TAG, "onResult: 发送的文本  " + string);
                        event.message = string;
                        RxBus.getDefault().post(event);
                    }
                });
                HashMap<String, ArrayList<String>> oWordList = new HashMap<>();
                ArrayList<String> list = new ArrayList<String>();
                list.add("川航");
                list.add("四川航空");
                list.add("3U");
                list.add("U");
                list.add("3U8706");
                list.add("440289198709239876");
                list.add("购票");

                oWordList.put("userword", list);
                SpeechRobotApi.get().updateLexicon(oWordList, new UpdateLexiconResultListener() {
                    @Override
                    public void onResult(int i, int i1) {
                        Log.d(TAG, "onResult: " + i + "  i1  " + i1);
                    }
                });
            }
        });

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

    private void handleEvent(ResultEvent event) {
        Object message = event.message;
        int retcode = event.retcode;
        switch (event.type) {
            case ResultEvent.TO_IBM:
                if (!SpeechRobotApi.get().isTtsSpeaking()) {
                    String comment = (String) message;
                    ChatBean chatBean = new ChatBean();
                    chatBean.setType(0);
                    chatBean.setChatinfo(comment);
                    Log.d(TAG, "发送: " + comment);
                    if (IBMShow != null && IBMShow.equals("2")) {
                        //发送身份证号或13位票号
                        Result result = CommonUtil.matchsTicket(comment);
                        Result result1 = CommonUtil.matchsIndentifyNo(comment);
                        if (result.isMacher || result1.isMacher) {
                            senMessageToIBM(comment);
                        }
                    } else if (IBMShow != null && IBMShow.equals("9")) {

                    } else if (IBMShow != null && IBMShow.equals("11")){
                        Result result1 = CommonUtil.matchsIndentifyNo(comment);
                        if (result1.isMacher){
                            senMessageToIBM(result1.macherStr);
                        }
                    }else {
                        senMessageToIBM(comment);
                    }
                    if (activityManager.currentActivity().getClass() != ChatActivity.class) {
                        Intent intent = new Intent(this, ChatActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        activityManager.pushActivity(new ChatActivity());
                    }
                }
                break;
            case ResultEvent.RECEIVE_IBM:
                String comment = (String) message;
                Log.d(TAG, "接收: " + comment);
                TtsPlay(comment);
//                //动作
                Random random = new Random();
                int i = random.nextInt(10);
                if (i < actions.size() && i >= 0) {
                    action(actions.get(i));
                }
                ChatBean chatBean = new ChatBean();
                chatBean.setType(1);
                chatBean.setChatinfo(comment);
                break;
        }
    }

    private List<String> actions = new ArrayList<>();

    private void action(String action) {
        RosRobotApi.get().run(action, new RemoteCommonListener() {
            @Override
            public void onResult(int sectionId, int status, String s) {
                Log.d(TAG, "onResult: " + status);
                if (status == 2 || status == 4) {
                }
            }
        });
    }


    public static void senMessageToIBM(final String message) {
        final StringBuilder builder = new StringBuilder();
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    //构造发送的文本信息
                    InputData input = new InputData.Builder(message).build();

                    //带上下文构造信息，上下文存储或定义多伦对话变量或其他设定，含义与作用 ，根据业务以及开发设计来确定
                    //context 上下文是一个Map<String,Object> 对象，发送信息返回的结果中带有上下文对象
                    MessageOptions newMessage;
                    if (AirportApp.IBMcontext != null) {
                        newMessage = new MessageOptions.Builder(AirportApp.workspace_id).input(input).context(AirportApp.IBMcontext).build();
                    } else {
                        newMessage = new MessageOptions.Builder(AirportApp.workspace_id).input(input).build();
                    }

                    //发送信息,并获得结果
                    MessageResponse response = AirportApp.conversationService.message(newMessage).execute();
                    //获取上下文  上下文简要 继承 Context extends DynamicModel extends HashMap<String, Object>;
                    //获取文本信息，返回一个list对象，返回可能包含多个句子
                    ArrayList responseList = (ArrayList) response.getOutput().get("text");


                    for (int i = 0; i < responseList.size(); i++) {
                        Log.d("print", "接收到的: " + i + responseList.get(i).toString());
                        builder.append(responseList.get(i).toString());
                    }
                    AirportApp.IBMcontext = response.getContext();
                    if (!TextUtils.isEmpty(builder.toString())) {
                        ResultEvent event = new ResultEvent();
                        event.type = ResultEvent.RECEIVE_IBM;

                        event.message = builder.toString();
                        RxBus.getDefault().post(event);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }).start();
    }

    //    private void senMessageToIBM(final String message){
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                CAAResult conversation = CAAApi.conversation(message);
//                String data = conversation.getData();
//                Log.d(TAG, "senMessageToIBM: " + data);
//                if (!TextUtils.isEmpty(data)) {
//                        ResultEvent event = new ResultEvent();
//                        event.type = ResultEvent.RECEIVE_IBM;
//                        event.message = data;
//                        RxBus.getDefault().post(event);
//                    }
//            }
//        }).start();
//
//    }

    public static void TtsPlay(String text) {
        Log.d("print", "TtsPlay: " + text);
        if (SpeechRobotApi.get().isTtsSpeaking()) {
            SpeechRobotApi.get().speechStopTTS();
        }
        SpeechRobotApi.get().speechStartTTS(text, new SpeechTtsListener() {
            @Override
            public void onAbort() {

            }

            @Override
            public void onEnd() {
            }
        });
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


    public List<FaceInfo> getFaces() {
        return faces;
    }

    /**
     * 由于关机的表情会无限执行，所以在演示的时候把关机表情去掉
     *
     * @param list
     */
    private void deletePowerOffFace(List<FaceInfo> list) {
        Iterator<FaceInfo> it = list.iterator();
        FaceInfo info = null;
        while (it.hasNext()) {
            info = it.next();
            if ("face_power_off".equals(info.faceId)) {
                list.remove(info);
                break;
            }
        }
    }


}
