package com.chris.daemon;

import android.os.Process;

public class ChrisDaemon {
    static {
        System.loadLibrary("native-lib");
    }

    public ChrisDaemon() {
    }

    /**
     * 创建socket
     *
     * @param packageName
     * @param userId      进程id
     */
    public native void creatDaemon(String packageName, String userId);

    /**
     * 客户端调用
     */
    public native void connectMonitor();


    public void initDaemon(String packageName) {
        creatDaemon("data/data/" + packageName + "/my.socket", String.valueOf(Process.myPid()));
        connectMonitor();

    }
}
