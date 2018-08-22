package com.oracle.truffle.sl.rev;

import com.oracle.truffle.api.frame.VirtualFrame;
import com.oracle.truffle.api.nodes.UnexpectedResultException;
import com.oracle.truffle.sl.nodes.expression.SLLongLiteralNode;

import java.util.HashMap;
import java.util.Map;

public class MySLLongLiteralNodeT implements SLLongLiteralNodeT {
    private final static Map<SLLongLiteralNode, SLLongLiteralNodeT> cache = new HashMap<>();
    private final ExecSLRevisitor alg;
    private final SLLongLiteralNode it;

    public MySLLongLiteralNodeT(ExecSLRevisitor alg, SLLongLiteralNode it) {
        this.alg = alg;
        this.it = it;
    }

    public static SLLongLiteralNodeT INSTANCE(ExecSLRevisitor alg, SLLongLiteralNode it) {
        if (!cache.containsKey(it)) {
            cache.put(it, new MySLLongLiteralNodeT(alg, it));
        }
        return cache.get(it);
    }

    @Override
    public long executeLong(VirtualFrame frame) throws UnexpectedResultException {
        return it.getValue();
    }

    @Override
    public Object executeGeneric(VirtualFrame frame) {
        return it.getValue();
    }
}
