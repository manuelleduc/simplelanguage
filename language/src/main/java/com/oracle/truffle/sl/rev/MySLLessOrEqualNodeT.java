package com.oracle.truffle.sl.rev;

import com.oracle.truffle.api.CompilerDirectives;
import com.oracle.truffle.api.dsl.Fallback;
import com.oracle.truffle.api.dsl.Specialization;
import com.oracle.truffle.api.frame.VirtualFrame;
import com.oracle.truffle.api.nodes.UnexpectedResultException;
import com.oracle.truffle.sl.SLException;
import com.oracle.truffle.sl.nodes.SLTypesGen;
import com.oracle.truffle.sl.nodes.expression.SLLessOrEqualNode;
import com.oracle.truffle.sl.runtime.SLBigNumber;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.Lock;

public class MySLLessOrEqualNodeT implements SLLessOrEqualNodeT {
    private final static Map<SLLessOrEqualNode, SLLessOrEqualNodeT> cache = new HashMap<>();
    private final ExecSLRevisitor alg;
    private final SLLessOrEqualNode it;
    private final SLExpressionNodeT opLeftNode;
    private final SLExpressionNodeT opRightNode;
    @CompilerDirectives.CompilationFinal
    private int state_;


    private MySLLessOrEqualNodeT(ExecSLRevisitor alg, SLLessOrEqualNode it) {
        this.alg = alg;
        this.it = it;
        this.opLeftNode = alg.$(this.it.getLeftNode());
        this.opRightNode = alg.$(this.it.getRightNode());
    }

    public static SLLessOrEqualNodeT INSTANCE(ExecSLRevisitor alg, SLLessOrEqualNode it) {
        if (!cache.containsKey(it)) {
            cache.put(it, new MySLLessOrEqualNodeT(alg, it));
        }
        return cache.get(it);
    }

    @Override
    public Object executeGeneric(VirtualFrame frameValue) {
        int state = state_;
        if ((state & 0b110) == 0 /* only-active lessOrEqual(long, long) */ && (state & 0b111) != 0  /* is-not lessOrEqual(long, long) && lessOrEqual(SLBigNumber, SLBigNumber) && typeError(Object, Object) */) {
            return executeGeneric_long_long0(frameValue, state);
        } else {
            return executeGeneric_generic1(frameValue, state);
        }
    }

    private Object executeGeneric_long_long0(VirtualFrame frameValue, int state) {
        long leftNodeValue_;
        try {
            leftNodeValue_ = opLeftNode.executeLong(frameValue);
        } catch (UnexpectedResultException ex) {
            Object rightNodeValue = opRightNode.executeGeneric(frameValue);
            return executeAndSpecialize(ex.getResult(), rightNodeValue);
        }
        long rightNodeValue_;
        try {
            rightNodeValue_ = opRightNode.executeLong(frameValue);
        } catch (UnexpectedResultException ex) {
            return executeAndSpecialize(leftNodeValue_, ex.getResult());
        }
        assert (state & 0b1) != 0 /* is-active lessOrEqual(long, long) */;
        return lessOrEqual(leftNodeValue_, rightNodeValue_);
    }

    private Object executeGeneric_generic1(VirtualFrame frameValue, int state) {
        SLExpressionNodeT op = opLeftNode;
        Object leftNodeValue_ = op.executeGeneric(frameValue);
        Object rightNodeValue_ = opRightNode.executeGeneric(frameValue);
        if ((state & 0b1) != 0 /* is-active lessOrEqual(long, long) */ && leftNodeValue_ instanceof Long) {
            long leftNodeValue__ = (long) leftNodeValue_;
            if (rightNodeValue_ instanceof Long) {
                long rightNodeValue__ = (long) rightNodeValue_;
                return lessOrEqual(leftNodeValue__, rightNodeValue__);
            }
        }
        if ((state & 0b10) != 0 /* is-active lessOrEqual(SLBigNumber, SLBigNumber) */ && SLTypesGen.isImplicitSLBigNumber((state & 0b11000) >>> 3 /* extract-implicit-active 0:SLBigNumber */, leftNodeValue_)) {
            SLBigNumber leftNodeValue__ = SLTypesGen.asImplicitSLBigNumber((state & 0b11000) >>> 3 /* extract-implicit-active 0:SLBigNumber */, leftNodeValue_);
            if (SLTypesGen.isImplicitSLBigNumber((state & 0b1100000) >>> 5 /* extract-implicit-active 1:SLBigNumber */, rightNodeValue_)) {
                SLBigNumber rightNodeValue__ = SLTypesGen.asImplicitSLBigNumber((state & 0b1100000) >>> 5 /* extract-implicit-active 1:SLBigNumber */, rightNodeValue_);
                return lessOrEqual(leftNodeValue__, rightNodeValue__);
            }
        }
        if ((state & 0b100) != 0 /* is-active typeError(Object, Object) */) {
            if (fallbackGuard_(leftNodeValue_, rightNodeValue_)) {
                return typeError(leftNodeValue_, rightNodeValue_);
            }
        }
        CompilerDirectives.transferToInterpreterAndInvalidate();
        return executeAndSpecialize(leftNodeValue_, rightNodeValue_);
    }

    @Override
    public boolean executeBoolean(VirtualFrame frameValue) throws UnexpectedResultException {
        int state = state_;
        if ((state & 0b100) != 0 /* is-active typeError(Object, Object) */) {
            return SLTypesGen.expectBoolean(executeGeneric(frameValue));
        }
        if ((state & 0b10) == 0 /* only-active lessOrEqual(long, long) */ && (state & 0b11) != 0  /* is-not lessOrEqual(long, long) && lessOrEqual(SLBigNumber, SLBigNumber) */) {
            return executeBoolean_long_long2(frameValue, state);
        } else {
            return executeBoolean_generic3(frameValue, state);
        }
    }

    private boolean executeBoolean_long_long2(VirtualFrame frameValue, int state) throws UnexpectedResultException {
        long leftNodeValue_;
        try {
            leftNodeValue_ = opLeftNode.executeLong(frameValue);
        } catch (UnexpectedResultException ex) {
            Object rightNodeValue = opRightNode.executeGeneric(frameValue);
            return SLTypesGen.expectBoolean(executeAndSpecialize(ex.getResult(), rightNodeValue));
        }
        long rightNodeValue_;
        try {
            rightNodeValue_ = opRightNode.executeLong(frameValue);
        } catch (UnexpectedResultException ex) {
            return SLTypesGen.expectBoolean(executeAndSpecialize(leftNodeValue_, ex.getResult()));
        }
        assert (state & 0b1) != 0 /* is-active lessOrEqual(long, long) */;
        return lessOrEqual(leftNodeValue_, rightNodeValue_);
    }




    private boolean executeBoolean_generic3(VirtualFrame frameValue, int state) throws UnexpectedResultException {
        SLExpressionNodeT opLeft = opLeftNode;
        Object leftNodeValue_ = opLeft.executeGeneric(frameValue);
        SLExpressionNodeT opRight = opRightNode;
        Object rightNodeValue_ = opRight.executeGeneric(frameValue);
        if ((state & 0b1) != 0 /* is-active lessOrEqual(long, long) */ && leftNodeValue_ instanceof Long) {
            long leftNodeValue__ = (long) leftNodeValue_;
            if (rightNodeValue_ instanceof Long) {
                long rightNodeValue__ = (long) rightNodeValue_;
                return lessOrEqual(leftNodeValue__, rightNodeValue__);
            }
        }
        if ((state & 0b10) != 0 /* is-active lessOrEqual(SLBigNumber, SLBigNumber) */ && SLTypesGen.isImplicitSLBigNumber((state & 0b11000) >>> 3 /* extract-implicit-active 0:SLBigNumber */, leftNodeValue_)) {
            SLBigNumber leftNodeValue__ = SLTypesGen.asImplicitSLBigNumber((state & 0b11000) >>> 3 /* extract-implicit-active 0:SLBigNumber */, leftNodeValue_);
            if (SLTypesGen.isImplicitSLBigNumber((state & 0b1100000) >>> 5 /* extract-implicit-active 1:SLBigNumber */, rightNodeValue_)) {
                SLBigNumber rightNodeValue__ = SLTypesGen.asImplicitSLBigNumber((state & 0b1100000) >>> 5 /* extract-implicit-active 1:SLBigNumber */, rightNodeValue_);
                return lessOrEqual(leftNodeValue__, rightNodeValue__);
            }
        }
        CompilerDirectives.transferToInterpreterAndInvalidate();
        return SLTypesGen.expectBoolean(executeAndSpecialize(leftNodeValue_, rightNodeValue_));
    }

    @Override
    public void executeVoid(VirtualFrame frameValue) {
        int state = state_;
        try {
            if ((state & 0b100) == 0 /* only-active lessOrEqual(long, long) && lessOrEqual(SLBigNumber, SLBigNumber) */ && (state & 0b111) != 0  /* is-not lessOrEqual(long, long) && lessOrEqual(SLBigNumber, SLBigNumber) && typeError(Object, Object) */) {
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
        int oldState = (state & 0b111);
        try {
            if (leftNodeValue instanceof Long) {
                long leftNodeValue_ = (long) leftNodeValue;
                if (rightNodeValue instanceof Long) {
                    long rightNodeValue_ = (long) rightNodeValue;
                    this.state_ = state = state | 0b1 /* add-active lessOrEqual(long, long) */;
                    lock.unlock();
                    hasLock = false;
                    return lessOrEqual(leftNodeValue_, rightNodeValue_);
                }
            }
            {
                int sLBigNumberCast0;
                if ((sLBigNumberCast0 = SLTypesGen.specializeImplicitSLBigNumber(leftNodeValue)) != 0) {
                    SLBigNumber leftNodeValue_ = SLTypesGen.asImplicitSLBigNumber(sLBigNumberCast0, leftNodeValue);
                    int sLBigNumberCast1;
                    if ((sLBigNumberCast1 = SLTypesGen.specializeImplicitSLBigNumber(rightNodeValue)) != 0) {
                        SLBigNumber rightNodeValue_ = SLTypesGen.asImplicitSLBigNumber(sLBigNumberCast1, rightNodeValue);
                        state = (state | (sLBigNumberCast0 << 3) /* set-implicit-active 0:SLBigNumber */);
                        state = (state | (sLBigNumberCast1 << 5) /* set-implicit-active 1:SLBigNumber */);
                        this.state_ = state = state | 0b10 /* add-active lessOrEqual(SLBigNumber, SLBigNumber) */;
                        lock.unlock();
                        hasLock = false;
                        return lessOrEqual(leftNodeValue_, rightNodeValue_);
                    }
                }
            }
            this.state_ = state = state | 0b100 /* add-active typeError(Object, Object) */;
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
        int newState = (this.state_ & 0b111);
        if ((oldState ^ newState) != 0) {
            it.reportPolymorphicSpecialize2();
        }
    }


    private static boolean fallbackGuard_(Object leftNodeValue, Object rightNodeValue) {
        if (SLTypesGen.isImplicitSLBigNumber(0b11, leftNodeValue) && SLTypesGen.isImplicitSLBigNumber(0b11, rightNodeValue)) {
            return false;
        }
        return true;
    }

    @Specialization
    protected boolean lessOrEqual(long left, long right) {
        return left <= right;
    }

    @Specialization
    @CompilerDirectives.TruffleBoundary
    protected boolean lessOrEqual(SLBigNumber left, SLBigNumber right) {
        return left.compareTo(right) <= 0;
    }

    @Fallback
    protected Object typeError(Object left, Object right) {
        throw SLException.typeError(it, left, right);
    }
}
