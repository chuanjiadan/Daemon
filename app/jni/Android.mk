LOCAL_PATH := $(call my-dir)

include $(CLEAR_VARS)

LOCAL_MODULE :=_daemon
LOCAL_C_INCLUDES :=native_lib.h
LOCAL_SRC_FILES := native-lib.cpp \


LOCAL_LDLIBS += -L$(SYSROOT)/usr/lib -llog -lm -lz

include $(BUILD_SHARED_LIBRARY)