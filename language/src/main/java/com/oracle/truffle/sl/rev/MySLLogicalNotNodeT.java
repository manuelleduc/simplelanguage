package com.oracle.truffle.sl.rev;

import com.oracle.truffle.api.dsl.Fallback;
import com.oracle.truffle.api.dsl.Specialization;
import com.oracle.truffle.sl.SLException;
import com.oracle.truffle.sl.nodes.expression.SLLogicalNotNode;

import java.util.HashMap;
import java.util.Map;

public abstract class MySLLogicalNotNodeT implements SLLogicalNotNodeT {
    private final static Map<SLLogicalNotNode, SLLogicalNotNodeT> cache = new HashMap<>();
    private final ExecSLRevisitor alg;
    private final SLLogicalNotNode it;

    private MySLLogicalNotNodeT(ExecSLRevisitor alg, SLLogicalNotNode it) {
        this.alg = alg;
        this.it = it;
    }

    public static SLLogicalNotNodeT INSTANCE(ExecSLRevisitor alg, SLLogicalNotNode it) {
        if (!cache.containsKey(it)) cache.put(it, /*new MySLLogicalNotNodeT(alg, it)*/ null);
        return cache.get(it);
    }

    @Specialization
    protected boolean doBoolean(boolean value) {
        return !value;
    }

    @Fallback
    protected Object typeError(Object value) {
        throw SLException.typeError(it, value);
    }
}
