package com.chris.daemon;

public class ChrisDaemon {
    static{
        System.loadLibrary("native-lib");
    }
    /**
     * 创建socket
     *
     * @param userId 进程id
     */
    public native void creatDaemon(String userId);

    /**
     * 客户端调用
     */
    public native void connectMonitor();


}
