package com.effective_java;

/**
 * Created by cwj on 16/8/23.
 * clone
 */
public class Item11 {

    public static void main(String[] args) {
        System.out.println(""+Math.floor(2.35f));
        System.out.println(""+Math.ceil(2.35f));
        System.out.println(""+Math.round(2.35f));
        System.out.println(""+Math.floor(2.65f));
        System.out.println(""+Math.ceil(2.65f));
        System.out.println(""+Math.round(2.65f));
        System.out.println(2.3f<2.3);
    }
}

class Item11Sub1 implements Cloneable {//此接口没有方法，但想实现object的clone方法必须实现此接口，否则报异常


    public Item11Sub1() {
    }

    /**
     * 通过构造方法来复制，还可以方便类型转换，返回的只要是Item11Sub1的子类即可
     */
    public Item11Sub1(Item11Sub1 item11Sub1) {

    }

    /**
     * 通过静态工厂方法手动复制，还可以方便类型转换，返回的只要是Item11Sub1的子类即可
     */
    public static Item11Sub1 newInstance(Item11Sub1 item11Sub1) {
        return new Item11Sub1();
    }

    @Override
    public Object clone() {
        try {
            return super.clone();//考虑深度拷贝(对象引用field重新创建，指向的还是原来的对象，要重新创建指向的对象)
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        return null;
    }
}
