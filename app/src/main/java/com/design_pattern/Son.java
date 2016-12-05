package com.design_pattern;

import java.util.HashMap;

/**
 * Created by cwj on 16/11/21.
 */
public class Son {

    private static final Son son = new Son();

    static {
        System.out.println(son.toString());
    }

    private Son() {
    }

    public static Son getInstance(){
        return son;
    }

}
