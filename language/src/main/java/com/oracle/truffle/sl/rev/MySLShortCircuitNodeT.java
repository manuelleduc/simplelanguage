package com.oracle.truffle.sl.rev;

import com.oracle.truffle.api.frame.VirtualFrame;
import com.oracle.truffle.api.nodes.UnexpectedResultException;
import com.oracle.truffle.api.profiles.ConditionProfile;
import com.oracle.truffle.sl.SLException;
import com.oracle.truffle.sl.nodes.expression.SLShortCircuitNode;

import java.util.HashMap;
import java.util.Map;

public abstract class MySLShortCircuitNodeT implements SLShortCircuitNodeT {
    private final static Map<SLShortCircuitNode, SLShortCircuitNodeT> cache = new HashMap<>();
    private final ExecSLRevisitor alg;
    private final SLShortCircuitNode it;
    /**
     * Short circuits might be used just like a conditional statement it makes sense to profile the
     * branch probability.
     */
    private final ConditionProfile evaluateRightProfile = ConditionProfile.createCountingProfile();


    protected MySLShortCircuitNodeT(ExecSLRevisitor alg, SLShortCircuitNode it) {
        this.alg = alg;
        this.it = it;
    }

//	public static SLShortCircuitNodeT INSTANCE(ExecSLRevisitor alg, SLShortCircuitNode it) {
//		if (!cache.containsKey(it)) { System.out.println("CACHE MISS $1"); cache.put(it, new $1(alg, it)); }
//		return cache.get(it);
//	}

    @Override
    public final Object executeGeneric(VirtualFrame frame) {
        return executeBoolean(frame);
    }

    @Override
    public final boolean executeBoolean(VirtualFrame frame) {
        boolean leftValue;
        try {
            leftValue = alg.$(it.getLeft()).executeBoolean(frame);
        } catch (UnexpectedResultException e) {
            throw SLException.typeError(it, e.getResult(), null);
        }
        boolean rightValue;
        try {
            if (evaluateRightProfile.profile(isEvaluateRight(leftValue))) {
                rightValue = alg.$(it.getRight()).executeBoolean(frame);
            } else {
                rightValue = false;
            }
        } catch (UnexpectedResultException e) {
            throw SLException.typeError(it, leftValue, e.getResult());
        }
        return execute(leftValue, rightValue);
    }


}
