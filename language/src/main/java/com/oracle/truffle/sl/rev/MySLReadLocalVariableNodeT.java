package com.oracle.truffle.sl.rev;

import com.oracle.truffle.api.CompilerDirectives;
import com.oracle.truffle.api.dsl.Specialization;
import com.oracle.truffle.api.frame.FrameSlotKind;
import com.oracle.truffle.api.frame.FrameUtil;
import com.oracle.truffle.api.frame.VirtualFrame;
import com.oracle.truffle.api.nodes.UnexpectedResultException;
import com.oracle.truffle.sl.nodes.SLTypesGen;
import com.oracle.truffle.sl.nodes.local.SLReadLocalVariableNode;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.Lock;

public class MySLReadLocalVariableNodeT implements SLReadLocalVariableNodeT {
    private final static Map<SLReadLocalVariableNode, SLReadLocalVariableNodeT> cache = new HashMap<>();
    private final ExecSLRevisitor alg;
    private final SLReadLocalVariableNode it;
    @CompilerDirectives.CompilationFinal
    private int state_;
    @CompilerDirectives.CompilationFinal
    private int exclude_;

    private MySLReadLocalVariableNodeT(ExecSLRevisitor alg, SLReadLocalVariableNode it) {
        this.alg = alg;
        this.it = it;
    }

    public static SLReadLocalVariableNodeT INSTANCE(ExecSLRevisitor alg, SLReadLocalVariableNode it) {
        if (!cache.containsKey(it)) {
            cache.put(it, new MySLReadLocalVariableNodeT(alg, it));
        }
        return cache.get(it);
    }

    @Override
    public Object executeGeneric(VirtualFrame frameValue) {
        int state = state_;
        if ((state & 0b1) != 0 /* is-active readLong(VirtualFrame) */) {
            if ((isLong(frameValue))) {
                return readLong(frameValue);
            }
        }
        if ((state & 0b10) != 0 /* is-active readBoolean(VirtualFrame) */) {
            if ((isBoolean(frameValue))) {
                return readBoolean(frameValue);
            }
        }
        if ((state & 0b100) != 0 /* is-active readObject(VirtualFrame) */) {
            return readObject(frameValue);
        }
        CompilerDirectives.transferToInterpreterAndInvalidate();
        return executeAndSpecialize(frameValue);
    }

    @Override
    public boolean executeBoolean(VirtualFrame frameValue) throws UnexpectedResultException {
        int state = state_;
        if ((state & 0b100) != 0 /* is-active readObject(VirtualFrame) */) {
            return SLTypesGen.expectBoolean(executeGeneric(frameValue));
        }
        if ((state & 0b10) != 0 /* is-active readBoolean(VirtualFrame) */) {
            if ((isBoolean(frameValue))) {
                return readBoolean(frameValue);
            }
        }
        CompilerDirectives.transferToInterpreterAndInvalidate();
        return SLTypesGen.expectBoolean(executeAndSpecialize(frameValue));
    }

    @Override
    public long executeLong(VirtualFrame frameValue) throws UnexpectedResultException {
        int state = state_;
        if ((state & 0b100) != 0 /* is-active readObject(VirtualFrame) */) {
            return SLTypesGen.expectLong(executeGeneric(frameValue));
        }
        if ((state & 0b1) != 0 /* is-active readLong(VirtualFrame) */) {
            if ((isLong(frameValue))) {
                return readLong(frameValue);
            }
        }
        CompilerDirectives.transferToInterpreterAndInvalidate();
        return SLTypesGen.expectLong(executeAndSpecialize(frameValue));
    }

    @Override
    public void executeVoid(VirtualFrame frameValue) {
        int state = state_;
        try {
            if ((state & 0b110) == 0 /* only-active readLong(VirtualFrame) */ && state != 0  /* is-not readLong(VirtualFrame) && readBoolean(VirtualFrame) && readObject(VirtualFrame) */) {
                executeLong(frameValue);
                return;
            } else if ((state & 0b101) == 0 /* only-active readBoolean(VirtualFrame) */ && state != 0  /* is-not readLong(VirtualFrame) && readBoolean(VirtualFrame) && readObject(VirtualFrame) */) {
                executeBoolean(frameValue);
                return;
            }
            executeGeneric(frameValue);
            return;
        } catch (UnexpectedResultException ex) {
            return;
        }
    }

    private Object executeAndSpecialize(VirtualFrame frameValue) {
        Lock lock = it.getLock2();
        boolean hasLock = true;
        lock.lock();
        int state = state_;
        int exclude = exclude_;
        int oldState = state;
        int oldExclude = exclude;
        try {
            if (((exclude & 0b1)) == 0 /* is-not-excluded readLong(VirtualFrame) */) {
                if ((isLong(frameValue))) {
                    this.state_ = state = state | 0b1 /* add-active readLong(VirtualFrame) */;
                    lock.unlock();
                    hasLock = false;
                    return readLong(frameValue);
                }
            }
            if (((exclude & 0b10)) == 0 /* is-not-excluded readBoolean(VirtualFrame) */) {
                if ((isBoolean(frameValue))) {
                    this.state_ = state = state | 0b10 /* add-active readBoolean(VirtualFrame) */;
                    lock.unlock();
                    hasLock = false;
                    return readBoolean(frameValue);
                }
            }
            this.exclude_ = exclude = exclude | 0b11 /* add-excluded readLong(VirtualFrame), readBoolean(VirtualFrame) */;
            state = state & 0xfffffffc /* remove-active readLong(VirtualFrame), readBoolean(VirtualFrame) */;
            this.state_ = state = state | 0b100 /* add-active readObject(VirtualFrame) */;
            lock.unlock();
            hasLock = false;
            return readObject(frameValue);
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
        int newState = this.state_;
        int newExclude = this.exclude_;
        if ((oldState ^ newState) != 0 || (oldExclude ^ newExclude) != 0) {
            it.reportPolymorphicSpecialize2();
        }
    }

    @Specialization(guards = "isLong(frame)")
    protected long readLong(VirtualFrame frame) {
        /*
         * When the FrameSlotKind is Long, we know that only primitive long values have ever been
         * written to the local variable. So we do not need to check that the frame really contains
         * a primitive long value.
         */
        return FrameUtil.getLongSafe(frame, it.getSlot());
    }

    @Specialization(guards = "isBoolean(frame)")
    protected boolean readBoolean(VirtualFrame frame) {
        return FrameUtil.getBooleanSafe(frame, it.getSlot());
    }

    @Specialization(replaces = {"readLong", "readBoolean"})
    protected Object readObject(VirtualFrame frame) {
        if (!frame.isObject(it.getSlot())) {
            /*
             * The FrameSlotKind has been set to Object, so from now on all writes to the local
             * variable will be Object writes. However, now we are in a frame that still has an old
             * non-Object value. This is a slow-path operation: we read the non-Object value, and
             * write it immediately as an Object value so that we do not hit this path again
             * multiple times for the same variable of the same frame.
             */
            CompilerDirectives.transferToInterpreter();
            Object result = frame.getValue(it.getSlot());
            frame.setObject(it.getSlot(), result);
            return result;
        }

        return FrameUtil.getObjectSafe(frame, it.getSlot());
    }

    /**
     * Guard function that the local variable has the type {@code long}.
     *
     * @param frame The parameter seems unnecessary, but it is required: Without the parameter, the
     *              Truffle DSL would not check the guard on every execution of the specialization.
     *              Guards without parameters are assumed to be pure, but our guard depends on the
     *              slot kind which can change.
     */
    protected boolean isLong(VirtualFrame frame) {
        return it.getSlot().getKind() == FrameSlotKind.Long;
    }

    protected boolean isBoolean(@SuppressWarnings("unused") VirtualFrame frame) {
        return it.getSlot().getKind() == FrameSlotKind.Boolean;
    }
}
