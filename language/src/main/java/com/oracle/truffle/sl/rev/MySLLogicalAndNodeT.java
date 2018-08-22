package com.oracle.truffle.sl.rev;

import com.oracle.truffle.sl.nodes.expression.SLLogicalAndNode;

import java.util.HashMap;
import java.util.Map;

public class MySLLogicalAndNodeT extends MySLShortCircuitNodeT implements SLLogicalAndNodeT {
    private final static Map<SLLogicalAndNode, SLLogicalAndNodeT> cache = new HashMap<>();
    private final ExecSLRevisitor alg;
    private final SLLogicalAndNode it;

    private MySLLogicalAndNodeT(ExecSLRevisitor alg, SLLogicalAndNode it) {
        super(alg, it);
        this.alg = alg;
        this.it = it;
    }

    public static SLLogicalAndNodeT INSTANCE(ExecSLRevisitor alg, SLLogicalAndNode it) {
        if (!cache.containsKey(it)) {
            cache.put(it, new MySLLogicalAndNodeT(alg, it));
        }
        return cache.get(it);
    }

    /**
     * The right value does not need to be evaluated if the left value is already <code>false</code>
     * .
     */
    @Override
    public boolean isEvaluateRight(boolean left) {
        return left;
    }

    /**
     * Only if left and right value are true the result of the logical and is <code>true</code>. If
     * the second parameter is not evaluated, <code>false</code> is provided.
     */
    @Override
    public boolean execute(boolean left, boolean right) {
        return left && right;
    }
}
