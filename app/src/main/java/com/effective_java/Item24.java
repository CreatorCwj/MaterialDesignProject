package com.effective_java;

/**
 * Created by cwj on 16/11/3.
 */
public class Item24 {

    public static void main(String[] args) {
        System.out.println(out(0) || out(1));
    }

    private static boolean out(int i) {
        System.out.println("out" + i);
        return i % 2 == 0;
    }
}
