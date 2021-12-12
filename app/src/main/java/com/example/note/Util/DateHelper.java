package com.example.note.Util;

import java.util.Calendar;

public class DateHelper {
    //使用单例设计模式的返回日期字符串

    private static DateHelper instance = null;

    private DateHelper(){

    }

    public static DateHelper getInstance(){
        if (instance == null)
            instance = new DateHelper();
        return instance;
    }

    public String getDataString(){
        Calendar calendar = Calendar.getInstance();
        //月  月份从0开始 —> +1
        int month = calendar.get(Calendar.MONTH) + 1;
        //日
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        //周(本月第几周)
//        int week = calendar.get(Calendar.DAY_OF_WEEK_IN_MONTH);
        //小时
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        //分
        int minute = calendar.get(Calendar.MINUTE);
        //秒
        int second = calendar.get(Calendar.SECOND);

        return month+"月"+day+"日"+hour+"时:"+minute+"分:"+second+"秒";
    }
}
