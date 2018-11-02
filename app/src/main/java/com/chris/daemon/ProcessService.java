package com.chris.daemon;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.Process;
import android.util.Log;
import android.widget.Toast;

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
        Log.d(TAG, "进程号： " + uId);

        //
        chrisDaemon.init(uId,getPackageName(),"com.chris.daemon/com.chris.daemon.ProcessService");

        Timer timer = new Timer();
        timer.scheduleAtFixedRate(
                new TimerTask() {
                    @Override
                    public void run() {
                        Log.d(TAG, "服务存活：" + i);
//                        Toast.makeText(getApplicationContext(),"服务存活",Toast.LENGTH_LONG).show();


                        i++;
                    }
                }, 0, 20000);
    }

    @Override
    public IBinder onBind(Intent intent) {
        Log.d(TAG, "onBind: --------------------");

        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "onStartCommand: ---------------------------------------------");
        Toast.makeText(getApplicationContext(),"服务唤醒",Toast.LENGTH_LONG).show();

        return super.onStartCommand(intent, flags, startId);
    }
}
