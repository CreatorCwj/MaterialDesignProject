package com.test;

import com.google.gson.Gson;

/**
 * Created by cwj on 17/2/15.
 */

public class Test {

    public static void main(String[] args) {
        String str = "{\"id\":123456789,\"idLong\":77559663}";
        String json = new Gson().toJson(str);
    }

}