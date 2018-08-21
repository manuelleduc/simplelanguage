package com.oracle.truffle.sl.rev;

import com.oracle.truffle.api.CompilerDirectives;
import com.oracle.truffle.api.dsl.Fallback;
import com.oracle.truffle.api.dsl.Specialization;
import com.oracle.truffle.sl.SLException;
import com.oracle.truffle.sl.nodes.expression.SLSubNode;
import com.oracle.truffle.sl.runtime.SLBigNumber;

import java.util.HashMap;
import java.util.Map;

public abstract class MySLSubNodeT implements SLSubNodeT {
    private final static Map<SLSubNode, SLSubNodeT> cache = new HashMap<>();
    private final ExecSLRevisitor alg;
    private final SLSubNode it;

    private MySLSubNodeT(ExecSLRevisitor alg, SLSubNode it) {
        this.alg = alg;
        this.it = it;
    }

    public static SLSubNodeT INSTANCE(ExecSLRevisitor alg, SLSubNode it) {
        if (!cache.containsKey(it)) cache.put(it, /*new MySLSubNodeT(alg, it)*/null);
        return cache.get(it);
    }


    @Specialization(rewriteOn = ArithmeticException.class)
    protected long sub(long left, long right) {
        return Math.subtractExact(left, right);
    }

    @Specialization
    @CompilerDirectives.TruffleBoundary
    protected SLBigNumber sub(SLBigNumber left, SLBigNumber right) {
        return new SLBigNumber(left.getValue().subtract(right.getValue()));
    }

    @Fallback
    protected Object typeError(Object left, Object right) {
        throw SLException.typeError(it, left, right);
    }
}
