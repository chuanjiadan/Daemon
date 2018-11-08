#include <jni.h>
#include <string>
#include <unistd.h>
#include <sys/system_properties.h>
#include "native_lib.h"

const char *path = "/data/data/com.chris.daemon/my.sock";//socket文件路径
int m_child;
const char *userId;


extern "C"
JNIEXPORT
void JNICALL
Java_com_chris_daemon_ChrisDaemon_creatDaemon(JNIEnv *env, jobject instance, jstring userId_) {
    userId = env->GetStringUTFChars(userId_, 0);
    LOGE("Ndk开起调用   %d", userId_);


    // linux中开启双进程
    pid_t pid = fork();
    LOGE("fork函数调用  %d", pid);
    if (pid < 0) {//进程开启失败
        LOGE("进程开启失败");
//        exit(1);

    } else if (pid == 0) {//子进程，即守护进程
        LOGE("子进程，即守护进程");
        child_work();

    } else if (pid > 0) {//父进程,住进程
        LOGE("父进程,即主进程");
//        child_work();
//        exit();


    }
    //生成子进程sectionID
    pid_t i1 = setsid();
    LOGE("生成子进程sectionID   %d", i1);
    //当前子进程运行目录改为根目录，防止os卸载
    int i = chdir("/");
    LOGE("子进程运行目录改为根目录  %d", i);
    int i2 = execv(path, NULL);

    LOGE("  分家：   %d ", i2);

    env->ReleaseStringUTFChars(userId_, userId);
}


extern "C"
JNIEXPORT
void JNICALL
Java_com_chris_daemon_ChrisDaemon_connectMonitor(JNIEnv *env, jobject instance) {
    int socket2;
    struct sockaddr_un addr;//内存区
    // apk进程调用
    while (1) {
        LOGE("客户端 apk进程开始连接");
        socket2 = socket(AF_LOCAL, SOCK_STREAM, 0);
        if (socket2 < 0) {
            LOGE("连接失败---");
            return;
        }


        memset(&addr, 0, sizeof(sockaddr));//清空内存
        addr.sun_family = AF_LOCAL;//指定协议
        strcpy(addr.sun_path, path);//内存拷贝
        int connet = connect(socket2, (const sockaddr *) (&addr), sizeof(sockaddr_un));

        if (connet < 0) {

            LOGE("连接失败 ***");
            close(socket2);
            sleep(1);
            //等待下一次连接尝试
            continue;
        }

        LOGE("连接成功》》》》》》》》  %d", connet);
        break;

    }

}


void child_work() {
// socket开启服务端
    LOGE("socket开启服务端");
    if (child_create_channel()) {
        child_listen_msg();
    }
}

/**
 * 创建服务端socket
 * @return 
 */
int child_create_channel() {
    //1.创建socket
    int socket1 = socket(AF_LOCAL, SOCK_STREAM, 0);
    unlink(path);

    struct sockaddr_un addr;
    memset(&addr, 0, sizeof(sockaddr_un));//清空内存
    addr.sun_family = AF_LOCAL;//指定协议
    strcpy(addr.sun_path, path);//内存拷贝
    //2.调用bind
    int bin = bind(socket1, (const sockaddr *) &addr, sizeof(sockaddr_un));
    LOGE("调用bind   %d", bin);
    if (bin < 0) {
        LOGE("绑定失败");
        return 0;
    }
    //3.监听
    int connfd = 0;//连接状态
    listen(socket1, 3);//监听3个客户端
    //循环尝试连接，直到成功
    while (1) {
        connfd = accept(socket1, NULL, NULL);
        if (connfd < 0) {//连接失败
            if (errno == EINTR) {
                continue;

            } else {
                LOGE("错误读取");
                return 0;
            }

        }
        m_child = connfd;
        LOGE("主进程连接上了   %d", m_child);
        break;
    }

    return 1;
}

/**
 * 
 * 服务端读取信息
 */

void child_listen_msg() {
    LOGE("服务端读取信息");
    //定义set集合
    fd_set rfds;
    //定义超时时间
    struct timeval timeout = {3, 0};
    //守护进程不需要与用户对话，关闭标准文件描述符，可以不关闭
    close(STDIN_FILENO);
    close(STDOUT_FILENO);
    close(STDERR_FILENO);

    //
    int sdk_version = get_version();
    LOGE("sdk的版本号：%d" , sdk_version);

    //循环去读  read resv 都是读
    while (1) {
        FD_ZERO(&rfds);//清空结合
        FD_SET(m_child, &rfds);//重新设置，指定下一个客户端

        //Linux阻塞函数，监视文件的具柄数
        int r = select(m_child + 1, &rfds, NULL, NULL, &timeout);
//        LOGE("读取消息  %d ", r);
        if (r > 0) {
            //缓冲区
            char pkg[256] = {0};
            //只读取指定apk的客户端
            if (FD_ISSET(m_child, &rfds)) {
                //阻塞函数，实际不读数据
                int result = read(m_child, pkg, sizeof(pkg));
                LOGE("阻塞函数，实际不读数据 %d ", result);
                //read不阻塞说明apk进程连接断开，就去启动服务


                if (sdk_version >= 17 || sdk_version == 0) {

                    int am = execlp("am",
                                    "am",
                                    "startservice",
                                    "--user", userId,
                                    "com.chris.daemon/com.chris.daemon.ProcessService",
                                    (char *) NULL);
                    LOGE("启动服务 >= 17 ：%d", am);


                } else {
                    int am = execlp("am", "am", "startservice", "-n",
                                    "com.chris.daemon/com.chris.daemon.ProcessService",
                                    (char *) NULL);
                    LOGE("启动服务：%d", am);
                }


                break;
            }
        }

    }


}

int get_version() {
    char value[8] = "";
    __system_property_get("ro.build.version.sdk", value);
    return atoi(value);
}




