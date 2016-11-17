package com.design_pattern;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by cwj on 16/11/21.
 */
public class Son extends Father {

    protected void method(HashMap map) {

    }

    @Override
    protected void method(Map map) {
        super.method(map);
    }

    private void m(){
        method(new LinkedHashMap());
    }

    public static void main(String[] args) {
        Son father = new Son();
        father.method(new LinkedHashMap());
    }
}
