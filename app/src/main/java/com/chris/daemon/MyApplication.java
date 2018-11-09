package com.chris.daemon;

import android.app.Application;
import android.os.Process;
import android.util.Log;

public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();


        ChrisDaemon chrisDaemon = new ChrisDaemon();
        int uId = Process.myUid();
        Log.d("sniper ", "进程号： " + uId);

        chrisDaemon.initDaemon(getPackageName());


    }

    @Override
    public void onTerminate() {
        super.onTerminate();

    }
}
