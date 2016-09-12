package com.effective_java;

import android.support.annotation.NonNull;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.TreeSet;

/**
 * Created by cwj on 16/8/23.
 * Comparable
 */
public class Item12 implements Comparator<Item12>{

    public static void main(String[] args) {
        TreeSet<Item12Sub1> treeSet = new TreeSet<>();
        treeSet.add(new Item12Sub1());
        new Comparator<String>() {
            @Override
            public int compare(String lhs, String rhs) {
                return 0;
            }
        };
    }

    @Override
    public int compare(Item12 lhs, Item12 rhs) {
        return 0;
    }

    @Override
    public boolean equals(Object o) {
        return super.equals(o);
    }
}

class Item12Sub1 implements Comparable<Item12Sub1> {

    private int a;
    private float b;
    private BigDecimal bd;

    @Override
    public int compareTo(@NonNull Item12Sub1 another) {//和域之间比较顺序有关
        if (a > another.a)
            return 1;
        if (a < another.a)
            return -1;
        if (Float.compare(b, another.b) > 0)
            return 1;
        if (Float.compare(b, another.b) < 0)
            return -1;
        if (bd.compareTo(another.bd) > 0)//bd不能为null需要考虑
            return 1;
        if (bd.compareTo(another.bd) < 0)
            return -1;
        return 0;//正负0值
    }
}
