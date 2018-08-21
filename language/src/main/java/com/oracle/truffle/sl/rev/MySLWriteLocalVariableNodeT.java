package com.oracle.truffle.sl.rev;

import com.oracle.truffle.api.dsl.Fallback;
import com.oracle.truffle.api.dsl.Specialization;
import com.oracle.truffle.api.frame.FrameSlotKind;
import com.oracle.truffle.api.frame.VirtualFrame;
import com.oracle.truffle.sl.nodes.local.SLWriteLocalVariableNode;

import java.util.HashMap;
import java.util.Map;

public abstract class MySLWriteLocalVariableNodeT implements SLWriteLocalVariableNodeT {
    private final static Map<SLWriteLocalVariableNode, SLWriteLocalVariableNodeT> cache = new HashMap<>();
    private final ExecSLRevisitor alg;
    private final SLWriteLocalVariableNode it;

    private MySLWriteLocalVariableNodeT(ExecSLRevisitor alg, SLWriteLocalVariableNode it) {
        this.alg = alg;
        this.it = it;
    }

    public static SLWriteLocalVariableNodeT INSTANCE(ExecSLRevisitor alg, SLWriteLocalVariableNode it) {
        if (!cache.containsKey(it)) cache.put(it, /*new MySLWriteLocalVariableNodeT(alg, it)*/null);
        return cache.get(it);
    }

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
