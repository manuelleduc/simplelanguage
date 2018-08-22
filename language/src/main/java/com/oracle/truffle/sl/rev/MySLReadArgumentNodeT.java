package com.oracle.truffle.sl.rev;

import com.oracle.truffle.api.frame.VirtualFrame;
import com.oracle.truffle.api.profiles.BranchProfile;
import com.oracle.truffle.sl.nodes.local.SLReadArgumentNode;
import com.oracle.truffle.sl.runtime.SLNull;

import java.util.HashMap;
import java.util.Map;

public class MySLReadArgumentNodeT implements SLReadArgumentNodeT {
    private final static Map<SLReadArgumentNode, SLReadArgumentNodeT> cache = new HashMap<>();
    private final ExecSLRevisitor alg;
    private final SLReadArgumentNode it;

    public MySLReadArgumentNodeT(ExecSLRevisitor alg, SLReadArgumentNode it) {
        this.alg = alg;
        this.it = it;
    }

    public static SLReadArgumentNodeT INSTANCE(ExecSLRevisitor alg, SLReadArgumentNode it) {
        if (!cache.containsKey(it)) {
            cache.put(it, new MySLReadArgumentNodeT(alg, it));
        }
        return cache.get(it);
    }

    /**
     * Profiling information, collected by the interpreter, capturing whether the function was
     * called with fewer actual arguments than formal arguments.
     */
    private final BranchProfile outOfBoundsTaken = BranchProfile.create();


    @Override
    public Object executeGeneric(VirtualFrame frame) {
        Object[] args = frame.getArguments();
        final int index = it.getIndex();
        if (index < args.length) {
            return args[index];
        } else {
            /* In the interpreter, record profiling information that the branch was used. */
            outOfBoundsTaken.enter();
            /* Use the default null value. */
            return SLNull.SINGLETON;
        }
    }
}
