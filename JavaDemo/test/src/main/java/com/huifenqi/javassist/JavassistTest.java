package com.huifenqi.javassist;

import javassist.ClassPool;
import javassist.CtClass;
import javassist.Loader;

public class JavassistTest {

    public static void main(String[] args) throws Exception {
        ClassPool pool = ClassPool.getDefault();
        //pool.appendClassPath(new javassist.LoaderClassPath(MyInterfaceImpl.class.getClassLoader()));

        //MyInterfaceImpl myInterfaceImpl = new MyInterfaceImpl();
        //Loader cl = new Loader(pool);
        //cl.loadClass("com.huifenqi.javassist.MyInterfaceImpl");

        CtClass cc = pool.get("com.huifenqi.javassist.MyInterfaceImpl");
        cc.addInterface(pool.get("com.huifenqi.javassist.MyInterface"));
        cc.writeFile();

        final Class aClass = cc.toClass();
//        final Class aClass = cc.toClass(myInterfaceImpl.getClass().getClassLoader(),myInterfaceImpl.getClass().getProtectionDomain());
        System.out.println(aClass);
        final MyInterface o = (MyInterface) aClass.newInstance();
        o.test();
    }

}
