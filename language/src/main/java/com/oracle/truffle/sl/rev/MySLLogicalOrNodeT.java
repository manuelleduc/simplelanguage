package com.oracle.truffle.sl.rev;

import com.oracle.truffle.sl.nodes.expression.SLLogicalOrNode;

import java.util.HashMap;
import java.util.Map;

public class MySLLogicalOrNodeT extends MySLShortCircuitNodeT implements SLLogicalOrNodeT {
    private final static Map<SLLogicalOrNode, SLLogicalOrNodeT> cache = new HashMap<>();
    private final ExecSLRevisitor alg;
    private final SLLogicalOrNode it;

    private MySLLogicalOrNodeT(ExecSLRevisitor alg, SLLogicalOrNode it) {
        super(alg, it);
        this.alg = alg;
        this.it = it;
    }

    public static SLLogicalOrNodeT INSTANCE(ExecSLRevisitor alg, SLLogicalOrNode it) {
        if (!cache.containsKey(it)) cache.put(it, new MySLLogicalOrNodeT(alg, it));
        return cache.get(it);
    }

    @Override
    public boolean isEvaluateRight(boolean left) {
        return !left;
    }

    @Override
    public boolean execute(boolean left, boolean right) {
        return left || right;
    }
}
