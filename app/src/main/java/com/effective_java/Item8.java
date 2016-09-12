package com.effective_java;

import java.util.Arrays;

/**
 * Created by cwj on 16/8/18.
 * equals
 */
public class Item8 {

    public static void main(String[] args) {
        Item8Sub1 item1 = new Item8Sub1(-0.0f, new String[]{"i1", "i2"}, null);
        Item8Sub1 item2 = new Item8Sub1(0.0f, new String[]{"i1", "i2"}, null);
        System.out.println(item1.equals(item2));
    }
}

final class Item8Sub1 {//final不让继承，instanceOf时其实和getClass一样

    private float a;
    private String[] b;
    private Item8Sub2 c;

    public Item8Sub1(float a, String[] b, Item8Sub2 c) {
        this.a = a;
        this.b = b;
        this.c = c;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this)//自反性
            return true;
        if (!(o instanceof Item8Sub1))//包括了非空性
            return false;
        Item8Sub1 tmp = (Item8Sub1) o;
        if (c == null) {
            return tmp.c == null;
        }
        return (Float.compare(a, tmp.a) == 0) && (Arrays.equals(b, tmp.b)) && (c.equals(tmp.c));
    }
}

final class Item8Sub2 {

    private int a;

    public Item8Sub2(int a) {
        this.a = a;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;
        if (!(o instanceof Item8Sub2))
            return false;
        Item8Sub2 tmp = (Item8Sub2) o;
        return a == tmp.a;
    }
}
