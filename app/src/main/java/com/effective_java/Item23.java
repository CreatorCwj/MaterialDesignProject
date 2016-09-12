package com.effective_java;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by cwj on 16/9/6.
 * 泛型
 */
public class Item23 {

    public static void main(String[] args) {
        List<String> l1 = new ArrayList<>();
        List<Integer> l2 = new ArrayList<>();
        test(l1, l2);
        test2(l1);
//        List<String>[] l = new ArrayList<String>[2];
    }

    public static void test(List<?> l1, List<?> l2) {
        l1.add(null);
//        l1.add(new Item23());//通配符不能添加(除了null元素)
    }

    public static void test2(Collection<?> c1) {//通配符子类可以赋值，但是有具体元素类型后不同元素类型就不能赋值

    }
}
