package com.oracle.truffle.sl.rev;

import com.oracle.truffle.api.CompilerDirectives;
import com.oracle.truffle.api.dsl.Cached;
import com.oracle.truffle.api.dsl.Fallback;
import com.oracle.truffle.api.dsl.Specialization;
import com.oracle.truffle.api.interop.ForeignAccess;
import com.oracle.truffle.api.interop.Message;
import com.oracle.truffle.api.interop.TruffleObject;
import com.oracle.truffle.api.nodes.Node;
import com.oracle.truffle.sl.SLException;
import com.oracle.truffle.sl.nodes.expression.SLUnboxNode;
import com.oracle.truffle.sl.nodes.interop.SLForeignToSLTypeNode;
import com.oracle.truffle.sl.runtime.SLBigNumber;
import com.oracle.truffle.sl.runtime.SLFunction;
import com.oracle.truffle.sl.runtime.SLNull;

import java.util.HashMap;
import java.util.Map;

public abstract class MySLUnboxNodeT implements SLUnboxNodeT {
    private final static Map<SLUnboxNode, SLUnboxNodeT> cache = new HashMap<>();
    private final ExecSLRevisitor alg;
    private final SLUnboxNode it;

    private MySLUnboxNodeT(ExecSLRevisitor alg, SLUnboxNode it) {
        this.alg = alg;
        this.it = it;
    }

    public static SLUnboxNodeT INSTANCE(ExecSLRevisitor alg, SLUnboxNode it) {
        if (!cache.containsKey(it)) cache.put(it, /*new MySLUnboxNodeT(alg, it)*/ null);
        return cache.get(it);
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
