package com.design_pattern;

import java.util.Locale;

/**
 * Created by cwj on 17/2/15.
 */

public class Test {

    public static void main(String[] args) {
        long data = 123456789;
        System.out.println(String.format(Locale.CHINA, "%,d", data));
    }

}