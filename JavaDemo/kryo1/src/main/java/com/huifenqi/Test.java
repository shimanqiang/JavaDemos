package com.huifenqi;

import com.esotericsoftware.kryo.Kryo;
import com.huifenqi.ext.KryoFactory;
import com.huifenqi.ext.KryoOutput;
import com.huifenqi.model.Man;
import com.huifenqi.model.Persion;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by t3tiger on 2017/7/5.
 */
public class Test {

    public static void main(String[] args) throws Exception {
        Persion persion = createPersion();

        long start = System.currentTimeMillis();

        Kryo kryo = KryoFactory.createOrGetKryo();
//        kryo.setRegistrationRequired(false);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        FileOutputStream fos = new FileOutputStream("d:/data.bin");
        KryoOutput output = new KryoOutput(fos);
        try {
            //kryo.register(persion.getClass());
            //Class type = kryo.getClassResolver().getRegistration(33).getType();
            //System.out.println(type);
            kryo.writeClassAndObject(output, persion);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }
        output.flush();
        output.close();

        System.out.println("write耗时：" + (System.currentTimeMillis() - start)  + "毫秒");
    }

    private static Persion createPersion() {
        Man p = new Man();
        p.setB1(true);
        p.setAdd1("add1");
        String s = "";
        for (int i = 0; i < 1000; i++) {
            s += i + "\t文档比较少，有些使用方法要看代码才能理解，最新版2.14有bug，不能正确反序列化map类型";
        }
        p.setS1(s);
        Map map = new HashMap();
        map.put("sdkfj", "sdfs");
        p.setM1(map);
        return p;
    }

}
