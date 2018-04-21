package com.example.lenovo.myapplication.manger;

import android.app.Activity;
import android.content.Context;
import android.telephony.TelephonyManager;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lenovo on 2018/3/12.
 */

public class ActivityCollector {
    public static List<Activity> activityList = new ArrayList<>();

    public static void addActivity(Activity activity) {
        activityList.add(activity);
    }
    public static void removeActivity(Activity activity){
        activityList.remove(activity);
    }
    public static void finishAll() {
        for (Activity activity:activityList) {
            if(!activity.isFinishing()){
                activity.finish();
            }
        }
        activityList.clear();
    }
    /*public static String getIMEI(Context context) {
        TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(context.TELEPHONY_SERVICE);
        String imei = telephonyManager;

        return imei;
    }*/
}
