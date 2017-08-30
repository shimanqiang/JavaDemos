package com.huifenqi.ext;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.Serializer;

/**
 * Created by t3tiger on 2017/7/5.
 */
public class MyKryo extends Kryo {
    @Override
    public Serializer getDefaultSerializer(Class type) {
        return super.getDefaultSerializer(type);
    }
}
