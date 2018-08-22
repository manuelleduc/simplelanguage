package com.oracle.truffle.sl.rev;

import com.oracle.truffle.api.frame.VirtualFrame;
import com.oracle.truffle.sl.nodes.controlflow.SLBreakException;
import com.oracle.truffle.sl.nodes.controlflow.SLBreakNode;

import java.util.HashMap;
import java.util.Map;

public class MySLBreakNodeT implements SLBreakNodeT {
    private final static Map<SLBreakNode, SLBreakNodeT> cache = new HashMap<>();
    private final ExecSLRevisitor alg;
    private final SLBreakNode it;

    private MySLBreakNodeT(ExecSLRevisitor alg, SLBreakNode it) {
        this.alg = alg;
        this.it = it;
    }

    public static SLBreakNodeT INSTANCE(ExecSLRevisitor alg, SLBreakNode it) {
        if (!cache.containsKey(it)) {
            cache.put(it, new MySLBreakNodeT(alg, it));
        }
        return cache.get(it);
    }

    @Override
    public void executeVoid(VirtualFrame frame) {
        throw SLBreakException.SINGLETON;
    }
}
