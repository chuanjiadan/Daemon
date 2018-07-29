package com.chris.daemon;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.Process;
import android.util.Log;

import java.util.Timer;
import java.util.TimerTask;

public class ProcessService extends Service {
    private static final String TAG = "sniper ProcessService ";

    private int i = 0;


    @Override
    public void onCreate() {
        super.onCreate();
        ChrisDaemon chrisDaemon = new ChrisDaemon();
        int uId = Process.myUid();
        Log.d(TAG, "进程好： " + uId);
        //
        chrisDaemon.creatDaemon(String.valueOf(Process.myPid()));
        chrisDaemon.connectMonitor();

        Timer timer = new Timer();
        timer.scheduleAtFixedRate(
                new TimerTask() {
                    @Override
                    public void run() {
                        Log.d(TAG, "服务存活：" + i);
                        i++;
                    }
                }, 0, 1000 * 3);
    }

    @Override
    public IBinder onBind(Intent intent) {
        Log.d(TAG, "onBind: --------------------");

        return null;
    }


}
