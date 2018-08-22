package com.oracle.truffle.sl.rev;

import com.oracle.truffle.api.frame.VirtualFrame;
import com.oracle.truffle.sl.nodes.expression.SLStringLiteralNode;

import java.util.HashMap;
import java.util.Map;

public class MySLStringLiteralNodeT implements SLStringLiteralNodeT {
    private final static Map<SLStringLiteralNode, SLStringLiteralNodeT> cache = new HashMap<>();
    private final ExecSLRevisitor alg;
    private final SLStringLiteralNode it;

    public MySLStringLiteralNodeT(ExecSLRevisitor alg, SLStringLiteralNode it) {
        this.alg = alg;
        this.it = it;
    }

    public static SLStringLiteralNodeT INSTANCE(ExecSLRevisitor alg, SLStringLiteralNode it) {
        if (!cache.containsKey(it)) {
            cache.put(it, new MySLStringLiteralNodeT(alg, it));
        }
        return cache.get(it);
    }

    @Override
    public String executeGeneric(VirtualFrame frame) {
        return it.getValue();
    }
}
