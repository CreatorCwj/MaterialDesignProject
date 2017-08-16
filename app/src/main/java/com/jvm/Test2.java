package com.jvm;

/**
 * Created by cwj on 16/12/18.
 */
public class Test2 {

    private int a;

    {
        setA(-1);
    }

    public Test2() {
        setA(2);
    }

    public void setA(int a) {
        this.a = a;
        System.out.println("" + a);
    }

    public static void main(String[] args) {
        Test2 test2 = new Test2() {
            {
                setA(1);
            }
        };
        test2.setA(3);
    }
}
