package com.huifenqi;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.KryoException;
import com.esotericsoftware.kryo.Registration;
import com.huifenqi.ext.KryoFactory;
import com.huifenqi.ext.KryoInput;
import com.huifenqi.ext.KryoOutput;
import com.huifenqi.model.Man;
import com.huifenqi.model.Persion;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Map;

/**
 * Created by t3tiger on 2017/7/5.
 */
public class Test {

    public static void main(String[] args) throws Exception {
        long start = System.currentTimeMillis();

        Kryo kryo = KryoFactory.createOrGetKryo();
        kryo.setRegistrationRequired(false);

        FileInputStream fis = new FileInputStream("d:/data.bin");
        KryoInput input = new KryoInput(fis);
        Object o = null;
        try {
//            int ClassID = input.readVarInt(true);
//            System.out.println(ClassID);
//            Registration registration = kryo.getClassResolver().getRegistration(ClassID - 2);
//            System.out.println("@@" + registration);
//            kryo.register(registration);

            o = kryo.readClassAndObject(input);
        } catch (KryoException e) {
            e.printStackTrace();
        }
        input.close();

        System.out.println(o.getClass().getName());

        if (o instanceof Man) {
            Man p = (Man) o;
            System.out.println(p.getS1());
            //System.out.println(p.isB1());
            System.out.println(p.getIntTest());

            Map<String, String> map = p.getM1();
            System.out.println(map.get("sdkfj"));
        }


        System.out.println("read耗时：" + (System.currentTimeMillis() - start) + "毫秒");
    }

}
