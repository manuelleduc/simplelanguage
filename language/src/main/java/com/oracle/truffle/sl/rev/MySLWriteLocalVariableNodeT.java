package com.oracle.truffle.sl.rev;

import com.oracle.truffle.api.dsl.Fallback;
import com.oracle.truffle.api.dsl.Specialization;
import com.oracle.truffle.api.frame.FrameSlotKind;
import com.oracle.truffle.api.frame.VirtualFrame;
import com.oracle.truffle.api.nodes.UnexpectedResultException;
import com.oracle.truffle.sl.nodes.SLTypesGen;
import com.oracle.truffle.sl.nodes.local.SLWriteLocalVariableNode;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.Lock;

import static com.oracle.truffle.api.CompilerDirectives.CompilationFinal;
import static com.oracle.truffle.api.CompilerDirectives.transferToInterpreterAndInvalidate;

public class MySLWriteLocalVariableNodeT implements SLWriteLocalVariableNodeT {
    private final static Map<SLWriteLocalVariableNode, SLWriteLocalVariableNodeT> cache = new HashMap<>();
    private final ExecSLRevisitor alg;
    private final SLWriteLocalVariableNode it;
    private final SLExpressionNodeT ops;
    @CompilationFinal
    private int state_;
    @CompilationFinal
    private int exclude_;


    public MySLWriteLocalVariableNodeT(ExecSLRevisitor alg, SLWriteLocalVariableNode it) {
        this.alg = alg;
        this.it = it;
        this.ops = this.alg.$(it.getValueNode());
    }

    public static SLWriteLocalVariableNodeT INSTANCE(ExecSLRevisitor alg, SLWriteLocalVariableNode it) {
        if (!cache.containsKey(it)) {
            cache.put(it, new MySLWriteLocalVariableNodeT(alg, it));
        }
        return cache.get(it);
    }

    @Override
    public Object executeGeneric(VirtualFrame frameValue) {
        int state = state_;
        if ((state & 0b110) == 0 /* only-active writeLong(VirtualFrame, long) */ && state != 0  /* is-not writeLong(VirtualFrame, long) && writeBoolean(VirtualFrame, boolean) && write(VirtualFrame, Object) */) {
            return executeGeneric_long0(frameValue, state);
        } else if ((state & 0b101) == 0 /* only-active writeBoolean(VirtualFrame, boolean) */ && state != 0  /* is-not writeLong(VirtualFrame, long) && writeBoolean(VirtualFrame, boolean) && write(VirtualFrame, Object) */) {
            return executeGeneric_boolean1(frameValue, state);
        } else {
            return executeGeneric_generic2(frameValue, state);
        }
    }

    private Object executeGeneric_long0(VirtualFrame frameValue, int state) {
        long valueNodeValue_;
        try {
            valueNodeValue_ = ops.executeLong(frameValue);
        } catch (UnexpectedResultException ex) {
            return executeAndSpecialize(frameValue, ex.getResult());
        }
        assert (state & 0b1) != 0 /* is-active writeLong(VirtualFrame, long) */;
        if ((isLongOrIllegal(frameValue))) {
            return writeLong(frameValue, valueNodeValue_);
        }
        transferToInterpreterAndInvalidate();
        return executeAndSpecialize(frameValue, valueNodeValue_);
    }

    private Object executeGeneric_boolean1(VirtualFrame frameValue, int state) {
        boolean valueNodeValue_;
        try {
            valueNodeValue_ = ops.executeBoolean(frameValue);
        } catch (UnexpectedResultException ex) {
            return executeAndSpecialize(frameValue, ex.getResult());
        }
        assert (state & 0b10) != 0 /* is-active writeBoolean(VirtualFrame, boolean) */;
        if ((isBooleanOrIllegal(frameValue))) {
            return writeBoolean(frameValue, valueNodeValue_);
        }
        transferToInterpreterAndInvalidate();
        return executeAndSpecialize(frameValue, valueNodeValue_);
    }

    private Object executeGeneric_generic2(VirtualFrame frameValue, int state) {
        Object valueNodeValue_ = ops.executeGeneric(frameValue);
        if ((state & 0b1) != 0 /* is-active writeLong(VirtualFrame, long) */ && valueNodeValue_ instanceof Long) {
            long valueNodeValue__ = (long) valueNodeValue_;
            if ((isLongOrIllegal(frameValue))) {
                return writeLong(frameValue, valueNodeValue__);
            }
        }
        if ((state & 0b10) != 0 /* is-active writeBoolean(VirtualFrame, boolean) */ && valueNodeValue_ instanceof Boolean) {
            boolean valueNodeValue__ = (boolean) valueNodeValue_;
            if ((isBooleanOrIllegal(frameValue))) {
                return writeBoolean(frameValue, valueNodeValue__);
            }
        }
        if ((state & 0b100) != 0 /* is-active write(VirtualFrame, Object) */) {
            return write(frameValue, valueNodeValue_);
        }
        transferToInterpreterAndInvalidate();
        return executeAndSpecialize(frameValue, valueNodeValue_);
    }

    @Override
    public boolean executeBoolean(VirtualFrame frameValue) throws UnexpectedResultException {
        int state = state_;
        if ((state & 0b100) != 0 /* is-active write(VirtualFrame, Object) */) {
            return SLTypesGen.expectBoolean(executeGeneric(frameValue));
        }
        boolean valueNodeValue_;
        try {
            valueNodeValue_ = ops.executeBoolean(frameValue);
        } catch (UnexpectedResultException ex) {
            return SLTypesGen.expectBoolean(executeAndSpecialize(frameValue, ex.getResult()));
        }
        if ((state & 0b10) != 0 /* is-active writeBoolean(VirtualFrame, boolean) */) {
            if ((isBooleanOrIllegal(frameValue))) {
                return writeBoolean(frameValue, valueNodeValue_);
            }
        }
        transferToInterpreterAndInvalidate();
        return SLTypesGen.expectBoolean(executeAndSpecialize(frameValue, valueNodeValue_));
    }

    @Override
    public long executeLong(VirtualFrame frameValue) throws UnexpectedResultException {
        int state = state_;
        if ((state & 0b100) != 0 /* is-active write(VirtualFrame, Object) */) {
            return SLTypesGen.expectLong(executeGeneric(frameValue));
        }
        long valueNodeValue_;
        try {
            valueNodeValue_ = ops.executeLong(frameValue);
        } catch (UnexpectedResultException ex) {
            return SLTypesGen.expectLong(executeAndSpecialize(frameValue, ex.getResult()));
        }
        if ((state & 0b1) != 0 /* is-active writeLong(VirtualFrame, long) */) {
            if ((isLongOrIllegal(frameValue))) {
                return writeLong(frameValue, valueNodeValue_);
            }
        }
        transferToInterpreterAndInvalidate();
        return SLTypesGen.expectLong(executeAndSpecialize(frameValue, valueNodeValue_));
    }



    @Override
    public void executeVoid(VirtualFrame frameValue) {
        int state = state_;
        try {
            if ((state & 0b110) == 0 /* only-active writeLong(VirtualFrame, long) */ && state != 0  /* is-not writeLong(VirtualFrame, long) && writeBoolean(VirtualFrame, boolean) && write(VirtualFrame, Object) */) {
                executeLong(frameValue);
                return;
            } else if ((state & 0b101) == 0 /* only-active writeBoolean(VirtualFrame, boolean) */ && state != 0  /* is-not writeLong(VirtualFrame, long) && writeBoolean(VirtualFrame, boolean) && write(VirtualFrame, Object) */) {
                executeBoolean(frameValue);
                return;
            }
            executeGeneric(frameValue);
            return;
        } catch (UnexpectedResultException ex) {
            return;
        }
    }

    private Object executeAndSpecialize(VirtualFrame frameValue, Object valueNodeValue) {
        Lock lock = it.getLock2();
        boolean hasLock = true;
        lock.lock();
        int state = state_;
        int exclude = exclude_;
        int oldState = state;
        int oldExclude = exclude;
        try {
            if (((exclude & 0b1)) == 0 /* is-not-excluded writeLong(VirtualFrame, long) */ && valueNodeValue instanceof Long) {
                long valueNodeValue_ = (long) valueNodeValue;
                if ((isLongOrIllegal(frameValue))) {
                    this.state_ = state = state | 0b1 /* add-active writeLong(VirtualFrame, long) */;
                    lock.unlock();
                    hasLock = false;
                    return writeLong(frameValue, valueNodeValue_);
                }
            }
            if (((exclude & 0b10)) == 0 /* is-not-excluded writeBoolean(VirtualFrame, boolean) */ && valueNodeValue instanceof Boolean) {
                boolean valueNodeValue_ = (boolean) valueNodeValue;
                if ((isBooleanOrIllegal(frameValue))) {
                    this.state_ = state = state | 0b10 /* add-active writeBoolean(VirtualFrame, boolean) */;
                    lock.unlock();
                    hasLock = false;
                    return writeBoolean(frameValue, valueNodeValue_);
                }
            }
            this.exclude_ = exclude = exclude | 0b11 /* add-excluded writeLong(VirtualFrame, long), writeBoolean(VirtualFrame, boolean) */;
            state = state & 0xfffffffc /* remove-active writeLong(VirtualFrame, long), writeBoolean(VirtualFrame, boolean) */;
            this.state_ = state = state | 0b100 /* add-active write(VirtualFrame, Object) */;
            lock.unlock();
            hasLock = false;
            return write(frameValue, valueNodeValue);
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
            this.it.reportPolymorphicSpecialize2();
        }
    }

//    @Override
//    public NodeCost getCost() {
//        int state = state_;
//        if (state == 0b0) {
//            return NodeCost.UNINITIALIZED;
//        } else if ((state & (state - 1)) == 0 /* is-single-active  */) {
//            return NodeCost.MONOMORPHIC;
//        }
//        return NodeCost.POLYMORPHIC;
//    }


    /**
     * Specialized method to write a primitive {@code long} value. This is only possible if the
     * local variable also has currently the type {@code long} or was never written before,
     * therefore a Truffle DSL {@link #isLongOrIllegal(VirtualFrame) custom guard} is specified.
     */
    @Specialization(guards = "isLongOrIllegal(frame)")
    protected long writeLong(VirtualFrame frame, long value) {
        /* Initialize type on first write of the local variable. No-op if kind is already Long. */
        it.getSlot().setKind(FrameSlotKind.Long);

        frame.setLong(it.getSlot(), value);
        return value;
    }

    @Specialization(guards = "isBooleanOrIllegal(frame)")
    protected boolean writeBoolean(VirtualFrame frame, boolean value) {
        /* Initialize type on first write of the local variable. No-op if kind is already Long. */
        it.getSlot().setKind(FrameSlotKind.Boolean);

        frame.setBoolean(it.getSlot(), value);
        return value;
    }

    /**
     * Generic write method that works for all possible types.
     * <p>
     * Why is this method annotated with {@link Specialization} and not {@link Fallback}? For a
     * {@link Fallback} method, the Truffle DSL generated code would try all other specializations
     * first before calling this method. We know that all these specializations would fail their
     * guards, so there is no point in calling them. Since this method takes a value of type
     * {@link Object}, it is guaranteed to never fail, i.e., once we are in this specialization the
     * node will never be re-specialized.
     */
    @Specialization(replaces = {"writeLong", "writeBoolean"})
    protected Object write(VirtualFrame frame, Object value) {
        /*
         * Regardless of the type before, the new and final type of the local variable is Object.
         * Changing the slot kind also discards compiled code, because the variable type is
         * important when the compiler optimizes a method.
         *
         * No-op if kind is already Object.
         */
        it.getSlot().setKind(FrameSlotKind.Object);

        frame.setObject(it.getSlot(), value);
        return value;
    }

    /**
     * Guard function that the local variable has the type {@code long}.
     *
     * @param frame The parameter seems unnecessary, but it is required: Without the parameter, the
     *              Truffle DSL would not check the guard on every execution of the specialization.
     *              Guards without parameters are assumed to be pure, but our guard depends on the
     *              slot kind which can change.
     */
    protected boolean isLongOrIllegal(VirtualFrame frame) {
        return it.getSlot().getKind() == FrameSlotKind.Long || it.getSlot().getKind() == FrameSlotKind.Illegal;
    }

    protected boolean isBooleanOrIllegal(@SuppressWarnings("unused") VirtualFrame frame) {
        return it.getSlot().getKind() == FrameSlotKind.Boolean || it.getSlot().getKind() == FrameSlotKind.Illegal;
    }
}
