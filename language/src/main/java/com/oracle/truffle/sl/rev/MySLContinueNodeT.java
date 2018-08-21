package com.oracle.truffle.sl.rev;

import com.oracle.truffle.api.frame.VirtualFrame;
import com.oracle.truffle.sl.nodes.controlflow.SLContinueException;
import com.oracle.truffle.sl.nodes.controlflow.SLContinueNode;

import java.util.HashMap;
import java.util.Map;

public class MySLContinueNodeT implements SLContinueNodeT {
    private final static Map<SLContinueNode, SLContinueNodeT> cache = new HashMap<>();
    private final ExecSLRevisitor alg;
    private final SLContinueNode it;

    private MySLContinueNodeT(ExecSLRevisitor alg, SLContinueNode it) {
        this.alg = alg;
        this.it = it;
    }

    public static SLContinueNodeT INSTANCE(ExecSLRevisitor alg, SLContinueNode it) {
        if (!cache.containsKey(it)) cache.put(it, new MySLContinueNodeT(alg, it));
        return cache.get(it);
    }

    @Override
    public void executeVoid(VirtualFrame frame) {
        throw SLContinueException.SINGLETON;
    }
}
