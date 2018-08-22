package com.oracle.truffle.sl.rev;

import com.oracle.truffle.sl.nodes.interop.SLForeignToSLTypeNode;

import java.util.HashMap;
import java.util.Map;

public class MySLForeignToSLTypeNodeT implements SLForeignToSLTypeNodeT {
    private final static Map<SLForeignToSLTypeNode, SLForeignToSLTypeNodeT> cache = new HashMap<>();
    private final ExecSLRevisitor alg;
    private final SLForeignToSLTypeNode it;

    private MySLForeignToSLTypeNodeT(ExecSLRevisitor alg, SLForeignToSLTypeNode it) {
        this.alg = alg;
        this.it = it;
    }

    public static SLForeignToSLTypeNodeT INSTANCE(ExecSLRevisitor alg, SLForeignToSLTypeNode it) {
        if (!cache.containsKey(it)) {
            cache.put(it, new MySLForeignToSLTypeNodeT(alg, it));
        }
        return cache.get(it);
    }
}
