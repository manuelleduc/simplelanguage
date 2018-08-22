package com.oracle.truffle.sl.rev;

import com.oracle.truffle.sl.nodes.call.SLDispatchNode;

import java.util.HashMap;
import java.util.Map;

public class MySLDispatchNodeT implements SLDispatchNodeT {
    private final static Map<SLDispatchNode, SLDispatchNodeT> cache = new HashMap<>();
    private final ExecSLRevisitor alg;
    private final SLDispatchNode it;

    private MySLDispatchNodeT(ExecSLRevisitor alg, SLDispatchNode it) {
        this.alg = alg;
        this.it = it;
    }

    public static SLDispatchNodeT INSTANCE(ExecSLRevisitor alg, SLDispatchNode it) {
        if (!cache.containsKey(it)) {
            cache.put(it, new MySLDispatchNodeT(alg, it));
        }
        return cache.get(it);
    }
}
