package com.oracle.truffle.sl.rev;

import com.oracle.truffle.sl.nodes.SLUndefinedFunctionRootNode;

import java.util.HashMap;
import java.util.Map;

public class MySLUndefinedFunctionRootNodeT implements SLUndefinedFunctionRootNodeT {
    private final static Map<SLUndefinedFunctionRootNode, SLUndefinedFunctionRootNodeT> cache = new HashMap<>();
    private final ExecSLRevisitor alg;
    private final SLUndefinedFunctionRootNode it;

    public MySLUndefinedFunctionRootNodeT(ExecSLRevisitor alg, SLUndefinedFunctionRootNode it) {
        this.alg = alg;
        this.it = it;
    }

    public static SLUndefinedFunctionRootNodeT INSTANCE(ExecSLRevisitor alg, SLUndefinedFunctionRootNode it) {
        if (!cache.containsKey(it)) {
            cache.put(it, new MySLUndefinedFunctionRootNodeT(alg, it));
        }
        return cache.get(it);
    }
}
