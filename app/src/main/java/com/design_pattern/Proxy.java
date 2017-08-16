package com.design_pattern;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * Created by cwj on 17/2/12.
 * 代理模式
 */

public class Proxy {

    public static void main(String[] args) {
        //普通代理
        IPlayer normalProxy = new NormalPlayerProxy("陈文杰");
        normalProxy.login();
        System.out.println("------");
        //强制代理
        IPlayer forceProxy = new ForceGamePlayer("陈文杰").getProxy();
        forceProxy.login();
        System.out.println("------");
        //动态代理
        GamePlayer player = new GamePlayer("陈文杰");
        IPlayer dynamicProxy = DynamicProxy.getProxy(player);
        dynamicProxy.login();
    }
}

interface IPlayer {

    void login();
}

class GamePlayer implements IPlayer {

    private final String name;

    GamePlayer(String name) {
        this.name = name;
    }

    @Override
    public void login() {
        System.out.println("玩家: " + name + " 登陆成功!");
    }
}

class NormalPlayerProxy implements IPlayer {

    private final IPlayer player;

    //普通代理,不需要实际类:代理类里创建实际类
    NormalPlayerProxy(String name) {
        this.player = new GamePlayer(name);
    }

    @Override
    public void login() {
        System.out.println("预处理");
        player.login();
        System.out.println("最终处理");
    }
}

class ForceGamePlayer implements IPlayer {

    private IPlayer proxy;
    private final String name;

    ForceGamePlayer(String name) {
        this.name = name;
    }

    @Override
    public void login() {
        if (!isIllegal()) {
            System.out.println("请使用代理类操作对象");
            return;
        }
        System.out.println("玩家: " + name + " 登陆成功!");
    }

    //强制代理,必须得从实际类拿到代理类进行操作,各个方法都会有是否是从代理操作的校验
    IPlayer getProxy() {
        if (proxy == null) {
            proxy = new ForcePlayerProxy(this);
        }
        return proxy;
    }

    private boolean isIllegal() {
        return proxy != null;
    }
}

class ForcePlayerProxy implements IPlayer {

    private final IPlayer player;

    ForcePlayerProxy(IPlayer player) {
        this.player = player;
    }

    @Override
    public void login() {
        System.out.println("预处理");
        player.login();
        System.out.println("最终处理");
    }
}

class DynamicProxy {

    static IPlayer getProxy(IPlayer player) {
        ClassLoader classLoader = player.getClass().getClassLoader();
        Class<?>[] classes = player.getClass().getInterfaces();
        InvocationHandler handler = new PlayerHandler(player);
        //noinspection unchecked
        //反射动态生成指定接口,各个方法为空实现,调用时调用到指定的handler的invoke方法进行实际的操作(交由被代理者调用并进行其他处理)
        return (IPlayer) java.lang.reflect.Proxy.newProxyInstance(classLoader, classes, handler);
    }
}

class PlayerHandler implements InvocationHandler {

    private final IPlayer player;

    PlayerHandler(IPlayer player) {
        this.player = player;
    }

    //动态代理,每个方法实际执行的是被代理对象的该方法,可以进行拦截
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        Object result = method.invoke(player, args);
        if (method.getName().equalsIgnoreCase("login")) {
            System.out.println("有人登陆您的账户!");
        }
        return result;
    }
}
