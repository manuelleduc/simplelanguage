package com.oracle.truffle.sl.rev;

import com.oracle.truffle.api.CompilerAsserts;
import com.oracle.truffle.api.CompilerDirectives;
import com.oracle.truffle.api.frame.VirtualFrame;
import com.oracle.truffle.api.nodes.ExplodeLoop;
import com.oracle.truffle.sl.nodes.SLExpressionNode;
import com.oracle.truffle.sl.nodes.call.SLInvokeNode;

import java.util.HashMap;
import java.util.Map;

public class MySLInvokeNodeT implements SLInvokeNodeT {
    private final static Map<SLInvokeNode, SLInvokeNodeT> cache = new HashMap<>();
    private final ExecSLRevisitor alg;
    private final SLInvokeNode it;
    @CompilerDirectives.CompilationFinal
    private final SLExpressionNodeT[] ops;
    private final SLExpressionNodeT opF;

    public MySLInvokeNodeT(ExecSLRevisitor alg, SLInvokeNode it) {
        this.alg = alg;
        this.it = it;
        SLExpressionNode[] bodyNodes = it.getArgumentNodes();
        int length = bodyNodes.length;
        this.ops = new SLExpressionNodeT[length];
        for (int i = 0; i < length; i++) ops[i] = alg.$(bodyNodes[i]);
        this.opF = alg.$(it.getFunctionNode());

    }

    public static SLInvokeNodeT INSTANCE(ExecSLRevisitor alg, SLInvokeNode it) {
        if (!cache.containsKey(it)) {
            cache.put(it, new MySLInvokeNodeT(alg, it));
        }
        return cache.get(it);
    }

    @ExplodeLoop
    @Override
    public Object executeGeneric(VirtualFrame frame) {
        Object function = opF.executeGeneric(frame);

        /*
         * The number of arguments is constant for one invoke node. During compilation, the loop is
         * unrolled and the execute methods of all arguments are inlined. This is triggered by the
         * ExplodeLoop annotation on the method. The compiler assertion below illustrates that the
         * array length is really constant.
         */
        CompilerAsserts.compilationConstant(ops.length);

        Object[] argumentValues = new Object[it.getArgumentNodes().length];
        int i=0;
        for (SLExpressionNodeT op: ops) {
            argumentValues[i] = op.executeGeneric(frame);
            i++;
        }
        return it.getDispatchNode().executeDispatch(function, argumentValues);
    }
}
