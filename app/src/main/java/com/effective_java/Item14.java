package com.effective_java;

import android.widget.TextView;

/**
 * Created by cwj on 16/12/23.
 */
public class Item14 {

    public static void main(String[] args) {
        C c = new C();
        c.setC0(null);
        System.out.println("" + c.getArr()[0]);
    }
}

class C {

    private final TextView[] arr = new TextView[2];

    public void setC0(TextView i) {
        arr[0] = i;
    }

    public TextView[] getArr() {
        return arr;
    }
}
