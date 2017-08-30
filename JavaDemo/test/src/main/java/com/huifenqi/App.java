package com.huifenqi;

import java.util.Iterator;
import java.util.ServiceLoader;

/**
 * Hello world!
 */
public class App {
    public static void main(String[] args) {
        System.out.println("Hello World!");

//        ServiceLoader<Computer> loader = ServiceLoader.load(Computer.class);
//
//        Iterator<Computer> iterator = loader.iterator();
//        //System.out.println(iterator.hasNext());
//
//        while (iterator.hasNext()) {
//            Computer next = iterator.next();
//            int add = next.computer(2, 3);
//            System.out.println(next.getClass().getSimpleName() + ":" + add);
//        }
    }
}
