package com.oracle.truffle.sl.rev;

import com.oracle.truffle.api.CompilerDirectives;
import com.oracle.truffle.api.dsl.Specialization;
import com.oracle.truffle.api.frame.FrameSlotKind;
import com.oracle.truffle.api.frame.FrameUtil;
import com.oracle.truffle.api.frame.VirtualFrame;
import com.oracle.truffle.sl.nodes.local.SLReadLocalVariableNode;

import java.util.HashMap;
import java.util.Map;

public abstract class MySLReadLocalVariableNodeT implements SLReadLocalVariableNodeT {
    private final static Map<SLReadLocalVariableNode, SLReadLocalVariableNodeT> cache = new HashMap<>();
    private final ExecSLRevisitor alg;
    private final SLReadLocalVariableNode it;

    private MySLReadLocalVariableNodeT(ExecSLRevisitor alg, SLReadLocalVariableNode it) {
        this.alg = alg;
        this.it = it;
    }

    public static SLReadLocalVariableNodeT INSTANCE(ExecSLRevisitor alg, SLReadLocalVariableNode it) {
        if (!cache.containsKey(it)) cache.put(it, /*new MySLReadLocalVariableNodeT(alg, it)*/ null);
        return cache.get(it);
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
