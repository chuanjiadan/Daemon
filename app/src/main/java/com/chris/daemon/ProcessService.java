package com.chris.daemon;

import android.annotation.SuppressLint;
import android.app.Application;
import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Process;
import android.util.Log;
import android.widget.Toast;

import com.xiyun.logutils.YLog;

import java.util.Timer;
import java.util.TimerTask;

public class ProcessService extends Service {
    private static final String TAG = "sniper ProcessService ";
    private static Application application;


    private int i = 0;
    @SuppressLint("HandlerLeak")
    static Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            int what = msg.what;
            YLog.d(TAG, "服务进程存活" + CUtils.formatTime(System.currentTimeMillis()));
            Toast.makeText(application, "进程存活" + what, Toast.LENGTH_LONG).show();


        }
    };


    @Override
    public void onCreate() {
        super.onCreate();
        application = getApplication();
        YLog.d(TAG, " 服务被守护者唤起 当前进程号：" + Process.myPid() + "   " + CUtils.formatTime(System.currentTimeMillis()));


        Timer timer = new Timer();
        timer.scheduleAtFixedRate(
                new TimerTask() {
                    @Override
                    public void run() {
//                        Log.d(TAG, "服务存活：" + i);
                        i++;
                        Message message = new Message();
                        message.what = i;
                        mHandler.sendMessage(message);
                    }
                }, 0, 15000);
    }

    @Override
    public IBinder onBind(Intent intent) {
        Log.d(TAG, "onBind: --------------------");

        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Toast.makeText(getApplication(), "服务被守护者拉活", Toast.LENGTH_LONG).show();
        return Service.START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        YLog.d(TAG, "onDestroy:" + CUtils.formatTime(System.currentTimeMillis()));
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        YLog.d(TAG, "低内存");
    }

    @Override
    public void onStart(Intent intent, int startId) {
        super.onStart(intent, startId);

    }
}
