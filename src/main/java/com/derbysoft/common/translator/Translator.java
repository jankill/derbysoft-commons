package com.derbysoft.common.translator;

public interface Translator<S, D> {

    D translate(S source);

}
