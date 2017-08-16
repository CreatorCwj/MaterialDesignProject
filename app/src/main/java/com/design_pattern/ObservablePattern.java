package com.design_pattern;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by cwj on 17/5/14.
 * 观察者模式
 */

public class ObservablePattern {

    public static void main(String[] args) {
        //被观察者
        JinSanPang jsp = new JinSanPang();
        //观察者
        jsp.addObserver(new ChinaSpy());
        jsp.addObserver(new USASpy());
        //出发事件，通知所有观察者
        jsp.announce();
    }
}

class JinSanPang extends Observable {

    public void announce() {
        System.out.println("金三胖：我要发射导弹");
        notifyObservers();
    }
}

class ChinaSpy implements Observer {

    @Override
    public void onUpdate() {
        System.out.println("中国间谍：警告金三胖");
    }
}

class USASpy implements Observer {

    @Override
    public void onUpdate() {
        System.out.println("美国间谍：干死金三胖");
    }
}

interface Observer {

    void onUpdate();
}

interface IObservable {

    void addObserver(Observer observer);

    void removeObserver(Observer observer);

    void notifyObservers();
}

class Observable implements IObservable {

    private final List<Observer> observers = new ArrayList<>();

    @Override
    public void addObserver(Observer observer) {
        if (!observers.contains(observer)) {
            observers.add(observer);
        }
    }

    @Override
    public void removeObserver(Observer observer) {
        observers.remove(observer);
    }

    @Override
    public void notifyObservers() {
        for (Observer observer : observers) {
            if (observer != null) {
                observer.onUpdate();
            }
        }
    }
}
