package com.oracle.truffle.sl.rev;

import com.oracle.truffle.api.CompilerDirectives;
import com.oracle.truffle.api.dsl.Fallback;
import com.oracle.truffle.api.dsl.Specialization;
import com.oracle.truffle.api.interop.TruffleObject;
import com.oracle.truffle.sl.SLException;
import com.oracle.truffle.sl.nodes.expression.SLEqualNode;
import com.oracle.truffle.sl.runtime.SLBigNumber;
import com.oracle.truffle.sl.runtime.SLFunction;
import com.oracle.truffle.sl.runtime.SLNull;

import java.util.HashMap;
import java.util.Map;

public abstract class MySLEqualNodeT implements SLEqualNodeT {
    private final static Map<SLEqualNode, SLEqualNodeT> cache = new HashMap<>();
    private final ExecSLRevisitor alg;
    private final SLEqualNode it;

    private MySLEqualNodeT(ExecSLRevisitor alg, SLEqualNode it) {
        this.alg = alg;
        this.it = it;
    }

    public static SLEqualNodeT INSTANCE(ExecSLRevisitor alg, SLEqualNode it) {
        if (!cache.containsKey(it)) cache.put(it, /*new MySLEqualNodeT(alg, it)*/null);
        return cache.get(it);
    }

    @Specialization
    protected boolean equal(long left, long right) {
        return left == right;
    }

    @Specialization
    @CompilerDirectives.TruffleBoundary
    protected boolean equal(SLBigNumber left, SLBigNumber right) {
        return left.equals(right);
    }

    @Specialization
    protected boolean equal(boolean left, boolean right) {
        return left == right;
    }

    @Specialization
    protected boolean equal(String left, String right) {
        return left.equals(right);
    }

    @Specialization
    protected boolean equal(SLFunction left, SLFunction right) {
        /*
         * Our function registry maintains one canonical SLFunction object per function name, so we
         * do not need equals().
         */
        return left == right;
    }

    @Specialization
    protected boolean equal(SLNull left, SLNull right) {
        /* There is only the singleton instance of SLNull, so we do not need equals(). */
        return left == right;
    }

    /**
     * Specialization for foreign {@link TruffleObject}s.
     */
    @Specialization
    protected boolean equal(TruffleObject left, TruffleObject right) {
        return left == right;
    }

    /**
     * We covered all the cases that can return true in the type specializations above. If we
     * compare two values with different types, the result is known to be false.
     * <p>
     * Note that the guard is essential for correctness: without the guard, the specialization would
     * also match when the left and right value have the same type. The following scenario would
     * return a wrong value: First, the node is executed with the left value 42 (type long) and the
     * right value "abc" (String). This specialization matches, and since it is the first execution
     * it is also the only specialization. Then, the node is executed with the left value "42" (type
     * long) and the right value "42" (type long). Since this specialization is already present, and
     * (without the guard) also matches (long values can be boxed to Object), it is executed. The
     * wrong return value is "false".
     */
    @Specialization(guards = "differentClasses(left, right)")
    protected boolean equal(Object left, Object right) {
        assert !left.equals(right);
        return false;
    }

    static boolean differentClasses(Object left, Object right) {
        return left.getClass() != right.getClass();
    }

    @Fallback
    protected Object typeError(Object left, Object right) {
        throw SLException.typeError(it, left, right);
    }


}
