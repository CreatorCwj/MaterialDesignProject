package com.design_pattern;

/**
 * Created by cwj on 16/12/5.
 */
public class FactoryClinet {

    public static void main(String[] args) {
        Factory1 factory1 = new Factory1();
        factory1.createProduct(Product1.class).produced();
        factory1.createProduct(Product2.class).produced();
    }
}

abstract class AbstractFactory {
    public abstract <T extends IProduct> T createProduct(Class<T> tClass);
}

class Factory1 extends AbstractFactory{

    @Override
    public <T extends IProduct> T createProduct(Class<T> tClass) {
        T product = null;
        try {
            //noinspection unchecked
            product = (T) Class.forName(tClass.getName()).newInstance();
        }catch (Exception e){
        }
        return product;
    }
}

interface IProduct {
    void produced();
}

class Product1 implements IProduct {

    @Override
    public void produced() {
        System.out.println(this.getClass().getSimpleName() + "produced");
    }
}

class Product2 implements IProduct {

    @Override
    public void produced() {
        System.out.println(this.getClass().getSimpleName() + "produced");
    }
}
