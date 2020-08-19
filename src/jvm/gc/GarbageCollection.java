package jvm.gc;

import org.junit.Test;

public class GarbageCollection {

    public static GarbageCollection obj;//只能是类变量，静态的，否则编译出错
    private byte[] bigSize = new byte[5 * 1024 * 1024];
    Object refrence = null;

    public static void main(String[] args) {

    }
    @Test
    public void test001(){
        GarbageCollection obj001 = new GarbageCollection();
        GarbageCollection obj002 = new GarbageCollection();

        obj001.refrence = obj002;
        obj002.refrence = obj001;

        obj001 = null;
        obj002 = null;

        System.gc();
        try {
            Thread.sleep(1000000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void test002() {
        try {
            obj = new GarbageCollection();
            obj = null;
            System.gc();
            System.out.println("first time gc");

            Thread.sleep(2000);

            if (obj == null) {
                System.out.println("obj is dead");
            } else {
                System.out.println("obj is still living");
            }
            obj = null;
            System.gc();
            System.out.println("sencond time gc");
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if (obj == null) {
                System.out.println("obj is dead");
            } else {
                System.out.println("obj is still living");
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
        System.out.println("override the finalize~");
        obj = this;
    }



}
