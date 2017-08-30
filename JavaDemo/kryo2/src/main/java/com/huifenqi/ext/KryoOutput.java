package com.huifenqi.ext;

import com.esotericsoftware.kryo.io.Output;

import java.io.OutputStream;

/**
 * Created by t3tiger on 2017/7/5.
 */
public class KryoOutput extends Output {
    public KryoOutput(OutputStream outputStream) {
        super(4096, 4096 * 2);
        if (outputStream == null) {
            throw new IllegalArgumentException("outputStream cannot be null.");
        } else {
            this.outputStream = outputStream;
        }
    }
}
