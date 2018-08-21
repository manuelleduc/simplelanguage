package com.oracle.truffle.sl.rev;

import com.oracle.truffle.api.CompilerDirectives;
import com.oracle.truffle.api.dsl.Fallback;
import com.oracle.truffle.api.dsl.Specialization;
import com.oracle.truffle.sl.SLException;
import com.oracle.truffle.sl.nodes.expression.SLLessThanNode;
import com.oracle.truffle.sl.runtime.SLBigNumber;

import java.util.HashMap;
import java.util.Map;

public abstract class MySLLessThanNodeT implements SLLessThanNodeT {
    private final static Map<SLLessThanNode, SLLessThanNodeT> cache = new HashMap<>();
    private final ExecSLRevisitor alg;
    private final SLLessThanNode it;

    private MySLLessThanNodeT(ExecSLRevisitor alg, SLLessThanNode it) {
        this.alg = alg;
        this.it = it;
    }

    public static SLLessThanNodeT INSTANCE(ExecSLRevisitor alg, SLLessThanNode it) {
        if (!cache.containsKey(it)) cache.put(it, /*new MySLLessThanNodeT(alg, it)*/ null);
        return cache.get(it);
    }

    @Specialization
    protected boolean lessThan(long left, long right) {
        return left < right;
    }

    @Specialization
    @CompilerDirectives.TruffleBoundary
    protected boolean lessThan(SLBigNumber left, SLBigNumber right) {
        return left.compareTo(right) < 0;
    }

    @Fallback
    protected Object typeError(Object left, Object right) {
        throw SLException.typeError(it, left, right);
    }
}
