package com.oracle.truffle.sl.rev;

import com.oracle.truffle.api.CompilerDirectives;
import com.oracle.truffle.api.dsl.Cached;
import com.oracle.truffle.api.dsl.Fallback;
import com.oracle.truffle.api.dsl.Specialization;
import com.oracle.truffle.api.frame.VirtualFrame;
import com.oracle.truffle.api.interop.ForeignAccess;
import com.oracle.truffle.api.interop.Message;
import com.oracle.truffle.api.interop.TruffleObject;
import com.oracle.truffle.api.nodes.Node;
import com.oracle.truffle.api.nodes.UnexpectedResultException;
import com.oracle.truffle.sl.SLException;
import com.oracle.truffle.sl.nodes.SLTypes;
import com.oracle.truffle.sl.nodes.SLTypesGen;
import com.oracle.truffle.sl.nodes.expression.SLUnboxNode;
import com.oracle.truffle.sl.nodes.interop.SLForeignToSLTypeNode;
import com.oracle.truffle.sl.runtime.SLBigNumber;
import com.oracle.truffle.sl.runtime.SLFunction;
import com.oracle.truffle.sl.runtime.SLNull;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.Lock;

public class MySLUnboxNodeT implements SLUnboxNodeT {
    private final static Map<SLUnboxNode, SLUnboxNodeT> cache = new HashMap<>();
    private final ExecSLRevisitor alg;
    private final SLUnboxNode it;
    private final SLExpressionNodeT opChild;
    @CompilerDirectives.CompilationFinal
    private int state_;
    @Node.Child
    private SLForeignToSLTypeNode unboxBoxed_foreignToSL_;

    public MySLUnboxNodeT(ExecSLRevisitor alg, SLUnboxNode it) {
        this.alg = alg;
        this.it = it;
        this.opChild = this.alg.$(this.it.getChild());
    }

    public static SLUnboxNodeT INSTANCE(ExecSLRevisitor alg, SLUnboxNode it) {
        if (!cache.containsKey(it)) {
            cache.put(it, new MySLUnboxNodeT(alg, it));
        }
        return cache.get(it);
    }

    private boolean fallbackGuard_(int state, Object childValue) {
        if (SLTypesGen.isImplicitSLBigNumber(0b11, childValue)) {
            return false;
        }
        if (((state & 0b100)) == 0 /* is-not-active unboxBoolean(boolean) */ && childValue instanceof Boolean) {
            return false;
        }
        if (((state & 0b1000)) == 0 /* is-not-active unboxString(String) */ && childValue instanceof String) {
            return false;
        }
        if (((state & 0b10000)) == 0 /* is-not-active unboxFunction(SLFunction) */ && childValue instanceof SLFunction) {
            return false;
        }
        if (((state & 0b100000)) == 0 /* is-not-active unboxNull(SLNull) */ && SLTypes.isSLNull(childValue)) {
            return false;
        }
        if (((state & 0b1000000)) == 0 /* is-not-active unboxBoxed(Object, SLForeignToSLTypeNode) */ && (isBoxedPrimitive(childValue))) {
            return false;
        }
        if (((state & 0b10000000)) == 0 /* is-not-active unboxGeneric(Object) */ && (!(isBoxedPrimitive(childValue)))) {
            return false;
        }
        return true;
    }

    @Override
    public Object executeGeneric(VirtualFrame frameValue) {
        int state = state_;
        if ((state & 0b111111110) == 0 /* only-active unboxLong(long) */ && (state & 0b111111111) != 0  /* is-not unboxLong(long) && unboxBigNumber(SLBigNumber) && unboxBoolean(boolean) && unboxString(String) && unboxFunction(SLFunction) && unboxNull(SLNull) && unboxBoxed(Object, SLForeignToSLTypeNode) && unboxGeneric(Object) && typeError(Object) */) {
            return executeGeneric_long0(frameValue, state);
        } else if ((state & 0b111111011) == 0 /* only-active unboxBoolean(boolean) */ && (state & 0b111111111) != 0  /* is-not unboxLong(long) && unboxBigNumber(SLBigNumber) && unboxBoolean(boolean) && unboxString(String) && unboxFunction(SLFunction) && unboxNull(SLNull) && unboxBoxed(Object, SLForeignToSLTypeNode) && unboxGeneric(Object) && typeError(Object) */) {
            return executeGeneric_boolean1(frameValue, state);
        } else {
            return executeGeneric_generic2(frameValue, state);
        }
    }

    private Object executeGeneric_long0(VirtualFrame frameValue, int state) {
        long childValue_;
        try {
            childValue_ = opChild.executeLong(frameValue);
        } catch (UnexpectedResultException ex) {
            return executeAndSpecialize(ex.getResult());
        }
        assert (state & 0b1) != 0 /* is-active unboxLong(long) */;
        return unboxLong(childValue_);
    }

    private Object executeGeneric_boolean1(VirtualFrame frameValue, int state) {
        boolean childValue_;
        try {
            childValue_ = opChild.executeBoolean(frameValue);
        } catch (UnexpectedResultException ex) {
            return executeAndSpecialize(ex.getResult());
        }
        assert (state & 0b100) != 0 /* is-active unboxBoolean(boolean) */;
        return unboxBoolean(childValue_);
    }

    private Object executeGeneric_generic2(VirtualFrame frameValue, int state) {
        Object childValue_ = opChild.executeGeneric(frameValue);
        if ((state & 0b1) != 0 /* is-active unboxLong(long) */ && childValue_ instanceof Long) {
            long childValue__ = (long) childValue_;
            return unboxLong(childValue__);
        }
        if ((state & 0b10) != 0 /* is-active unboxBigNumber(SLBigNumber) */ && SLTypesGen.isImplicitSLBigNumber((state & 0b11000000000) >>> 9 /* extract-implicit-active 0:SLBigNumber */, childValue_)) {
            SLBigNumber childValue__ = SLTypesGen.asImplicitSLBigNumber((state & 0b11000000000) >>> 9 /* extract-implicit-active 0:SLBigNumber */, childValue_);
            return unboxBigNumber(childValue__);
        }
        if ((state & 0b100) != 0 /* is-active unboxBoolean(boolean) */ && childValue_ instanceof Boolean) {
            boolean childValue__ = (boolean) childValue_;
            return unboxBoolean(childValue__);
        }
        if ((state & 0b1000) != 0 /* is-active unboxString(String) */ && childValue_ instanceof String) {
            String childValue__ = (String) childValue_;
            return unboxString(childValue__);
        }
        if ((state & 0b10000) != 0 /* is-active unboxFunction(SLFunction) */ && childValue_ instanceof SLFunction) {
            SLFunction childValue__ = (SLFunction) childValue_;
            return unboxFunction(childValue__);
        }
        if ((state & 0b100000) != 0 /* is-active unboxNull(SLNull) */ && SLTypes.isSLNull(childValue_)) {
            SLNull childValue__ = SLTypes.asSLNull(childValue_);
            return unboxNull(childValue__);
        }
        if ((state & 0b111000000) != 0 /* is-active unboxBoxed(Object, SLForeignToSLTypeNode) || unboxGeneric(Object) || typeError(Object) */) {
            if ((state & 0b1000000) != 0 /* is-active unboxBoxed(Object, SLForeignToSLTypeNode) */) {
                if ((isBoxedPrimitive(childValue_))) {
                    return unboxBoxed(childValue_, this.unboxBoxed_foreignToSL_);
                }
            }
            if ((state & 0b10000000) != 0 /* is-active unboxGeneric(Object) */) {
                if ((!(isBoxedPrimitive(childValue_)))) {
                    return unboxGeneric(childValue_);
                }
            }
            if ((state & 0b100000000) != 0 /* is-active typeError(Object) */) {
                if (fallbackGuard_(state, childValue_)) {
                    return typeError(childValue_);
                }
            }
        }
        CompilerDirectives.transferToInterpreterAndInvalidate();
        return executeAndSpecialize(childValue_);
    }

    @Override
    public boolean executeBoolean(VirtualFrame frameValue) throws UnexpectedResultException {
        int state = state_;
        if ((state & 0b111000000) != 0 /* is-active unboxBoxed(Object, SLForeignToSLTypeNode) || unboxGeneric(Object) || typeError(Object) */) {
            return SLTypesGen.expectBoolean(executeGeneric(frameValue));
        }
        boolean childValue_;
        try {
            childValue_ = opChild.executeBoolean(frameValue);
        } catch (UnexpectedResultException ex) {
            return SLTypesGen.expectBoolean(executeAndSpecialize(ex.getResult()));
        }
        if ((state & 0b100) != 0 /* is-active unboxBoolean(boolean) */) {
            return unboxBoolean(childValue_);
        }
        CompilerDirectives.transferToInterpreterAndInvalidate();
        return SLTypesGen.expectBoolean(executeAndSpecialize(childValue_));
    }

    @Override
    public long executeLong(VirtualFrame frameValue) throws UnexpectedResultException {
        int state = state_;
        if ((state & 0b111000000) != 0 /* is-active unboxBoxed(Object, SLForeignToSLTypeNode) || unboxGeneric(Object) || typeError(Object) */) {
            return SLTypesGen.expectLong(executeGeneric(frameValue));
        }
        long childValue_;
        try {
            childValue_ = opChild.executeLong(frameValue);
        } catch (UnexpectedResultException ex) {
            return SLTypesGen.expectLong(executeAndSpecialize(ex.getResult()));
        }
        if ((state & 0b1) != 0 /* is-active unboxLong(long) */) {
            return unboxLong(childValue_);
        }
        CompilerDirectives.transferToInterpreterAndInvalidate();
        return SLTypesGen.expectLong(executeAndSpecialize(childValue_));
    }

    

    @Override
    public void executeVoid(VirtualFrame frameValue) {
        int state = state_;
        try {
            if ((state & 0b111111110) == 0 /* only-active unboxLong(long) */ && (state & 0b111111111) != 0  /* is-not unboxLong(long) && unboxBigNumber(SLBigNumber) && unboxBoolean(boolean) && unboxString(String) && unboxFunction(SLFunction) && unboxNull(SLNull) && unboxBoxed(Object, SLForeignToSLTypeNode) && unboxGeneric(Object) && typeError(Object) */) {
                executeLong(frameValue);
                return;
            } else if ((state & 0b111111011) == 0 /* only-active unboxBoolean(boolean) */ && (state & 0b111111111) != 0  /* is-not unboxLong(long) && unboxBigNumber(SLBigNumber) && unboxBoolean(boolean) && unboxString(String) && unboxFunction(SLFunction) && unboxNull(SLNull) && unboxBoxed(Object, SLForeignToSLTypeNode) && unboxGeneric(Object) && typeError(Object) */) {
                executeBoolean(frameValue);
                return;
            }
            executeGeneric(frameValue);
            return;
        } catch (UnexpectedResultException ex) {
            return;
        }
    }

    private Object executeAndSpecialize(Object childValue) {
        Lock lock = it.getLock2();
        boolean hasLock = true;
        lock.lock();
        int state = state_;
        int oldState = (state & 0b111111111);
        try {
            if (childValue instanceof Long) {
                long childValue_ = (long) childValue;
                this.state_ = state = state | 0b1 /* add-active unboxLong(long) */;
                lock.unlock();
                hasLock = false;
                return unboxLong(childValue_);
            }
            {
                int sLBigNumberCast0;
                if ((sLBigNumberCast0 = SLTypesGen.specializeImplicitSLBigNumber(childValue)) != 0) {
                    SLBigNumber childValue_ = SLTypesGen.asImplicitSLBigNumber(sLBigNumberCast0, childValue);
                    state = (state | (sLBigNumberCast0 << 9) /* set-implicit-active 0:SLBigNumber */);
                    this.state_ = state = state | 0b10 /* add-active unboxBigNumber(SLBigNumber) */;
                    lock.unlock();
                    hasLock = false;
                    return unboxBigNumber(childValue_);
                }
            }
            if (childValue instanceof Boolean) {
                boolean childValue_ = (boolean) childValue;
                this.state_ = state = state | 0b100 /* add-active unboxBoolean(boolean) */;
                lock.unlock();
                hasLock = false;
                return unboxBoolean(childValue_);
            }
            if (childValue instanceof String) {
                String childValue_ = (String) childValue;
                this.state_ = state = state | 0b1000 /* add-active unboxString(String) */;
                lock.unlock();
                hasLock = false;
                return unboxString(childValue_);
            }
            if (childValue instanceof SLFunction) {
                SLFunction childValue_ = (SLFunction) childValue;
                this.state_ = state = state | 0b10000 /* add-active unboxFunction(SLFunction) */;
                lock.unlock();
                hasLock = false;
                return unboxFunction(childValue_);
            }
            if (SLTypes.isSLNull(childValue)) {
                SLNull childValue_ = SLTypes.asSLNull(childValue);
                this.state_ = state = state | 0b100000 /* add-active unboxNull(SLNull) */;
                lock.unlock();
                hasLock = false;
                return unboxNull(childValue_);
            }
            if ((isBoxedPrimitive(childValue))) {
                this.unboxBoxed_foreignToSL_ = it.insert2((SLForeignToSLTypeNode.create()));
                this.state_ = state = state | 0b1000000 /* add-active unboxBoxed(Object, SLForeignToSLTypeNode) */;
                lock.unlock();
                hasLock = false;
                return unboxBoxed(childValue, this.unboxBoxed_foreignToSL_);
            }
            if ((!(isBoxedPrimitive(childValue)))) {
                this.state_ = state = state | 0b10000000 /* add-active unboxGeneric(Object) */;
                lock.unlock();
                hasLock = false;
                return unboxGeneric(childValue);
            }
            this.state_ = state = state | 0b100000000 /* add-active typeError(Object) */;
            lock.unlock();
            hasLock = false;
            return typeError(childValue);
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

    @Specialization
    protected long unboxLong(long value) {
        return value;
    }

    @Specialization
    protected SLBigNumber unboxBigNumber(SLBigNumber value) {
        return value;
    }

    @Specialization
    protected boolean unboxBoolean(boolean value) {
        return value;
    }

    @Specialization
    protected String unboxString(String value) {
        return value;
    }

    @Specialization
    protected SLFunction unboxFunction(SLFunction value) {
        return value;
    }

    @Specialization
    protected SLNull unboxNull(SLNull value) {
        return value;
    }

    @Specialization(guards = "isBoxedPrimitive(value)")
    protected Object unboxBoxed(
            Object value,
            @Cached("create()") SLForeignToSLTypeNode foreignToSL) {
        return foreignToSL.unbox((TruffleObject) value);
    }

    @Specialization(guards = "!isBoxedPrimitive(value)")
    protected Object unboxGeneric(Object value) {
        return value;
    }

    @Node.Child
    private Node isBoxed;

    protected boolean isBoxedPrimitive(Object value) {
        if (value instanceof TruffleObject) {
            if (isBoxed == null) {
                CompilerDirectives.transferToInterpreterAndInvalidate();
                isBoxed = it.insert2(Message.IS_BOXED.createNode());
            }
            if (ForeignAccess.sendIsBoxed(isBoxed, (TruffleObject) value)) {
                return true;
            }
        }
        return false;
    }

    @Fallback
    protected Object typeError(Object value) {
        throw SLException.typeError(it, value);
    }
}
