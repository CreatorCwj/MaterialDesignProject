package testpmd;

/**
 * Created by cwj on 17/4/7.
 */

public class TestMultiThread {

    public static void main(String[] args) {
//        Volatile v = new Volatile();
//        v.start();
//        while (Thread.activeCount() > 1) {
//            Thread.yield();
//        }
        m1(new Object());
    }

    private static void m1(Object o1, Object... objects) {
        if (o1 == null) {
            throw new IllegalArgumentException("o1 can not be null");
        }
        if (objects == null) {
            throw new IllegalArgumentException("objects can not be null");
        }
    }
}

class Volatile {

    public volatile int i = 0;

    private void increase() {
        ++i;
    }

    public void start() {
        for (int j = 0; j < 2; j++) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    for (int k = 0; k < 1000; k++) {
                        increase();
                    }
                    System.out.println("result:" + i);
                }
            }).start();
        }
    }
}
