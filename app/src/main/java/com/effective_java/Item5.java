package com.effective_java;

import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

/**
 * Created by cwj on 16/8/13.
 * 避免创建多次不必要的对象
 */
public class Item5 {

    private static final Date date;

    static {//静态块初始化date
        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("GMT"));//创建代价较大
        calendar.set(1946, Calendar.JANUARY, 1, 0, 0, 0);
        date = calendar.getTime();
        System.out.println(date.getTime());
    }

    public void method() {
        //use date
        System.out.println(date.getTime());
    }

    public static void main(String[] args) {
        new Item5().method();
    }
}
