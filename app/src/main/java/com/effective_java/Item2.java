package com.effective_java;

/**
 * Created by cwj on 16/8/12.
 * Builder模式代替构造参数解决多个可选参数构造的问题
 */
public class Item2 {

    public void test() {
        MyView myView = new MyView.Builder(true, 1).size(10).scale(5).build();
    }
}

class MyView {

    private final boolean canRefresh;
    private final int items;

    private int size;
    private int scale;

    public MyView(Builder builder) {
        this.canRefresh = builder.canRefresh;
        this.items = builder.items;
        this.size = builder.size;
        this.scale = builder.scale;
        if (items < 0) {//judge
            throw new IllegalArgumentException("items can not less than 0");
        }
    }

    public static class Builder {

        private final boolean canRefresh;
        private final int items;

        private int size = 0;
        private int scale = 0;

        public Builder(boolean canRefresh, int items) {
            this.canRefresh = canRefresh;
            this.items = items;
        }

        public Builder size(int size) {
            this.size = size;
            return this;
        }

        public Builder scale(int scale) {
            this.scale = scale;
            return this;
        }

        public MyView build() {
            return new MyView(this);
        }
    }
}
