package com.example.administrator.task4;

import android.app.ActivityManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.provider.Settings;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Administrator on 2017/2/24.
 */
public class MyService extends Service {

    private Timer timer = null;
    String foreground;

    @Override
    public IBinder onBind(Intent arg0) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        //获取此应用包名
        foreground = getForeground();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        this.init();
        //每0.5秒监控一次
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                //若前台应用不是该应用，打开该应用
                if (!getForeground().equals(foreground)) {
                    startAPP(foreground);
                    System.out.println("233");
                }
            }
        }, 500, 500);

        return START_STICKY;
    }

    public void init(){
        timer = new Timer();
    }

    @Override
    public void onDestroy() {
        timer.cancel();
        super.onDestroy();
    }

    public String getForeground() {
        ActivityManager activityManager = (ActivityManager)getSystemService(Context.ACTIVITY_SERVICE);
        //获取到当前所有进程
        List<ActivityManager.RunningAppProcessInfo> processInfoList = activityManager.getRunningAppProcesses();
        if (processInfoList == null || processInfoList.isEmpty()) {
            return "";
        }
        //遍历进程列表，找到第一个前台进程
        for (ActivityManager.RunningAppProcessInfo processInfo : processInfoList) {
            if (processInfo.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                return processInfo.processName;
            }
        }
        return "";
    }

    //打开应用
    public void startAPP(String appPackageName) {
        try{
            Intent intent = this.getPackageManager().getLaunchIntentForPackage(appPackageName);
            startActivity(intent);
        }catch(Exception e){

        }
    }
}
