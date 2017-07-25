package com.test;

/**
 * Created by cwj on 17/2/15.
 */

public class Test {

    public static void main(String[] args) {
        String[] strs = new String[]{"abc"};
        Object[] objs = strs;
        String res = strs[0];
        objs[0] = 1;
        res = strs[0];
    }

}