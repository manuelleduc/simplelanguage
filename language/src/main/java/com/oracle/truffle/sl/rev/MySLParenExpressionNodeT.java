package com.oracle.truffle.sl.rev;

import com.oracle.truffle.api.frame.VirtualFrame;
import com.oracle.truffle.api.nodes.UnexpectedResultException;
import com.oracle.truffle.sl.nodes.expression.SLParenExpressionNode;

import java.util.HashMap;
import java.util.Map;

public class MySLParenExpressionNodeT implements SLParenExpressionNodeT {
    private final static Map<SLParenExpressionNode, SLParenExpressionNodeT> cache = new HashMap<>();
    private final ExecSLRevisitor alg;
    private final SLParenExpressionNode it;

    private MySLParenExpressionNodeT(ExecSLRevisitor alg, SLParenExpressionNode it) {
        this.alg = alg;
        this.it = it;
    }

    public static SLParenExpressionNodeT INSTANCE(ExecSLRevisitor alg, SLParenExpressionNode it) {
        if (!cache.containsKey(it)) cache.put(it, new MySLParenExpressionNodeT(alg, it));
        return cache.get(it);
    }

    @Override
    public Object executeGeneric(VirtualFrame frame) {
        return alg.$(it.getExpression()).executeGeneric(frame);
    }

    @Override
    public long executeLong(VirtualFrame frame) throws UnexpectedResultException {
        return alg.$(it.getExpression()).executeLong(frame);
    }

    @Override
    public boolean executeBoolean(VirtualFrame frame) throws UnexpectedResultException {
        return alg.$(it.getExpression()).executeBoolean(frame);
    }
}
