//
// Created by sniper on 2018/7/25.必要的头文件
//

#ifndef DAEMON_NATIVE_LIB_H
#define DAEMON_NATIVE_LIB_H

#endif //DAEMON_NATIVE_LIB_H

#define LOG_TAG "sniper"

#define LOGE(...) __android_log_print(ANDROID_LOG_ERROR,LOG_TAG,__VA_ARGS__)
#define LOGD(...)    __android_log_print(ANDROID_LOG_DEBUG, LOG_TAG, __VA_ARGS__)

#include <sys/select.h>


#include <jni.h>
#include <unistd.h>
#include <sys/socket.h>
#include <pthread.h>
#include <signal.h>
#include <sys/wait.h>
#include <android/log.h>
#include <sys/types.h>
#include <sys/un.h>
#include <stdlib.h>
#include <linux/signal.h>
#include <android/log.h>
#include <unistd.h>
#include <errno.h>

void child_work();

int child_create_channel();

void child_listen_msg();

int get_version();


