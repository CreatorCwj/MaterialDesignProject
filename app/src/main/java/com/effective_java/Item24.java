package com.effective_java;

import java.text.SimpleDateFormat;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.TimeZone;

import static java.util.Calendar.JANUARY;

/**
 * Created by cwj on 16/11/3.
 */
public class Item24 {

    public static void main(String[] args) {
        TimeZone gmtZone = TimeZone.getTimeZone("GMT");

        GregorianCalendar calendar = new GregorianCalendar(2030, JANUARY, 1, 0, 0, 0);
        calendar.setTimeZone(gmtZone);

        SimpleDateFormat sdf = new SimpleDateFormat("E, d MMM y HH:mm:ss 'GMT'", Locale.US);
        sdf.setTimeZone(gmtZone);

        System.out.println(sdf.format(calendar.getTime()));
    }

    private static boolean out(int i) {
        System.out.println("out" + i);
        return i % 2 == 0;
    }
}
