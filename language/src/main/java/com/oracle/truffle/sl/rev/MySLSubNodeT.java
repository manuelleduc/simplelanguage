package com.oracle.truffle.sl.rev;

import com.oracle.truffle.api.CompilerDirectives;
import com.oracle.truffle.api.dsl.Fallback;
import com.oracle.truffle.api.dsl.Specialization;
import com.oracle.truffle.api.frame.VirtualFrame;
import com.oracle.truffle.api.nodes.UnexpectedResultException;
import com.oracle.truffle.sl.SLException;
import com.oracle.truffle.sl.nodes.SLTypesGen;
import com.oracle.truffle.sl.nodes.expression.SLSubNode;
import com.oracle.truffle.sl.runtime.SLBigNumber;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.Lock;

public class MySLSubNodeT implements SLSubNodeT {
    private final static Map<SLSubNode, SLSubNodeT> cache = new HashMap<>();
    private final ExecSLRevisitor alg;
    private final SLSubNode it;
    private final SLExpressionNodeT opLeftNode;
    private final SLExpressionNodeT opRightNode;
    @CompilerDirectives.CompilationFinal
    private int state_;
    @CompilerDirectives.CompilationFinal
    private int exclude_;


    private MySLSubNodeT(ExecSLRevisitor alg, SLSubNode it) {
        this.alg = alg;
        this.it = it;
        this.opLeftNode = alg.$(it.getLeftNode());
        this.opRightNode = alg.$(it.getRightNode());
    }

    public static SLSubNodeT INSTANCE(ExecSLRevisitor alg, SLSubNode it) {
        if (!cache.containsKey(it)) {
            cache.put(it, new MySLSubNodeT(alg, it));
        }
        return cache.get(it);
    }

    @Override
    public Object executeGeneric(VirtualFrame frameValue) {
        int state = state_;
        if ((state & 0b110) == 0 /* only-active sub(long, long) */ && (state & 0b111) != 0  /* is-not sub(long, long) && sub(SLBigNumber, SLBigNumber) && typeError(Object, Object) */) {
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
        assert (state & 0b1) != 0 /* is-active sub(long, long) */;
        try {
            return sub(leftNodeValue_, rightNodeValue_);
        } catch (ArithmeticException ex) {
            // implicit transferToInterpreterAndInvalidate()
            Lock lock = it.getLock2();
            lock.lock();
            try {
                this.exclude_ = this.exclude_ | 0b1 /* add-excluded sub(long, long) */;
                this.state_ = this.state_ & 0xfffffffe /* remove-active sub(long, long) */;
            } finally {
                lock.unlock();
            }
            return executeAndSpecialize(leftNodeValue_, rightNodeValue_);
        }
    }

    private Object executeGeneric_generic1(VirtualFrame frameValue, int state) {
        Object leftNodeValue_ = opLeftNode.executeGeneric(frameValue);
        Object rightNodeValue_ = opRightNode.executeGeneric(frameValue);
        if ((state & 0b1) != 0 /* is-active sub(long, long) */ && leftNodeValue_ instanceof Long) {
            long leftNodeValue__ = (long) leftNodeValue_;
            if (rightNodeValue_ instanceof Long) {
                long rightNodeValue__ = (long) rightNodeValue_;
                try {
                    return sub(leftNodeValue__, rightNodeValue__);
                } catch (ArithmeticException ex) {
                    // implicit transferToInterpreterAndInvalidate()
                    Lock lock = it.getLock2();
                    lock.lock();
                    try {
                        this.exclude_ = this.exclude_ | 0b1 /* add-excluded sub(long, long) */;
                        this.state_ = this.state_ & 0xfffffffe /* remove-active sub(long, long) */;
                    } finally {
                        lock.unlock();
                    }
                    return executeAndSpecialize(leftNodeValue__, rightNodeValue__);
                }
            }
        }
        if ((state & 0b10) != 0 /* is-active sub(SLBigNumber, SLBigNumber) */ && SLTypesGen.isImplicitSLBigNumber((state & 0b11000) >>> 3 /* extract-implicit-active 0:SLBigNumber */, leftNodeValue_)) {
            SLBigNumber leftNodeValue__ = SLTypesGen.asImplicitSLBigNumber((state & 0b11000) >>> 3 /* extract-implicit-active 0:SLBigNumber */, leftNodeValue_);
            if (SLTypesGen.isImplicitSLBigNumber((state & 0b1100000) >>> 5 /* extract-implicit-active 1:SLBigNumber */, rightNodeValue_)) {
                SLBigNumber rightNodeValue__ = SLTypesGen.asImplicitSLBigNumber((state & 0b1100000) >>> 5 /* extract-implicit-active 1:SLBigNumber */, rightNodeValue_);
                return sub(leftNodeValue__, rightNodeValue__);
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
    public long executeLong(VirtualFrame frameValue) throws UnexpectedResultException {
        int state = state_;
        if ((state & 0b100) != 0 /* is-active typeError(Object, Object) */) {
            return SLTypesGen.expectLong(executeGeneric(frameValue));
        }
        long leftNodeValue_;
        try {
            leftNodeValue_ = opLeftNode.executeLong(frameValue);
        } catch (UnexpectedResultException ex) {
            Object rightNodeValue = opRightNode.executeGeneric(frameValue);
            return SLTypesGen.expectLong(executeAndSpecialize(ex.getResult(), rightNodeValue));
        }
        long rightNodeValue_;
        try {
            rightNodeValue_ = opRightNode.executeLong(frameValue);
        } catch (UnexpectedResultException ex) {
            return SLTypesGen.expectLong(executeAndSpecialize(leftNodeValue_, ex.getResult()));
        }
        if ((state & 0b1) != 0 /* is-active sub(long, long) */) {
            try {
                return sub(leftNodeValue_, rightNodeValue_);
            } catch (ArithmeticException ex) {
                // implicit transferToInterpreterAndInvalidate()
                Lock lock = it.getLock2();
                lock.lock();
                try {
                    this.exclude_ = this.exclude_ | 0b1 /* add-excluded sub(long, long) */;
                    this.state_ = this.state_ & 0xfffffffe /* remove-active sub(long, long) */;
                } finally {
                    lock.unlock();
                }
                return SLTypesGen.expectLong(executeAndSpecialize(leftNodeValue_, rightNodeValue_));
            }
        }
        CompilerDirectives.transferToInterpreterAndInvalidate();
        return SLTypesGen.expectLong(executeAndSpecialize(leftNodeValue_, rightNodeValue_));
    }

    @Override
    public void executeVoid(VirtualFrame frameValue) {
        int state = state_;
        try {
            if ((state & 0b110) == 0 /* only-active sub(long, long) */ && (state & 0b111) != 0  /* is-not sub(long, long) && sub(SLBigNumber, SLBigNumber) && typeError(Object, Object) */) {
                executeLong(frameValue);
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
        int exclude = exclude_;
        int oldState = (state & 0b111);
        int oldExclude = exclude;
        try {
            if ((exclude) == 0 /* is-not-excluded sub(long, long) */ && leftNodeValue instanceof Long) {
                long leftNodeValue_ = (long) leftNodeValue;
                if (rightNodeValue instanceof Long) {
                    long rightNodeValue_ = (long) rightNodeValue;
                    this.state_ = state = state | 0b1 /* add-active sub(long, long) */;
                    try {
                        lock.unlock();
                        hasLock = false;
                        return sub(leftNodeValue_, rightNodeValue_);
                    } catch (ArithmeticException ex) {
                        // implicit transferToInterpreterAndInvalidate()
                        lock.lock();
                        try {
                            this.exclude_ = this.exclude_ | 0b1 /* add-excluded sub(long, long) */;
                            this.state_ = this.state_ & 0xfffffffe /* remove-active sub(long, long) */;
                        } finally {
                            lock.unlock();
                        }
                        return executeAndSpecialize(leftNodeValue_, rightNodeValue_);
                    }
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
                        this.state_ = state = state | 0b10 /* add-active sub(SLBigNumber, SLBigNumber) */;
                        lock.unlock();
                        hasLock = false;
                        return sub(leftNodeValue_, rightNodeValue_);
                    }
                }
            }
            this.state_ = state = state | 0b100 /* add-active typeError(Object, Object) */;
            lock.unlock();
            hasLock = false;
            return typeError(leftNodeValue, rightNodeValue);
        } finally {
            if (oldState != 0 || oldExclude != 0) {
                checkForPolymorphicSpecialize(oldState, oldExclude);
            }
            if (hasLock) {
                lock.unlock();
            }
        }
    }

    private void checkForPolymorphicSpecialize(int oldState, int oldExclude) {
        int newState = (this.state_ & 0b111);
        int newExclude = this.exclude_;
        if ((oldState ^ newState) != 0 || (oldExclude ^ newExclude) != 0) {
            it.reportPolymorphicSpecialize2();
        }
    }


    private static boolean fallbackGuard_(Object leftNodeValue, Object rightNodeValue) {
        if (SLTypesGen.isImplicitSLBigNumber(0b11, leftNodeValue) && SLTypesGen.isImplicitSLBigNumber(0b11, rightNodeValue)) {
            return false;
        }
        return true;
    }


    @Specialization(rewriteOn = ArithmeticException.class)
    protected long sub(long left, long right) {
        return Math.subtractExact(left, right);
    }

    @Specialization
    @CompilerDirectives.TruffleBoundary
    protected SLBigNumber sub(SLBigNumber left, SLBigNumber right) {
        return new SLBigNumber(left.getValue().subtract(right.getValue()));
    }

    @Fallback
    protected Object typeError(Object left, Object right) {
        throw SLException.typeError(it, left, right);
    }
}
