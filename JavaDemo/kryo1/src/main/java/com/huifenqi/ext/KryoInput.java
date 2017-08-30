package com.huifenqi.ext;

import com.esotericsoftware.kryo.io.Input;

import java.io.InputStream;

/**
 * Created by t3tiger on 2017/7/5.
 */
public class KryoInput extends Input {
    public KryoInput(InputStream inputStream) {
        super(4096);
        if (inputStream == null) {
            throw new IllegalArgumentException("inputStream cannot be null.");
        } else {
            this.inputStream = inputStream;
        }
    }
}
