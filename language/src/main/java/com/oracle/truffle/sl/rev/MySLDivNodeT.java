package com.oracle.truffle.sl.rev;

import com.oracle.truffle.api.CompilerDirectives;
import com.oracle.truffle.api.dsl.Fallback;
import com.oracle.truffle.api.dsl.Specialization;
import com.oracle.truffle.sl.SLException;
import com.oracle.truffle.sl.nodes.expression.SLDivNode;
import com.oracle.truffle.sl.runtime.SLBigNumber;

import java.util.HashMap;
import java.util.Map;

public abstract class MySLDivNodeT implements SLDivNodeT {
    private final static Map<SLDivNode, SLDivNodeT> cache = new HashMap<>();
    private final ExecSLRevisitor alg;
    private final SLDivNode it;

    private MySLDivNodeT(ExecSLRevisitor alg, SLDivNode it) {
        this.alg = alg;
        this.it = it;
    }

    public static SLDivNodeT INSTANCE(ExecSLRevisitor alg, SLDivNode it) {
        if (!cache.containsKey(it)) cache.put(it, /*new MySLDivNodeT(alg, it)*/null);
        return cache.get(it);
    }

    @Specialization(rewriteOn = ArithmeticException.class)
    protected long div(long left, long right) throws ArithmeticException {
        long result = left / right;
        /*
         * The division overflows if left is Long.MIN_VALUE and right is -1.
         */
        if ((left & right & result) < 0) {
            throw new ArithmeticException("long overflow");
        }
        return result;
    }

    @Specialization
    @CompilerDirectives.TruffleBoundary
    protected SLBigNumber div(SLBigNumber left, SLBigNumber right) {
        return new SLBigNumber(left.getValue().divide(right.getValue()));
    }

    @Fallback
    protected Object typeError(Object left, Object right) {
        throw SLException.typeError(it, left, right);
    }
}
