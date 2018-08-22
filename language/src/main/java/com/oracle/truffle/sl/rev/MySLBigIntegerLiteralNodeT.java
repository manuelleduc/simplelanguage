package com.oracle.truffle.sl.rev;

import com.oracle.truffle.api.frame.VirtualFrame;
import com.oracle.truffle.sl.nodes.expression.SLBigIntegerLiteralNode;
import com.oracle.truffle.sl.runtime.SLBigNumber;

import java.util.HashMap;
import java.util.Map;

public class MySLBigIntegerLiteralNodeT implements SLBigIntegerLiteralNodeT {
    private final static Map<SLBigIntegerLiteralNode, SLBigIntegerLiteralNodeT> cache = new HashMap<>();
    private final ExecSLRevisitor alg;
    private final SLBigIntegerLiteralNode it;

    private MySLBigIntegerLiteralNodeT(ExecSLRevisitor alg, SLBigIntegerLiteralNode it) {
        this.alg = alg;
        this.it = it;
    }

    public static SLBigIntegerLiteralNodeT INSTANCE(ExecSLRevisitor alg, SLBigIntegerLiteralNode it) {
        if (!cache.containsKey(it)) {
            cache.put(it, new MySLBigIntegerLiteralNodeT(alg, it));
        }
        return cache.get(it);
    }

    @Override
    public SLBigNumber executeGeneric(VirtualFrame frame) {
        return it.getValue();
    }
}
