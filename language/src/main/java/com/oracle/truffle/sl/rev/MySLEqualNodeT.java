package com.oracle.truffle.sl.rev;

import com.oracle.truffle.api.CompilerDirectives;
import com.oracle.truffle.api.dsl.Fallback;
import com.oracle.truffle.api.dsl.Specialization;
import com.oracle.truffle.api.frame.VirtualFrame;
import com.oracle.truffle.api.interop.TruffleObject;
import com.oracle.truffle.api.nodes.UnexpectedResultException;
import com.oracle.truffle.sl.SLException;
import com.oracle.truffle.sl.nodes.SLTypes;
import com.oracle.truffle.sl.nodes.SLTypesGen;
import com.oracle.truffle.sl.nodes.expression.SLEqualNode;
import com.oracle.truffle.sl.runtime.SLBigNumber;
import com.oracle.truffle.sl.runtime.SLFunction;
import com.oracle.truffle.sl.runtime.SLNull;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.Lock;

public class MySLEqualNodeT implements SLEqualNodeT {
    private final static Map<SLEqualNode, SLEqualNodeT> cache = new HashMap<>();
    private final ExecSLRevisitor alg;
    private final SLEqualNode it;

    @CompilerDirectives.CompilationFinal
    private int state_;


    private MySLEqualNodeT(ExecSLRevisitor alg, SLEqualNode it) {
        this.alg = alg;
        this.it = it;
    }

    public static SLEqualNodeT INSTANCE(ExecSLRevisitor alg, SLEqualNode it) {
        if (!cache.containsKey(it)) {
            cache.put(it, new MySLEqualNodeT(alg, it));
        }
        return cache.get(it);
    }

    @Override
    public Object executeGeneric(VirtualFrame frameValue) {
        int state = state_;
        if ((state & 0b111111110) == 0 /* only-active equal(long, long) */ && (state & 0b111111111) != 0  /* is-not equal(long, long) && equal(SLBigNumber, SLBigNumber) && equal(boolean, boolean) && equal(String, String) && equal(SLFunction, SLFunction) && equal(SLNull, SLNull) && equal(TruffleObject, TruffleObject) && equal(Object, Object) && typeError(Object, Object) */) {
            return executeGeneric_long_long0(frameValue, state);
        } else if ((state & 0b111111011) == 0 /* only-active equal(boolean, boolean) */ && (state & 0b111111111) != 0  /* is-not equal(long, long) && equal(SLBigNumber, SLBigNumber) && equal(boolean, boolean) && equal(String, String) && equal(SLFunction, SLFunction) && equal(SLNull, SLNull) && equal(TruffleObject, TruffleObject) && equal(Object, Object) && typeError(Object, Object) */) {
            return executeGeneric_boolean_boolean1(frameValue, state);
        } else {
            return executeGeneric_generic2(frameValue, state);
        }
    }

    private Object executeGeneric_long_long0(VirtualFrame frameValue, int state) {
        long leftNodeValue_;
        try {
            leftNodeValue_ = alg.$(this.it.getLeftNode()).executeLong(frameValue);
        } catch (UnexpectedResultException ex) {
            Object rightNodeValue = alg.$(this.it.getRightNode()).executeGeneric(frameValue);
            return executeAndSpecialize(ex.getResult(), rightNodeValue);
        }
        long rightNodeValue_;
        try {
            rightNodeValue_ = alg.$(this.it.getRightNode()).executeLong(frameValue);
        } catch (UnexpectedResultException ex) {
            return executeAndSpecialize(leftNodeValue_, ex.getResult());
        }
        assert (state & 0b1) != 0 /* is-active equal(long, long) */;
        return equal(leftNodeValue_, rightNodeValue_);
    }

    private Object executeGeneric_boolean_boolean1(VirtualFrame frameValue, int state) {
        boolean leftNodeValue_;
        try {
            leftNodeValue_ = alg.$(this.it.getLeftNode()).executeBoolean(frameValue);
        } catch (UnexpectedResultException ex) {
            Object rightNodeValue = alg.$(this.it.getRightNode()).executeGeneric(frameValue);
            return executeAndSpecialize(ex.getResult(), rightNodeValue);
        }
        boolean rightNodeValue_;
        try {
            rightNodeValue_ = alg.$(this.it.getRightNode()).executeBoolean(frameValue);
        } catch (UnexpectedResultException ex) {
            return executeAndSpecialize(leftNodeValue_, ex.getResult());
        }
        assert (state & 0b100) != 0 /* is-active equal(boolean, boolean) */;
        return equal(leftNodeValue_, rightNodeValue_);
    }

    private Object executeGeneric_generic2(VirtualFrame frameValue, int state) {
        Object leftNodeValue_ = alg.$(this.it.getLeftNode()).executeGeneric(frameValue);
        Object rightNodeValue_ = alg.$(this.it.getRightNode()).executeGeneric(frameValue);
        if ((state & 0b1) != 0 /* is-active equal(long, long) */ && leftNodeValue_ instanceof Long) {
            long leftNodeValue__ = (long) leftNodeValue_;
            if (rightNodeValue_ instanceof Long) {
                long rightNodeValue__ = (long) rightNodeValue_;
                return equal(leftNodeValue__, rightNodeValue__);
            }
        }
        if ((state & 0b10) != 0 /* is-active equal(SLBigNumber, SLBigNumber) */ && SLTypesGen.isImplicitSLBigNumber((state & 0b11000000000) >>> 9 /* extract-implicit-active 0:SLBigNumber */, leftNodeValue_)) {
            SLBigNumber leftNodeValue__ = SLTypesGen.asImplicitSLBigNumber((state & 0b11000000000) >>> 9 /* extract-implicit-active 0:SLBigNumber */, leftNodeValue_);
            if (SLTypesGen.isImplicitSLBigNumber((state & 0b1100000000000) >>> 11 /* extract-implicit-active 1:SLBigNumber */, rightNodeValue_)) {
                SLBigNumber rightNodeValue__ = SLTypesGen.asImplicitSLBigNumber((state & 0b1100000000000) >>> 11 /* extract-implicit-active 1:SLBigNumber */, rightNodeValue_);
                return equal(leftNodeValue__, rightNodeValue__);
            }
        }
        if ((state & 0b100) != 0 /* is-active equal(boolean, boolean) */ && leftNodeValue_ instanceof Boolean) {
            boolean leftNodeValue__ = (boolean) leftNodeValue_;
            if (rightNodeValue_ instanceof Boolean) {
                boolean rightNodeValue__ = (boolean) rightNodeValue_;
                return equal(leftNodeValue__, rightNodeValue__);
            }
        }
        if ((state & 0b1000) != 0 /* is-active equal(String, String) */ && leftNodeValue_ instanceof String) {
            String leftNodeValue__ = (String) leftNodeValue_;
            if (rightNodeValue_ instanceof String) {
                String rightNodeValue__ = (String) rightNodeValue_;
                return equal(leftNodeValue__, rightNodeValue__);
            }
        }
        if ((state & 0b10000) != 0 /* is-active equal(SLFunction, SLFunction) */ && leftNodeValue_ instanceof SLFunction) {
            SLFunction leftNodeValue__ = (SLFunction) leftNodeValue_;
            if (rightNodeValue_ instanceof SLFunction) {
                SLFunction rightNodeValue__ = (SLFunction) rightNodeValue_;
                return equal(leftNodeValue__, rightNodeValue__);
            }
        }
        if ((state & 0b100000) != 0 /* is-active equal(SLNull, SLNull) */ && SLTypes.isSLNull(leftNodeValue_)) {
            SLNull leftNodeValue__ = SLTypes.asSLNull(leftNodeValue_);
            if (SLTypes.isSLNull(rightNodeValue_)) {
                SLNull rightNodeValue__ = SLTypes.asSLNull(rightNodeValue_);
                return equal(leftNodeValue__, rightNodeValue__);
            }
        }
        if ((state & 0b1000000) != 0 /* is-active equal(TruffleObject, TruffleObject) */ && leftNodeValue_ instanceof TruffleObject) {
            TruffleObject leftNodeValue__ = (TruffleObject) leftNodeValue_;
            if (rightNodeValue_ instanceof TruffleObject) {
                TruffleObject rightNodeValue__ = (TruffleObject) rightNodeValue_;
                return equal(leftNodeValue__, rightNodeValue__);
            }
        }
        if ((state & 0b110000000) != 0 /* is-active equal(Object, Object) || typeError(Object, Object) */) {
            if ((state & 0b10000000) != 0 /* is-active equal(Object, Object) */) {
                if ((differentClasses(leftNodeValue_, rightNodeValue_))) {
                    return equal(leftNodeValue_, rightNodeValue_);
                }
            }
            if ((state & 0b100000000) != 0 /* is-active typeError(Object, Object) */) {
                if (fallbackGuard_(state, leftNodeValue_, rightNodeValue_)) {
                    return typeError(leftNodeValue_, rightNodeValue_);
                }
            }
        }
        CompilerDirectives.transferToInterpreterAndInvalidate();
        return executeAndSpecialize(leftNodeValue_, rightNodeValue_);
    }

    @Override
    public boolean executeBoolean(VirtualFrame frameValue) throws UnexpectedResultException {
        int state = state_;
        if ((state & 0b100000000) != 0 /* is-active typeError(Object, Object) */) {
            return SLTypesGen.expectBoolean(executeGeneric(frameValue));
        }
        if ((state & 0b11111110) == 0 /* only-active equal(long, long) */ && (state & 0b11111111) != 0  /* is-not equal(long, long) && equal(SLBigNumber, SLBigNumber) && equal(boolean, boolean) && equal(String, String) && equal(SLFunction, SLFunction) && equal(SLNull, SLNull) && equal(TruffleObject, TruffleObject) && equal(Object, Object) */) {
            return executeBoolean_long_long3(frameValue, state);
        } else if ((state & 0b11111011) == 0 /* only-active equal(boolean, boolean) */ && (state & 0b11111111) != 0  /* is-not equal(long, long) && equal(SLBigNumber, SLBigNumber) && equal(boolean, boolean) && equal(String, String) && equal(SLFunction, SLFunction) && equal(SLNull, SLNull) && equal(TruffleObject, TruffleObject) && equal(Object, Object) */) {
            return executeBoolean_boolean_boolean4(frameValue, state);
        } else {
            return executeBoolean_generic5(frameValue, state);
        }
    }

    private boolean executeBoolean_long_long3(VirtualFrame frameValue, int state) throws UnexpectedResultException {
        long leftNodeValue_;
        try {
            leftNodeValue_ = alg.$(this.it.getLeftNode()).executeLong(frameValue);
        } catch (UnexpectedResultException ex) {
            Object rightNodeValue = alg.$(this.it.getRightNode()).executeGeneric(frameValue);
            return SLTypesGen.expectBoolean(executeAndSpecialize(ex.getResult(), rightNodeValue));
        }
        long rightNodeValue_;
        try {
            rightNodeValue_ = alg.$(this.it.getRightNode()).executeLong(frameValue);
        } catch (UnexpectedResultException ex) {
            return SLTypesGen.expectBoolean(executeAndSpecialize(leftNodeValue_, ex.getResult()));
        }
        assert (state & 0b1) != 0 /* is-active equal(long, long) */;
        return equal(leftNodeValue_, rightNodeValue_);
    }

    private boolean executeBoolean_boolean_boolean4(VirtualFrame frameValue, int state) throws UnexpectedResultException {
        boolean leftNodeValue_;
        try {
            leftNodeValue_ = alg.$(this.it.getLeftNode()).executeBoolean(frameValue);
        } catch (UnexpectedResultException ex) {
            Object rightNodeValue = alg.$(this.it.getRightNode()).executeGeneric(frameValue);
            return SLTypesGen.expectBoolean(executeAndSpecialize(ex.getResult(), rightNodeValue));
        }
        boolean rightNodeValue_;
        try {
            rightNodeValue_ = alg.$(this.it.getRightNode()).executeBoolean(frameValue);
        } catch (UnexpectedResultException ex) {
            return SLTypesGen.expectBoolean(executeAndSpecialize(leftNodeValue_, ex.getResult()));
        }
        assert (state & 0b100) != 0 /* is-active equal(boolean, boolean) */;
        return equal(leftNodeValue_, rightNodeValue_);
    }

    private boolean executeBoolean_generic5(VirtualFrame frameValue, int state) throws UnexpectedResultException {
        Object leftNodeValue_ = alg.$(this.it.getLeftNode()).executeGeneric(frameValue);
        Object rightNodeValue_ = alg.$(this.it.getRightNode()).executeGeneric(frameValue);
        if ((state & 0b1) != 0 /* is-active equal(long, long) */ && leftNodeValue_ instanceof Long) {
            long leftNodeValue__ = (long) leftNodeValue_;
            if (rightNodeValue_ instanceof Long) {
                long rightNodeValue__ = (long) rightNodeValue_;
                return equal(leftNodeValue__, rightNodeValue__);
            }
        }
        if ((state & 0b10) != 0 /* is-active equal(SLBigNumber, SLBigNumber) */ && SLTypesGen.isImplicitSLBigNumber((state & 0b11000000000) >>> 9 /* extract-implicit-active 0:SLBigNumber */, leftNodeValue_)) {
            SLBigNumber leftNodeValue__ = SLTypesGen.asImplicitSLBigNumber((state & 0b11000000000) >>> 9 /* extract-implicit-active 0:SLBigNumber */, leftNodeValue_);
            if (SLTypesGen.isImplicitSLBigNumber((state & 0b1100000000000) >>> 11 /* extract-implicit-active 1:SLBigNumber */, rightNodeValue_)) {
                SLBigNumber rightNodeValue__ = SLTypesGen.asImplicitSLBigNumber((state & 0b1100000000000) >>> 11 /* extract-implicit-active 1:SLBigNumber */, rightNodeValue_);
                return equal(leftNodeValue__, rightNodeValue__);
            }
        }
        if ((state & 0b100) != 0 /* is-active equal(boolean, boolean) */ && leftNodeValue_ instanceof Boolean) {
            boolean leftNodeValue__ = (boolean) leftNodeValue_;
            if (rightNodeValue_ instanceof Boolean) {
                boolean rightNodeValue__ = (boolean) rightNodeValue_;
                return equal(leftNodeValue__, rightNodeValue__);
            }
        }
        if ((state & 0b1000) != 0 /* is-active equal(String, String) */ && leftNodeValue_ instanceof String) {
            String leftNodeValue__ = (String) leftNodeValue_;
            if (rightNodeValue_ instanceof String) {
                String rightNodeValue__ = (String) rightNodeValue_;
                return equal(leftNodeValue__, rightNodeValue__);
            }
        }
        if ((state & 0b10000) != 0 /* is-active equal(SLFunction, SLFunction) */ && leftNodeValue_ instanceof SLFunction) {
            SLFunction leftNodeValue__ = (SLFunction) leftNodeValue_;
            if (rightNodeValue_ instanceof SLFunction) {
                SLFunction rightNodeValue__ = (SLFunction) rightNodeValue_;
                return equal(leftNodeValue__, rightNodeValue__);
            }
        }
        if ((state & 0b100000) != 0 /* is-active equal(SLNull, SLNull) */ && SLTypes.isSLNull(leftNodeValue_)) {
            SLNull leftNodeValue__ = SLTypes.asSLNull(leftNodeValue_);
            if (SLTypes.isSLNull(rightNodeValue_)) {
                SLNull rightNodeValue__ = SLTypes.asSLNull(rightNodeValue_);
                return equal(leftNodeValue__, rightNodeValue__);
            }
        }
        if ((state & 0b1000000) != 0 /* is-active equal(TruffleObject, TruffleObject) */ && leftNodeValue_ instanceof TruffleObject) {
            TruffleObject leftNodeValue__ = (TruffleObject) leftNodeValue_;
            if (rightNodeValue_ instanceof TruffleObject) {
                TruffleObject rightNodeValue__ = (TruffleObject) rightNodeValue_;
                return equal(leftNodeValue__, rightNodeValue__);
            }
        }
        if ((state & 0b10000000) != 0 /* is-active equal(Object, Object) */) {
            if ((differentClasses(leftNodeValue_, rightNodeValue_))) {
                return equal(leftNodeValue_, rightNodeValue_);
            }
        }
        CompilerDirectives.transferToInterpreterAndInvalidate();
        return SLTypesGen.expectBoolean(executeAndSpecialize(leftNodeValue_, rightNodeValue_));
    }


    @Override
    public void executeVoid(VirtualFrame frameValue) {
        int state = state_;
        try {
            if ((state & 0b100000000) == 0 /* only-active equal(long, long) && equal(SLBigNumber, SLBigNumber) && equal(boolean, boolean) && equal(String, String) && equal(SLFunction, SLFunction) && equal(SLNull, SLNull) && equal(TruffleObject, TruffleObject) && equal(Object, Object) */ && (state & 0b111111111) != 0  /* is-not equal(long, long) && equal(SLBigNumber, SLBigNumber) && equal(boolean, boolean) && equal(String, String) && equal(SLFunction, SLFunction) && equal(SLNull, SLNull) && equal(TruffleObject, TruffleObject) && equal(Object, Object) && typeError(Object, Object) */) {
                executeBoolean(frameValue);
                return;
            }
            executeGeneric(frameValue);
            return;
        } catch (UnexpectedResultException ex) {
            return;
        }
    }

    private Object executeAndSpecialize(Object leftNodeValue, Object rightNodeValue) {
        Lock lock = it.getLock2();
        boolean hasLock = true;
        lock.lock();
        int state = state_;
        int oldState = (state & 0b111111111);
        try {
            if (leftNodeValue instanceof Long) {
                long leftNodeValue_ = (long) leftNodeValue;
                if (rightNodeValue instanceof Long) {
                    long rightNodeValue_ = (long) rightNodeValue;
                    this.state_ = state = state | 0b1 /* add-active equal(long, long) */;
                    lock.unlock();
                    hasLock = false;
                    return equal(leftNodeValue_, rightNodeValue_);
                }
            }
            {
                int sLBigNumberCast0;
                if ((sLBigNumberCast0 = SLTypesGen.specializeImplicitSLBigNumber(leftNodeValue)) != 0) {
                    SLBigNumber leftNodeValue_ = SLTypesGen.asImplicitSLBigNumber(sLBigNumberCast0, leftNodeValue);
                    int sLBigNumberCast1;
                    if ((sLBigNumberCast1 = SLTypesGen.specializeImplicitSLBigNumber(rightNodeValue)) != 0) {
                        SLBigNumber rightNodeValue_ = SLTypesGen.asImplicitSLBigNumber(sLBigNumberCast1, rightNodeValue);
                        state = (state | (sLBigNumberCast0 << 9) /* set-implicit-active 0:SLBigNumber */);
                        state = (state | (sLBigNumberCast1 << 11) /* set-implicit-active 1:SLBigNumber */);
                        this.state_ = state = state | 0b10 /* add-active equal(SLBigNumber, SLBigNumber) */;
                        lock.unlock();
                        hasLock = false;
                        return equal(leftNodeValue_, rightNodeValue_);
                    }
                }
            }
            if (leftNodeValue instanceof Boolean) {
                boolean leftNodeValue_ = (boolean) leftNodeValue;
                if (rightNodeValue instanceof Boolean) {
                    boolean rightNodeValue_ = (boolean) rightNodeValue;
                    this.state_ = state = state | 0b100 /* add-active equal(boolean, boolean) */;
                    lock.unlock();
                    hasLock = false;
                    return equal(leftNodeValue_, rightNodeValue_);
                }
            }
            if (leftNodeValue instanceof String) {
                String leftNodeValue_ = (String) leftNodeValue;
                if (rightNodeValue instanceof String) {
                    String rightNodeValue_ = (String) rightNodeValue;
                    this.state_ = state = state | 0b1000 /* add-active equal(String, String) */;
                    lock.unlock();
                    hasLock = false;
                    return equal(leftNodeValue_, rightNodeValue_);
                }
            }
            if (leftNodeValue instanceof SLFunction) {
                SLFunction leftNodeValue_ = (SLFunction) leftNodeValue;
                if (rightNodeValue instanceof SLFunction) {
                    SLFunction rightNodeValue_ = (SLFunction) rightNodeValue;
                    this.state_ = state = state | 0b10000 /* add-active equal(SLFunction, SLFunction) */;
                    lock.unlock();
                    hasLock = false;
                    return equal(leftNodeValue_, rightNodeValue_);
                }
            }
            if (SLTypes.isSLNull(leftNodeValue)) {
                SLNull leftNodeValue_ = SLTypes.asSLNull(leftNodeValue);
                if (SLTypes.isSLNull(rightNodeValue)) {
                    SLNull rightNodeValue_ = SLTypes.asSLNull(rightNodeValue);
                    this.state_ = state = state | 0b100000 /* add-active equal(SLNull, SLNull) */;
                    lock.unlock();
                    hasLock = false;
                    return equal(leftNodeValue_, rightNodeValue_);
                }
            }
            if (leftNodeValue instanceof TruffleObject) {
                TruffleObject leftNodeValue_ = (TruffleObject) leftNodeValue;
                if (rightNodeValue instanceof TruffleObject) {
                    TruffleObject rightNodeValue_ = (TruffleObject) rightNodeValue;
                    this.state_ = state = state | 0b1000000 /* add-active equal(TruffleObject, TruffleObject) */;
                    lock.unlock();
                    hasLock = false;
                    return equal(leftNodeValue_, rightNodeValue_);
                }
            }
            if ((differentClasses(leftNodeValue, rightNodeValue))) {
                this.state_ = state = state | 0b10000000 /* add-active equal(Object, Object) */;
                lock.unlock();
                hasLock = false;
                return equal(leftNodeValue, rightNodeValue);
            }
            this.state_ = state = state | 0b100000000 /* add-active typeError(Object, Object) */;
            lock.unlock();
            hasLock = false;
            return typeError(leftNodeValue, rightNodeValue);
        } finally {
            if (oldState != 0) {
                checkForPolymorphicSpecialize(oldState);
            }
            if (hasLock) {
                lock.unlock();
            }
        }
    }

    private void checkForPolymorphicSpecialize(int oldState) {
        int newState = (this.state_ & 0b111111111);
        if ((oldState ^ newState) != 0) {
            it.reportPolymorphicSpecialize2();
        }
    }


    private static boolean fallbackGuard_(int state, Object leftNodeValue, Object rightNodeValue) {
        if (((state & 0b100)) == 0 /* is-not-active equal(boolean, boolean) */ && leftNodeValue instanceof Boolean && rightNodeValue instanceof Boolean) {
            return false;
        }
        if (((state & 0b1000)) == 0 /* is-not-active equal(String, String) */ && leftNodeValue instanceof String && rightNodeValue instanceof String) {
            return false;
        }
        if (((state & 0b1000000)) == 0 /* is-not-active equal(TruffleObject, TruffleObject) */ && leftNodeValue instanceof TruffleObject && rightNodeValue instanceof TruffleObject) {
            return false;
        }
        if (((state & 0b10000000)) == 0 /* is-not-active equal(Object, Object) */ && (differentClasses(leftNodeValue, rightNodeValue))) {
            return false;
        }
        return true;
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
