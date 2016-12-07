package com.jvm;

import java.util.Locale;

/**
 * Created by cwj on 16/8/14.
 * 类加载时候常量进入常量池(final在编译时即可进入)
 */
public class Test1 extends Test1Parent {

    static {
        System.out.println("Test1 load");
    }

    public static void main(String[] args) {
        double value = 1.25;
        String str = String.format(Locale.CHINA, "%.1f%%", -2.56);
        System.out.println(str);
//        int b = Test1Sub1.A;
//        System.out.println("" + b);
//        Test1Sub1.m();
//        new Test1Sub1().m2();
//        int l = Integer.parseInt("123");
//        System.out.println("" + l);
    }
}

class Test1Parent {
    static {
        System.out.println("Test1Parent load");
    }
}

class Test1Sub1 {

    public static final int A = 1;
    public static int B = 1;
    private int s = 0;

    static {
        B = 2;
        System.out.println("" + A + " " + B);
    }

    {
        s = 1;
    }

    public Test1Sub1() {
        m2();
    }

    public void m2() {
        System.out.println("" + s);
    }

    public static void m() {
        System.out.println("m");
    }
}
