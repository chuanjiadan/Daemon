package com.chris.daemon;

public class ChrisDaemon {
    static {
        System.loadLibrary("native-lib");
    }


    /**
     * 创建socket
     *
     * @param serviceName
     * @param userId 进程id
     */
    public native void creatDaemon( String userId, String sockeName,String serviceName);

    /**
     * 客户端调用
     */
    public native void connectMonitor();



    public void init(int uId, String packageName,String serviceName) {
        String of = String.valueOf(uId);
        this.creatDaemon(of, "/data/data/" + packageName  + "/daemon.socket",serviceName);
        this.connectMonitor();
    }
}
