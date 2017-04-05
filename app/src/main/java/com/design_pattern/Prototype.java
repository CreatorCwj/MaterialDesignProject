package com.design_pattern;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by cwj on 17/3/27.
 */

public class Prototype {

    public static void main(String[] args) {
        A a = new A();
        a.add("new1");
        A clone = a.clone();
        clone.add("clone 1");
        clone.setI(2);
        clone.setS("new 2");
        System.out.println(a.toString());
        System.out.println(clone.toString());
    }
}

class A implements Cloneable {

    private int i = 1;
    private String s = "s";
    private ArrayList<String> list = new ArrayList<>();//clone时不可以加final,否则产生歧义(final怎么能再赋值呢?)
    private B b = new B();

    public B getB() {
        return b;
    }

    public void add(String s) {
        list.add(s);
    }

    public List<String> getList() {
        return list;
    }

    public int getI() {
        return i;
    }

    public void setI(int i) {
        this.i = i;
    }

    public String getS() {
        return s;
    }

    public void setS(String s) {
        this.s = s;
    }

    @Override
    public String toString() {
        return "" + i + s + list.toString() + b.hashCode();
    }

    @Override
    protected A clone() {
        A clone = null;
        try {
            clone = (A) super.clone();
            //noinspection unchecked
            clone.list = (ArrayList<String>) this.list.clone();//需要clone,否则是浅拷贝,不会拷贝引用对象(String除外)
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        return clone;
    }
}

class B {

    private final int b = 999;

    public int getB() {
        return b;
    }

    @Override
    public String toString() {
        return "" + b;
    }
}