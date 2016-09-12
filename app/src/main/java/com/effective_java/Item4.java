package com.effective_java;

/**
 * Created by cwj on 16/8/13.
 * 私有构造器实现不可实例化
 */
public class Item4 {

    public static void main(String[] args) {
//        Utility utility = new Utility();
    }
}

class Utility {//不可被子类化

    //不可被调用创建对象
    private Utility() {
        //也可抛出一个异常,避免内部不小心调用
    }
}
