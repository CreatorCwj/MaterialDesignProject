package testpmd;

/**
 * Created by cwj on 17/1/10.
 */
public class TestPmd {

    public int method(int i) throws Exception {
        String str = "1";
        try {
            return Integer.parseInt(str);
        } catch (Exception e) {
            //do nothing
        }
        return (Integer.MIN_VALUE);
    }

    public void m1(){
        return;
    }

}
