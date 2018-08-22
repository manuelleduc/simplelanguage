package com.oracle.truffle.sl.rev;

import com.oracle.truffle.api.frame.VirtualFrame;
import com.oracle.truffle.sl.nodes.controlflow.SLDebuggerNode;

import java.util.HashMap;
import java.util.Map;

public class MySLDebuggerNodeT implements SLDebuggerNodeT {
    private final static Map<SLDebuggerNode, SLDebuggerNodeT> cache = new HashMap<>();
    private final ExecSLRevisitor alg;
    private final SLDebuggerNode it;

    public MySLDebuggerNodeT(ExecSLRevisitor alg, SLDebuggerNode it) {
        this.alg = alg;
        this.it = it;
    }

    public static SLDebuggerNodeT INSTANCE(ExecSLRevisitor alg, SLDebuggerNode it) {
        if (!cache.containsKey(it)) {
            cache.put(it, new MySLDebuggerNodeT(alg, it));
        }
        return cache.get(it);
    }

    @Override
    public void executeVoid(VirtualFrame frame) {
        // No op.
    }
}
