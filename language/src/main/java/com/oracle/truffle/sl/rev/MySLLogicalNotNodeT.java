package com.oracle.truffle.sl.rev;

import com.oracle.truffle.api.CompilerDirectives;
import com.oracle.truffle.api.dsl.Fallback;
import com.oracle.truffle.api.dsl.Specialization;
import com.oracle.truffle.api.frame.VirtualFrame;
import com.oracle.truffle.api.nodes.UnexpectedResultException;
import com.oracle.truffle.sl.SLException;
import com.oracle.truffle.sl.nodes.SLTypesGen;
import com.oracle.truffle.sl.nodes.expression.SLLogicalNotNode;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.Lock;

public class MySLLogicalNotNodeT implements SLLogicalNotNodeT {
    private final static Map<SLLogicalNotNode, SLLogicalNotNodeT> cache = new HashMap<>();
    private final ExecSLRevisitor alg;
    private final SLLogicalNotNode it;
    private final SLExpressionNodeT valueNode_;
    @CompilerDirectives.CompilationFinal
    private int state_;


    public MySLLogicalNotNodeT(ExecSLRevisitor alg, SLLogicalNotNode it) {
        this.alg = alg;
        this.it = it;
        this.valueNode_ = alg.$(it.getValueNode());
    }

    public static SLLogicalNotNodeT INSTANCE(ExecSLRevisitor alg, SLLogicalNotNode it) {
        if (!cache.containsKey(it)) cache.put(it, new MySLLogicalNotNodeT(alg, it));
        return cache.get(it);
    }

    @Override
    public Object executeGeneric(VirtualFrame frameValue) {
        int state = state_;
        if ((state & 0b10) == 0 /* only-active doBoolean(boolean) */ && state != 0  /* is-not doBoolean(boolean) && typeError(Object) */) {
            return executeGeneric_boolean0(frameValue, state);
        } else {
            return executeGeneric_generic1(frameValue, state);
        }
    }

    private Object executeGeneric_boolean0(VirtualFrame frameValue, int state) {
        boolean valueNodeValue_;
        try {
            valueNodeValue_ = this.valueNode_.executeBoolean(frameValue);
        } catch (UnexpectedResultException ex) {
            return executeAndSpecialize(ex.getResult());
        }
        assert (state & 0b1) != 0 /* is-active doBoolean(boolean) */;
        return doBoolean(valueNodeValue_);
    }

    private Object executeGeneric_generic1(VirtualFrame frameValue, int state) {
        Object valueNodeValue_ = this.valueNode_.executeGeneric(frameValue);
        if ((state & 0b1) != 0 /* is-active doBoolean(boolean) */ && valueNodeValue_ instanceof Boolean) {
            boolean valueNodeValue__ = (boolean) valueNodeValue_;
            return doBoolean(valueNodeValue__);
        }
        if ((state & 0b10) != 0 /* is-active typeError(Object) */) {
            if (fallbackGuard_(state, valueNodeValue_)) {
                return typeError(valueNodeValue_);
            }
        }
        CompilerDirectives.transferToInterpreterAndInvalidate();
        return executeAndSpecialize(valueNodeValue_);
    }

    @Override
    public boolean executeBoolean(VirtualFrame frameValue) throws UnexpectedResultException {
        int state = state_;
        if ((state & 0b10) != 0 /* is-active typeError(Object) */) {
            return SLTypesGen.expectBoolean(executeGeneric(frameValue));
        }
        boolean valueNodeValue_;
        try {
            valueNodeValue_ = this.valueNode_.executeBoolean(frameValue);
        } catch (UnexpectedResultException ex) {
            return SLTypesGen.expectBoolean(executeAndSpecialize(ex.getResult()));
        }
        if ((state & 0b1) != 0 /* is-active doBoolean(boolean) */) {
            return doBoolean(valueNodeValue_);
        }
        CompilerDirectives.transferToInterpreterAndInvalidate();
        return SLTypesGen.expectBoolean(executeAndSpecialize(valueNodeValue_));
    }

    @Override
    public void executeVoid(VirtualFrame frameValue) {
        int state = state_;
        try {
            if ((state & 0b10) == 0 /* only-active doBoolean(boolean) */ && state != 0  /* is-not doBoolean(boolean) && typeError(Object) */) {
                executeBoolean(frameValue);
                return;
            }
            executeGeneric(frameValue);
            return;
        } catch (UnexpectedResultException ex) {
            return;
        }
    }

    private Object executeAndSpecialize(Object valueNodeValue) {
        Lock lock = it.getLock2();
        boolean hasLock = true;
        lock.lock();
        int state = state_;
        int oldState = state;
        try {
            if (valueNodeValue instanceof Boolean) {
                boolean valueNodeValue_ = (boolean) valueNodeValue;
                this.state_ = state = state | 0b1 /* add-active doBoolean(boolean) */;
                lock.unlock();
                hasLock = false;
                return doBoolean(valueNodeValue_);
            }
            this.state_ = state = state | 0b10 /* add-active typeError(Object) */;
            lock.unlock();
            hasLock = false;
            return typeError(valueNodeValue);
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
        int newState = this.state_;
        if ((oldState ^ newState) != 0) {
            it.reportPolymorphicSpecialize2();
        }
    }


    private static boolean fallbackGuard_(int state, Object valueNodeValue) {
        if (((state & 0b1)) == 0 /* is-not-active doBoolean(boolean) */ && valueNodeValue instanceof Boolean) {
            return false;
        }
        return true;
    }

    @Specialization
    protected boolean doBoolean(boolean value) {
        return !value;
    }

    @Fallback
    protected Object typeError(Object value) {
        throw SLException.typeError(it, value);
    }
}
