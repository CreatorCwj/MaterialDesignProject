package com.effective_java;

/**
 * Created by cwj on 16/8/13.
 * Singleton模式强化
 */
public class Item3 {

    public static void main(String[] args) {
        Singleton1 singleton1 = Singleton1.singleton1;
        Singleton2 singleton2 = Singleton2.getInstance();
        Singleton3 singleton3 = Singleton3.INSTANCE;
        Singleton4 singleton4 = Singleton4.getInstance();
    }

}

class Singleton1 {

    public static final Singleton1 singleton1 = new Singleton1();

    private Singleton1() {
        //...
    }

    //...
}

class Singleton2 {

    private static final Singleton2 singleton2 = new Singleton2();

    private Singleton2() {
        //...
    }

    public static Singleton2 getInstance() {//静态工厂方法可以修改里面的返回逻辑而不用客户做出修改
        return singleton2;
    }

    //...
}

enum Singleton3 {//枚举保证了序列化时的唯一性

    INSTANCE;

    Singleton3() {
        //...
    }

    //...

}

class Singleton4 {

    private static Singleton4 singleton4;

    private Singleton4() {
        //...
    }

    public synchronized static Singleton4 getInstance() {//要在方法上加同步控制,否则也有可能出现new两次的情况
        if (singleton4 == null) {
            singleton4 = new Singleton4();
        }
        return singleton4;
    }

    //...
}
