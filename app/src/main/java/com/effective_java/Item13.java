package com.effective_java;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by cwj on 16/8/26.
 */
public class Item13 {

    public static void main(String[] args) {
//        Item13 b = Item13Sub1.a;
//        String s = Item13Sub1.s3;
        List<String> list = new ArrayList<>();
        list.add("1");
        list.add("2");
        list.add("3");
        for (int i = 0; i < list.size(); i=0) {
            list.remove(i);
            System.out.println(list.size());
        }
        System.out.println(list.size());
    }
}

class Item13Sub1 {

    //编译时可确定才会在编译时赋值，不会触发类加载，否则都会触发类加载
    private static final String s1 = "1";
    private static final String s2 = "23";
    public static final String s3 = s1 + s2;//宏变量

    public static final Item13 a = new Item13();//编译时不可确定

    static {
        System.out.println(a);
    }
}
