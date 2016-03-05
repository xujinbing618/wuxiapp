package com.magus.enviroment.global.log;

/**
 *
 *  打印日志，加一个开关便于控制
 *  2015-02-25
 */

public final class Log {
    public static final boolean DEBUG = true;

    private static final int DEFALUT = 0;

    private Log() {
    }

    public static int v(String tag, String msg) {
        if (DEBUG)
            return android.util.Log.v(tag, msg);
        else
            return DEFALUT;
    }

    public static int d(String tag, String msg) {
        if (DEBUG)
            return android.util.Log.d(tag, msg);
        else
            return DEFALUT;
    }

    public static int i(String tag, String msg) {
        if (DEBUG)
            return android.util.Log.i(tag, msg);
        else
            return DEFALUT;
    }

    public static int e(String tag, String msg) {
        if (DEBUG)
            return android.util.Log.e(tag, msg);
        else
            return DEFALUT;
    }
    public static int e(String tag, String msg, Throwable throwable) {
        if (DEBUG)
            return android.util.Log.e(tag, msg, throwable);
        else
            return DEFALUT;
    }
}
