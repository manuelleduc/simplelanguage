package com.oracle.truffle.sl.rev;

import com.oracle.truffle.sl.nodes.SLExpressionNode;

import java.util.HashMap;
import java.util.Map;

public abstract class MySLExpressionNodeT implements SLExpressionNodeT {
    private final static Map<SLExpressionNode, SLExpressionNodeT> cache = new HashMap<>();
    private final ExecSLRevisitor alg;
    private final SLExpressionNode it;

    private MySLExpressionNodeT(ExecSLRevisitor alg, SLExpressionNode it) {
        this.alg = alg;
        this.it = it;
    }

    public static SLExpressionNodeT INSTANCE(ExecSLRevisitor alg, SLExpressionNode it) {
        if (!cache.containsKey(it)) cache.put(it, /*new MySLExpressionNodeT(alg, it)*/ null);
        return cache.get(it);
    }


}
