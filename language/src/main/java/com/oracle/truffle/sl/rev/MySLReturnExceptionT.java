package com.oracle.truffle.sl.rev;

import com.oracle.truffle.sl.nodes.controlflow.SLReturnException;

import java.util.HashMap;
import java.util.Map;

public class MySLReturnExceptionT implements SLReturnExceptionT {
    private final static Map<SLReturnException, SLReturnExceptionT> cache = new HashMap<>();
    private final ExecSLRevisitor alg;
    private final SLReturnException it;

    private MySLReturnExceptionT(ExecSLRevisitor alg, SLReturnException it) {
        this.alg = alg;
        this.it = it;
    }

    public static SLReturnExceptionT INSTANCE(ExecSLRevisitor alg, SLReturnException it) {
        if (!cache.containsKey(it)) {
            cache.put(it, new MySLReturnExceptionT(alg, it));
        }
        return cache.get(it);
    }
}
