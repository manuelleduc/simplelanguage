package com.oracle.truffle.sl.rev;

import com.oracle.truffle.sl.nodes.SLStatementNode;

import java.util.HashMap;
import java.util.Map;

public abstract class MySLStatementNodeT implements SLStatementNodeT {
    private final static Map<SLStatementNode, SLStatementNodeT> cache = new HashMap<>();
    private final ExecSLRevisitor alg;
    private final SLStatementNode it;

    private MySLStatementNodeT(ExecSLRevisitor alg, SLStatementNode it) {
        this.alg = alg;
        this.it = it;
    }

    public static SLStatementNodeT INSTANCE(ExecSLRevisitor alg, SLStatementNode it) {
        if (!cache.containsKey(it)) cache.put(it, /*new MySLStatementNodeT(alg, it)*/ null);
        return cache.get(it);
    }
}
