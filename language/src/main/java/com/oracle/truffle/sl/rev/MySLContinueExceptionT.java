package com.oracle.truffle.sl.rev;

import com.oracle.truffle.sl.nodes.controlflow.SLContinueException;

import java.util.HashMap;
import java.util.Map;

public class MySLContinueExceptionT implements SLContinueExceptionT {
    private final static Map<SLContinueException, SLContinueExceptionT> cache = new HashMap<>();
    private final ExecSLRevisitor alg;
    private final SLContinueException it;

    private MySLContinueExceptionT(ExecSLRevisitor alg, SLContinueException it) {
        this.alg = alg;
        this.it = it;
    }

    public static SLContinueExceptionT INSTANCE(ExecSLRevisitor alg, SLContinueException it) {
        if (!cache.containsKey(it)) {
            cache.put(it, new MySLContinueExceptionT(alg, it));
        }
        return cache.get(it);
    }
}
