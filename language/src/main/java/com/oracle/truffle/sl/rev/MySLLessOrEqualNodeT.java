package com.oracle.truffle.sl.rev;

import com.oracle.truffle.api.CompilerDirectives;
import com.oracle.truffle.api.dsl.Fallback;
import com.oracle.truffle.api.dsl.Specialization;
import com.oracle.truffle.sl.SLException;
import com.oracle.truffle.sl.nodes.expression.SLLessOrEqualNode;
import com.oracle.truffle.sl.runtime.SLBigNumber;

import java.util.HashMap;
import java.util.Map;

public abstract class MySLLessOrEqualNodeT implements SLLessOrEqualNodeT {
    private final static Map<SLLessOrEqualNode, SLLessOrEqualNodeT> cache = new HashMap<>();
    private final ExecSLRevisitor alg;
    private final SLLessOrEqualNode it;

    private MySLLessOrEqualNodeT(ExecSLRevisitor alg, SLLessOrEqualNode it) {
        this.alg = alg;
        this.it = it;
    }

    public static SLLessOrEqualNodeT INSTANCE(ExecSLRevisitor alg, SLLessOrEqualNode it) {
        if (!cache.containsKey(it)) cache.put(it, null /*new MySLLessOrEqualNodeT(alg, it)*/);
        return cache.get(it);
    }

    @Specialization
    protected boolean lessOrEqual(long left, long right) {
        return left <= right;
    }

    @Specialization
    @CompilerDirectives.TruffleBoundary
    protected boolean lessOrEqual(SLBigNumber left, SLBigNumber right) {
        return left.compareTo(right) <= 0;
    }

    @Fallback
    protected Object typeError(Object left, Object right) {
        throw SLException.typeError(it, left, right);
    }
}
