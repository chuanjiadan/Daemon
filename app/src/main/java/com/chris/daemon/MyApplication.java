package com.chris.daemon;

import android.app.Application;
import android.os.Process;
import android.util.Log;

public class MyApplication extends Application {

//    String name="com.chris.daemon/com.chris.daemon.ProcessServic";
    String name="com.chris.daemon/com.chris.daemon.ProcessServic";

    @Override
    public void onCreate() {
        super.onCreate();


        ChrisDaemon chrisDaemon = new ChrisDaemon();
        int uId = Process.myUid();
        Log.d("sniper ", "进程号： " + uId);

        chrisDaemon.initDaemon(getPackageName(),name);


    }

    @Override
    public void onTerminate() {
        super.onTerminate();

    }
}
