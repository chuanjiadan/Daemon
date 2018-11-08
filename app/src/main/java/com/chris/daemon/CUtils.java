package com.chris.daemon;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

class CUtils {

    public static String formatTime(long time) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS", Locale.CHINA);
        return format.format(new Date(time));
    }
}
