package com.huifenqi.model;

import java.util.List;
import java.util.Map;

/**
 * Created by t3tiger on 2017/7/5.
 */
public class Persion {
    private int intTest;

    private Boolean b2;

    private int i1;
    private Integer i2;



    private List<String> l1;
    private List<Persion> l2;

    private Map<String, String> m1;
    private Map<String, Persion> m2;




    public Boolean getB2() {
        return b2;
    }

    public void setB2(Boolean b2) {
        this.b2 = b2;
    }

    public int getI1() {
        return i1;
    }

    public void setI1(int i1) {
        this.i1 = i1;
    }

    public Integer getI2() {
        return i2;
    }

    public void setI2(Integer i2) {
        this.i2 = i2;
    }


    public List<String> getL1() {
        return l1;
    }

    public void setL1(List<String> l1) {
        this.l1 = l1;
    }

    public List<Persion> getL2() {
        return l2;
    }

    public void setL2(List<Persion> l2) {
        this.l2 = l2;
    }

    public Map<String, String> getM1() {
        return m1;
    }

    public void setM1(Map<String, String> m1) {
        this.m1 = m1;
    }

    public Map<String, Persion> getM2() {
        return m2;
    }

    public void setM2(Map<String, Persion> m2) {
        this.m2 = m2;
    }

    public int getIntTest() {
        return intTest;
    }

    public void setIntTest(int intTest) {
        this.intTest = intTest;
    }
}
