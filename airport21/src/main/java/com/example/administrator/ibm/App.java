package com.ubtech.airport.ibm;

import android.app.Activity;
import android.app.Application;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;

import com.ubtech.airport.ibm.airport.ChatBean;
import com.ubtech.airport.ibm.airport.ChatPopuWindow;
import com.ubtech.airport.ibm.event.ResultEvent;
import com.ubtech.airport.ibm.rxbus.RxBus;
import com.ibm.watson.developer_cloud.conversation.v1.Conversation;
import com.ibm.watson.developer_cloud.conversation.v1.model.Context;
import com.ubtechinc.cruzr.sdk.face.FaceInfo;
import com.ubtechinc.cruzr.sdk.ros.RosRobotApi;
import com.ubtechinc.cruzr.sdk.speech.ISpeechContext;
import com.ubtechinc.cruzr.sdk.speech.SpeechRobotApi;
import com.ubtechinc.cruzr.serverlibutil.interfaces.InitListener;
import com.ubtechinc.cruzr.serverlibutil.interfaces.SpeechTtsListener;
import com.ubtechinc.cruzr.serverlibutil.interfaces.UpdateLexiconResultListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by Administrator on 2018/2/2.
 */

public class App extends Application {
    public static String workspace_id;
    public static String conversation_password;
    public static String conversation_username;
    public static Conversation conversationService;
    public static Context IBMcontext;

    public static App app;
    public static int appid = 4542;

    private List<FaceInfo> faces;
    private String TAG = "print";
    private ChatPopuWindow chatPopuWindow;

    private List<ChatBean> chatBeans;

    //RefWatcher watcher;
    @Override
    public void onCreate() {
        super.onCreate();
        app = this;
        context = this.getApplicationContext();

//        conversation_username = "b91a0b3c-af14-454a-9c14-32d2eedd98d0";
//        conversation_password = "qPIy6VPdGQ0V";
//        workspace_id = "4f0cd608-ac93-4f80-a40f-3ec1c451ca8d";
        conversation_username = "1786320b-99d9-41b1-b1cf-a4dee72b9628";
        conversation_password = "TfmVqRqYdGBR";
        workspace_id = "b3becac0-0350-4be3-92b5-d14bfe10863c";
        conversationService = new Conversation(Conversation.VERSION_DATE_2017_05_26);
        conversationService.setUsernameAndPassword(conversation_username, conversation_password);
        RobotInit(getApplicationContext());
        chatBeans = new ArrayList<>();
//        //开启悬浮窗
//        FloatActionController.getInstance().startMonkServer(this);

//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                CAAApi.login("18616350016");
//            }
//        }).start();
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
                HashMap<String,ArrayList<String>> oWordList = new HashMap<>();
                ArrayList<String> list =new ArrayList<String>();
                list.add("川航");
                list.add("四川航空");
                list.add("3U");
                list.add("U");
                list.add("3U8706");
                list.add("440289198709239876");
                list.add("购票");

                oWordList.put("userword",list);
                SpeechRobotApi.get().updateLexicon(oWordList, new UpdateLexiconResultListener() {
                    @Override
                    public void onResult(int i, int i1) {
                        Log.d(TAG, "onResult: " + i +"  i1  " + i1);
                    }
                });
            }
        });

    }

//    private void senMessageToIBM(final String message) {
//        final StringBuilder builder = new StringBuilder();
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                try {
//                    //构造发送的文本信息
//                    InputData input = new InputData.Builder(message).build();
//
//                    //带上下文构造信息，上下文存储或定义多伦对话变量或其他设定，含义与作用 ，根据业务以及开发设计来确定
//                    //context 上下文是一个Map<String,Object> 对象，发送信息返回的结果中带有上下文对象
//                    MessageOptions newMessage;
//                    if (App.IBMcontext != null) {
//                        newMessage = new MessageOptions.Builder(App.workspace_id).input(input).context(App.IBMcontext).build();
//                    } else {
//                        newMessage = new MessageOptions.Builder(App.workspace_id).input(input).build();
//                    }
//
//                    //发送信息,并获得结果
//                    MessageResponse response = App.conversationService.message(newMessage).execute();
//                    //获取上下文  上下文简要 继承 Context extends DynamicModel extends HashMap<String, Object>;
//                    //获取文本信息，返回一个list对象，返回可能包含多个句子
//                    ArrayList responseList = (ArrayList) response.getOutput().get("text");
//
//
//                    for (int i = 0; i < responseList.size(); i++) {
//                        builder.append(responseList.get(i).toString());
//                    }
//                    if (!TextUtils.isEmpty(builder.toString())) {
//                        App.IBMcontext = response.getContext();
//                        Log.d(TAG, "接收的文本: i " + builder.toString());
//                        TtsPlay(builder.toString());
//                    }
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//
//            }
//        }).start();
//    }

    public void TtsPlay(String text) {
        Log.d("print", "TtsPlay: ");
        if (SpeechRobotApi.get().isTtsSpeaking()) {
            SpeechRobotApi.get().speechStopTTS();
        }
        SpeechRobotApi.get().speechStartTTS(text, new SpeechTtsListener() {
            @Override
            public void onAbort() {

            }

            @Override
            public void onEnd() {
//                Log.d("print", "onEnd: 播放结束" );
//                ResultEvent event = new ResultEvent();
//                event.type = type;
//                event.message = "播放结束";
//                RxBus.getDefault().post(event);
            }
        });
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

    /**
     * 维护Activity 的list
     */
    private static List<Activity> mActivitys = Collections
            .synchronizedList(new LinkedList<Activity>());
    protected static android.content.Context context;

    public static android.content.Context getContext() {
        return context;
    }

//    protected abstract String getAppNameFromSub();

    /**
     * @param activity 作用说明 ：添加一个activity到管理里
     */
    public void pushActivity(Activity activity) {
        mActivitys.add(activity);
//        LogUtils.d("activityList:size:" + mActivitys.size());
    }

    /**
     * @param activity 作用说明 ：删除一个activity在管理里
     */
    public void popActivity(Activity activity) {
        mActivitys.remove(activity);
    }

    /**
     * get current Activity 获取当前Activity（栈中最后一个压入的）
     */
    public static Activity currentActivity() {
        if (mActivitys == null || mActivitys.isEmpty()) {
            return null;
        }
        Activity activity = mActivitys.get(mActivitys.size() - 1);
        return activity;
    }

    /**
     * 结束当前Activity（栈中最后一个压入的）
     */
    public static void finishCurrentActivity() {
        if (mActivitys == null || mActivitys.isEmpty()) {
            return;
        }
        Activity activity = mActivitys.get(mActivitys.size() - 1);
        finishActivity(activity);
    }

    /**
     * 结束指定的Activity
     */
    public static void finishActivity(Activity activity) {
        if (mActivitys == null || mActivitys.isEmpty()) {
            return;
        }
        if (activity != null) {
            mActivitys.remove(activity);
            activity.finish();
            activity = null;
        }
    }

    /**
     * 结束指定类名的Activity
     */
    public static void finishActivity(Class<?> cls) {
        if (mActivitys == null || mActivitys.isEmpty()) {
            return;
        }
        for (Activity activity : mActivitys) {
            if (activity.getClass().equals(cls)) {
                finishActivity(activity);
            }
        }
    }

    /**
     * 按照指定类名找到activity * * @param cls * @return
     */
    public static Activity findActivity(Class<?> cls) {
        Activity targetActivity = null;
        if (mActivitys != null) {
            for (Activity activity : mActivitys) {
                if (activity.getClass().equals(cls)) {
                    targetActivity = activity;
                    break;
                }
            }
        }
        return targetActivity;
    }

    /**
     * @return 作用说明 ：获取当前最顶部activity的实例
     */
    public Activity getTopActivity() {
        Activity mBaseActivity = null;
        synchronized (mActivitys) {
            final int size = mActivitys.size() - 1;
            if (size < 0) {
                return null;
            }
            mBaseActivity = mActivitys.get(size);
        }
        return mBaseActivity;
    }

    /**
     * @return 作用说明 ：获取当前最顶部的acitivity 名字
     */
    public String getTopActivityName() {
        Activity mBaseActivity = null;
        synchronized (mActivitys) {
            final int size = mActivitys.size() - 1;
            if (size < 0) {
                return null;
            }
            mBaseActivity = mActivitys.get(size);
        }
        return mBaseActivity.getClass().getName();
    }

    /**
     * 结束所有Activity
     */
    public static void finishAllActivity() {
        if (mActivitys == null) {
            return;
        }
        for (Activity activity : mActivitys) {
            activity.finish();
        }
        mActivitys.clear();
    }

    /**
     * 退出应用程序
     */
    public static void appExit() {
        try {
//            LogUtils.e("app exit");
            finishAllActivity();
        } catch (Exception e) {
        }
    }

    private void registerActivityListener() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
            registerActivityLifecycleCallbacks(new ActivityLifecycleCallbacks() {
                @Override
                public void onActivityCreated(Activity activity, Bundle savedInstanceState) { /** * 监听到 Activity创建事件 将该 Activity 加入list */
                    pushActivity(activity);
                }

                @Override
                public void onActivityStarted(Activity activity) {
                }

                @Override
                public void onActivityResumed(Activity activity) {
                }

                @Override
                public void onActivityPaused(Activity activity) {
                }

                @Override
                public void onActivityStopped(Activity activity) {
                }

                @Override
                public void onActivitySaveInstanceState(Activity activity, Bundle outState) {
                }

                @Override
                public void onActivityDestroyed(Activity activity) {
                    if (null == mActivitys || mActivitys.isEmpty()) {
                        return;
                    }
                    if (mActivitys.contains(activity)) { /** * 监听到 Activity销毁事件 将该Activity 从list中移除 */
                        popActivity(activity);
                    }
                }
            });
        }
    }
}
