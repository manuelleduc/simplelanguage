package com.oracle.truffle.sl.rev;

import com.oracle.truffle.api.CompilerDirectives;
import com.oracle.truffle.api.frame.VirtualFrame;
import com.oracle.truffle.sl.nodes.expression.SLFunctionLiteralNode;
import com.oracle.truffle.sl.runtime.SLFunction;

import java.util.HashMap;
import java.util.Map;

public class MySLFunctionLiteralNodeT implements SLFunctionLiteralNodeT {
    private final static Map<SLFunctionLiteralNode, SLFunctionLiteralNodeT> cache = new HashMap<>();
    private final ExecSLRevisitor alg;
    private final SLFunctionLiteralNode it;

    private MySLFunctionLiteralNodeT(ExecSLRevisitor alg, SLFunctionLiteralNode it) {
        this.alg = alg;
        this.it = it;
    }

    public static SLFunctionLiteralNodeT INSTANCE(ExecSLRevisitor alg, SLFunctionLiteralNode it) {
        if (!cache.containsKey(it)) {
            cache.put(it, new MySLFunctionLiteralNodeT(alg, it));
        }
        return cache.get(it);
    }

    @Override
    public SLFunction executeGeneric(VirtualFrame frame) {
        if (it.getCachedFunction() == null) {
            /* We are about to change a @CompilationFinal field. */
            CompilerDirectives.transferToInterpreterAndInvalidate();
            /* First execution of the node: lookup the function in the function registry. */
            it.setCachedFunction(it.getReference().get().getFunctionRegistry().lookup(it.getFunctionName(), true));
        }
        return it.getCachedFunction();
    }
}
