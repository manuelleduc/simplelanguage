package com.oracle.truffle.sl.rev;

import com.oracle.truffle.api.CompilerDirectives;
import com.oracle.truffle.api.frame.VirtualFrame;
import com.oracle.truffle.api.profiles.BranchProfile;
import com.oracle.truffle.sl.nodes.controlflow.SLFunctionBodyNode;
import com.oracle.truffle.sl.nodes.controlflow.SLReturnException;
import com.oracle.truffle.sl.nodes.controlflow.SLReturnNode;
import com.oracle.truffle.sl.runtime.SLNull;

import java.util.HashMap;
import java.util.Map;

public class MySLFunctionBodyNodeT implements SLFunctionBodyNodeT {
    private final static Map<SLFunctionBodyNode, SLFunctionBodyNodeT> cache = new HashMap<>();
    private final ExecSLRevisitor alg;
    private final SLFunctionBodyNode it;

    /**
     * Profiling information, collected by the interpreter, capturing whether the function had an
     * {@link SLReturnNode explicit return statement}. This allows the compiler to generate better
     * code.
     */
    private final BranchProfile exceptionTaken = BranchProfile.create();
    private final BranchProfile nullTaken = BranchProfile.create();
    private final SLStatementNodeT op;


    public MySLFunctionBodyNodeT(ExecSLRevisitor alg, SLFunctionBodyNode it) {
        this.alg = alg;
        this.it = it;
        this.op = alg.$(it.getBodyNode());
    }

    public static SLFunctionBodyNodeT INSTANCE(ExecSLRevisitor alg, SLFunctionBodyNode it) {
        if (!cache.containsKey(it)) {
            cache.put(it, new MySLFunctionBodyNodeT(alg, it));
        }
        return cache.get(it);
    }

    @Override
    public Object executeGeneric(VirtualFrame frame) {
        try {
            /* Execute the function body. */

            op.executeVoid(frame);

        } catch (SLReturnException ex) {
            /*
             * In the interpreter, record profiling information that the function has an explicit
             * return.
             */
            exceptionTaken.enter();
            /* The exception transports the actual return value. */
            return ex.getResult();
        }

        /*
         * In the interpreter, record profiling information that the function ends without an
         * explicit return.
         */
        nullTaken.enter();
        /* Return the default null value. */
        return SLNull.SINGLETON;
    }
}
