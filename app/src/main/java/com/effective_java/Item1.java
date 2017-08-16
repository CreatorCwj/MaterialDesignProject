package com.effective_java;

import java.util.EnumSet;

/**
 * Created by cwj on 16/8/11.
 * 静态工厂方法创建对象
 */
public class Item1 {

    enum Season {
        SPRING, SUMMER, AUTUMN, WINTER
    }

    public void test1() {
        Boolean btrue = Boolean.valueOf(true);

        EnumSet<Season> emptyEnumSet = EnumSet.noneOf(Season.class);
        EnumSet<Season> enumSet = EnumSet.allOf(Season.class);
        emptyEnumSet.addAll(enumSet);

        TestMap<String, Integer> map = TestMap.newInstance();

        TestStaticFactory testStaticFactory = TestStaticFactory.valueOf(true);
        TestStaticFactory testStaticFactory2 = TestStaticFactory.valueOf(true);
        if (testStaticFactory == testStaticFactory2) {//唯一实例所以＝＝即可辨别值是否相同
            //xxx
        }
    }

}

class TestMap<K, V> {

    public static <K, V> TestMap<K, V> newInstance() {
        return new TestMap<>();
    }
}

class TestStaticFactory {

    private static final TestStaticFactory first = new TestStaticFactory();
    private static final TestStaticFactory second = new TestStaticFactory();

    public TestStaticFactory() {

    }

    public static TestStaticFactory valueOf(boolean b) {
        return b ? first : second;
    }
}
