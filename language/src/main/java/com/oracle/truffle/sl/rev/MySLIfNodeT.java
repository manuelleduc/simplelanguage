package com.oracle.truffle.sl.rev;

import com.oracle.truffle.api.frame.VirtualFrame;
import com.oracle.truffle.api.nodes.UnexpectedResultException;
import com.oracle.truffle.api.profiles.ConditionProfile;
import com.oracle.truffle.sl.SLException;
import com.oracle.truffle.sl.nodes.SLExpressionNode;
import com.oracle.truffle.sl.nodes.controlflow.SLIfNode;

import java.util.HashMap;
import java.util.Map;

public class MySLIfNodeT implements SLIfNodeT {
    private final static Map<SLIfNode, SLIfNodeT> cache = new HashMap<>();
    private final ExecSLRevisitor alg;
    private final SLIfNode it;


    /**
     * Profiling information, collected by the interpreter, capturing the profiling information of
     * the condition. This allows the compiler to generate better code for conditions that are
     * always true or always false. Additionally the CountingConditionProfile implementation
     * (as opposed to BinaryConditionProfile implementation) transmits the probability of
     * the condition to be true to the compiler.
     */
    private final ConditionProfile condition = ConditionProfile.createCountingProfile();
    private final SLExpressionNodeT op;
    private final SLStatementNodeT opThen;
    private final SLStatementNodeT opElse;

    private MySLIfNodeT(ExecSLRevisitor alg, SLIfNode it) {
        this.alg = alg;
        this.it = it;
        this.op = alg.$(it.getConditionNode());
        this.opThen = alg.$(it.getThenPartNode());
        if (it.getElsePartNode() != null)
            this.opElse = alg.$(it.getElsePartNode());
        else opElse = null;
    }

    public static SLIfNodeT INSTANCE(ExecSLRevisitor alg, SLIfNode it) {
        if (!cache.containsKey(it)) {
            cache.put(it, new MySLIfNodeT(alg, it));
        }
        return cache.get(it);
    }

    @Override
    public void executeVoid(VirtualFrame frame) {
        /*
         * In the interpreter, record profiling information that the condition was executed and with
         * which outcome.
         */
        if (condition.profile(evaluateCondition(frame, it.getConditionNode()))) {
            /* Execute the then-branch. */
            opThen.executeVoid(frame);
        } else {
            /* Execute the else-branch (which is optional according to the SL syntax). */
            if (it.getElsePartNode() != null) {
                opElse.executeVoid(frame);
            }
        }
    }


    private boolean evaluateCondition(VirtualFrame frame, SLExpressionNode conditionNode) {
        try {
            /*
             * The condition must evaluate to a boolean value, so we call the boolean-specialized
             * execute method.
             */
            return op.executeBoolean(frame);
        } catch (UnexpectedResultException ex) {
            /*
             * The condition evaluated to a non-boolean result. This is a type error in the SL
             * program.
             */
            throw SLException.typeError(it, ex.getResult());
        }
    }

}
