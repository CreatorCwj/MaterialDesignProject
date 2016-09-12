package com.materialdesign;

import android.app.Application;
import android.test.ApplicationTestCase;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * <a href="http://d.android.com/tools/testing/testing_android.html">Testing Fundamentals</a>
 */
public class ApplicationTest extends ApplicationTestCase<Application> {
    public ApplicationTest() {
        super(Application.class);
    }

    public void test(){
//        add(20000000000);
        int d = 20160919;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd");
        SimpleDateFormat simpleDateFormat2 = new SimpleDateFormat("yyyy.MM.dd");
        SimpleDateFormat simpleDateFormat3 = new SimpleDateFormat("MM.dd");
        String s = Integer.toString(d);
        try {
            Date date = simpleDateFormat.parse(s);
            System.out.println(date.getTime());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        try {
            Date date2 = simpleDateFormat2.parse(s);
            System.out.println(date2.getTime());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        try {
            Date date3 = simpleDateFormat3.parse(s);
            System.out.println(date3.getTime());
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    public void add(int a){
        System.out.println(""+a);
    }
}