package jvm.analyseFun;

        import org.junit.Test;

public class Analyse {

    public String sout;
    public static void main(String[] args) throws InterruptedException {
        Analyse analyse = new Analyse();
        System.out.println(analyse.sout);
    }

    public Analyse() throws InterruptedException {
        Thread.sleep(1000);
        sout = "hellow";
    }

    @Test
    public void test001(){
        String s1 = "a";
        String s2 = "b";
        String s3 = "ab";
        String s4 = s1 + s2;
        System.out.println(s3 == s4);
    }

    /**
     * 没有用string builder的方式，final修饰的是常量
     */
    @Test
    public void test002(){
        final String s1 = "a";
        final String s2 = "b";
        String s3 = "ab";
        String s4 = s1 + s2;
        System.out.println(s3 == s4);
    }

    @Test
    public void test003() {

        long startT = System.currentTimeMillis();
        method001(100000);
        long endT = System.currentTimeMillis();
        System.out.println("cost total times: " + (endT - startT));
        startT = System.currentTimeMillis();
        method001(100000);
        endT = System.currentTimeMillis();
        System.out.println("method 2 cost total times: " + (endT - startT));
    }

    public void method001(int highLevel) {
        String src = "";
        for (int i = 0; i < highLevel; i++) {
            src = src + "a";
        }
    }

    public void method002(int highLevel) {
        StringBuilder src = new StringBuilder();
        for (int i = 0; i < highLevel; i++) {
            src.append("a");
        }
    }

    @Test
    public void test004(){
        String str = new String("abc");
        String strN = new String("a") + new String("b");
    }

    @Test
    public void test005(){
        String s = new String("1");
        s.intern();
        String s2 = "1";
        System.out.println(s == s2);
    }

    @Test
    public void test006(){
        String s3 = new String("1") + new String("1");
        s3=s3.intern();

        String s4 = "11";
        System.out.println(s3 ==s4);
    }


    static final int MAX_COUNT = 1000 * 10000;
    static final String[] arr = new String[MAX_COUNT];
    @Test
    public void test007(){
        Integer[] data = new Integer[]{1,2,3,4,5,6,7,8,9};

        long start = System.currentTimeMillis();
        for (int i = 0; i < MAX_COUNT; i++) {
            arr[i] = new String(String.valueOf(i));
        }
        long endT = System.currentTimeMillis();
        System.out.println("cost time in total: " + (endT - start));

        start = System.currentTimeMillis();
        for (int i = 0; i < MAX_COUNT; i++) {
            arr[i] = new String(String.valueOf(i)).intern();
        }
        endT = System.currentTimeMillis();
        System.out.println("cost time in total: " + (endT - start));
    }
}
