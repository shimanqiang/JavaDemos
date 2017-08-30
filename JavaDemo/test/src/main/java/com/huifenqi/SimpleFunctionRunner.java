package com.huifenqi;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by t3tiger on 2017/7/3.
 */
public class SimpleFunctionRunner {
    private List<SimpleFunction> list = new ArrayList<>();

    public void register(SimpleFunction simpleFunction) {
        list.add(simpleFunction);
    }


    public void exec(Object input, Object output) {
        Object nextInput = input;
        for (SimpleFunction sf : list) {
            nextInput = sf.invoke(nextInput);
        }
    }
}
