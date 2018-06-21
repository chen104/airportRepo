package com.ubtech.airport.ibm;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.ubtech.airport.ibm.airport.AirportMainActivity1;

/**
 * Created by Administrator on 2018/2/3.
 */

public class StartBootComplete extends BroadcastReceiver {
    public static final String CORE_SERVICE_WORK_ON = "com.ubtechinc.cruzr.coreservices.ACTION.WORK_ON";

    @Override
    public void onReceive(Context context, Intent intent) {

        Log.d("print", "StartBootComplete: ");
        if (intent.getAction().equals(CORE_SERVICE_WORK_ON)) {
            Intent intent2 = new Intent(context, AirportMainActivity1.class);
            // 下面这句话必须加上才能实现开机自动运行app的界面
            intent2.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent2);
        }
    }
}
