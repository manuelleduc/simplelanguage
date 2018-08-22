package com.oracle.truffle.sl.rev;

import com.oracle.truffle.api.dsl.UnsupportedSpecializationException;
import com.oracle.truffle.api.frame.VirtualFrame;
import com.oracle.truffle.api.nodes.Node;
import com.oracle.truffle.api.nodes.UnexpectedResultException;
import com.oracle.truffle.api.profiles.BranchProfile;
import com.oracle.truffle.sl.nodes.SLExpressionNode;
import com.oracle.truffle.sl.nodes.controlflow.SLBreakException;
import com.oracle.truffle.sl.nodes.controlflow.SLContinueException;
import com.oracle.truffle.sl.nodes.controlflow.SLWhileNode;

import java.util.HashMap;
import java.util.Map;

public class MySLWhileNodeT implements SLWhileNodeT {
    private final static Map<SLWhileNode, SLWhileNodeT> cache = new HashMap<>();
    private final ExecSLRevisitor alg;
    private final SLWhileNode it;

    private MySLWhileNodeT(ExecSLRevisitor alg, SLWhileNode it) {
        this.alg = alg;
        this.it = it;
    }

    public static SLWhileNodeT INSTANCE(ExecSLRevisitor alg, SLWhileNode it) {
        if (!cache.containsKey(it)) {
            cache.put(it, new MySLWhileNodeT(alg, it));
        }
        return cache.get(it);
    }

    /**
     * Profiling information, collected by the interpreter, capturing whether a {@code continue}
     * statement was used in this loop. This allows the compiler to generate better code for loops
     * without a {@code continue}.
     */
    private final BranchProfile continueTaken = BranchProfile.create();
    private final BranchProfile breakTaken = BranchProfile.create();

    @Override
    public void executeVoid(VirtualFrame frame) {
        try {
            while (evaluateCondition(frame, it.getConditionNode())) {


                try {
                    /* Normal exit of the loop when loop condition is false. */
                    alg.$(it.getBodyNode()).executeVoid(frame);
                    /* Execute the loop body. */
                    /* Continue with next loop iteration. */


                } catch (SLContinueException ex) {
                    /* In the interpreter, record profiling information that the loop uses continue. */
                    continueTaken.enter();
                    /* Continue with next loop iteration. */


                }
            }

        } catch (SLBreakException ex) {
            /* In the interpreter, record profiling information that the loop uses break. */
            breakTaken.enter();
            /* Break out of the loop. */

        }

    }

    private boolean evaluateCondition(VirtualFrame frame, SLExpressionNode conditionNode) {
        try {
            /*
             * The condition must evaluate to a boolean value, so we call the boolean-specialized
             * execute method.
             */
            return alg.$(conditionNode).executeBoolean(frame);
        } catch (UnexpectedResultException ex) {
            /*
             * The condition evaluated to a non-boolean result. This is a type error in the SL
             * program. We report it with the same exception that Truffle DSL generated nodes use to
             * report type errors.
             */
            throw new UnsupportedSpecializationException(it, new Node[]{conditionNode}, ex.getResult());
        }
    }
}
