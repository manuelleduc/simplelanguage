package com.oracle.truffle.sl.rev;

import com.oracle.truffle.api.frame.VirtualFrame;
import com.oracle.truffle.sl.nodes.controlflow.SLReturnException;
import com.oracle.truffle.sl.nodes.controlflow.SLReturnNode;
import com.oracle.truffle.sl.runtime.SLNull;

import java.util.HashMap;
import java.util.Map;

public class MySLReturnNodeT implements SLReturnNodeT {
    private final static Map<SLReturnNode, SLReturnNodeT> cache = new HashMap<>();
    private final ExecSLRevisitor alg;
    private final SLReturnNode it;
    private final SLExpressionNodeT op;

    public MySLReturnNodeT(ExecSLRevisitor alg, SLReturnNode it) {
        this.alg = alg;
        this.it = it;
        if (it.getValueNode() != null)
            this.op = alg.$(it.getValueNode());
        else this.op = null;
    }

    public static SLReturnNodeT INSTANCE(ExecSLRevisitor alg, SLReturnNode it) {
        if (!cache.containsKey(it)) {
            cache.put(it, new MySLReturnNodeT(alg, it));
        }
        return cache.get(it);
    }

    @Override
    public void executeVoid(VirtualFrame frame) {
        Object result;
        if (it.getValueNode() != null) {
            result = op.executeGeneric(frame);
        } else {
            /*
             * Return statement that was not followed by an expression, so return the SL null value.
             */
            result = SLNull.SINGLETON;
        }
        throw new SLReturnException(result);
    }
}
