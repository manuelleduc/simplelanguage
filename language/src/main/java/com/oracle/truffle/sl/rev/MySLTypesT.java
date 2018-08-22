package com.oracle.truffle.sl.rev;

import com.oracle.truffle.sl.nodes.SLTypes;

import java.util.HashMap;
import java.util.Map;

public class MySLTypesT implements SLTypesT {
    private final static Map<SLTypes, SLTypesT> cache = new HashMap<>();
    private final ExecSLRevisitor alg;
    private final SLTypes it;

    private MySLTypesT(ExecSLRevisitor alg, SLTypes it) {
        this.alg = alg;
        this.it = it;
    }

    public static SLTypesT INSTANCE(ExecSLRevisitor alg, SLTypes it) {
        if (!cache.containsKey(it)) {
            cache.put(it, new MySLTypesT(alg, it));
        }
        return cache.get(it);
    }
}
