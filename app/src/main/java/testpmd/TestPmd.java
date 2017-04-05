package testpmd;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by cwj on 17/1/10.
 */
public class TestPmd<T extends String> {

    private T t;

    private int pmd;

    public int method(int i) throws Exception {
        List list = new ArrayList();
        list.add("ss");
        list.add(Integer.MIN_VALUE);
        cwj(list);

        new Inner().inner = 1;
        String str = "1";
        try {
            return Integer.parseInt(str);
        } catch (Exception e) {
            //do nothing
        }
        return (Integer.MIN_VALUE);
    }

    private void cwj(List<?> list){

    }

    public List<? super String> m1() {
        return null;
    }


    public class Inner{
        private int inner;

        private void test() throws Exception {
            pmd = 2;
            method(1);
        }
    }
}
