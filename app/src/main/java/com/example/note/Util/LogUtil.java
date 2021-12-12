package com.example.note.Util;

import android.util.Log;
/*
* 自定义日志工具
* */
public class LogUtil {

    public static final int VERBOSE = 1;
    public static final int DEBUG = 2;
    public static final int INFO = 3;
    public static final int WARN = 4;
    public static final int ERROR = 5;
    public static final int NOTHING = 6;

    //开发阶段 打印所有日志
    public static int level = VERBOSE;

    //v 打印琐碎的、意义最小的日志信息
    public static void v(String tag, String msg) {
        if (level <= VERBOSE) {
            Log.v(tag, msg);
        }
    }

    //d 打印一些调试信息
    public static void d(String tag, String msg) {
        if (level <= DEBUG) {
            Log.d(tag, msg);
        }
    }

    //i 打印一些比较重要的数据
    public static void i(String tag, String msg) {
        if (level <= INFO) {
            Log.i(tag, msg);
        }
    }

    //w 打印警告信息
    public static void w(String tag, String msg) {
        if (level <= WARN) {
            Log.w(tag, msg);
        }
    }

    //e 打印程序中的错误
    public static void e(String tag, String msg){
        if (level <= ERROR) {
            Log.e(tag, msg);
        }
    }

}
