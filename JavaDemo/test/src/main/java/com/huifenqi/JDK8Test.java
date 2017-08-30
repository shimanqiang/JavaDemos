package com.huifenqi;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by t3tiger on 2017/7/10.
 */
public class JDK8Test {
    private static List<String> list;
    private static List<Person> persions = new ArrayList<>();

    static {
        list = Arrays.asList("Java", "Go", "C++", "Php", "Python");
        for (int i = 0;i<list.size();i++) {
            Person p = new Person();
            p.setAge(i);
            p.setName(list.get(i));
            persions.add(p);
        }
    }


    public static void main(String[] args) {
        new JDK8Test().test001();

    }

    public void test001() {
        String collect = list.stream().collect(Collectors.joining(",#"));
        System.out.println(collect);

    }
}

class Person{
    private int age;
    private String name;

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
