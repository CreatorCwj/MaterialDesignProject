package com.manager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by cwj on 16/12/20.
 */
public abstract class BaseManager {

    private View view;

    public BaseManager(LayoutInflater inflater, ViewGroup container) {
        view = generateView(inflater, container);
    }

    protected abstract View generateView(LayoutInflater inflater, ViewGroup container);

    public abstract void updView(Object data);

    final public View getView() {
        return view;
    }

}
