package com.huifenqi;

import com.esotericsoftware.kryo.Registration;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.util.DefaultClassResolver;

/**
 * Created by t3tiger on 2017/7/5.
 */
public class DefaultClassResolverExt extends DefaultClassResolver {

    @Override
    protected Registration readName(Input input) {
        return super.readName(input);
    }
}
