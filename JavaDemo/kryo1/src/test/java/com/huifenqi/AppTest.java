package com.huifenqi;

import com.huifenqi.model.Man;
import com.huifenqi.model.Persion;

/**
 * Unit test for simple App.
 */
public class AppTest {

    @org.junit.Test
    public void test001() {
        Man man = new Man();

        System.out.println(Persion.class.isAssignableFrom(man.getClass()));
        System.out.println(man instanceof Persion);
        System.out.println(man instanceof Man);
    }
}
