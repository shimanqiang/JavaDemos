package com.huifenqi;

/**
 * Created by t3tiger on 2017/7/3.
 */

@FunctionalInterface
public interface SimpleFunction<I, O> {
    O invoke(I input);
}
