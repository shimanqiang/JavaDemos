package com.huifenqi;

import com.esotericsoftware.kryo.Kryo;
import com.huifenqi.ext.KryoFactory;
import com.huifenqi.ext.KryoInput;
import com.huifenqi.ext.KryoOutput;
import com.huifenqi.model.Persion;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

/**
 * Hello world!
 */
public class App {
    public static void main(String[] args) {
        System.out.println("Hello World!");


        Persion persion = createPersion();

        long start = System.currentTimeMillis();

        Kryo kryo = KryoFactory.createOrGetKryo();

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        KryoOutput output = new KryoOutput(baos);
        try {
            kryo.register(persion.getClass());
            kryo.writeClassAndObject(output, persion);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }
        output.flush();
        output.close();

        byte[] data = baos.toByteArray();


        ByteArrayInputStream bais = new ByteArrayInputStream(data);
        KryoInput input = new KryoInput(bais);
        Object o = kryo.readClassAndObject(input);
        input.close();


        System.out.println(o.getClass().getName());

        if (o instanceof Persion) {
            Persion p = (Persion) o;
//            System.out.println(p.getS1());
//            System.out.println(p.isB1());
        }


        System.out.println("耗时：" + (System.currentTimeMillis() - start)  + "毫秒");

    }

    private static Persion createPersion() {
        Persion p = new Persion();
//        p.setB1(true);
        String s = "";
        for (int i = 0; i < 1000; i++) {
            s += i + "\t文档比较少，有些使用方法要看代码才能理解，最新版2.14有bug，不能正确反序列化map类型";
        }
//        p.setS1(s);
        return p;
    }

}
