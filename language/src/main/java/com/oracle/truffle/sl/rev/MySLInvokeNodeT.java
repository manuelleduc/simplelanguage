package com.oracle.truffle.sl.rev;

import com.oracle.truffle.api.CompilerAsserts;
import com.oracle.truffle.api.frame.VirtualFrame;
import com.oracle.truffle.api.nodes.ExplodeLoop;
import com.oracle.truffle.sl.nodes.call.SLInvokeNode;

import java.util.HashMap;
import java.util.Map;

public class MySLInvokeNodeT implements SLInvokeNodeT {
    private final static Map<SLInvokeNode, SLInvokeNodeT> cache = new HashMap<>();
    private final ExecSLRevisitor alg;
    private final SLInvokeNode it;

    private MySLInvokeNodeT(ExecSLRevisitor alg, SLInvokeNode it) {
        this.alg = alg;
        this.it = it;
    }

    public static SLInvokeNodeT INSTANCE(ExecSLRevisitor alg, SLInvokeNode it) {
        if (!cache.containsKey(it)) cache.put(it, new MySLInvokeNodeT(alg, it));
        return cache.get(it);
    }

    @ExplodeLoop
    @Override
    public Object executeGeneric(VirtualFrame frame) {
        Object function = alg.$(it.getFunctionNode()).executeGeneric(frame);

        /*
         * The number of arguments is constant for one invoke node. During compilation, the loop is
         * unrolled and the execute methods of all arguments are inlined. This is triggered by the
         * ExplodeLoop annotation on the method. The compiler assertion below illustrates that the
         * array length is really constant.
         */
        CompilerAsserts.compilationConstant(it.getArgumentNodes().length);

        Object[] argumentValues = new Object[it.getArgumentNodes().length];
        for (int i = 0; i < it.getArgumentNodes().length; i++) {
            argumentValues[i] = alg.$(it.getArgumentNodes()[i]).executeGeneric(frame);
        }
        return it.getDispatchNode().executeDispatch(function, argumentValues);
    }
}
